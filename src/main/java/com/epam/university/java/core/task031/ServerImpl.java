package com.epam.university.java.core.task031;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Deque;
import java.util.LinkedList;

public class ServerImpl implements Server {
    private static final int PORT = 7777;
    private Thread listenerThread;
    private static ServerSocket serverSocket;
    private boolean isRunning;
    private static final Deque<String> messages = new LinkedList<>();

    @Override
    public String readMessage() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        synchronized (this) {
            if (messages.size() == 0) {
                return "";
            }
            String message = messages.poll();
            System.out.println("readMessage() " + message);
            return message;
        }
    }

    @Override
    public void start() {
        listenerThread = new Thread(this::listenerThreadHandler);
        listenerThread.start();
    }

    @Override
    public void stop() {
        System.out.println("stop method.");
        if (serverSocket == null) {
            throw new RuntimeException();
        }
        isRunning = false;
        try {
            serverSocket.close();
            listenerThread.join();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void listenerThreadHandler() {
        try {
            serverSocket = new ServerSocket(PORT);
            isRunning = true;
            while (isRunning) {
                Socket socket = serverSocket.accept();
                new Thread(() -> {
                    this.clientThreadHandler(socket);
                }).start();
            }
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    private void clientThreadHandler(Socket socket) {
        System.out.println("clientThreadHandler is running");
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            while (!socket.isClosed()) {
                if (in.ready()) {
                    String s = in.readLine();
                    synchronized (this) {
                        messages.push(s);
                        System.out.println("-----------" + messages.peek());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
package com.epam.university.java.core.task039;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class Task039Impl implements Task039 {
    List<Node> nodeTree = new LinkedList<>();

    @Override
    public Map<Character, String> getEncoding(Map<Character, Integer> charFrequencies) {
        if (charFrequencies == null) {
            throw new IllegalArgumentException();
        }
        List<Integer> repeatedValues = charFrequencies
                .values()
                .stream()
                .filter(i -> Collections.frequency(charFrequencies.values(), i) > 1)
                .collect(Collectors.toList());

        for (Character character : charFrequencies.keySet()) {
            if (repeatedValues.contains(charFrequencies.get(character))) {
                nodeTree.add(0, new Node(character, charFrequencies.get(character)));
            } else {
                nodeTree.add(new Node(character, charFrequencies.get(character)));
            }
        }

        Map<Character, String> encodingMap = new TreeMap<>();
        Node tree = getHuffmanTree(nodeTree);

        for (Character c : charFrequencies.keySet()) {
            encodingMap.put(c, tree.getNodeCode(c, ""));
        }
        System.out.println(encodingMap.toString());
        return encodingMap;
    }

    @Override
    public String getEncodedString(Map<Character, Integer> charFrequencies, String string) {
        if (charFrequencies == null || string == null) {
            throw new IllegalArgumentException();
        }
        if (string.isEmpty()) {
            return "";
        }

        Map<Character, String> encodingMap = getEncoding(charFrequencies);
        StringBuilder encodedString = new StringBuilder();
        for (int i = 0; i < string.length(); i++) {
            encodedString.append(encodingMap.get(string.charAt(i)));
        }

        return encodedString.toString();
    }

    @Override
    public String getDecodedString(Map<Character, Integer> charFrequencies, String encodedString) {
        StringBuilder sb = new StringBuilder();
        Map<Character, String> encoding = getEncoding(charFrequencies);

        Node node = getHuffmanTree(nodeTree);

        for (int i = 0; i < encodedString.length(); i++) {
            node = encodedString.charAt(i) == '0' ? node.left : node.right;
            if (node.value != null) {
                sb.append(node.value);
                node = getHuffmanTree(nodeTree);
            }
        }
        return sb.toString();
    }

    private static Node getHuffmanTree(List<Node> nodes) {
        while (nodes.size() > 1) {
            Collections.sort(nodes);
            Node left = nodes.remove(nodes.size() - 1);
            Node right = nodes.remove(nodes.size() - 1);
            Node parentNode = new Node(null, left.weight + right.weight, left, right);
            nodes.add(parentNode);
        }
        return nodes.get(0);
    }

}

package com.epam.university.java.core.task015;

public class SquareImpl implements Square {
    Point first;
    Point second;

    public SquareImpl(Point first, Point second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public Point getFirst() {
        return this.first;
    }

    @Override
    public Point getSecond() {
        return this.second;
    }

    @Override
    public void setFirst(Point first) {
        this.first = first;
    }

    @Override
    public void setSecond(Point second) {
        this.second = second;
    }
}

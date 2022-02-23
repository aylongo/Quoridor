package com.quoridorproj;

import java.util.HashMap;

public class Square {
    private Tuple<Integer, Integer> coordinates;
    private int value;
    private HashMap<Integer, Square> neighbors;

    public Square(int x, int y) {
        this.coordinates = new Tuple<>(x, y);
        this.value = BoardFill.EMPTY.value();
        this.neighbors = new HashMap<>();
        // Setting the square's neighbors HashMap keys
        for (Direction direction : Direction.values()) {
            this.neighbors.put(direction.key(), null);
        }
    }

    public int getX() {
        return this.coordinates.x;
    }

    public int getY() {
        return this.coordinates.y;
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Square getNeighbor(Direction direction) {
        return this.neighbors.get(direction.key());
    }

    public void setNeighbor(Direction direction, Square neighbor) {
        // Replaces a neighbor of the Square in a certain direction with a Square
        this.neighbors.replace(direction.key(), neighbor);
    }
}

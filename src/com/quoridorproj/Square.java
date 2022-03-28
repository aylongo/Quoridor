package com.quoridorproj;

import java.util.HashMap;

public class Square {
    private Tuple<Integer, Integer> coordinates;
    private int value;
    private boolean isWallPlaced; // A boolean variable that defines if the linked wall was placed
    // (A wall is linked to the square closest to (0, 0) out of the 4 squares it touches)
    private HashMap<Integer, Square> neighbors; // An HashMap object that holds the square's neighbors on the board

    public Square(int x, int y) {
        this.coordinates = new Tuple<>(x, y);
        this.value = BoardFill.EMPTY.value();
        this.neighbors = new HashMap<>();
        // Sets the square's neighbors HashMap keys
        for (Direction direction : Direction.values()) {
            this.neighbors.put(direction.key(), null);
        }
    }

    private Square(Square square) {
        this.coordinates = new Tuple<>(square.coordinates.x, square.coordinates.y);
        this.value = square.value;
        this.neighbors = new HashMap<>();
        // Sets the square's neighbors HashMap keys
        for (Direction direction : Direction.values()) {
            this.neighbors.put(direction.key(), null);
        }
    }

    public Square duplicate() {
        return new Square(this);
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

    public boolean isWallPlaced() {
        return this.isWallPlaced;
    }

    public void setWallPlaced(boolean wallPlaced) {
        this.isWallPlaced = wallPlaced;
    }

    public Square getNeighbor(Direction direction) {
        return this.neighbors.get(direction.key());
    }

    public void setNeighbor(Direction direction, Square neighbor) {
        // Replaces a neighbor of the Square in a certain direction with a Square
        this.neighbors.replace(direction.key(), neighbor);
    }
}

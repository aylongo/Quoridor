package com.quoridorproj;

import java.util.HashMap;

public class Square {
    private Tuple<Integer, Integer> coordinates;
    private int value;
    private boolean isWallPlaced; // A boolean variable that defines if the linked wall was placed
    // (A wall is linked to the square closest to the northwest corner of the board (0, 0) out of the 4 squares it touches)
    private HashMap<Integer, Square> neighbors; // An HashMap object that holds the square's neighbors on the board

    /**
     * Square Class Constructor
     *
     * @param x The square's X value
     * @param y The square's Y value
     */
    public Square(int x, int y) {
        this.coordinates = new Tuple<>(x, y);
        this.value = BoardFill.EMPTY.value();
        this.neighbors = new HashMap<>();
        // Sets the square's neighbors HashMap keys
        for (Direction direction : Direction.values()) {
            this.neighbors.put(direction.key(), null);
        }
    }

    /**
     * Square Class Constructor specifying the Square object to get the data from
     *
     * @param square The Square object to make a duplicate from
     */
    private Square(Square square) {
        this.coordinates = new Tuple<>(square.coordinates.x, square.coordinates.y);
        this.value = square.value;
        this.neighbors = new HashMap<>();
        // Sets the square's neighbors HashMap keys
        for (Direction direction : Direction.values()) {
            this.neighbors.put(direction.key(), null);
        }
    }

    /**
     * The function returns a Square clone of the function calling Square object
     *
     * @return Square object clone
     */
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

    /**
     * The function replaces a neighbor of this Square in a certain direction with the given Square object
     *
     * @param direction The Direction enum of the neighbor
     * @param neighbor The Square object to set as a neighbor in that direction
     */
    public void setNeighbor(Direction direction, Square neighbor) {
        this.neighbors.replace(direction.key(), neighbor);
    }
}

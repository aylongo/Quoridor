package com.quoridorproj;

import java.util.ArrayList;

public enum Direction {
    LEFT(0), UP(1), RIGHT(2), DOWN(3);
    private final int key;

    /**
     * Direction Enum Constructor
     *
     * @param key The integer value of the enum
     */
    Direction(int key) {
        this.key = key;
    }

    public int key() {
        return this.key;
    }

    /**
     * The function returns a list of Direction Enums adjacent to this Direction
     *
     * @return An ArrayList of the adjacent directions
     */
    public ArrayList<Direction> getAdjacents() {
        ArrayList<Direction> adjacents = new ArrayList<>();
        if (this == Direction.LEFT || this == Direction.RIGHT) {
            adjacents.add(Direction.UP);
            adjacents.add(Direction.DOWN);
        } else if (this == Direction.UP || this == Direction.DOWN) {
            adjacents.add(Direction.LEFT);
            adjacents.add(Direction.RIGHT);
        }
        return adjacents;
    }
}

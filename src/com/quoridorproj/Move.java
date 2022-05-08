package com.quoridorproj;

import java.util.Objects;

public class Move {
    private Tuple<Integer, Integer> coordinates;
    private boolean isWall;
    private Orientation orientation;

    /**
     * Move Class Constructor for a wall place
     *
     * @param x The move's X value
     * @param y The move's Y value
     * @param orientation The wall orientation
     */
    public Move(int x, int y, Orientation orientation) {
        setCoordinates(x, y);
        this.isWall = true;
        this.orientation = orientation;
    }

    /**
     * Move Class Constructor for a player's move
     *
     * @param x The move's X value
     * @param y The move's Y value
     */
    public Move(int x, int y) {
        setCoordinates(x, y);
        this.isWall = false;
        this.orientation = null;
    }

    /**
     * The function returns a player's Move object from the given Square object's X and Y values
     *
     * @param square A Square object
     * @return A player's Move object
     */
    public static Move convertSquareToMove(Square square) {
        return new Move(square.getX(), square.getY());
    }

    public int getX() {
        return this.coordinates.x;
    }

    public int getY() {
        return this.coordinates.y;
    }

    public void setCoordinates(int x, int y) {
        this.coordinates = new Tuple<>(x, y);
    }

    public boolean isWall() {
        return this.isWall;
    }

    public Orientation getOrientation() {
        return this.orientation;
    }

    /**
     * The function compares between this move object to the given object
     *
     * @param object The object being compared
     * @return True if objects are equal and False if otherwise
     */
    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (object == null || getClass() != object.getClass())
            return false;
        Move move = (Move) object;
        return this.getX() == move.getX() && this.getY() == move.getY() &&
               this.isWall == move.isWall &&
               this.orientation == move.orientation;
    }

    @Override
    public int hashCode() {
        return Objects.hash(coordinates, isWall, orientation);
    }

    /**
     * The function builds a String which describes the move according to Quoridor's notation
     *
     * @return A String describing the Move object
     */
    @Override
    public String toString() {
        StringBuilder strMove = new StringBuilder();
        char colMove = 'a', rowMove = '1';
        colMove += this.coordinates.x;
        rowMove += this.coordinates.y;
        strMove.append(colMove);
        strMove.append(rowMove);
        if (isWall)
            strMove.append(this.orientation == Orientation.HORIZONTAL ? 'h' : 'v');
        return strMove.toString();
    }
}

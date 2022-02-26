package com.quoridorproj;

import java.util.Objects;

public class Move {
    private Tuple<Integer, Integer> coordinates;
    private boolean isWall;
    private Orientation orientation;

    public Move() {
        this.coordinates = null;
        this.isWall = false;
        this.orientation = null;
    }

    public Move(int x, int y, Orientation orientation) {
        setCoordinates(x, y);
        this.isWall = true;
        this.orientation = orientation;
    }

    public Move(int x, int y) {
        setCoordinates(x, y);
        this.isWall = false;
        this.orientation = null;
    }

    public static Move convertSquareToMove(Square square) {
        return new Move(square.getX(), square.getY());
    }

    public void rotate() {
        this.orientation = (orientation == Orientation.HORIZONTAL) ? Orientation.VERTICAL : Orientation.HORIZONTAL;
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

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    public void setWall(boolean isWall) {
        this.isWall = isWall;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Move move = (Move) o;
        return this.getX() == move.getX() && this.getY() == move.getY() &&
               this.isWall == move.isWall &&
               this.orientation == move.orientation;
    }

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

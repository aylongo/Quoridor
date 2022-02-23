package com.quoridorproj;

public class Wall {
    public enum Orientation {HORIZONTAL, VERTICAL};

    private Tuple<Integer, Integer> coordinates;
    private Orientation orientation;
    private boolean isPlaced;

    public Wall(int x, int y) {
        this.coordinates = new Tuple<>(x, y);
        this.orientation = Orientation.HORIZONTAL;
        this.isPlaced = false;
    }

    public void rotate() {
        this.orientation = (orientation == Orientation.HORIZONTAL) ? Orientation.VERTICAL : Orientation.HORIZONTAL;
    }

    public void setPlaced(boolean isPlaced) {
        this.isPlaced = isPlaced;
    }
}

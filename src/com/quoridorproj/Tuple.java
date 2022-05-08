package com.quoridorproj;

public class Tuple<X, Y> {
    public final X x; // A field of type X
    public final Y y; // A field of type Y

    /**
     * Tuple Class Constructor
     *
     * @param x A parameter of type X
     * @param y A parameter of type Y
     */
    public Tuple(X x, Y y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return String.format("(%s, %s)", this.x.toString(), this.y.toString());
    }
}

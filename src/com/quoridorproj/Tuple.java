package com.quoridorproj;

public class Tuple<X, Y> {
    public final X x;
    public final Y y;

    public Tuple(X x, Y y) {
        this.x = x;
        this.y = y;
    }

    /**
     *
     * @return String
     */

    @Override
    public String toString() {
        return String.format("(%s, %s)", this.x.toString(), this.y.toString());
    }
}

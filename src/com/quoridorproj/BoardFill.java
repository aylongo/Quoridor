package com.quoridorproj;

public enum BoardFill {
    EMPTY(0), PLAYER1(1), PLAYER2(2);
    private final int value;

    BoardFill(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}


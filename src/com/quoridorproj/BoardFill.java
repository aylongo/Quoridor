package com.quoridorproj;

public enum BoardFill {
    EMPTY(0), PLAYER1(1), PLAYER2(2);
    private final int value;

    /**
     * BoardFill Enum Constructor
     *
     * @param value The integer value of the enum
     */
    BoardFill(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}


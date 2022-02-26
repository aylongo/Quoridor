package com.quoridorproj;

public class Player {
    private final int MAX_WALLS = 10;

    private int id;
    private int wallsLeft;
    private Square lastMove; // Current square player stands on

    public Player(int id) {
        this.id = id;
        this.wallsLeft = MAX_WALLS;
        this.lastMove = null;
    }

    public int getId() {
        return this.id;
    }

    public Square getLastMove() {
        return this.lastMove;
    }

    public void setLastTurn(Square lastTurn) {
        this.lastMove = lastTurn;
    }
}

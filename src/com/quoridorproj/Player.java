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

    private Player(Player player, Board board) {
        this.id = player.id;
        this.wallsLeft = player.wallsLeft;

        int lastMoveX = player.getLastMove().getX(), lastMoveY = player.getLastMove().getY();
        this.lastMove = board.getSquares()[lastMoveY][lastMoveX];
    }

    public Player duplicate(Board board) {
        return new Player(this, board);
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

    public int getWallsLeft() {
        return this.wallsLeft;
    }

    public void decWallLeft() {
        this.wallsLeft--;
    }
}

package com.quoridorproj;

public class Player {
    private final int MAX_WALLS = 10;

    private int id;
    private int wallsLeft;
    private Square lastSquare; // Last square player stood on
    private Square currentSquare; // Current square player stands on
    private Move lastMove; // The last turn the player made

    public Player(int id) {
        this.id = id;
        this.wallsLeft = MAX_WALLS;
        this.currentSquare = null;
        this.lastMove = null;
    }

    private Player(Player player, Board board) {
        this.id = player.id;
        this.wallsLeft = player.wallsLeft;

        int currentSquareX = player.getCurrentSquare().getX(), currentSquareY = player.getCurrentSquare().getY();
        this.currentSquare = board.getSquares()[currentSquareY][currentSquareX];
        this.lastMove = player.lastMove;
    }

    public Player duplicate(Board board) {
        return new Player(this, board);
    }

    public int getID() {
        return this.id;
    }

    public Square getLastSquare() {
        return this.lastSquare;
    }

    public void setLastSquare(Square lastSquare) {
        this.lastSquare = lastSquare;
    }

    public Square getCurrentSquare() {
        return this.currentSquare;
    }

    public void setCurrentSquare(Square currentSquare) {
        this.currentSquare = currentSquare;
    }

    public Move getLastMove() {
        return this.lastMove;
    }

    public void setLastMove(Move lastMove) {
        this.lastMove = lastMove;
    }

    public int getWallsLeft() {
        return this.wallsLeft;
    }

    public void decWallsLeft() {
        this.wallsLeft--;
    }

    public void incWallsLeft() {
        this.wallsLeft++;
    }
}

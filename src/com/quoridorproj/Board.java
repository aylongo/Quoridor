package com.quoridorproj;

public class Board {
    private final int SQUARES_SIZE = 9;

    private Square[][] squares;

    public Board() {
        resetBoard();
    }

    private void configureSquaresNeighbors() {
        for (int i = 0; i < SQUARES_SIZE; i++) {
            for (int j = 1; j < SQUARES_SIZE; j++) {
                this.squares[i][j].setNeighbor(Direction.LEFT, this.squares[i][j - 1]);
            }
        }
        for (int i = 1; i < SQUARES_SIZE; i++) {
            for (int j = 0; j < SQUARES_SIZE; j++) {
                this.squares[i][j].setNeighbor(Direction.UP, this.squares[i - 1][j]);
            }
        }
        for (int i = 0; i < SQUARES_SIZE; i++) {
            for (int j = 0; j < SQUARES_SIZE - 1; j++) {
                this.squares[i][j].setNeighbor(Direction.RIGHT, this.squares[i][j + 1]);
            }
        }
        for (int i = 0; i < SQUARES_SIZE - 1; i++) {
            for (int j = 0; j < SQUARES_SIZE; j++) {
                this.squares[i][j].setNeighbor(Direction.DOWN, this.squares[i + 1][j]);
            }
        }
    }

    public Square[][] getSquares() {
        return this.squares;
    }

    public void resetBoard() {
        this.squares = new Square[SQUARES_SIZE][SQUARES_SIZE];
        for (int i = 0; i < SQUARES_SIZE; i++) {
            for (int j = 0; j < SQUARES_SIZE; j++) {
                this.squares[i][j] = new Square(j, i);
            }
        }
        configureSquaresNeighbors();
    }
}

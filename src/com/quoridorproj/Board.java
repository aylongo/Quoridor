package com.quoridorproj;

public class Board {
    private final int SQUARES_SIZE = 9;

    private Square[][] squares;

    /**
     * Board Class Constructor
     */
    public Board() {
        this.squares = new Square[SQUARES_SIZE][SQUARES_SIZE];
        for (int i = 0; i < SQUARES_SIZE; i++) {
            for (int j = 0; j < SQUARES_SIZE; j++) {
                this.squares[i][j] = new Square(j, i);
            }
        }
        configureSquaresNeighbors();
    }

    /**
     * Board Class Constructor specifying the Board object to get the data from
     *
     * @param board The Board object to make a duplicate from
     */
    private Board(Board board) {
        this.squares = new Square[SQUARES_SIZE][SQUARES_SIZE];

        for (int i = 0; i < SQUARES_SIZE; i++) {
            for (int j = 0; j < SQUARES_SIZE; j++) {
                this.squares[i][j] = board.squares[i][j].duplicate();
            }
        }
        configureDuplicateSquaresNeighbors(board);
    }

    /**
     * The function returns a Board clone of the function calling Board object
     *
     * @return Board object clone
     */
    public Board duplicate() {
        return new Board(this);
    }

    /**
     * The function initializes the neighbors of every Square in the Board
     */
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

    /**
     * The function initializes the neighbors of every Square in the Board according to the original Board
     */
    private void configureDuplicateSquaresNeighbors(Board board) {
        for (int i = 0; i < SQUARES_SIZE; i++) {
            for (int j = 1; j < SQUARES_SIZE; j++) {
                Square OGBoardLeftNeighbor = board.squares[i][j].getNeighbor(Direction.LEFT);
                if (OGBoardLeftNeighbor != null)
                    this.squares[i][j].setNeighbor(Direction.LEFT, this.squares[i][j - 1]);
            }
        }
        for (int i = 1; i < SQUARES_SIZE; i++) {
            for (int j = 0; j < SQUARES_SIZE; j++) {
                Square OGBoardUpNeighbor = board.squares[i][j].getNeighbor(Direction.UP);
                if (OGBoardUpNeighbor != null)
                    this.squares[i][j].setNeighbor(Direction.UP, this.squares[i - 1][j]);
            }
        }
        for (int i = 0; i < SQUARES_SIZE; i++) {
            for (int j = 0; j < SQUARES_SIZE - 1; j++) {
                Square OGBoardRightNeighbor = board.squares[i][j].getNeighbor(Direction.RIGHT);
                if (OGBoardRightNeighbor != null)
                    this.squares[i][j].setNeighbor(Direction.RIGHT, this.squares[i][j + 1]);
            }
        }
        for (int i = 0; i < SQUARES_SIZE - 1; i++) {
            for (int j = 0; j < SQUARES_SIZE; j++) {
                Square OGBoardDownNeighbor = board.squares[i][j].getNeighbor(Direction.DOWN);
                if (OGBoardDownNeighbor != null)
                    this.squares[i][j].setNeighbor(Direction.DOWN, this.squares[i + 1][j]);
            }
        }
    }

    public Square[][] getSquares() {
        return this.squares;
    }

    public int getSquaresSize() {
        return this.SQUARES_SIZE;
    }
}

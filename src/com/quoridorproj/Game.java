package com.quoridorproj;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Game {
    private Board board;
    private Player[] players;
    private int currentTurn;
    private int turnsCounter;

    public Game() {
        this.board = new Board();
        this.players = new Player[3];
        reset();
    }

    private Game(Game game) {
        this.board = game.board.duplicate();
        this.players = new Player[3];
        this.players[BoardFill.PLAYER1.value()] = game.players[BoardFill.PLAYER1.value()].duplicate(this.board);
        this.players[BoardFill.PLAYER2.value()] = game.players[BoardFill.PLAYER2.value()].duplicate(this.board);
        this.currentTurn = game.currentTurn;
        this.turnsCounter = game.turnsCounter;
    }

    public Game duplicate() {
        return new Game(this);
    }

    public Player[] getPlayers() {
        return this.players;
    }

    public int getCurrentTurn() {
        return this.currentTurn;
    }

    public int getTurnsCounter() {
        return this.turnsCounter;
    }

    public void incTurns() {
        this.turnsCounter++;
    }

    private void placePlayersOnBoard() {
        Square[][] squares = this.board.getSquares();
        int size = this.board.getSquaresSize();
        squares[size - 1][size / 2].setValue(BoardFill.PLAYER1.value());
        players[BoardFill.PLAYER1.value()].setLastTurn(squares[size - 1][size / 2]);
        squares[0][size / 2].setValue(BoardFill.PLAYER2.value());
        players[BoardFill.PLAYER2.value()].setLastTurn(squares[0][size / 2]);
    }

    public boolean isGameOver() {
        // Game ends when one of the players reaches one of the squares opposite to his baseline (his starting line)
        int size = this.board.getSquaresSize();
        int winRow = this.players[currentTurn].getId() == BoardFill.PLAYER1.value() ? 0 : size - 1;
        return this.players[currentTurn].getLastMove().getY() == winRow;
    }

    public void updateCurrentTurn() {
        this.currentTurn++;
        if (this.currentTurn > BoardFill.PLAYER2.value())
            this.currentTurn = 1;
    }

    public ArrayList<Move> getValidMoves(Player player) {
        // TODO: Comments
        // The function returns a list of the valid moves for the current player
        ArrayList<Move> validMoves = new ArrayList<>();
        Move move;
        for (Direction direction : Direction.values()) {
            Square neighbor = player.getLastMove().getNeighbor(direction);
            if (neighbor != null) {
                if (neighbor.getValue() == BoardFill.EMPTY.value()) {
                    move = Move.convertSquareToMove(neighbor);
                    validMoves.add(move);
                } else if (neighbor.getValue() != player.getId()) {
                    if (neighbor.getNeighbor(direction) != null) {
                        move = Move.convertSquareToMove(neighbor.getNeighbor(direction));
                        validMoves.add(move);
                    } else {
                        ArrayList<Direction> adjacents = direction.getAdjacents();
                        Direction firstAdjacent = adjacents.get(0);
                        Direction secondAdjacent = adjacents.get(1);
                        if (neighbor.getNeighbor(firstAdjacent) != null) {
                            move = Move.convertSquareToMove(neighbor.getNeighbor(firstAdjacent));
                            validMoves.add(move);
                        } if (neighbor.getNeighbor(secondAdjacent) != null) {
                            move = Move.convertSquareToMove(neighbor.getNeighbor(secondAdjacent));
                            validMoves.add(move);
                        }
                    }
                }
            }
        }
        return validMoves;
    }

    private Tuple<Integer, Square> getShortestPathToGoal(Player player) {
        // The function uses the BFS (Breadth-First Search) method to return the player's shortest path to his goal
        int size = this.board.getSquaresSize();
        int winRow = player.getId() == BoardFill.PLAYER1.value() ? 0 : size - 1;
        boolean[][] squaresVisited = new boolean[size][size];
        int[][] distanceToGoal = new int[size][size];
        Queue<Square> squaresQueue = new LinkedList<Square>();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                squaresVisited[i][j] = false;
                distanceToGoal[i][j] = -1;
            }
        }

        Square square = player.getLastMove(); // Source square
        squaresVisited[square.getY()][square.getX()] = true;
        distanceToGoal[square.getY()][square.getX()] = 0;
        squaresQueue.add(square);

        while (!squaresQueue.isEmpty()) {
            square = squaresQueue.poll();
            if (square.getY() == winRow) {
                int squareX = square.getX(), squareY = square.getY();
                return new Tuple<Integer, Square>(distanceToGoal[squareY][squareX], square);
            }
            for (Direction direction : Direction.values()) {
                Square neighbor = square.getNeighbor(direction);
                if (neighbor != null && !squaresVisited[neighbor.getY()][neighbor.getX()] && neighbor.getValue() == BoardFill.EMPTY.value()) {
                    squaresVisited[neighbor.getY()][neighbor.getX()] = true;
                    distanceToGoal[neighbor.getY()][neighbor.getX()] = distanceToGoal[square.getY()][square.getX()] + 1;
                    squaresQueue.add(neighbor);
                }
            }
        }
        return null;
    }

    private boolean isTrapped(Player player, Move move) {
        // The function checks if the player is locked (between walls). Returns true if player locked and false if otherwise
        Game gameDuplicate = this.duplicate();
        gameDuplicate.doPlaceWall(move);
        Player playerDuplicate = gameDuplicate.getPlayers()[player.getId()];
        return gameDuplicate.getShortestPathToGoal(playerDuplicate) == null;
    }

    public boolean isValidMove(Move move) {
        // TODO: Comments
        if (move.isWall())
            return false;
        ArrayList<Move> validMoves = this.getValidMoves(players[this.currentTurn]);
        for (Move validMove : validMoves) {
            if (move.equals(validMove))
                return true;
        }
        return false;
    }

    public void doMove(Move move) {
        // TODO: Comments
        int x = move.getX(), y = move.getY();
        Player player = this.players[this.currentTurn];
        int lastX = player.getLastMove().getX(), lastY = this.players[currentTurn].getLastMove().getY();
        this.board.getSquares()[y][x].setValue(this.currentTurn);
        this.board.getSquares()[lastY][lastX].setValue(BoardFill.EMPTY.value());
        player.setLastTurn(this.board.getSquares()[y][x]);
    }

    public boolean isValidWall(Move move) {
        // The function checks if a wall can be placed in the chosen place. Checks if at least one of the players is trapped and if the requested wall is placed over other past walls
        if (!move.isWall() || this.players[this.currentTurn].getWallsLeft() == 0)
            return false;
        int moveX = move.getX(), moveY = move.getY();
        if (this.board.getSquares()[moveY][moveX].isWallPlaced())
            return false;
        if (move.getOrientation() == Orientation.HORIZONTAL && (this.board.getSquares()[moveY][moveX].getNeighbor(Direction.DOWN) == null || this.board.getSquares()[moveY][moveX + 1].getNeighbor(Direction.DOWN) == null)) // Checks if horizontally wall was placed
            return false;
        if (move.getOrientation() == Orientation.VERTICAL && (this.board.getSquares()[moveY][moveX].getNeighbor(Direction.RIGHT) == null || this.board.getSquares()[moveY + 1][moveX].getNeighbor(Direction.RIGHT) == null)) // Checks if vertically wall was placed
            return false;
        if (isTrapped(this.players[BoardFill.PLAYER1.value()], move) || isTrapped(this.players[BoardFill.PLAYER2.value()], move)) // Checks if at least one of the players is trapped between walls
            return false;
        return true;
    }

    public void doPlaceWall(Move move) {
        // TODO: Comments, Efficient
        // The function places the wall on the board (by changing neighbors to null)
        int moveX = move.getX(), moveY = move.getY();
        this.players[this.currentTurn].decWallLeft();
        this.board.getSquares()[moveY][moveX].setWallPlaced(true);
        if (move.getOrientation() == Orientation.HORIZONTAL) {
            this.board.getSquares()[moveY][moveX].setNeighbor(Direction.DOWN, null);
            this.board.getSquares()[moveY][moveX + 1].setNeighbor(Direction.DOWN, null);
            this.board.getSquares()[moveY + 1][moveX].setNeighbor(Direction.UP, null);
            this.board.getSquares()[moveY + 1][moveX + 1].setNeighbor(Direction.UP, null);
        } else {
            this.board.getSquares()[moveY][moveX].setNeighbor(Direction.RIGHT, null);
            this.board.getSquares()[moveY][moveX + 1].setNeighbor(Direction.LEFT, null);
            this.board.getSquares()[moveY + 1][moveX].setNeighbor(Direction.RIGHT, null);
            this.board.getSquares()[moveY + 1][moveX + 1].setNeighbor(Direction.LEFT, null);
        }
    }

    public void reset() {
        this.players[BoardFill.PLAYER1.value()] = new Player(BoardFill.PLAYER1.value());
        this.players[BoardFill.PLAYER2.value()] = new Player(BoardFill.PLAYER2.value());
        this.turnsCounter = 0;
        // Who plays first is chosen randomly: (int) (Math.random() * (max - min + 1) + min)
        this.currentTurn = (int) (Math.random() * (BoardFill.PLAYER2.value()) + BoardFill.PLAYER1.value());
        this.board.resetBoard();
        placePlayersOnBoard();
    }
}

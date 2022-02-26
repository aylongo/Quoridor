package com.quoridorproj;

import java.security.cert.CertificateRevokedException;
import java.util.ArrayList;

public class Game {
    private final int NUM_PLAYERS = 2;

    private Board board;
    private Player[] players;
    private int currentTurn;
    private int turnsCounter;

    public Game() {
        this.board = new Board();
        this.players = new Player[NUM_PLAYERS + 1];
        this.players[BoardFill.PLAYER1.value()] = new Player(BoardFill.PLAYER1.value());
        this.players[BoardFill.PLAYER2.value()] = new Player(BoardFill.PLAYER2.value());
        this.turnsCounter = 0;
        // Who plays first is chosen randomly: (int) (Math.random() * (max - min + 1) + min)
        this.currentTurn = (int) (Math.random() * (BoardFill.PLAYER2.value()) + BoardFill.PLAYER1.value());
        placePlayersOnBoard();
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

    private void placePlayersOnBoard() {
        Square[][] squares = this.board.getSquares();
        squares[8][4].setValue(BoardFill.PLAYER1.value());
        players[BoardFill.PLAYER1.value()].setLastTurn(squares[8][4]);
        squares[0][4].setValue(BoardFill.PLAYER2.value());
        players[BoardFill.PLAYER2.value()].setLastTurn(squares[0][4]);
    }

    public boolean isGameOver() {
        // Game ends when one of the players reaches one of the 9 squares opposite to his baseline
        if (this.currentTurn == BoardFill.PLAYER1.value())
            return (this.players[currentTurn].getLastMove().getY() == 0);
        else
            return (this.players[currentTurn].getLastMove().getY() == 8);
    }

    /*
    public ArrayList<Square> getValidMoves() {
        // The function returns a list of the valid moves from the current square
        ArrayList<Square> validMoves = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            Square neighbor = this.lastTurn.getNeighbor(direction);
            if (neighbor != null) {
                if (neighbor.getValue() == BoardFill.EMPTY.value())
                    validMoves.add(neighbor);
                else if (neighbor.getValue() != this.id) {
                    if (neighbor.getNeighbor(direction) != null)
                        validMoves.add(neighbor.getNeighbor(direction));
                    else {
                        ArrayList<Direction> adjacents = direction.getAdjacents();
                        Direction firstAdjacent = adjacents.get(0);
                        Direction secondAdjacent = adjacents.get(1);
                        if (neighbor.getNeighbor(firstAdjacent) != null)
                            validMoves.add(neighbor.getNeighbor(firstAdjacent));
                        if (neighbor.getNeighbor(secondAdjacent) != null)
                            validMoves.add(neighbor.getNeighbor(secondAdjacent));
                    }
                }
            }
        }
        return validMoves;
    }
    */

    public ArrayList<Move> getValidMoves(Player player) {
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

    public boolean isLocked(Player player) {
        // The function checks if the player is locked (between walls). Returns true if player locked and false if otherwise
        int winRow = player.getId() == BoardFill.PLAYER1.value() ? 0 : 8;
        return isLockedRecursive(player.getLastMove(), winRow);
    }

    private boolean isLockedRecursive(Square square, int winRow) {
        if (square == null)
            return true;
        if (square.getY() == winRow)
            return false;
        return isLockedRecursive(square.getNeighbor(Direction.LEFT), winRow) &&
               isLockedRecursive(square.getNeighbor(Direction.UP), winRow) &&
               isLockedRecursive(square.getNeighbor(Direction.DOWN), winRow) &&
               isLockedRecursive(square.getNeighbor(Direction.RIGHT), winRow);
    }

    public boolean isValidMove(Move move) {
        ArrayList<Move> validMoves = this.getValidMoves(players[this.getCurrentTurn()]);
        for (Move validMove : validMoves) {
            if (move.equals(validMove))
                return true;
        }
        return false;
    }

    public boolean isValidWall(Move move) {
        // TODO: This function checks if a wall can be placed in chosen place. Checks if both players aren't locked and the current wall is placed over other walls
    }

    public void placeWall(Move move) {
        // TODO: This function places the wall on the board by changing neighbors to null
    }
}

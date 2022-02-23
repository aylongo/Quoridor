package com.quoridorproj;

import java.util.ArrayList;

public class Player {
    private final int MAX_WALLS = 10;

    private int id;
    private int wallsLeft;
    private Wall[] walls;
    private Square lastTurn; // Current square player stands on

    public Player(int id) {
        this.id = id;
        this.wallsLeft = MAX_WALLS;
        this.lastTurn = null;

        this.walls = new Wall[MAX_WALLS];
        for (int i = 0; i < MAX_WALLS; i++) {
            this.walls[i] = new Wall(-1, -1);
        }
    }

    public int getId() {
        return this.id;
    }

    public Square getLastTurn() {
        return this.lastTurn;
    }

    public void setLastTurn(Square lastTurn) {
        this.lastTurn = lastTurn;
    }

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

    public boolean isLocked(Player player) {
        // The function checks if the player is locked (between walls). Returns true if player locked and false if otherwise
        int winRow = player.getId() == BoardFill.PLAYER1.value() ? 0 : 8;
        return isLockedRecursive(player.getLastTurn(), winRow);
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
}

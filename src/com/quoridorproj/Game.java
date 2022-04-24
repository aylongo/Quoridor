package com.quoridorproj;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Game {
    private Board board;
    private Player[] players;
    private ArrayList<Move> movesList;
    private int currentTurn;
    private int turnsCounter;

    public Game() {
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

    public Board getBoard() {
        return this.board;
    }

    public Player[] getPlayers() {
        return this.players;
    }

    public Player getPlayer(int id) {
        return this.players[id];
    }

    public ArrayList<Move> getMovesList() {
        return this.movesList;
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

    public int getWinRow(int playerID) {
        int size = this.board.getSquaresSize();
        return playerID == BoardFill.PLAYER1.value() ? 0 : size - 1;
    }

    private void placePlayersOnBoard() {
        Square[][] squares = this.board.getSquares();
        int size = this.board.getSquaresSize();
        squares[size - 1][size / 2].setValue(BoardFill.PLAYER1.value());
        players[BoardFill.PLAYER1.value()].setCurrentSquare(squares[size - 1][size / 2]);
        players[BoardFill.PLAYER1.value()].setLastMove(Move.convertSquareToMove(squares[size - 1][size / 2]));
        squares[0][size / 2].setValue(BoardFill.PLAYER2.value());
        players[BoardFill.PLAYER2.value()].setCurrentSquare(squares[0][size / 2]);
        players[BoardFill.PLAYER2.value()].setLastMove(Move.convertSquareToMove(squares[0][size / 2]));
    }

    public void addMoveToList(Move move) {
        this.movesList.add(move);
    }

    public boolean isGoalSide(int playerID, int playerRow) {
        int size = this.board.getSquaresSize();
        int playerWinRow = getWinRow(playerID);
        if (playerID == BoardFill.PLAYER1.value())
            return (playerRow > playerWinRow && playerRow <= size / 2);
        else
            return (playerRow < playerWinRow && playerRow >= size / 2);
    }

    public boolean isGameOver() {
        // Game ends when one of the players reaches one of the squares opposite to his baseline (his starting line)
        int playerOneRow = getPlayer(BoardFill.PLAYER1.value()).getCurrentSquare().getY();
        int playerTwoRow = getPlayer(BoardFill.PLAYER2.value()).getCurrentSquare().getY();

        if (playerOneRow == getWinRow(BoardFill.PLAYER1.value()))
            return true;
        if (playerTwoRow == getWinRow(BoardFill.PLAYER2.value()))
            return true;
        return false;
    }

    public void updateCurrentTurn() {
        this.currentTurn++;
        if (this.currentTurn > BoardFill.PLAYER2.value())
            this.currentTurn = 1;
    }

    public ArrayList<Move> getValidMoves(int playerID) {
        // TODO: Comments
        // The function returns a list of the valid moves for the current player
        Player player = getPlayer(playerID);
        ArrayList<Move> validMoves = new ArrayList<>();
        Move move;
        for (Direction direction : Direction.values()) {
            Square neighbor = player.getCurrentSquare().getNeighbor(direction);
            if (neighbor != null) {
                if (neighbor.getValue() == BoardFill.EMPTY.value()) {
                    move = Move.convertSquareToMove(neighbor);
                    validMoves.add(move);
                } else if (neighbor.getValue() != playerID) {
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

    private ArrayList<Move> getPossibleWalls(int playerID) {
        int size = this.board.getSquaresSize();
        Player player = getPlayer(playerID);
        Player opponent = getPlayer((this.players.length - 1) - playerID + 1);
        int[][] offsets = new int[][] {{-1, -1}, {-1, 0}, {0, -1}, {0, 0},};
        ArrayList<Move> moves = new ArrayList<>();

        if (player.getWallsLeft() == 0)
            return moves; // returns an empty array list

        int playerX = player.getCurrentSquare().getX(), opponentX = opponent.getCurrentSquare().getX();
        int playerY = player.getCurrentSquare().getY(), opponentY = opponent.getCurrentSquare().getY();

        for (int[] offset : offsets) {
            int playerMoveX = playerX + offset[0], playerMoveY = playerY + offset[1];
            if ((playerMoveX >= 0 && playerMoveX < size - 1) && (playerMoveY >= 0 && playerMoveY < size - 1)) {
                Move horizontalWallMove = new Move(playerMoveX, playerMoveY, Orientation.HORIZONTAL);
                if (!moves.contains(horizontalWallMove) && isValidWall(horizontalWallMove))
                    moves.add(horizontalWallMove);
                Move verticalWallMove = new Move(playerMoveX, playerMoveY, Orientation.VERTICAL);
                if (!moves.contains(verticalWallMove) && isValidWall(verticalWallMove))
                    moves.add(verticalWallMove);
            }
            int opponentMoveX = opponentX + offset[0], opponentMoveY = opponentY + offset[1];
            if ((opponentMoveX >= 0 && opponentMoveX < size - 1) && (opponentMoveY >= 0 && opponentMoveY < size - 1)) {
                Move horizontalWallMove = new Move(opponentMoveX, opponentMoveY, Orientation.HORIZONTAL);
                if (!moves.contains(horizontalWallMove) && isValidWall(horizontalWallMove))
                    moves.add(horizontalWallMove);
                Move verticalWallMove = new Move(opponentMoveX, opponentMoveY, Orientation.VERTICAL);
                if (!moves.contains(verticalWallMove) && isValidWall(verticalWallMove))
                    moves.add(verticalWallMove);
            }
        }
        return moves;
    }

    public ArrayList<Move> getPossibleTurns(int playerID) {
        ArrayList<Move> moves = new ArrayList<>();
        moves.addAll(getPossibleWalls(playerID));
        moves.addAll(getValidMoves(playerID));
        return moves;
    }

    public Tuple<Integer, Square> getShortestPathToGoal(int playerID) {
        // The function uses the BFS (Breadth-First Search) method to return the player's shortest path to his goal
        Player player = getPlayer(playerID);
        int size = this.board.getSquaresSize();
        int winRow = getWinRow(playerID);
        boolean[][] squaresVisited = new boolean[size][size];
        int[][] distanceToGoal = new int[size][size];
        Queue<Square> squaresQueue = new LinkedList<Square>();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                squaresVisited[i][j] = false;
                distanceToGoal[i][j] = -1;
            }
        }

        Square square = player.getCurrentSquare(); // Source square
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
                if (neighbor != null && !squaresVisited[neighbor.getY()][neighbor.getX()]) { //  && neighbor.getValue() == BoardFill.EMPTY.value()
                    squaresVisited[neighbor.getY()][neighbor.getX()] = true;
                    distanceToGoal[neighbor.getY()][neighbor.getX()] = distanceToGoal[square.getY()][square.getX()] + 1;
                    squaresQueue.add(neighbor);
                }
            }
        }
        return null;
    }

    private boolean isTrapped(int playerID, Move move) {
        // The function checks if the player is blocked (between walls). Returns true if the player is trapped and false if otherwise
        Game gameDuplicate = this.duplicate();
        gameDuplicate.doPlaceWall(move);
        return gameDuplicate.getShortestPathToGoal(playerID) == null;
    }

    public boolean isValidMove(Move move) {
        // TODO: Comments
        if (move.isWall())
            return false;
        ArrayList<Move> validMoves = this.getValidMoves(this.currentTurn);
        for (Move validMove : validMoves) {
            if (move.equals(validMove))
                return true;
        }
        return false;
    }

    public void doMove(Move move) {
        // TODO: Comments
        int newX = move.getX(), newY = move.getY();
        Player player = getPlayer(this.currentTurn);
        int lastX = player.getCurrentSquare().getX(), lastY = player.getCurrentSquare().getY();
        this.board.getSquares()[newY][newX].setValue(this.currentTurn);
        this.board.getSquares()[lastY][lastX].setValue(BoardFill.EMPTY.value());

        player.setLastSquare(this.board.getSquares()[lastY][lastX]);
        player.setCurrentSquare(this.board.getSquares()[newY][newX]);
        player.setLastMove(move);
    }

    public boolean isValidWall(Move move) {
        // The function checks if a wall can be placed in the chosen place. Checks if at least one of the players is trapped and if the requested wall is placed over other past walls
        if (!move.isWall() || getPlayer(this.currentTurn).getWallsLeft() == 0)
            return false;
        int moveX = move.getX(), moveY = move.getY();
        if (this.board.getSquares()[moveY][moveX].isWallPlaced())
            return false;
        if (move.getOrientation() == Orientation.HORIZONTAL && (this.board.getSquares()[moveY][moveX].getNeighbor(Direction.DOWN) == null || this.board.getSquares()[moveY][moveX + 1].getNeighbor(Direction.DOWN) == null)) // Checks if horizontally wall was placed
            return false;
        if (move.getOrientation() == Orientation.VERTICAL && (this.board.getSquares()[moveY][moveX].getNeighbor(Direction.RIGHT) == null || this.board.getSquares()[moveY + 1][moveX].getNeighbor(Direction.RIGHT) == null)) // Checks if vertically wall was placed
            return false;
        if (isTrapped(BoardFill.PLAYER1.value(), move) || isTrapped(BoardFill.PLAYER2.value(), move)) // Checks if at least one of the players is trapped between walls
            return false;
        return true;
    }

    public void doPlaceWall(Move move) {
        // TODO: Comments, Efficient
        // The function places the wall on the board (by changing neighbors to null)
        int moveX = move.getX(), moveY = move.getY();
        Player player = getPlayer(this.currentTurn);
        player.decWallsLeft();
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
        player.setLastMove(move);
    }

    public void doRemoveWall(Move move) {
        int moveX = move.getX(), moveY = move.getY();
        Player player = getPlayer(this.currentTurn);
        Square[][] squares = this.board.getSquares();
        player.incWallsLeft();
        squares[moveY][moveX].setWallPlaced(false);
        if (move.getOrientation() == Orientation.HORIZONTAL) {
            squares[moveY][moveX].setNeighbor(Direction.DOWN, squares[moveY + 1][moveX]);
            squares[moveY][moveX + 1].setNeighbor(Direction.DOWN, squares[moveY + 1][moveX + 1]);
            squares[moveY + 1][moveX].setNeighbor(Direction.UP, squares[moveY][moveX]);
            squares[moveY + 1][moveX + 1].setNeighbor(Direction.UP, squares[moveY][moveX + 1]);
        } else {
            squares[moveY][moveX].setNeighbor(Direction.RIGHT, squares[moveY][moveX + 1]);
            squares[moveY][moveX + 1].setNeighbor(Direction.LEFT, squares[moveY][moveX]);
            squares[moveY + 1][moveX].setNeighbor(Direction.RIGHT, squares[moveY + 1][moveX + 1]);
            squares[moveY + 1][moveX + 1].setNeighbor(Direction.LEFT, squares[moveY + 1][moveX]);
        }
    }

    public void reset() {
        this.players[BoardFill.PLAYER1.value()] = new Player(BoardFill.PLAYER1.value());
        this.players[BoardFill.PLAYER2.value()] = new Player(BoardFill.PLAYER2.value());
        this.turnsCounter = 0;
        // Who plays first is chosen randomly: (int) (Math.random() * (max - min + 1) + min)
        this.currentTurn = (int) (Math.random() * (BoardFill.PLAYER2.value()) + BoardFill.PLAYER1.value());
        this.movesList = new ArrayList<>();
        this.board = new Board();
        placePlayersOnBoard();
    }
}

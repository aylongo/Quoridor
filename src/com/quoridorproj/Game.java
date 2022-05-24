package com.quoridorproj;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Game {
    private Board board;
    private Player[] players;
    private ArrayList<Move> movesList;
    private int currentTurn;
    private int firstTurn;
    private int turnsCounter;

    /**
     * Game Class Constructor
     */
    public Game() {
        this.players = new Player[3];
        reset();
    }

    /**
     * Game Class Constructor specifying the Game object to get the data from
     *
     * @param game The Game object to make a duplicate from
     */
    private Game(Game game) {
        this.board = game.board.duplicate();
        this.players = new Player[3];
        this.players[BoardFill.PLAYER1.value()] = game.players[BoardFill.PLAYER1.value()].duplicate(this.board);
        this.players[BoardFill.PLAYER2.value()] = game.players[BoardFill.PLAYER2.value()].duplicate(this.board);
        this.currentTurn = game.currentTurn;
        this.turnsCounter = game.turnsCounter;
    }

    /**
     * The function returns a Game clone of the function calling Game object
     *
     * @return Game object clone
     */
    public Game duplicate() {
        return new Game(this);
    }

    public Player[] getPlayers() { return this.players; }

    public Player getPlayer(int id) { return this.players[id]; }

    public int getCurrentTurn() { return this.currentTurn; }

    public int getTurnsCounter() { return this.turnsCounter; }

    public void incTurns() { this.turnsCounter++; }

    public int getWinRow(int playerID) {
        int size = this.board.getSquaresSize();
        return playerID == BoardFill.PLAYER1.value() ? 0 : size - 1;
    }

    /**
     * The function places the players on the board on their starting positions
     */
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

    public ArrayList<Move> getMovesList() { return this.movesList; }

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

    /**
     * The function checks if the game was ended
     * A game ends when one of the players reaches one of the squares opposite to his baseline (his starting line)
     *
     * @return True if the game was ended and False if otherwise
     */
    public boolean isGameOver() {
        int playerOneRow = getPlayer(BoardFill.PLAYER1.value()).getCurrentSquare().getY();
        int playerTwoRow = getPlayer(BoardFill.PLAYER2.value()).getCurrentSquare().getY();

        if (playerOneRow == getWinRow(BoardFill.PLAYER1.value()))
            return true;
        if (playerTwoRow == getWinRow(BoardFill.PLAYER2.value()))
            return true;
        return false;
    }

    /**
     * The function switches the current player turn
     */
    public void updateCurrentTurn() {
        this.currentTurn++;
        if (this.currentTurn > BoardFill.PLAYER2.value())
            this.currentTurn = 1;
    }

    /**
     * The function searches for the possible turns of the player
     *
     * @param playerID The ID of the player that the function will work on
     * @return A list of the whole player's possible turns (moves and walls placing)
     */
    public ArrayList<Move> getPossibleTurns(int playerID) {
        ArrayList<Move> moves = new ArrayList<>();
        moves.addAll(getPossibleWalls(playerID));
        moves.addAll(getValidMoves(playerID));
        return moves;
    }

    /**
     * The function searches for the valid moves of the player and returns it as a List of Moves
     *
     * @param playerID The ID of the player that the function will work on
     * @return A list of the player's valid moves
     */
    public ArrayList<Move> getValidMoves(int playerID) {
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

    /**
     * The function searches for the possible walls to place for the player and returns it as a List of Moves
     *
     * @param playerID The ID of the player that the function will work on
     * @return A list of the player's possible walls
     */
    private ArrayList<Move> getPossibleWalls(int playerID) {
        int size = this.board.getSquaresSize();
        Player player = getPlayer(playerID);
        Player opponent = getPlayer((this.players.length - 1) - playerID + 1);
        int[][] offsets = new int[][] {{-1, -1}, {-1, 0}, {0, -1}, {0, 0}};
        ArrayList<Move> moves = new ArrayList<>();

        if (player.getWallsLeft() == 0)
            return moves; // returns an empty array list

        int playerX = player.getCurrentSquare().getX(), opponentX = opponent.getCurrentSquare().getX();
        int playerY = player.getCurrentSquare().getY(), opponentY = opponent.getCurrentSquare().getY();

        for (int[] offset : offsets) {
            // Searches for walls around the player's position
            int playerMoveX = playerX + offset[0], playerMoveY = playerY + offset[1];
            if ((playerMoveX >= 0 && playerMoveX < size - 1) && (playerMoveY >= 0 && playerMoveY < size - 1)) {
                Move horizontalWallMove = new Move(playerMoveX, playerMoveY, Orientation.HORIZONTAL);
                if (!moves.contains(horizontalWallMove) && isValidWall(horizontalWallMove))
                    moves.add(horizontalWallMove);
                Move verticalWallMove = new Move(playerMoveX, playerMoveY, Orientation.VERTICAL);
                if (!moves.contains(verticalWallMove) && isValidWall(verticalWallMove))
                    moves.add(verticalWallMove);
            }
            // Searches for walls around the opponent's position
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

    /**
     * The function searches for the shortest path for the player's goal
     * It uses the BFS (Breadth-First Search) algorithm
     *
     * @param playerID The ID of the player that the function will work on
     * @return If a path exists, returns a tuple which contains the length of the shortest path to the player's goal and the goal square that the function reached during the iteration and null if path doesn't exist
     */
    public Tuple<Integer, Square> getShortestPathToGoal(int playerID) {
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

    /**
     * The function checks if the player is blocked (from reaching his goal) after placing the wall represented in the given move object
     *
     * @param playerID The ID of the player that the function will work on
     * @param move The wall Move object
     * @return True if the player is blocked and False if otherwise
     */
    private boolean isTrapped(int playerID, Move move) {
        Game gameDuplicate = this.duplicate();
        gameDuplicate.doPlaceWall(move);
        return gameDuplicate.getShortestPathToGoal(playerID) == null;
    }

    /**
     * The function makes a move turn on the game board
     *
     * @param move The Move object
     */
    public void doMove(Move move) {
        Player player = getPlayer(this.currentTurn);
        int newX = move.getX(), newY = move.getY();
        int lastX = player.getCurrentSquare().getX(), lastY = player.getCurrentSquare().getY();
        this.board.getSquares()[newY][newX].setValue(this.currentTurn);
        this.board.getSquares()[lastY][lastX].setValue(BoardFill.EMPTY.value());

        player.setLastSquare(this.board.getSquares()[lastY][lastX]);
        player.setCurrentSquare(this.board.getSquares()[newY][newX]);
        player.setLastMove(move);
    }

    /**
     * The function checks if the given move is a valid move turn
     *
     * @param move The tested Move object
     * @return True if the move is valid and False if otherwise
     */
    public boolean isValidMove(Move move) {
        if (move.isWall())
            return false;
        ArrayList<Move> validMoves = this.getValidMoves(this.currentTurn);
        for (Move validMove : validMoves) {
            if (move.equals(validMove))
                return true;
        }
        return false;
    }

    /**
     * The function makes a wall placing turn on the game board (by changing squares' neighbors to null)
     *
     * @param move The wall Move object
     */
    public void doPlaceWall(Move move) {
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

    /**
     * The function checks if the given move is a valid wall placing turn (checks if a wall can be placed in the chosen place)
     * It checks if at least one of the players is trapped and if the requested wall is placed over other past walls
     *
     * @param move The tested wall Move object
     * @return True if the move is valid and False if otherwise
     */
    public boolean isValidWall(Move move) {
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

    /**
     * The function removes a wall from the game board
     *
     * @param move The wall Move object
     */
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

    public void resetBoardForReview() {
        this.players[BoardFill.PLAYER1.value()] = new Player(BoardFill.PLAYER1.value());
        this.players[BoardFill.PLAYER2.value()] = new Player(BoardFill.PLAYER2.value());
        this.currentTurn = this.firstTurn;
        this.board = new Board();
        placePlayersOnBoard();
    }

    /**
     * The function resets the game object
     */
    public void reset() {
        this.players[BoardFill.PLAYER1.value()] = new Player(BoardFill.PLAYER1.value());
        this.players[BoardFill.PLAYER2.value()] = new Player(BoardFill.PLAYER2.value());
        this.turnsCounter = 0;
        // Who plays first is chosen randomly: (int) (Math.random() * (max - min + 1) + min)
        this.currentTurn = (int) (Math.random() * (BoardFill.PLAYER2.value()) + BoardFill.PLAYER1.value());
        this.firstTurn = currentTurn;
        this.movesList = new ArrayList<>();
        this.board = new Board();
        placePlayersOnBoard();
    }
}

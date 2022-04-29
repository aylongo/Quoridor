package com.quoridorproj;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class GameHandler implements ActionListener {
    private final int MODE_AI = 1;
    private final int MODE_TWO_PLAYERS = 2;
    private final int AI_DEPTH = 4;

    private Game game;
    private GameGraphics graphics;
    private AI ai;
    private int mode;

    /**
     * GameHandler Class Constructor
     */
    public GameHandler() {
        this.game = new Game();
        this.graphics = new GameGraphics(this);
        this.ai = new AI(BoardFill.PLAYER2.value(), BoardFill.PLAYER1.value());
    }

    /**
     * The function handles the GUI's events
     *
     * @param e The event that triggered the function
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // The function handles buttons clicks
        if (e.getSource() == graphics.getRotateButton())
            handleRotate();
        else if (e.getSource() == graphics.getUndoButton())
            handleUndo();
        else if (e.getSource() == graphics.getContinueButton())
            handleContinue();
        else if (e.getSource() == graphics.getPlayAIButton())
            handleMode(MODE_AI);
        else if (e.getSource() == graphics.getPlayTwoPlayersButton())
            handleMode(MODE_TWO_PLAYERS);
        else
            handleTurn(e);
    }

    private void handleRotate() {
        this.graphics.rotate();
    }

    /**
     * The function cancels the current (User) player's turn and updates the GUI accordingly
     */
    private void handleUndo() {
        Player player = this.game.getPlayer(this.game.getCurrentTurn());
        Move canceledMove = player.getLastMove();

        if (canceledMove.isWall()) {
            int lastMoveButtonX = canceledMove.getX() * 2, lastMoveButtonY = canceledMove.getY() * 2;
            doRemoveWall(canceledMove, lastMoveButtonX + 1, lastMoveButtonY + 1);
        } else {
            Square lastSquare = player.getLastSquare();
            int lastMoveButtonX = lastSquare.getX() * 2, lastMoveButtonY = lastSquare.getY() * 2;
            Move lastSquareMove = new Move(lastMoveButtonX / 2, lastMoveButtonY / 2);
            doMoveLastSquare(lastSquareMove, lastMoveButtonX, lastMoveButtonY);
        }
        paintValidMoves();
    }

    /**
     * The function continues the game and updates the GUI
     */
    private void handleContinue() {
        Player player = this.game.getPlayer(this.game.getCurrentTurn());
        this.game.incTurns();
        this.game.addMoveToList(player.getLastMove());
        this.graphics.addMoveToList(player.getID(), player.getLastMove().toString());
        this.graphics.resetComment();
        this.graphics.resetRotateButton();
        handleGameOver();

        if (isAITurn())
            handleAITurn();
    }

    /**
     * The function calls for other function to start the game
     *
     * @param mode The type of game mode the user chooses to play
     */
    private void handleMode(int mode) {
        resetGame(mode);
    }

    /**
     * The function handles the current (User) player's turn
     *
     * @param e The button that was pressed
     */
    private void handleTurn(ActionEvent e) {
        Tuple<Integer, Integer> buttonCoordinates = this.graphics.getBoardButtonCoordinates((JButton) e.getSource());
        int buttonX = buttonCoordinates.x, buttonY = buttonCoordinates.y;
        if (buttonY % 2 == 0 && buttonX % 2 == 0) { // A player move turn
            Move move = new Move(buttonX / 2, buttonY / 2);
            doMove(move, buttonX, buttonY);
        } else if (buttonY % 2 == 1 && buttonX % 2 == 1) { // A wall placing turn
            int buttonMoveX = buttonX - 1, buttonMoveY = buttonY - 1; // These are the coordinates of the closest move button to (0, 0)
            Orientation orientation = this.graphics.getOrientation();
            Move move = new Move(buttonMoveX / 2, buttonMoveY / 2, orientation);
            doPlaceWall(move, buttonX, buttonY);
        }
    }

    /**
     * The function handles the game state and updates the GUI accordingly
     */
    private void handleGameOver() {
        if (this.game.isGameOver()) {
            this.graphics.setPostGameGraphics(this.game.getCurrentTurn(), this.game.getTurnsCounter());
        } else {
            this.game.updateCurrentTurn();
            this.graphics.updateCurrentTurn(this.game.getCurrentTurn());
            paintValidMoves();
        }
    }

    /**
     * The function handles the AI player turn
     */
    private void handleAITurn() {
        Player playerAI = this.game.getPlayer(this.game.getCurrentTurn());
        this.graphics.setAITurnGraphics();

        Move moveAI = ai.getBestMove(this.game, AI_DEPTH);
        int moveButtonX = moveAI.getX() * 2, moveButtonY = moveAI.getY() * 2;

        if (moveAI.isWall())
            doPlaceWall(moveAI, moveButtonX + 1, moveButtonY + 1);
        else
            doMove(moveAI, moveButtonX, moveButtonY);

        this.game.incTurns();
        this.game.addMoveToList(playerAI.getLastMove());
        this.graphics.addMoveToList(playerAI.getID(), moveAI.toString());

        handleGameOver();
    }

    /**
     * The function makes a move on the game board (the logical board and the GUI's)
     *
     * @param move The Move object that's being done
     * @param buttonX The X value of the move's button on the GUI's board
     * @param buttonY The Y value of the move's button on the GUI's board
     */
    private void doMove(Move move, int buttonX, int buttonY) {
        if (this.game.isValidMove(move)) {
            removeValidMoves();
            Player player = this.game.getPlayer(this.game.getCurrentTurn());
            int lastSquareX = player.getCurrentSquare().getX(), lastSquareY = player.getCurrentSquare().getY();
            int lastButtonX = lastSquareX * 2, lastButtonY = lastSquareY * 2;
            this.game.doMove(move);
            this.graphics.doMove(player.getID(), lastButtonX, lastButtonY, buttonX, buttonY, move.toString());
        } else {
            this.graphics.updateInvalidTurnComment(false);
        }
    }

    /**
     * The function makes a move to the last position of the player
     *
     * @param lastSquareMove The last square Move object that the player returns to
     * @param lastMoveButtonX The X value of the player's last position button on the GUI's board
     * @param lastMoveButtonY The Y value of the player's last position button on the GUI's board
     */
    private void doMoveLastSquare(Move lastSquareMove, int lastMoveButtonX, int lastMoveButtonY) {
        Player player = this.game.getPlayer(this.game.getCurrentTurn());
        Move canceledMove = player.getLastMove();
        int currentSquareX = player.getCurrentSquare().getX(), currentSquareY = player.getCurrentSquare().getY();
        int currentButtonX = currentSquareX * 2, currentButtonY = currentSquareY * 2;
        this.game.doMove(lastSquareMove);
        this.graphics.doMoveLastSquare(player.getID(), currentButtonX, currentButtonY, lastMoveButtonX, lastMoveButtonY, canceledMove.toString());
    }

    /**
     * The function places a wall on the game board (the logical board and the GUI's)
     *
     * @param move The Move object that's being done
     * @param buttonX The X value of the wall's button on the GUI's board
     * @param buttonY The Y value of the wall's button on the GUI's board
     */
    private void doPlaceWall(Move move, int buttonX, int buttonY) {
        if (this.game.isValidWall(move)) {
            removeValidMoves();
            Player player = this.game.getPlayer(this.game.getCurrentTurn());
            this.game.doPlaceWall(move);
            this.graphics.doPlaceWall(player.getID(), buttonX, buttonY, move.getOrientation(), player.getWallsLeft(), move.toString());
        } else {
            this.graphics.updateInvalidTurnComment(true);
        }
    }

    /**
     * The function removes the wall from the game board (the logical board and the GUI's)
     * @param canceledMove The wall Move object that's being removed
     * @param buttonX The X value of the wall's button on the GUI's board
     * @param buttonY The Y value of the wall's button on the GUI's board
     */
    private void doRemoveWall(Move canceledMove, int buttonX, int buttonY) {
        Player player = this.game.getPlayer(this.game.getCurrentTurn());
        this.game.doRemoveWall(canceledMove);
        this.graphics.doRemoveWall(player.getID(), buttonX, buttonY, canceledMove.getOrientation(), player.getWallsLeft(), canceledMove.toString());
    }

    /**
     * The function marks the valid moves of the current player on the GUI's board
     */
    private void paintValidMoves() {
        ArrayList<Move> validMoves = this.game.getValidMoves(this.game.getCurrentTurn());
        for (Move validMove : validMoves) {
            int x = validMove.getX(), y = validMove.getY();
            int buttonX = x * 2, buttonY = y * 2; // Switches to GUI's Board coordinates format
            this.graphics.setValidMove(buttonX, buttonY, this.game.getCurrentTurn());
        }
    }

    /**
     * The function removes the valid moves of the current player from the GUI's board
     */
    private void removeValidMoves() {
        ArrayList<Move> validMoves = game.getValidMoves(this.game.getCurrentTurn());
        for (Move validMove : validMoves) {
            int x = validMove.getX(), y = validMove.getY();
            int buttonX = x * 2, buttonY = y * 2; // Switches to GUI's Board coordinates format
            this.graphics.removeValidMove(buttonX, buttonY);
        }
    }

    private boolean isAITurn() {
        return (this.mode == MODE_AI && this.game.getCurrentTurn() == ai.getMaxPlayer());
    }

    /**
     * The function starts the game in the selected mode
     * @param mode The type of game mode the user chooses to play
     */
    private void resetGame(int mode) {
        // this.game.reset();
        // this.graphics.reset();
        this.mode = mode;
        int playerOneWallsLeft = this.game.getPlayers()[BoardFill.PLAYER1.value()].getWallsLeft();
        int playerTwoWallsLeft = this.game.getPlayers()[BoardFill.PLAYER2.value()].getWallsLeft();
        this.graphics.setStartGameGraphics(this.game.getCurrentTurn(), playerOneWallsLeft, playerTwoWallsLeft);
        paintValidMoves();

        if (isAITurn())
            handleAITurn();
    }
}

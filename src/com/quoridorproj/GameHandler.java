package com.quoridorproj;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class GameHandler implements ActionListener {
    private final int MODE_AI = 1;
    private final int MODE_TWO_PLAYERS = 2;
    private final int AI_DEPTH = 2;

    private Game game;
    private GameGraphics graphics;
    private AI ai;
    private int mode;

    public GameHandler() {
        this.game = new Game();
        this.graphics = new GameGraphics();
        this.ai = new AI(BoardFill.PLAYER2.value(), BoardFill.PLAYER1.value());
        setPreGameGraphicsSets();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // The function handles buttons clicks
        if (e.getSource() == graphics.getRotateButton())
            handleRotate();
        else if (e.getSource() == graphics.getContinueButton())
            handleContinue();
        else if (e.getSource() == graphics.getPlayAIButton())
            handleMode(MODE_AI);
        else if (e.getSource() == graphics.getPlayTwoPlayersButton())
            handleMode(MODE_TWO_PLAYERS);
        else
            handleTurn(e);
    }

    private void handleTurn(ActionEvent e) {
        Tuple<Integer, Integer> buttonCoordinates = this.graphics.getBoardButtonCoordinates((JButton) e.getSource());
        int buttonX = buttonCoordinates.x, buttonY = buttonCoordinates.y;
        if (buttonY % 2 == 0 && buttonX % 2 == 0) { // A player move turn
            Move move = new Move(buttonX / 2, buttonY / 2);
            doMove(move, buttonX, buttonY);
        } else if (buttonY % 2 == 1 && buttonX % 2 == 1) { // A wall placing turn
            int buttonMoveX = buttonX - 1, buttonMoveY = buttonY - 1; // These are the coordinates of the closest move button to (0, 0)
            Orientation orientation = getOrientation();
            Move move = new Move(buttonMoveX / 2, buttonMoveY / 2, orientation);
            doPlaceWall(move, buttonX, buttonY);
        }
    }

    private void handleRotate() {
        this.graphics.rotate(getOrientation());
    }

    private void handleMode(int mode) {
        resetGame(mode);
    }

    private void handleContinue() {
        this.game.incTurns();
        this.graphics.updateComment("");
        this.graphics.getRotateButton().setText("Horizontal");
        handleGameOver();

        if (this.mode == MODE_AI && this.game.getCurrentTurn() == ai.getMaxPlayer()) {
            handleAITurn();
            this.game.incTurns();
            handleGameOver();
        }
    }

    private void handleGameOver() {
        if (this.game.isGameOver()) {
            this.graphics.setBoardButtonsEnabled(false);
            this.graphics.updateGameStatus(String.format("PLAYER %d WON!", game.getCurrentTurn()));
            GameGraphics.setButtonEnabled(this.graphics.getContinueButton(), false);
            GameGraphics.setButtonEnabled(this.graphics.getRotateButton(), false);
        } else {
            this.game.updateCurrentTurn();
            this.graphics.setBoardButtonsEnabled(true);
            this.graphics.updateGameStatus(String.format("Player %d's Turn!", game.getCurrentTurn()));
            GameGraphics.setButtonEnabled(this.graphics.getContinueButton(), false);
            GameGraphics.setButtonEnabled(this.graphics.getRotateButton(), true);
            paintValidMoves();
        }
    }

    private void handleAITurn() {
        this.graphics.setBoardButtonsEnabled(false);
        GameGraphics.setButtonEnabled(this.graphics.getRotateButton(), false);

        Move moveAI = ai.getBestMove(this.game, AI_DEPTH);
        int moveButtonX = moveAI.getX() * 2, moveButtonY = moveAI.getY() * 2;

        if (moveAI.isWall()) {
            doPlaceWall(moveAI, moveButtonX + 1, moveButtonY + 1);
        } else {
            doMove(moveAI, moveButtonX, moveButtonY);
        }
        this.graphics.updateComment(String.format("Turn Made: %s", moveAI.toString()));
    }

    private void doMove(Move move, int buttonX, int buttonY) {
        if (this.game.isValidMove(move)) {
            this.graphics.setBoardButtonsEnabled(false);
            removeValidMoves();

            Player player = this.game.getPlayer(this.game.getCurrentTurn());
            int lastMoveX = player.getLastMove().getX(), lastMoveY = player.getLastMove().getY();
            int lastMoveButtonX = lastMoveX * 2, lastMoveButtonY = lastMoveY * 2;
            this.game.doMove(move);
            this.graphics.removePlayer(lastMoveButtonX, lastMoveButtonY);
            this.graphics.paintPlayer(buttonX, buttonY, game.getCurrentTurn());
            this.graphics.updateComment(String.format("Turn Made: %s", move.toString()));
            GameGraphics.setButtonEnabled(this.graphics.getContinueButton(), true);
            GameGraphics.setButtonEnabled(this.graphics.getRotateButton(), false);
            // TODO: game.addMoveToList(move);
        } else {
            this.graphics.updateComment("Invalid move!");
        }
    }

    private void doPlaceWall(Move move, int buttonX, int buttonY) {
        if (this.game.isValidWall(move)) {
            this.graphics.setBoardButtonsEnabled(false);
            removeValidMoves();

            this.game.doPlaceWall(move);
            this.graphics.paintWall(buttonX, buttonY, move.getOrientation());
            this.graphics.updateComment(String.format("Turn Made: %s", move.toString()));
            this.graphics.updatePlayerWallsLeft(this.game.getCurrentTurn(), this.game.getPlayers()[game.getCurrentTurn()].getWallsLeft());
            GameGraphics.setButtonEnabled(this.graphics.getContinueButton(), true);
            GameGraphics.setButtonEnabled(this.graphics.getRotateButton(), false);
            // TODO: game.addMoveToList(move);
        } else {
            this.graphics.updateComment("Invalid wall!");
        }
    }

    private void paintValidMoves() {
        ArrayList<Move> validMoves = this.game.getValidMoves(this.game.getCurrentTurn());
        for (Move validMove : validMoves) {
            int x = validMove.getX(), y = validMove.getY();
            int buttonX = x * 2, buttonY = y * 2; // Switches to GUI's Board coordinates format
            this.graphics.setValidMove(buttonX, buttonY, game.getCurrentTurn());
        }
    }

    private void removeValidMoves() {
        ArrayList<Move> validMoves = game.getValidMoves(this.game.getCurrentTurn());
        for (Move validMove : validMoves) {
            int x = validMove.getX(), y = validMove.getY();
            int buttonX = x * 2, buttonY = y * 2; // Switches to GUI's Board coordinates format
            this.graphics.removeValidMove(buttonX, buttonY);
        }
    }

    private Orientation getOrientation() {
        return this.graphics.getRotateButton().getText().equals("Horizontal") ? Orientation.HORIZONTAL : Orientation.VERTICAL;
    }

    private void setPreGameGraphicsSets() {
        this.graphics.setSidePanelVisibility(false);
        this.graphics.setPreGameSidePanelVisibility(true);

        GameGraphics.setButtonEnabled(this.graphics.getPlayAIButton(), true);
        GameGraphics.setButtonListener(this.graphics.getPlayAIButton(), this);
        GameGraphics.setButtonEnabled(this.graphics.getPlayTwoPlayersButton(), true);
        GameGraphics.setButtonListener(this.graphics.getPlayTwoPlayersButton(), this);

        this.graphics.setBoardButtonsEnabled(false);
    }

    private void setGameGraphicsSets() {
        this.graphics.setSidePanelVisibility(true);
        this.graphics.setPreGameSidePanelVisibility(false);

        GameGraphics.setButtonEnabled(this.graphics.getContinueButton(), false);
        GameGraphics.setButtonListener(this.graphics.getContinueButton(), this);
        GameGraphics.setButtonEnabled(this.graphics.getRotateButton(), true);
        GameGraphics.setButtonListener(this.graphics.getRotateButton(), this);
        this.graphics.updatePlayerWallsLeft(BoardFill.PLAYER1.value(), this.game.getPlayers()[BoardFill.PLAYER1.value()].getWallsLeft());
        this.graphics.updatePlayerWallsLeft(BoardFill.PLAYER2.value(), this.game.getPlayers()[BoardFill.PLAYER2.value()].getWallsLeft());
        this.graphics.updateGameStatus(String.format("Player %d's Turn!", this.game.getCurrentTurn()));

        paintValidMoves();

        this.graphics.setBoardButtonsListener(this);
        this.graphics.setBoardButtonsEnabled(true);
    }

    private void resetGame(int mode) {
        this.graphics.reset();
        this.game.reset();
        this.mode = mode;
        setGameGraphicsSets();

        if (this.mode == MODE_AI && this.game.getCurrentTurn() == ai.getMaxPlayer()) {
            handleAITurn();
            handleContinue();
        }
    }
}

package com.quoridorproj;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class GameHandler implements ActionListener {
    private Game game;
    private GameGraphics graphics;

    public GameHandler() {
        this.game = new Game();
        this.graphics = new GameGraphics();
        resetGame();
        paintValidMoves();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO: Handle buttons clicks here. Check if a wall place click or a move click
        handleTurn(e);
    }

    private void handleTurn(ActionEvent e) {
        Tuple<Integer, Integer> buttonCoordinates = this.graphics.getBoardButtonCoordinates((JButton) e.getSource());
        int buttonX = buttonCoordinates.x, buttonY = buttonCoordinates.y;
        if (buttonY % 2 == 0 && buttonX % 2 == 0) { // A player move turn
            Move move = new Move(buttonX / 2, buttonY / 2);
            if (game.isValidMove(move)) {
                game.doMove();
                graphics.paintPlayer();
                graphics.removePlayer();
            } else {

            }
        } else if (buttonY % 2 == 1 && buttonX % 2 == 0) { // A wall placing turn
            if (game.isValidWall())
        }
    }

    private void paintValidMoves() {
        Player[] players = this.game.getPlayers();
        ArrayList<Move> validMoves = game.getValidMoves(players[game.getCurrentTurn()]);
        for (Move validMove : validMoves) {
            int x = validMove.getX(), y = validMove.getY();
            int buttonX = x * 2, buttonY = y * 2; // Switch to GUI's Board coordinates format
            this.graphics.setValidMove(buttonX, buttonY);
        }
    }

    private void resetGame() {
        // TODO: this.graphics.reset();
        // TODO: this.game.reset();
        this.graphics.setBoardButtonsListener(this);
    }
}

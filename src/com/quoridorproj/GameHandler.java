package com.quoridorproj;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameHandler implements ActionListener {
    private Game game;
    private GameGraphics graphics;

    public GameHandler() {
        this.game = new Game();
        this.graphics = new GameGraphics();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO: Handle buttons clicks here. Check if a wall place click or a move click
    }
}

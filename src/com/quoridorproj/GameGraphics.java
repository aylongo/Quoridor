package com.quoridorproj;

import javax.swing.*;
import java.awt.*;

public class GameGraphics {
    private final int BOARD_SIZE = 17;

    JFrame frame;
    JButton[][] buttons;

    public GameGraphics() {
        setFrame();
        addBoardButtons();
    }

    public void setFrame() {
        this.frame = new JFrame();
        this.frame.setTitle("Quoridor");
        this.frame.setSize(1200, 1000);
        this.frame.setFocusable(true);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setResizable(false);
        this.frame.setVisible(true);
    }

    private void addBoardButtons() {
        JPanel boardPanel = new JPanel(new GridBagLayout());
        boardPanel.setSize(1000, 1000);
        setBoardButtons(boardPanel);
        this.frame.add(boardPanel, "Center"); // After adding the buttons to the panel, it adds the panel to the frame
    }

    private void setBoardButtons(JPanel boardPanel) {
        int width, height;
        int SIZE1 = 40, SIZE2 = 10;
        GridBagConstraints constraints = new GridBagConstraints();
        this.buttons = new JButton[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                this.buttons[i][j] = new JButton();
                if (i % 2 == 0)
                    height = SIZE1;
                else
                    height = SIZE2;
                if (j % 2 == 0)
                    width = SIZE1;
                else
                    width = SIZE2;

                // Setting the button's size and margins
                this.buttons[i][j].setPreferredSize(new Dimension(width, height));
                this.buttons[i][j].setMaximumSize(new Dimension(width, height));
                constraints.fill = GridBagConstraints.NONE;
                constraints.gridx = j; constraints.gridy = i;
                constraints.ipadx = width; constraints.ipady = height;
                constraints.insets = new Insets(2, 2, 2, 2);
                boardPanel.add(this.buttons[i][j], constraints);
            }
        }
    }

    public void setBoardButtonsEnabled(boolean state) {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                this.buttons[i][j].setEnabled(state);
            }
        }
    }

    public void setBoardButtonsListener(GameHandler context) {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                this.buttons[i][j].addActionListener(context);
            }
        }
    }

    protected int getBoardButtonCoordinates(JButton button) {
        // The function returns the coordinates of the clicked button (The tens' digit is the row and the units' digit is the column)
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (this.buttons[i][j] == button) {
                    return ((i * 10) + j);
                }
            }
        }
        return -1;
    }
}

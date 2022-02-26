package com.quoridorproj;

import javax.swing.*;
import java.awt.*;

public class GameGraphics {
    private final int BOARD_SIZE = 17;
    private final Color MOVE_COLOR = new Color(224, 224, 224);
    private final Color WALL_COLOR = new Color(192, 192, 192);
    private final Color PLAYER1_COLOR = new Color(255, 51, 51);
    private final Color PLAYER2_COLOR = new Color(51, 153, 255);
    private final Color VMOVE_COLOR = new Color(255, 178, 102);

    private JFrame frame;
    private JButton[][] buttons;

    public GameGraphics() {
        this.frame = new JFrame();
        addBoardButtons();
        setBoardButtonsColor();
        setFrame();
    }

    public JButton[][] getButtons() {
        return this.buttons;
    }

    private void setFrame() {
        this.frame.setTitle("Quoridor");
        this.frame.setSize(1300, 1000);
        this.frame.setFocusable(true);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setResizable(false);
        this.frame.setVisible(true);
    }

    private void addBoardButtons() {
        JPanel boardPanel = new JPanel(new GridBagLayout());
        boardPanel.setSize(1000, 1000);
        setBoardButtons(boardPanel);
        this.frame.add(boardPanel, "West"); // After adding the buttons to the panel, it adds the panel to the frame
    }

    private void setBoardButtons(JPanel boardPanel) {
        int width, height;
        int SIZE1 = 39, SIZE2 = 11;
        GridBagConstraints constraints = new GridBagConstraints();
        this.buttons = new JButton[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                this.buttons[i][j] = new JButton();

                // Setting width and height by the button's coordinates
                height = i % 2 == 0 ? SIZE1 : SIZE2;
                width = j % 2 == 0 ? SIZE1 : SIZE2;

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

    private void setBoardButtonsColor() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                Color buttonColor;
                if (i % 2 == 0 && j % 2 == 0)
                    buttonColor = MOVE_COLOR;
                else
                    buttonColor = WALL_COLOR;

                this.buttons[i][j].setBackground(buttonColor);
            }
        }
        this.buttons[16][8].setBackground(PLAYER1_COLOR);
        this.buttons[0][8].setBackground(PLAYER2_COLOR);
    }

    public void setValidMove(int x, int y) {
        this.buttons[y][x].setBackground(VMOVE_COLOR);
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

    public Tuple<Integer, Integer> getBoardButtonCoordinates(JButton button) {
        // The function returns the coordinates of the clicked button (The tens' digit is the row and the units' digit is the column)
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (this.buttons[i][j] == button) {
                    return new Tuple<>(j, i);
                }
            }
        }
        return null;
    }
}

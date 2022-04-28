package com.quoridorproj;

import javax.swing.*;
import java.awt.*;

public class GameGraphics {
    private final int BOARD_SIZE = 17;
    private ColorMap colorMap;

    private GameHandler context;
    private JFrame frame;
    private JPanel mainPanel;
    private JButton[][] buttons;
    private PreGamePanel preGamePanel;
    private SidePanel sidePanel;
    private PostGamePanel postGamePanel;

    public GameGraphics(GameHandler context) {
        this.colorMap = new ColorMap();
        this.context = context;
        this.frame = new JFrame();
        this.mainPanel = new JPanel(new FlowLayout());

        this.preGamePanel = new PreGamePanel();
        this.sidePanel = new SidePanel();
        this.postGamePanel = new PostGamePanel();

        addBoardButtons();
        setBoardButtonsColor();
        setPreGamePanel(true, true);

        setFrame();
    }

    public JPanel getPreGamePanel() { return this.preGamePanel.getPreGamePanel(); }

    public void setPreGamePanel(boolean playAIButtonState, boolean playTwoPlayersButtonState) {
        this.preGamePanel.setPreGamePanel(this.context, this.mainPanel);
        setButtonEnabled(getPlayAIButton(), playAIButtonState);
        setButtonEnabled(getPlayTwoPlayersButton(), playTwoPlayersButtonState);
        setBoardButtonsEnabled(false);
    }

    public JPanel getSidePanel() { return this.sidePanel.getSidePanel(); }

    public void setSidePanel(boolean continueButtonState, boolean rotateButtonState, boolean undoButtonState) {
        this.sidePanel.setSidePanel(this.context, this.mainPanel);
        setButtonEnabled(getContinueButton(), continueButtonState);
        setButtonEnabled(getRotateButton(), rotateButtonState);
        setButtonEnabled(getUndoButton(), undoButtonState);
        setBoardButtonsEnabled(true);
    }

    public JPanel getPostGamePanel() { return this.postGamePanel.getPostGamePanel();}

    public void setPostGamePanel(int winnerID, int numTurns) {
        this.postGamePanel.setPostGamePanel(this.mainPanel, winnerID, numTurns);
        setBoardButtonsEnabled(false);
    }

    public JButton getPlayAIButton() { return this.preGamePanel.getPlayAIButton(); }

    public JButton getPlayTwoPlayersButton() { return this.preGamePanel.getPlayTwoPlayersButton(); }

    public JButton getContinueButton() { return this.sidePanel.getContinueButton(); }

    public JButton getUndoButton() { return this.sidePanel.getUndoButton(); }

    public JButton getRotateButton() { return this.sidePanel.getRotateButton(); }

    private void addBoardButtons() {
        JPanel boardPanel = new JPanel(new GridBagLayout());
        boardPanel.setSize(1000, 1000);
        setBoardButtons(boardPanel);
        this.mainPanel.add(boardPanel, BorderLayout.WEST); // After adding the buttons to the panel, it adds the panel to the frame
    }

    private void setBoardButtons(JPanel boardPanel) {
        int width, height;
        int SIZE1 = 39, SIZE2 = 11;
        GridBagConstraints constraints = new GridBagConstraints();
        this.buttons = new JButton[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                this.buttons[i][j] = new JButton();

                // Sets width and height by the button's coordinates
                height = i % 2 == 0 ? SIZE1 : SIZE2;
                width = j % 2 == 0 ? SIZE1 : SIZE2;

                // Sets the button's size and margins
                this.buttons[i][j].setPreferredSize(new Dimension(width, height));
                this.buttons[i][j].setMaximumSize(new Dimension(width, height));
                constraints.fill = GridBagConstraints.NONE;
                constraints.gridx = j; constraints.gridy = i;
                constraints.ipadx = width; constraints.ipady = height;
                constraints.insets = new Insets(2, 2, 2, 2);

                // Adds the button to the board panel
                boardPanel.add(this.buttons[i][j], constraints);
            }
        }
    }

    private void setBoardButtonsColor() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                Color buttonColor;
                if (i % 2 == 0 && j % 2 == 0)
                    buttonColor = this.colorMap.get(ColorEnum.EMPTY_COLOR);
                else
                    buttonColor = this.colorMap.get(ColorEnum.WALL_COLOR);

                this.buttons[i][j].setBackground(buttonColor);
            }
        }
        this.buttons[BOARD_SIZE - 1][BOARD_SIZE / 2].setBackground(this.colorMap.get(ColorEnum.PLAYER1_COLOR));
        this.buttons[0][BOARD_SIZE / 2].setBackground(this.colorMap.get(ColorEnum.PLAYER2_COLOR));
    }

    private void setFrame() {
        this.frame.setTitle("Quoridor");
        this.frame.setSize(1300, 1000);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setResizable(false);
        this.frame.setFocusable(true);
        this.frame.setVisible(true);
        this.frame.add(this.mainPanel);
    }

    public void setBoardButtonsEnabled(boolean state) {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                this.buttons[i][j].setEnabled(state);
            }
        }
    }

    public void setBoardButtonsListener() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                this.buttons[i][j].addActionListener(this.context);
            }
        }
    }

    public void setButtonEnabled(JButton button, boolean state) {
        button.setEnabled(state);
    }

    public void removePanel(JPanel panel) {
        this.mainPanel.remove(panel);
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

    public void setValidMove(int x, int y, int currentTurn) {
        Color validMoveColor = (currentTurn == BoardFill.PLAYER1.value() ? this.colorMap.get(ColorEnum.VALID_MOVE_PLAYER1_COLOR) : this.colorMap.get(ColorEnum.VALID_MOVE_PLAYER2_COLOR));
        this.buttons[y][x].setBackground(validMoveColor);
    }

    public void removeValidMove(int x, int y) {
        this.buttons[y][x].setBackground(this.colorMap.get(ColorEnum.EMPTY_COLOR));
    }

    public void paintPlayer(int x, int y, int currentTurn) {
        Color playerColor = (currentTurn == BoardFill.PLAYER1.value() ? this.colorMap.get(ColorEnum.PLAYER1_COLOR) : this.colorMap.get(ColorEnum.PLAYER2_COLOR));
        this.buttons[y][x].setBackground(playerColor);
    }

    public void removePlayer(int x, int y) {
        this.buttons[y][x].setBackground(this.colorMap.get(ColorEnum.EMPTY_COLOR));
    }

    public void paintWall(int x, int y, Orientation orientation) {
        if (orientation == Orientation.HORIZONTAL) {
            this.buttons[y][x].setBackground(this.colorMap.get(ColorEnum.PLACED_WALL_COLOR));
            this.buttons[y][x + 1].setBackground(this.colorMap.get(ColorEnum.PLACED_WALL_COLOR));
            this.buttons[y][x - 1].setBackground(this.colorMap.get(ColorEnum.PLACED_WALL_COLOR));
        } else {
            this.buttons[y][x].setBackground(this.colorMap.get(ColorEnum.PLACED_WALL_COLOR));
            this.buttons[y + 1][x].setBackground(this.colorMap.get(ColorEnum.PLACED_WALL_COLOR));
            this.buttons[y - 1][x].setBackground(this.colorMap.get(ColorEnum.PLACED_WALL_COLOR));
        }
    }

    public void deleteWall(int x, int y, Orientation orientation) {
        if (orientation == Orientation.HORIZONTAL) {
            this.buttons[y][x].setBackground(this.colorMap.get(ColorEnum.WALL_COLOR));
            this.buttons[y][x + 1].setBackground(this.colorMap.get(ColorEnum.WALL_COLOR));
            this.buttons[y][x - 1].setBackground(this.colorMap.get(ColorEnum.WALL_COLOR));
        } else {
            this.buttons[y][x].setBackground(this.colorMap.get(ColorEnum.WALL_COLOR));
            this.buttons[y + 1][x].setBackground(this.colorMap.get(ColorEnum.WALL_COLOR));
            this.buttons[y - 1][x].setBackground(this.colorMap.get(ColorEnum.WALL_COLOR));
        }
    }

    public void rotate(Orientation orientation) {
        this.sidePanel.updateRotateButton(orientation);
    }

    public void updateComment(String comment) {
        this.sidePanel.updateComment(comment);
    }

    public void updateGameStatus(String gameStatus) {
        this.sidePanel.updateGameStatus(gameStatus);
    }

    public void updatePlayerWallsLeft(int playerID, int wallsLeft) {
        this.sidePanel.updatePlayerWallsLeft(playerID, wallsLeft);
    }

    public void addMoveToList(String move) {
        this.postGamePanel.addMoveToList(move);
    }

    public void reset() {
        // FIXME
        setBoardButtonsColor();
        updateComment("");
    }
}

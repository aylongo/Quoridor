package com.quoridorproj;

import javax.swing.*;
import java.awt.*;

public class GameGraphics {
    private final int BOARD_SIZE = 17;
    // TODO: Colors HashMap
    private final Color EMPTY_COLOR = new Color(224, 224, 224);
    private final Color WALL_COLOR = new Color(192, 192, 192);
    private final Color PLACED_WALL_COLOR = new Color(105, 105, 105, 255);
    private final Color PLAYER1_COLOR = new Color(255, 64, 64);
    private final Color PLAYER2_COLOR = new Color(51, 153, 255);
    private final Color VALID_MOVE_PLAYER1_COLOR = new Color(255, 195, 139);
    private final Color VALID_MOVE_PLAYER2_COLOR = new Color(198, 226, 255);

    private JFrame frame;
    private JPanel panel;
    private JPanel preGameSidePanel;
    private JPanel sidePanel;
    private JButton[][] buttons;
    private JButton continueButton;
    private JButton rotateButton;
    private JButton undoButton;
    private JButton playAIButton;
    private JButton playTwoPlayersButton;
    private JLabel comment;
    private JLabel gameStatus;
    private JLabel[] playersWallsLeft;

    public GameGraphics() {
        this.frame = new JFrame();
        this.panel = new JPanel(new FlowLayout());
        addBoardButtons();
        setBoardButtonsColor();

        setSidePanel();
        setPreGameSidePanel();
        setFrame();
    }

    public JButton[][] getButtons() {
        return this.buttons;
    }

    public JButton getContinueButton() {
        return this.continueButton;
    }

    public JButton getUndoButton() {
        return this.undoButton;
    }

    public JButton getRotateButton() {
        return this.rotateButton;
    }

    public JButton getPlayAIButton() { return this.playAIButton; }

    public JButton getPlayTwoPlayersButton() { return this.playTwoPlayersButton; }

    private void setFrame() {
        this.frame.setTitle("Quoridor");
        this.frame.setSize(1300, 1000);
        this.frame.setFocusable(true);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setResizable(false);
        this.frame.setVisible(true);
        this.frame.add(this.panel);
    }

    private void addBoardButtons() {
        JPanel boardPanel = new JPanel(new GridBagLayout());
        boardPanel.setSize(1000, 1000);
        setBoardButtons(boardPanel);
        this.panel.add(boardPanel, BorderLayout.WEST); // After adding the buttons to the panel, it adds the panel to the frame
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
                    buttonColor = EMPTY_COLOR;
                else
                    buttonColor = WALL_COLOR;

                this.buttons[i][j].setBackground(buttonColor);
            }
        }
        this.buttons[BOARD_SIZE - 1][BOARD_SIZE / 2].setBackground(PLAYER1_COLOR);
        this.buttons[0][BOARD_SIZE / 2].setBackground(PLAYER2_COLOR);
    }

    private void setPreGameSidePanel() {
        // Sets an outer panel to hold the pre-game side panel
        this.preGameSidePanel = new JPanel();
        this.preGameSidePanel.setBorder(BorderFactory.createEmptyBorder(100, 0, 0, 15));

        // Sets the panel
        JPanel preGameSidePanel = new JPanel();
        preGameSidePanel.setLayout(new FlowLayout());
        preGameSidePanel.setPreferredSize(new Dimension(300, 400));
        preGameSidePanel.setMaximumSize(new Dimension(300, 400));
        preGameSidePanel.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.GRAY));
        preGameSidePanel.setBackground(WALL_COLOR);

        // Adds the components to the panel
        setPlayAIButton();
        preGameSidePanel.add(this.playAIButton);
        setPlayTwoPlayersButton();
        preGameSidePanel.add(this.playTwoPlayersButton);

        this.preGameSidePanel.add(preGameSidePanel);
        this.panel.add(this.preGameSidePanel, BorderLayout.EAST);
    }

    private void setSidePanel() {
        // Sets an outer panel to hold the side panel
        this.sidePanel = new JPanel();
        this.sidePanel.setBorder(BorderFactory.createEmptyBorder(100, 0, 0, 15));

        // Sets the side panel
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new FlowLayout());
        sidePanel.setPreferredSize(new Dimension(300, 400));
        sidePanel.setMaximumSize(new Dimension(300, 400));
        sidePanel.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.GRAY));
        sidePanel.setBackground(WALL_COLOR);

        // Adds the components to the side panel
        setGameStatusLabel();
        sidePanel.add(this.gameStatus);
        setCommentLabel();
        sidePanel.add(this.comment);
        setContinueButton();
        sidePanel.add(this.continueButton);
        setUndoButton();
        sidePanel.add(this.undoButton);
        setRotateButton();
        sidePanel.add(this.rotateButton);
        setPlayersWallsLeftLables();
        sidePanel.add(this.playersWallsLeft[BoardFill.PLAYER1.value()]);
        sidePanel.add(this.playersWallsLeft[BoardFill.PLAYER2.value()]);

        this.sidePanel.add(sidePanel);
        this.panel.add(this.sidePanel, BorderLayout.EAST);
    }

    private void setGameStatusLabel() {
        this.gameStatus = new JLabel("", JLabel.CENTER);
        this.gameStatus.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.gameStatus.setAlignmentY(Component.TOP_ALIGNMENT);
        this.gameStatus.setPreferredSize(new Dimension(290, 80));
        this.gameStatus.setMaximumSize(new Dimension(290, 80));
        this.gameStatus.setFont(new Font("Arial", Font.BOLD, 22));
        this.gameStatus.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
    }

    private void setCommentLabel() {
        this.comment = new JLabel("", JLabel.CENTER);
        this.comment.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.comment.setPreferredSize(new Dimension(290, 80));
        this.comment.setMaximumSize(new Dimension(290, 80));
        this.comment.setFont(new Font("Arial", Font.PLAIN, 16));
        this.comment.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
    }

    private void setContinueButton() {
        this.continueButton = new JButton("Continue");
        this.continueButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.continueButton.setFont(new Font("Arial", Font.PLAIN, 15));
        this.continueButton.setBackground(EMPTY_COLOR);
    }

    private void setUndoButton() {
        this.undoButton = new JButton("Undo");
        this.undoButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.undoButton.setFont(new Font("Arial", Font.PLAIN, 15));
        this.undoButton.setBackground(EMPTY_COLOR);
    }

    private void setRotateButton() {
        this.rotateButton = new JButton("Horizontal");
        this.rotateButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.rotateButton.setFont(new Font("Arial", Font.PLAIN, 15));
        this.rotateButton.setBackground(EMPTY_COLOR);
    }

    private void setPlayAIButton() {
        this.playAIButton = new JButton("Play Against AI");
        this.playAIButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.playAIButton.setFont(new Font("Arial", Font.PLAIN, 15));
        this.playAIButton.setBackground(EMPTY_COLOR);
    }

    private void setPlayTwoPlayersButton() {
        this.playTwoPlayersButton = new JButton("Play 2 Players");
        this.playTwoPlayersButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.playTwoPlayersButton.setFont(new Font("Arial", Font.PLAIN, 15));
        this.playTwoPlayersButton.setBackground(EMPTY_COLOR);
    }

    private void setPlayersWallsLeftLables() {
        JLabel playerWallsLeftLabel;
        this.playersWallsLeft = new JLabel[3];

        for (int i = BoardFill.PLAYER1.value(); i < 3; i++) {
            playerWallsLeftLabel = new JLabel("", JLabel.CENTER);
            playerWallsLeftLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            playerWallsLeftLabel.setPreferredSize(new Dimension(290, 80));
            playerWallsLeftLabel.setMaximumSize(new Dimension(290, 80));
            playerWallsLeftLabel.setFont(new Font("Arial", Font.BOLD, 16));
            playerWallsLeftLabel.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
            this.playersWallsLeft[i] = playerWallsLeftLabel;
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

    public static void setButtonListener(JButton button, GameHandler context) {
        button.addActionListener(context);
    }

    public static void setButtonEnabled(JButton button, boolean state) {
        button.setEnabled(state);
    }

    public void setPreGameSidePanelVisibility(boolean state) {
        this.preGameSidePanel.setVisible(state);
    }

    public void setSidePanelVisibility(boolean state) {
        this.sidePanel.setVisible(state);
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
        Color validColor = (currentTurn == BoardFill.PLAYER1.value() ? VALID_MOVE_PLAYER1_COLOR : VALID_MOVE_PLAYER2_COLOR);
        this.buttons[y][x].setBackground(validColor);
    }

    public void removeValidMove(int x, int y) {
        this.buttons[y][x].setBackground(EMPTY_COLOR);
    }

    public void paintPlayer(int x, int y, int currentTurn) {
        Color playerColor = (currentTurn == BoardFill.PLAYER1.value() ? PLAYER1_COLOR : PLAYER2_COLOR);
        this.buttons[y][x].setBackground(playerColor);
    }

    public void removePlayer(int x, int y) {
        this.buttons[y][x].setBackground(EMPTY_COLOR);
    }

    public void paintWall(int x, int y, Orientation orientation) {
        if (orientation == Orientation.HORIZONTAL) {
            this.buttons[y][x].setBackground(PLACED_WALL_COLOR);
            this.buttons[y][x + 1].setBackground(PLACED_WALL_COLOR);
            this.buttons[y][x - 1].setBackground(PLACED_WALL_COLOR);
        } else {
            this.buttons[y][x].setBackground(PLACED_WALL_COLOR);
            this.buttons[y + 1][x].setBackground(PLACED_WALL_COLOR);
            this.buttons[y - 1][x].setBackground(PLACED_WALL_COLOR);
        }
    }

    public void deleteWall(int x, int y, Orientation orientation) {
        if (orientation == Orientation.HORIZONTAL) {
            this.buttons[y][x].setBackground(WALL_COLOR);
            this.buttons[y][x + 1].setBackground(WALL_COLOR);
            this.buttons[y][x - 1].setBackground(WALL_COLOR);
        } else {
            this.buttons[y][x].setBackground(WALL_COLOR);
            this.buttons[y + 1][x].setBackground(WALL_COLOR);
            this.buttons[y - 1][x].setBackground(WALL_COLOR);
        }
    }

    public void rotate(Orientation orientation) {
        if (orientation == Orientation.HORIZONTAL)
            this.rotateButton.setText("Vertical");
        else
            this.rotateButton.setText("Horizontal");
    }

    public void updateComment(String comment) {
        this.comment.setText(comment);
    }

    public void updateGameStatus(String gameStatus) {
        this.gameStatus.setText(gameStatus);
    }

    public void updatePlayerWallsLeft(int playerID, int wallsLeft) {
        this.playersWallsLeft[playerID].setText(String.format("Player %d: %d Walls", playerID, wallsLeft));
    }

    public void reset() {
        setBoardButtonsColor();
        updateComment("");
    }
}

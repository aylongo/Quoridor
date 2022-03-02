package com.quoridorproj;

import javax.swing.*;
import java.awt.*;

public class GameGraphics {
    private final int BOARD_SIZE = 17;
    // TODO: Colors HashMap
    private final Color EMPTY_COLOR = new Color(224, 224, 224);
    private final Color WALL_COLOR = new Color(192, 192, 192);
    private final Color PWALL_COLOR = new Color(133, 126, 105);
    private final Color PLAYER1_COLOR = new Color(255, 51, 51);
    private final Color PLAYER2_COLOR = new Color(51, 153, 255);
    private final Color VMOVE_COLOR = new Color(255, 178, 102);

    private JFrame frame;
    private JPanel panel;
    private JButton[][] buttons;
    private JButton continueButton;
    private JButton rotateButton;
    private JLabel comment;
    private JLabel gameStatus;
    private JLabel[] playersWallsLeft;

    public GameGraphics() {
        this.frame = new JFrame();
        this.panel = new JPanel(new BorderLayout());
        addBoardButtons();
        setBoardButtonsColor();
        setSidePanel();
        setFrame();
    }

    public JButton[][] getButtons() {
        return this.buttons;
    }

    public JButton getContinueButton() {
        return this.continueButton;
    }

    public JButton getRotateButton() {
        return this.rotateButton;
    }

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

                // Adding the button to the board panel
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
        this.buttons[16][8].setBackground(PLAYER1_COLOR);
        this.buttons[0][8].setBackground(PLAYER2_COLOR);
    }

    private void setSidePanel() {
        // Setting an outer panel to hold the side panel
        JPanel outer = new JPanel();
        outer.setBorder(BorderFactory.createEmptyBorder(300, 0, 0, 15));

        // Setting the side panel
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.PAGE_AXIS));
        sidePanel.setPreferredSize(new Dimension(300, 400));
        sidePanel.setMaximumSize(new Dimension(300, 400));
        sidePanel.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.GRAY));
        sidePanel.setBackground(WALL_COLOR);

        // Adding the components to the side panel
        setGameStatusLabel();
        sidePanel.add(this.gameStatus);
        setCommentLabel();
        sidePanel.add(this.comment);
        setContinueButton();
        sidePanel.add(this.continueButton);
        setRotateButton();
        sidePanel.add(this.rotateButton);
        setPlayersWallsLeftLables();
        sidePanel.add(this.playersWallsLeft[BoardFill.PLAYER1.value()]);
        sidePanel.add(this.playersWallsLeft[BoardFill.PLAYER2.value()]);

        outer.add(sidePanel);
        this.panel.add(outer, BorderLayout.EAST);
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

    private void setRotateButton() {
        this.rotateButton = new JButton("Horizontal");
        this.rotateButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.rotateButton.setFont(new Font("Arial", Font.PLAIN, 15));
        this.rotateButton.setBackground(EMPTY_COLOR);
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

    public void setValidMove(int x, int y) {
        this.buttons[y][x].setBackground(VMOVE_COLOR);
    }

    public void removeValidMove(int x, int y) {
        this.buttons[y][x].setBackground(EMPTY_COLOR);
    }

    public void removePlayer(int x, int y) {
        this.buttons[y][x].setBackground(EMPTY_COLOR);
    }

    public void paintPlayer(int x, int y, int currentTurn) {
        Color playerColor = (currentTurn == BoardFill.PLAYER1.value() ? PLAYER1_COLOR : PLAYER2_COLOR);
        this.buttons[y][x].setBackground(playerColor);
    }

    public void paintWall(int x, int y, Orientation orientation) {
        if (orientation == Orientation.HORIZONTAL) {
            this.buttons[y][x].setBackground(PWALL_COLOR);
            this.buttons[y][x + 1].setBackground(PWALL_COLOR);
            this.buttons[y][x - 1].setBackground(PWALL_COLOR);
        } else {
            this.buttons[y][x].setBackground(PWALL_COLOR);
            this.buttons[y + 1][x].setBackground(PWALL_COLOR);
            this.buttons[y - 1][x].setBackground(PWALL_COLOR);
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

    public void updatePlayerWallsLeft(int player, int wallsLeft) {
        this.playersWallsLeft[player].setText(String.format("Player %d: %d Walls", player, wallsLeft));
    }

    public void reset() {
        setBoardButtonsColor();
        updateComment("");
    }
}

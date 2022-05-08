package com.quoridorproj;

import javax.swing.*;
import java.awt.*;

public class SidePanel {
    private ColorMap colorMap;
    private JPanel sidePanel;
    private JButton continueButton;
    private JButton rotateButton;
    private JButton undoButton;
    private JLabel comment;
    private JLabel gameStatus;
    private JLabel[] playersWallsLeft;

    /**
     * SidePanel Class Constructor
     */
    public SidePanel() {
        this.colorMap = new ColorMap();
    }

    public JPanel getSidePanel() { return this.sidePanel; }

    public JButton getContinueButton() { return this.continueButton; }

    public JButton getRotateButton() {
        return this.rotateButton;
    }

    public JButton getUndoButton() {
        return this.undoButton;
    }

    /**
     * The function initializes the GUI's side panel
     *
     * @param context The class which the function was called from
     * @param mainPanel The GUI's main panel
     */
    public void setSidePanel(GameHandler context, JPanel mainPanel) {
        // Sets an outer panel to hold the side panel
        this.sidePanel = new JPanel();
        this.sidePanel.setBorder(BorderFactory.createEmptyBorder(100, 0, 0, 15));

        // Sets the side panel
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new FlowLayout());
        sidePanel.setPreferredSize(new Dimension(300, 400));
        sidePanel.setMaximumSize(new Dimension(300, 400));
        sidePanel.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.GRAY));
        sidePanel.setBackground(this.colorMap.get(ColorEnum.WALL_COLOR));

        // Adds the components to the side panel
        setGameStatusLabel();
        sidePanel.add(this.gameStatus);
        setCommentLabel();
        sidePanel.add(this.comment);
        setContinueButton();
        sidePanel.add(this.continueButton);
        this.continueButton.addActionListener(context);
        setUndoButton();
        sidePanel.add(this.undoButton);
        this.undoButton.addActionListener(context);
        setRotateButton();
        sidePanel.add(this.rotateButton);
        this.rotateButton.addActionListener(context);
        setPlayersWallsLeftLables();
        sidePanel.add(this.playersWallsLeft[BoardFill.PLAYER1.value()]);
        sidePanel.add(this.playersWallsLeft[BoardFill.PLAYER2.value()]);

        this.sidePanel.add(sidePanel);

        mainPanel.add(this.sidePanel, BorderLayout.EAST);
    }

    /**
     * The function initializes the game status label
     */
    private void setGameStatusLabel() {
        this.gameStatus = new JLabel("", JLabel.CENTER);
        this.gameStatus.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.gameStatus.setAlignmentY(Component.TOP_ALIGNMENT);
        this.gameStatus.setPreferredSize(new Dimension(290, 80));
        this.gameStatus.setMaximumSize(new Dimension(290, 80));
        this.gameStatus.setFont(new Font("Arial", Font.BOLD, 22));
        this.gameStatus.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
    }

    /**
     * The function initializes the comment label
     */
    private void setCommentLabel() {
        this.comment = new JLabel("", JLabel.CENTER);
        this.comment.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.comment.setPreferredSize(new Dimension(290, 60));
        this.comment.setMaximumSize(new Dimension(290, 60));
        this.comment.setFont(new Font("Arial", Font.PLAIN, 16));
        this.comment.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
    }

    /**
     * The function initializes the continue button
     */
    private void setContinueButton() {
        this.continueButton = new JButton("Continue");
        this.continueButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.continueButton.setFont(new Font("Arial", Font.PLAIN, 15));
        this.continueButton.setBackground(this.colorMap.get(ColorEnum.EMPTY_COLOR));
    }

    /**
     * The function initializes the undo button
     */
    private void setUndoButton() {
        this.undoButton = new JButton("Undo");
        this.undoButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.undoButton.setFont(new Font("Arial", Font.PLAIN, 15));
        this.undoButton.setBackground(this.colorMap.get(ColorEnum.EMPTY_COLOR));
    }

    /**
     * The function initializes the rotate button
     */
    private void setRotateButton() {
        this.rotateButton = new JButton("Horizontal");
        this.rotateButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.rotateButton.setFont(new Font("Arial", Font.PLAIN, 15));
        this.rotateButton.setBackground(this.colorMap.get(ColorEnum.EMPTY_COLOR));
    }

    /**
     * The function initializes the number of walls left labels
     */
    private void setPlayersWallsLeftLables() {
        JLabel playerWallsLeftLabel;
        this.playersWallsLeft = new JLabel[3];

        for (int i = BoardFill.PLAYER1.value(); i < 3; i++) {
            playerWallsLeftLabel = new JLabel("", JLabel.CENTER);
            playerWallsLeftLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            playerWallsLeftLabel.setPreferredSize(new Dimension(290, 80));
            playerWallsLeftLabel.setMaximumSize(new Dimension(290, 80));
            playerWallsLeftLabel.setFont(new Font("Arial", Font.BOLD, 16));
            playerWallsLeftLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
            this.playersWallsLeft[i] = playerWallsLeftLabel;
        }
    }

    /**
     * The function updates the text on the rotate button according to the given orientation
     *
     * @param orientation An Orientation enum according to which the text on the button changes
     */
    public void updateRotateButton(Orientation orientation) {
        if (orientation == Orientation.HORIZONTAL)
            this.rotateButton.setText("Vertical");
        else
            this.rotateButton.setText("Horizontal");
    }

    /**
     * The function updates the comment label
     *
     * @param comment The String to which the label updates to
     */
    public void updateComment(String comment) { this.comment.setText(comment); }

    /**
     * The function updates the game status label
     *
     * @param gameStatus The String to which the label updates to
     */
    public void updateGameStatus(String gameStatus) { this.gameStatus.setText(gameStatus); }

    /**
     * The function updates the player's number of walls left
     *
     * @param playerID The player's id
     * @param wallsLeft The number of walls left the label changes to
     */
    public void updatePlayerWallsLeft(int playerID, int wallsLeft) { this.playersWallsLeft[playerID].setText(String.format("Player %d: %d Walls", playerID, wallsLeft)); }

    /**
     * The function resets the text on the rotate button (to 'Horizontal')
     */
    public void resetRotateButton() { this.rotateButton.setText("Horizontal"); }
}

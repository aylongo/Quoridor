package com.quoridorproj;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PostGamePanel {
    private ColorMap colorMap;
    private JPanel postGamePanel;
    private JScrollPane movesListScroller;
    private DefaultListModel<String> movesListModel;
    private JList<String> movesList;
    private JLabel gameWinner;
    private JLabel numTurns;

    public PostGamePanel() {
        this.colorMap = new ColorMap();
        this.movesListModel = new DefaultListModel<String>();
    }

    public JPanel getPostGamePanel() {
        return this.postGamePanel;
    }

    public void setPostGamePanel(JPanel mainPanel, int winnerID, int numTurns) {
        // Sets an outer panel to hold the side panel
        this.postGamePanel = new JPanel();
        this.postGamePanel.setBorder(BorderFactory.createEmptyBorder(100, 0, 0, 15));

        // Sets the side panel
        JPanel postGamePanel = new JPanel();
        postGamePanel.setLayout(new BoxLayout(postGamePanel, BoxLayout.Y_AXIS));
        // sidePanel.setLayout(new FlowLayout());
        postGamePanel.setPreferredSize(new Dimension(300, 400));
        postGamePanel.setMaximumSize(new Dimension(300, 400));
        postGamePanel.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.GRAY));
        postGamePanel.setBackground(this.colorMap.get(ColorEnum.WALL_COLOR));

        // Adds the components to the side panel
        JLabel title = new JLabel("Game Statistics");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setBorder(new EmptyBorder(20, 0, 20, 0));
        postGamePanel.add(title);

        setGameWinnerLabel(winnerID);
        postGamePanel.add(this.gameWinner);
        setMovesList();
        postGamePanel.add(this.movesListScroller);
        setNumTurnsLabel(numTurns);
        postGamePanel.add(this.numTurns);

        this.postGamePanel.add(postGamePanel);

        mainPanel.add(this.postGamePanel, BorderLayout.EAST);
    }

    private void setGameWinnerLabel(int winnerID) {
        this.gameWinner = new JLabel(String.format("PLAYER %d WON!", winnerID), JLabel.CENTER);
        this.gameWinner.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.gameWinner.setFont(new Font("Arial", Font.BOLD, 16));
        this.gameWinner.setBorder(BorderFactory.createEmptyBorder(20, 0, 30, 0));
    }

    private void setMovesList() {
        this.movesList = new JList<>();
        this.movesList.setModel(this.movesListModel);
        this.movesList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        this.movesList.setVisibleRowCount(-1);
        this.movesList.setFont(new Font("Arial", Font.PLAIN, 12));
        this.movesList.setFixedCellWidth(25); this.movesList.setFixedCellHeight(20);

        this.movesListScroller = new JScrollPane(this.movesList);
        this.movesListScroller.setPreferredSize(new Dimension(220, 150));
        this.movesListScroller.setMaximumSize(new Dimension(220, 150));
        this.movesListScroller.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    private void setNumTurnsLabel(int numTurns) {
        this.numTurns = new JLabel(String.format("Game has ended in %d turns!", numTurns), JLabel.CENTER);
        this.numTurns.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.numTurns.setFont(new Font("Arial", Font.BOLD, 14));
        this.numTurns.setBorder(BorderFactory.createEmptyBorder(30, 0, 10, 0));
    }

    public void addMoveToList(String move) {
        this.movesListModel.addElement(move);
    }
}

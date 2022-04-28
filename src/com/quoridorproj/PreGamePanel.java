package com.quoridorproj;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PreGamePanel {
    private ColorMap colorMap;
    private JPanel preGamePanel;
    private JButton playAIButton;
    private JButton playTwoPlayersButton;

    public PreGamePanel() {
        this.colorMap = new ColorMap();
    }

    public JPanel getPreGamePanel() {
        return this.preGamePanel;
    }

    public JButton getPlayAIButton() {
        return this.playAIButton;
    }

    public JButton getPlayTwoPlayersButton() {
        return this.playTwoPlayersButton;
    }

    public void setPreGamePanel(GameHandler context, JPanel mainPanel) {
        // Sets an outer panel to hold the pre-game side panel
        this.preGamePanel = new JPanel();
        this.preGamePanel.setBorder(BorderFactory.createEmptyBorder(100, 0, 0, 15));

        // Sets the panel
        JPanel preGamePanel = new JPanel();
        preGamePanel.setLayout(new FlowLayout());
        preGamePanel.setPreferredSize(new Dimension(300, 400));
        preGamePanel.setMaximumSize(new Dimension(300, 400));
        preGamePanel.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.GRAY));
        preGamePanel.setBackground(this.colorMap.get(ColorEnum.WALL_COLOR));

        // Adds the components to the panel
        JLabel title = new JLabel("Choose Your Game Mode");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setBorder(new EmptyBorder(30, 0, 50, 0));
        preGamePanel.add(title);

        setPlayAIButton();
        preGamePanel.add(this.playAIButton);
        this.playAIButton.addActionListener(context);
        setPlayTwoPlayersButton();
        preGamePanel.add(this.playTwoPlayersButton);
        this.playTwoPlayersButton.addActionListener(context);

        this.preGamePanel.add(preGamePanel);

        mainPanel.add(this.preGamePanel, BorderLayout.EAST);
    }

    private void setPlayAIButton() {
        this.playAIButton = new JButton("Play Against AI");
        this.playAIButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.playAIButton.setFont(new Font("Arial", Font.PLAIN, 15));
        this.playAIButton.setBackground(this.colorMap.get(ColorEnum.EMPTY_COLOR));
    }

    private void setPlayTwoPlayersButton() {
        this.playTwoPlayersButton = new JButton("Play 2 Players");
        this.playTwoPlayersButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.playTwoPlayersButton.setFont(new Font("Arial", Font.PLAIN, 15));
        this.playTwoPlayersButton.setBackground(this.colorMap.get(ColorEnum.EMPTY_COLOR));
    }
}

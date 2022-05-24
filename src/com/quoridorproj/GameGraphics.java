package com.quoridorproj;

import javax.swing.*;
import java.awt.*;

public class GameGraphics {
    private final int BOARD_SIZE = 17;
    private ColorMap colorMap;

    private GameHandler context;
    private JFrame frame;
    private JPanel mainPanel;
    private JPanel boardPanel;
    private JButton[][] buttons;
    private PreGamePanel preGamePanel;
    private SidePanel sidePanel;
    private PostGamePanel postGamePanel;

    /**
     * GameGraphics Class Constructor
     *
     * @param context The class which the function was called from
     */
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

    private JPanel getPreGamePanel() { return this.preGamePanel.getPreGamePanel(); }

    /**
     * The function initializes the pre game panel and sets the graphics according to the situation
     *
     * @param playAIButtonState The state of the play against an AI button (True for enabled and False for disabled)
     * @param playTwoPlayersButtonState The state of the two players game button (True for enabled and False for disabled)
     */
    private void setPreGamePanel(boolean playAIButtonState, boolean playTwoPlayersButtonState) {
        this.preGamePanel.setPreGamePanel(this.context, this.mainPanel);
        setButtonEnabled(getPlayAIButton(), playAIButtonState);
        setButtonEnabled(getPlayTwoPlayersButton(), playTwoPlayersButtonState);
        setBoardButtonsEnabled(false);
    }

    private JPanel getSidePanel() { return this.sidePanel.getSidePanel(); }

    /**
     * The function initializes the side panel and sets the graphics according to the situation
     *
     * @param continueButtonState The state of the continue button (True for enabled and False for disabled)
     * @param rotateButtonState The state of the rotate button (True for enabled and False for disabled)
     * @param undoButtonState The state of the undo button (True for enabled and False for disabled)
     */
    private void setSidePanel(boolean continueButtonState, boolean rotateButtonState, boolean undoButtonState) {
        this.sidePanel.setSidePanel(this.context, this.mainPanel);
        setButtonEnabled(getContinueButton(), continueButtonState);
        setButtonEnabled(getRotateButton(), rotateButtonState);
        setButtonEnabled(getUndoButton(), undoButtonState);
        setBoardButtonsEnabled(true);
    }

    private JPanel getPostGamePanel() { return this.postGamePanel.getPostGamePanel(); }

    /**
     * The function initializes the post game panel and sets the graphics according to the situation
     *
     * @param winnerID The game winner's id
     * @param numTurns The game's total number of turns
     */
    private void setPostGamePanel(int winnerID, int numTurns) {
        this.postGamePanel.setPostGamePanel(this.context, this.mainPanel, winnerID, numTurns);
        setBoardButtonsEnabled(false);
    }

    public JButton getPlayAIButton() { return this.preGamePanel.getPlayAIButton(); }

    public JButton getPlayTwoPlayersButton() { return this.preGamePanel.getPlayTwoPlayersButton(); }

    public JButton getContinueButton() { return this.sidePanel.getContinueButton(); }

    public JButton getUndoButton() { return this.sidePanel.getUndoButton(); }

    public JButton getRotateButton() { return this.sidePanel.getRotateButton(); }

    public JButton getPlayAgainButton() { return this.postGamePanel.getPlayAgainButton(); }

    public int getSelectedMovesListIndex() { return this.postGamePanel.getSelectedMovesListIndex(); }

    /**
     * The function creates a panel for the GUI's game board, adds the board buttons and adds it to the main panel
     */
    private void addBoardButtons() {
        this.boardPanel = new JPanel(new GridBagLayout());
        this.boardPanel.setSize(1000, 1000);
        setBoardButtons(this.boardPanel);
        this.mainPanel.add(this.boardPanel, BorderLayout.WEST); // After adding the buttons to the panel, it adds the panel to the frame
    }

    /**
     * The function adds the board buttons to the given panel
     *
     * @param boardPanel The board JPanel
     */
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

    /**
     * The function initializes the board buttons colors
     */
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

    /**
     * The function initializes the GUI's window
     */
    private void setFrame() {
        this.frame.setTitle("Quoridor");
        this.frame.setSize(1300, 1000);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setResizable(false);
        this.frame.setFocusable(true);
        this.frame.setVisible(true);
        this.frame.add(this.mainPanel);
    }

    /**
     * The function initializes the graphics of the beginning of the game
     *
     * @param playerID The player's id which plays first
     * @param playerOneWallsLeft Number of walls left for player one
     * @param playerTwoWallsLeft Number of walls left for player two
     */
    public void setStartGameGraphics(int playerID, int playerOneWallsLeft, int playerTwoWallsLeft) {
        removePanel(this.getPreGamePanel());
        setSidePanel(false, true, false);
        updatePlayerWallsLeft(BoardFill.PLAYER1.value(), playerOneWallsLeft);
        updatePlayerWallsLeft(BoardFill.PLAYER2.value(), playerTwoWallsLeft);
        updateGameStatus(String.format("Player %d's Turn!", playerID));
        this.postGamePanel.resetMovesListModel();
        setBoardButtonsListener();
    }

    /**
     * The function initializes the graphics of the ending of the game
     *
     * @param winnerID The game winner's id
     * @param numTurns The game's total number of turns
     */
    public void setPostGameGraphics(int winnerID, int numTurns) {
        removePanel(this.getSidePanel());
        setPostGamePanel(winnerID, numTurns);
    }

    /**
     * The function initializes the graphics for an AI turn
     */
    public void setAITurnGraphics() {
        setBoardButtonsEnabled(false);
        setButtonEnabled(this.getRotateButton(), false);
    }

    /**
     * The function sets the state of the board buttons
     *
     * @param state The state of the board buttons (True for enabled and False for disabled)
     */
    private void setBoardButtonsEnabled(boolean state) {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                setButtonEnabled(this.buttons[i][j], state);
            }
        }
    }

    /**
     * The function adds an action listener to the board buttons in the context class
     */
    private void setBoardButtonsListener() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                this.buttons[i][j].addActionListener(this.context);
            }
        }
    }

    /**
     * The function sets the state of the given button
     *
     * @param button The button to set its state
     * @param state The state to set the button (True for enabled and False for disabled)
     */
    private void setButtonEnabled(JButton button, boolean state) {
        button.setEnabled(state);
    }

    /**
     * The function removes the given panel from the main panel
     *
     * @param panel The panel to remove
     */
    private void removePanel(JPanel panel) { this.mainPanel.remove(panel); }

    /**
     * The function returns the coordinates of the given button on the buttons board
     *
     * @param button The button being checked
     * @return A Tuple of the coordinates of the given button on the buttons board or null if not on it
     */
    public Tuple<Integer, Integer> getBoardButtonCoordinates(JButton button) {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (this.buttons[i][j] == button) {
                    return new Tuple<>(j, i);
                }
            }
        }
        return null;
    }

    /**
     * The function makes a move on the GUI
     *
     * @param playerID The id of the move making player
     * @param lastButtonX The X value of the player's current position on the buttons board
     * @param lastButtonY The Y value of the player's current position on the buttons board
     * @param buttonX The X value of the player's move position on the buttons board
     * @param buttonY The Y value of the player's move position on the buttons board
     * @param move The String describing the move
     */
    public void doMove(int playerID, int lastButtonX, int lastButtonY, int buttonX, int buttonY, String move) {
        setBoardButtonsEnabled(false);
        setButtonEnabled(this.getContinueButton(), true);
        setButtonEnabled(this.getUndoButton(), true);
        setButtonEnabled(this.getRotateButton(), false);
        removePlayer(lastButtonX, lastButtonY);
        paintPlayer(buttonX, buttonY, playerID);
        updateComment(String.format("Turn Made: %s", move));
    }

    /**
     * The function makes the last move (cancels the current move) on the GUI
     *
     * @param playerID The id of the move canceling player
     * @param currentButtonX The X value of the player's current position on the buttons board
     * @param currentButtonY The Y value of the player's current position on the buttons board
     * @param lastButtonX The X value of the player's last move position on the buttons board
     * @param lastButtonY The Y value of the player's last move position on the buttons board
     * @param canceledMove The String describing the move which was canceled
     */
    public void doMoveLastSquare(int playerID, int currentButtonX, int currentButtonY, int lastButtonX, int lastButtonY, String canceledMove) {
        setBoardButtonsEnabled(true);
        setButtonEnabled(this.getContinueButton(), false);
        setButtonEnabled(this.getUndoButton(), false);
        setButtonEnabled(this.getRotateButton(), true);
        removePlayer(currentButtonX, currentButtonY);
        paintPlayer(lastButtonX, lastButtonY, playerID);
        this.getRotateButton().setText("Horizontal");
        updateComment(String.format("Turn Canceled: %s", canceledMove));
    }

    /**
     * The function makes a wall place on the GUI
     *
     * @param playerID The id of the move making player
     * @param buttonX The X value of the wall's position on the buttons board
     * @param buttonY The Y value of the wall's position on the buttons board
     * @param wallOrientation The wall's orientation
     * @param wallsLeft The player's number of walls left
     * @param move The String describing the wall place
     */
    public void doPlaceWall(int playerID, int buttonX, int buttonY, Orientation wallOrientation, int wallsLeft, String move) {
        setBoardButtonsEnabled(false);
        setButtonEnabled(this.getContinueButton(), true);
        setButtonEnabled(this.getUndoButton(), true);
        setButtonEnabled(this.getRotateButton(), false);
        paintWall(buttonX, buttonY, wallOrientation);
        updatePlayerWallsLeft(playerID, wallsLeft);
        updateComment(String.format("Turn Made: %s", move));
    }

    /**
     * The function removes a wall from the GUI
     *
     * @param playerID The id of the wall removing player
     * @param buttonX The X value of the wall's position on the buttons board
     * @param buttonY The Y value of the wall's position on the buttons board
     * @param wallOrientation The wall's orientation
     * @param wallsLeft The player's number of walls left
     * @param canceledMove The String describing the wall remove
     */
    public void doRemoveWall(int playerID, int buttonX, int buttonY, Orientation wallOrientation, int wallsLeft, String canceledMove) {
        setBoardButtonsEnabled(true);
        setButtonEnabled(this.getContinueButton(), false);
        setButtonEnabled(this.getUndoButton(), false);
        setButtonEnabled(this.getRotateButton(), true);
        deleteWall(buttonX, buttonY, wallOrientation);
        updatePlayerWallsLeft(playerID, wallsLeft);
        this.getRotateButton().setText("Horizontal");
        updateComment(String.format("Turn Canceled: %s", canceledMove));
    }

    /**
     * The function updates the comment label to an invalid turn
     *
     * @param isWallTurn True if wall place turn and False if a move turn
     */
    public void updateInvalidTurnComment(boolean isWallTurn) {
        updateComment(isWallTurn ? "Invalid wall!" : "Invalid move!");
    }

    /**
     * The function updates the game status label to the given player's id
     *
     * @param playerID The player's id
     */
    public void updateCurrentTurn(int playerID) {
        setBoardButtonsEnabled(true);
        setButtonEnabled(this.getContinueButton(), false);
        setButtonEnabled(this.getUndoButton(), false);
        setButtonEnabled(this.getRotateButton(), true);
        updateGameStatus(String.format("Player %d's Turn!", playerID));
    }

    /**
     * The function paints a button as a valid move
     *
     * @param x The X value of the valid move on the buttons board
     * @param y The Y value of the valid move on the buttons board
     * @param currentTurn The player's id
     */
    public void setValidMove(int x, int y, int currentTurn) {
        Color validMoveColor = (currentTurn == BoardFill.PLAYER1.value() ? this.colorMap.get(ColorEnum.VALID_MOVE_PLAYER1_COLOR) : this.colorMap.get(ColorEnum.VALID_MOVE_PLAYER2_COLOR));
        this.buttons[y][x].setBackground(validMoveColor);
    }

    /**
     * The function removes the valid move color from a button on the buttons board
     *
     * @param x The X value of the button on the buttons board
     * @param y The Y value of the button on the buttons board
     */
    public void removeValidMove(int x, int y) {
        this.buttons[y][x].setBackground(this.colorMap.get(ColorEnum.EMPTY_COLOR));
    }

    /**
     * The function paints the player on the buttons board
     *
     * @param x The X value of the player on the buttons board
     * @param y The Y value of the player on the buttons board
     * @param currentTurn The player's id
     */
    private void paintPlayer(int x, int y, int currentTurn) {
        Color playerColor = (currentTurn == BoardFill.PLAYER1.value() ? this.colorMap.get(ColorEnum.PLAYER1_COLOR) : this.colorMap.get(ColorEnum.PLAYER2_COLOR));
        this.buttons[y][x].setBackground(playerColor);
    }

    /**
     * The function removes the player from a button on the buttons board
     *
     * @param x The X value of the button on the buttons board
     * @param y The Y value of the button on the buttons board
     */
    private void removePlayer(int x, int y) {
        this.buttons[y][x].setBackground(this.colorMap.get(ColorEnum.EMPTY_COLOR));
    }

    /**
     * The function paints a wall on the buttons board
     *
     * @param x The X value of wall on the buttons board
     * @param y The Y value of wall on the buttons board
     * @param orientation The wall's orientation
     */
    private void paintWall(int x, int y, Orientation orientation) {
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

    /**
     * The function removes a wall from the buttons board
     *
     * @param x The X value of wall on the buttons board
     * @param y The Y value of wall on the buttons board
     * @param orientation The wall's orientation
     */
    private void deleteWall(int x, int y, Orientation orientation) {
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

    /**
     * The function updates the rotate button
     */
    public void rotate() { this.sidePanel.updateRotateButton(getOrientation()); }

    /**
     * The function returns an Orientation enum matching the text on the rotate button
     *
     * @return Orientation enum
     */
    public Orientation getOrientation() { return this.getRotateButton().getText().equals("Horizontal") ? Orientation.HORIZONTAL : Orientation.VERTICAL; }

    /**
     * The function resets the rotate button
     */
    public void resetRotateButton() { this.sidePanel.resetRotateButton(); }

    /**
     * The function updates the comment label
     *
     * @param comment The String to which the label updates to
     */
    private void updateComment(String comment) { this.sidePanel.updateComment(comment); }

    /**
     * The function resets the comment label to blank
     */
    public void resetComment() { this.sidePanel.updateComment(""); }

    /**
     * The function updates the game status label
     *
     * @param gameStatus The String to which the label updates to
     */
    private void updateGameStatus(String gameStatus) { this.sidePanel.updateGameStatus(gameStatus); }

    /**
     * The function updates the player's number of walls left
     *
     * @param playerID The player's id
     * @param wallsLeft The number of walls left the label changes to
     */
    private void updatePlayerWallsLeft(int playerID, int wallsLeft) { this.sidePanel.updatePlayerWallsLeft(playerID, wallsLeft); }

    /**
     * The function adds a move to the moves list (according to the graphics format)
     *
     * @param playerID The id of the player that made the move
     * @param move The String describing a move
     */
    public void addMoveToList(int playerID, String move) { this.postGamePanel.addMoveToList(toFormattedString(playerID, move)); }

    /**
     * The function creates a new string from the move according to GUI's formatting
     *
     * @param playerID The ID of the player who made the move
     * @param move The move's string
     * @return A GUI formatted string from the move
     */
    private String toFormattedString(int playerID, String move) {
        String formattedMove;
        if (playerID == BoardFill.PLAYER1.value())
            formattedMove = String.format("%s", move);
        else
            formattedMove = String.format("<html><b>%s</b></html>", move);
        return formattedMove;
    }

    public void resetBoardForReview() {
        setBoardButtonsColor();
    }

    public void reset() {
        removePanel(this.boardPanel);
        addBoardButtons();
        setBoardButtonsColor();
        removePanel(this.getPostGamePanel());
        setPreGamePanel(true, true);
        resetComment();
        setFrame();
    }
}

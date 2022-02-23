package com.quoridorproj;

public class Game {
    private Board board;
    private Player player1;
    private Player player2;
    private int currentTurn;
    private int turnsCounter;

    public Game() {
        this.board = new Board();
        this.player1 = new Player(BoardFill.PLAYER1.value());
        this.player2 = new Player(BoardFill.PLAYER2.value());
        this.turnsCounter = 0;
        // (int) (Math.random() * (max - min + 1) + min)
        this.currentTurn = (int) (Math.random() * (BoardFill.PLAYER2.value()) + BoardFill.PLAYER1.value());
        placePlayersOnBoard();
    }

    private void placePlayersOnBoard() {
        Square[][] squares = this.board.getSquares();
        squares[8][4].setValue(player1.getId());
        squares[0][4].setValue(player2.getId());
    }

    public boolean isGameOver() {
        // Game ends when one of the players reaches one of the 9 squares opposite to his baseline
        if (this.currentTurn == player1.getId())
            return (player1.getLastTurn().getY() == 0);
        else
            return (player2.getLastTurn().getY() == 8);
    }
}

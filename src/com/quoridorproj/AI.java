package com.quoridorproj;

import java.util.ArrayList;

public class AI {
    private final double MAX_SCORE = 1000000; // Maximum possible score for a game state
    private final double MIN_SCORE = -1000000; // Minimum possible score for a game state

    private int maxPlayer; // represents the AI player
    private int minPlayer; // represents the User player

    /**
     * AI Class Constructor
     *
     * @param maxPlayer AI player's ID
     * @param minPlayer User player's ID
     */
    public AI(int maxPlayer, int minPlayer) {
        this.maxPlayer = maxPlayer;
        this.minPlayer = minPlayer;
    }

    public int getMaxPlayer() {
        return this.maxPlayer;
    }

    /**
     * The function searches for the best move that the AI player should play using a Minimax function
     *
     * @param game Current state of the game
     * @param depth Number of turns ahead to search. How deep does the Minimax function play through the game
     * @return The best move (with the highest score) among the possible moves
     */
    public Move getBestMove(Game game, int depth) {
        double bestScore = Double.NEGATIVE_INFINITY;
        Move bestMove = null;

        ArrayList<Move> moves = game.getPossibleTurns(this.maxPlayer);
        for (Move move : moves) {
            Game tempGame = game.duplicate();

            if (move.isWall())
                tempGame.doPlaceWall(move);
            else
                tempGame.doMove(move);

            tempGame.incTurns();
            tempGame.updateCurrentTurn();

            double score = minimax(tempGame, depth - 1, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, false);
            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
                System.out.printf("%s: %f\t", bestMove, bestScore);
            }
        }
        System.out.printf("\nBest Move: %s\n\n", bestMove);
        return bestMove;
    }

    /**
     * The function simulates the game, using recursion, and plays the possible turns in the depth given.
     * When depth reaches zero or the game was ended, the function returns the evaluation (score) of the final game state it reached using a heuristic function.
     *
     * @param game Current state of the game
     * @param depth Number of turns ahead to search. How deep does the function play through the game
     * @param alpha Negative infinity
     * @param beta Positive infinity
     * @param maximizingPlayer True if it's the AI player's turn and False if otherwise
     * @return The best value (score) out of the origin (root) game state
     */
    private double minimax(Game game, int depth, double alpha, double beta, boolean maximizingPlayer) {
        if (depth == 0 || game.isGameOver())
            return evaluate(game, depth);

        if (maximizingPlayer) {
            double maxEval = Double.NEGATIVE_INFINITY;
            ArrayList<Move> moves = game.getPossibleTurns(this.maxPlayer);
            for (Move move : moves) {
                Game tempGame = game.duplicate();

                if (move.isWall())
                    tempGame.doPlaceWall(move);
                else
                    tempGame.doMove(move);

                tempGame.incTurns();
                tempGame.updateCurrentTurn();

                double eval = minimax(tempGame, depth - 1, alpha, beta, false);
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);
                if (beta <= alpha)
                    break;
            }
            return maxEval;
        } else {
            double minEval = Double.POSITIVE_INFINITY;
            ArrayList<Move> moves = game.getPossibleTurns(this.minPlayer);
            for (Move move : moves) {
                Game tempGame = game.duplicate();

                if (move.isWall())
                    tempGame.doPlaceWall(move);
                else
                    tempGame.doMove(move);

                tempGame.incTurns();
                tempGame.updateCurrentTurn();

                double eval = minimax(tempGame, depth - 1, alpha, beta, true);
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);
                if (beta <= alpha)
                    break;
            }
            return minEval;
        }
    }

    /**
     * The function evaluates the given game state according to the AI player's and User player's progress to their goal
     * @param game State of game
     * @param depth Number of turns ahead to finish the search
     * @return The score of this specific game state
     */
    private double evaluate(Game game, int depth) {
        int playerRow = game.getPlayer(this.maxPlayer).getLastMove().getY();
        int opponentRow = game.getPlayer(this.minPlayer).getLastMove().getY();

        int playerNumWalls = game.getPlayer(this.maxPlayer).getWallsLeft();

        if (game.isGameOver() && playerNumWalls > 0) {
            if (playerRow == game.getWinRow(this.maxPlayer))
                return MAX_SCORE + depth;
            else if (opponentRow == game.getWinRow(this.minPlayer))
                return MIN_SCORE - depth;
        }

        Tuple<Integer, Square> tuplePlayerShortestPath = game.getShortestPathToGoal(this.maxPlayer);
        Tuple<Integer, Square> tupleOpponentShortestPath = game.getShortestPathToGoal(this.minPlayer);

        int playerShortestPathToGoal = tuplePlayerShortestPath != null ? tuplePlayerShortestPath.x : Integer.MIN_VALUE;
        int opponentShortestPathToGoal = tupleOpponentShortestPath != null ? tupleOpponentShortestPath.x : Integer.MIN_VALUE;

        if (playerNumWalls > 0)
            return (opponentShortestPathToGoal - playerShortestPathToGoal) + Math.random();
        else
            return Math.pow(playerShortestPathToGoal, -1);
    }
}

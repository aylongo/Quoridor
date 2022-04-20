package com.quoridorproj;

import java.util.ArrayList;

public class AI {
    private final int FEATURES_SIZE = 5;
    private final double MAX_SCORE = 1000000;
    private final double MIN_SCORE = -1000000;

    private int maxPlayer;
    private int minPlayer;

    public AI(int maxPlayer, int minPlayer) {
        this.maxPlayer = maxPlayer;
        this.minPlayer = minPlayer;
    }

    public int getMaxPlayer() {
        return this.maxPlayer;
    }

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

    private double evaluate(Game game, int depth) {
        int size = game.getBoard().getSquaresSize();

        int playerRow = game.getPlayer(this.maxPlayer).getLastMove().getY();
        int opponentRow = game.getPlayer(this.minPlayer).getLastMove().getY();

        if (game.isGameOver()) {
            if (playerRow == game.getWinRow(this.maxPlayer))
                return MAX_SCORE + depth;
            else if (opponentRow == game.getWinRow(this.minPlayer))
                return MIN_SCORE - depth;
        }

        Tuple<Integer, Square> tuplePlayerShortestPath = game.getShortestPathToGoal(this.maxPlayer);
        Tuple<Integer, Square> tupleOpponentShortestPath = game.getShortestPathToGoal(this.minPlayer);

        int playerShortestPathToGoal = tuplePlayerShortestPath != null ? tuplePlayerShortestPath.x : Integer.MIN_VALUE;
        int opponentShortestPathToGoal = tupleOpponentShortestPath != null ? tupleOpponentShortestPath.x : Integer.MIN_VALUE;

        // FIXME: With 0 walls the AI GOES CRAZY
        if (game.getPlayer(this.maxPlayer).getWallsLeft() > 0)
            return (opponentShortestPathToGoal - playerShortestPathToGoal) + Math.random();
        else
            return Math.pow(playerShortestPathToGoal, -1);
    }

    private int[] getFeatures(Game game) {
        int playerRow = game.getPlayer(this.maxPlayer).getLastMove().getY();
        int opponentRow = game.getPlayer(this.minPlayer).getLastMove().getY();

        Tuple<Integer, Square> tuplePlayerSPG = game.getShortestPathToGoal(this.maxPlayer);
        Tuple<Integer, Square> tupleOpponentSPG = game.getShortestPathToGoal(this.minPlayer);

        int playerShortestPathToGoal = tuplePlayerSPG != null ? tuplePlayerSPG.x : Integer.MIN_VALUE;
        int opponentShortestPathToGoal = tupleOpponentSPG != null ? tupleOpponentSPG.x : Integer.MIN_VALUE;
        int playerManhattanDistance = Math.abs(game.getWinRow(this.maxPlayer) - playerRow);
        int opponentManhattanDistance = Math.abs(game.getWinRow(this.minPlayer) - opponentRow);
        int playerNumOfWalls = game.getPlayer(this.maxPlayer).getWallsLeft();
        int opponentNumOfWalls = game.getPlayer(this.minPlayer).getWallsLeft();

        return new int[] {playerShortestPathToGoal, opponentShortestPathToGoal, playerManhattanDistance, opponentManhattanDistance, playerNumOfWalls, opponentNumOfWalls};
    }
}

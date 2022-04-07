package com.quoridorproj;

import java.util.ArrayList;

public class AI {
    private final int FEATURES_SIZE = 6;

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

            double score = minimax(tempGame, depth, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, false);
            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }
        return bestMove;
    }

    private double minimax(Game game, int depth, double alpha, double beta, boolean maximizingPlayer) {
        if (depth == 0 || game.isGameOver())
            return evaluate(game);

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

                double eval = minimax(tempGame, depth - 1, alpha, beta, true);
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);
                if (beta <= alpha)
                    break;
            }
            return minEval;
        }
    }

    private double evaluate(Game game) {
        int size = game.getBoard().getSquaresSize();
        int playerRow = game.getPlayer(this.maxPlayer).getLastMove().getY();
        int opponentRow = game.getPlayer(this.minPlayer).getLastMove().getY();

        if (game.isGameOver()) {
            if (playerRow == game.getWinRow(this.maxPlayer))
                return Double.MAX_VALUE;
            else if (opponentRow == game.getWinRow(this.minPlayer))
                return Double.MIN_VALUE;
        }

        int eval = 0;
        double[] featuresWeights;
        int[] features = getFeatures(game);
        boolean playerGoalSide = isGoalSide(this.maxPlayer, playerRow, game.getWinRow(this.maxPlayer), size);
        boolean opponentGoalSide = isGoalSide(this.minPlayer, opponentRow, game.getWinRow(this.minPlayer), size);

        if (game.getTurnsCounter() < 6)
            featuresWeights = new double[] {-5, 5, 0.25, 0.25, 100, 100};
        else {
            featuresWeights = new double[] {-10, 10, 0.25, 0.25, 2, 2};

            /*
            if (playerGoalSide)
                featuresWeights = new double[] {-3, 5, 0.25, 0.25, -5, -2.5};
            else if (opponentGoalSide)
                featuresWeights = new double[] {-5, 3, 0.25, 0.25, -2.5, -5};
            else
                featuresWeights = new double[] {-5, 5, 0.25, 0.25, 10, 10};
            */
        }

        for (int i = 0; i < FEATURES_SIZE; i++) {
            eval += featuresWeights[i] * features[i];
        }
        return eval + Math.random();
    }

    private int[] getFeatures(Game game) {
        int playerRow = game.getPlayer(this.maxPlayer).getLastMove().getY();
        int opponentRow = game.getPlayer(this.minPlayer).getLastMove().getY();

        Tuple<Integer, Square> tuplePlayerSPG = game.getShortestPathToGoal(this.maxPlayer);
        Tuple<Integer, Square> tupleOpponentSPG = game.getShortestPathToGoal(this.minPlayer);

        int playerShortestPathToGoal = tuplePlayerSPG != null ? tuplePlayerSPG.x : 1;
        int opponentShortestPathToGoal = tupleOpponentSPG != null ? tupleOpponentSPG.x : 1;
        int playerManhattanDistance = Math.abs(game.getWinRow(this.maxPlayer) - playerRow);
        int opponentManhattanDistance = Math.abs(game.getWinRow(this.minPlayer) - opponentRow);
        int playerNumOfWalls = game.getPlayer(this.maxPlayer).getWallsLeft();
        int opponentNumOfWalls = game.getPlayer(this.minPlayer).getWallsLeft();

        return new int[] {playerShortestPathToGoal, opponentShortestPathToGoal, playerManhattanDistance, opponentManhattanDistance, playerNumOfWalls, opponentNumOfWalls};
    }

    private boolean isGoalSide(int playerID, int playerRow, int playerWinRow, int size) {
        if (playerID == BoardFill.PLAYER1.value())
            return (playerRow > playerWinRow && playerRow <= size / 2);
        else
            return (playerRow < playerWinRow && playerRow >= size / 2);
    }
}

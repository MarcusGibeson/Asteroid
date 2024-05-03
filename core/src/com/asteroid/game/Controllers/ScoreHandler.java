package com.asteroid.game.Controllers;

public class ScoreHandler {
    private int score;

    public ScoreHandler() {
        score = 0;
    }

    public void increaseScore(int amount) {
        score += amount;
    }

    public int getScore() {
        return score;
    }

    public void resetScore() {
        score =0;
    }

}

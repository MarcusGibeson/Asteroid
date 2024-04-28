package com.asteroid.game;

public class Stage {
    private int stageNumber;
    private int asteroidLimit;
    private boolean hasBossAsteriod;
    private int spawnCooldownMin;
    private int spawnCooldownMax;
    private int thresholdScore;

    public Stage(int stageNumber, int asteroidLimit, boolean hasBossAsteriod, int spawnCooldownMin, int spawnCooldownMax, int thresholdScore) {
        this.stageNumber = stageNumber;
        this.asteroidLimit = asteroidLimit;
        this.hasBossAsteriod = hasBossAsteriod;
        this.spawnCooldownMin = spawnCooldownMin;
        this.spawnCooldownMax = spawnCooldownMax;
        this.thresholdScore = thresholdScore;
    }

    public int getAsteroidLimit() {
        return asteroidLimit;
    }

    public int getStageNumber() {
        return stageNumber;
    }

    public boolean isHasBossAsteriod() {
        return hasBossAsteriod;
    }

    public int getSpawnCooldownMin() {
        return spawnCooldownMin;
    }

    public int getSpawnCooldownMax() {
        return spawnCooldownMax;
    }

    public int getThresholdScore() {
        return thresholdScore;
    }
}

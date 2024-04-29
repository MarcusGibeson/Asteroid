package com.asteroid.game;

public class Stage {
    private int stageNumber;
    private int asteroidCount;
    private boolean hasBossAsteriod;
    private int spawnCooldownMin;
    private int spawnCooldownMax;
    private int thresholdScore;
    private int bossHealth;

    public Stage(int stageNumber, int asteroidCount, boolean hasBossAsteriod, int spawnCooldownMin, int spawnCooldownMax, int thresholdScore, int bossHealth) {
        this.stageNumber = stageNumber;
        this.asteroidCount = asteroidCount;
        this.hasBossAsteriod = hasBossAsteriod;
        this.spawnCooldownMin = spawnCooldownMin;
        this.spawnCooldownMax = spawnCooldownMax;
        this.thresholdScore = thresholdScore;
        this.bossHealth = bossHealth;
    }

    public int getBossHealth() {
        return bossHealth;
    }
    public int getAsteroidCount() {
        return asteroidCount;
    }

    public int getStageNumber() {
        return stageNumber;
    }

    public boolean hasBossAsteriod() {
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

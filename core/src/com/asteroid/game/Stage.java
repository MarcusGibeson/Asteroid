package com.asteroid.game;

public class Stage {
    private int stageNumber;
    private int asteroidCount;
    private int bossCount;
    private int spawnCooldownMin;
    private int spawnCooldownMax;
    private int bossHealth;

    public Stage(int stageNumber, int asteroidCount, int bossCount, int spawnCooldownMin, int spawnCooldownMax, int bossHealth) {
        this.stageNumber = stageNumber;
        this.asteroidCount = asteroidCount;
        this.bossCount = bossCount;
        this.spawnCooldownMin = spawnCooldownMin;
        this.spawnCooldownMax = spawnCooldownMax;
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

    public int getBossCount() {
        return bossCount;
    }

    public int getSpawnCooldownMin() {
        return spawnCooldownMin;
    }

    public int getSpawnCooldownMax() {
        return spawnCooldownMax;
    }

}

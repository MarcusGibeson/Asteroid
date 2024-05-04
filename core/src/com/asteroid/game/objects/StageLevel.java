package com.asteroid.game.objects;

import com.asteroid.game.objects.BossAsteroid;

import java.util.ArrayList;
import java.util.List;

public class StageLevel {
    private int stageNumber;
    private int asteroidCount;
    private int bossCount;
    private int bossHealth;
    private List<BossAsteroid> bossAsteroids;

    public StageLevel(int stageNumber, int asteroidCount, int bossCount, int bossHealth) {
        this.stageNumber = stageNumber;
        this.asteroidCount = asteroidCount;
        this.bossCount = bossCount;
        this.bossHealth = bossHealth;
        this.bossAsteroids = new ArrayList<>();
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


    public List<BossAsteroid> getBossAsteroids() {
        return bossAsteroids;
    }

    public void addBossAsteroid(BossAsteroid bossAsteroid) {
        bossAsteroids.add(bossAsteroid);
    }
}
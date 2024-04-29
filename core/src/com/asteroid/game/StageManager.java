package com.asteroid.game;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class StageManager {
    private List<Stage> stages;
    private int currentStageIndex;
    private Stage currentStage;
    private AsteroidHandler asteroidHandler;
    PlayerShip playerShip;

    private static final float MAX_STAGES = 99;
    public StageManager(AsteroidHandler asteroidHandler, PlayerShip playerShip) {
        this.asteroidHandler = asteroidHandler;
        this.playerShip = playerShip;
        this.stages = new ArrayList<>();
        this.currentStageIndex = 0;
        initializeStages();
        startCurrentStage();

    }

    public void update(float delta) {
        if (asteroidHandler.getAsteroids().isEmpty()) {
            // Advance to the next stage if not already at the last stage
            if (currentStageIndex < stages.size() - 1) {
                currentStageIndex++;
                startCurrentStage();
            } else {
                //handle game completion
            }
        }
    }

    public void initializeStages() {
        int initialAsteroidCount = 4;
        for(int i = 1; i <= MAX_STAGES; i++) {
            int asteroidCount = initialAsteroidCount + (i-1) * 2; //increment asteroids by 2 each stage
            boolean hasBossAsteroid = i % 5 == 0; //Every 5th stage has a boss
            int minSpawnCooldown = 2000 - (i - 1) * 200; //Decrease min spawn cooldown by 200 each stage
            int maxSpawnCooldown = 3000 - (i - 1) * 300;//Decrease max spawn cooldown by 300 each stage
            int scoreThreshold = (i - 1) * 1000; //Increase score threshold by 1000 each stage
            int bossHealth = 500;

            Stage stage = new Stage(i, asteroidCount, hasBossAsteroid, minSpawnCooldown, maxSpawnCooldown, scoreThreshold, bossHealth);
            stages.add(stage);
            if (hasBossAsteroid) {
                Vector2 bossSpawnPosition = new Vector2(650, 300);
                BossAsteroid bossAsteroid = new BossAsteroid(bossSpawnPosition, 3, playerShip, bossHealth);
                bossAsteroid.setPlayerShip(playerShip);
                asteroidHandler.setBossAsteroid(bossAsteroid);
            }
        }
    }

    private void startCurrentStage() {
        currentStage = stages.get(currentStageIndex);
        asteroidHandler.setAsteroidsPerSpawn(currentStage.getAsteroidCount());
        asteroidHandler.setSpawnCooldown(currentStage.getSpawnCooldownMin(), currentStage.getSpawnCooldownMax());
        if (currentStage.hasBossAsteriod()) {
            asteroidHandler.spawnBossAsteroid();
        } else {
            asteroidHandler.startSpawning();
        }

    }

    public Stage getCurrentStage() {
        return currentStage;
    }
}

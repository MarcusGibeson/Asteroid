package com.asteroid.game;

import java.util.ArrayList;
import java.util.List;

public class StageManager {
    private List<Stage> stages;
    private int currentStageIndex;
    private Stage currentStage;
    private AsteroidHandler asteroidHandler;

    private static final float MAX_STAGES = 99;
    public StageManager(AsteroidHandler asteroidHandler) {
        this.asteroidHandler = asteroidHandler;
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
            boolean hasBossAstroid = i % 5 == 0; //Every 5th stage has a boss
            int minSpawnCooldown = 2000 - (i - 1) * 200; //Decrease min spawn cooldown by 200 each stage
            int maxSpawnCooldown = 3000 - (i - 1) * 300;//Decrease max spawn cooldown by 300 each stage
            int scoreThreshold = (i - 1) * 1000; //Increase score threshold by 1000 each stage

            Stage stage = new Stage(i, asteroidCount, hasBossAstroid, minSpawnCooldown, maxSpawnCooldown, scoreThreshold);
            stages.add(stage);
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

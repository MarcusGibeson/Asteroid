package com.asteroid.game.Controllers;

import com.asteroid.game.objects.StageLevel;
import com.asteroid.game.objects.BossAsteroid;
import com.asteroid.game.objects.PlayerShip;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;
import java.util.List;


public class StageManager {
    private List<StageLevel> stageLevels;
    private int currentStageIndex;
    private StageLevel currentStageLevel;
    private AsteroidHandler asteroidHandler;
    private UFOHandler ufoHandler;
    PlayerShip playerShip;
    public boolean gameWon = false;
    private static final int STAGE_TRANSITION_COOLDOWN_MIN = 2000;
    private static final int STAGE_TRANSITION_COOLDOWN_MAX = 3000;
    private boolean bossAsteroidsSpawned = false;
    private static final float MAX_STAGES = 50;


    public StageManager(AsteroidHandler asteroidHandler, UFOHandler ufoHandler, PlayerShip playerShip) {
        this.asteroidHandler = asteroidHandler;
        this.ufoHandler = ufoHandler;
        this.playerShip = playerShip;
        this.stageLevels = new ArrayList<>();
        this.currentStageIndex = 0;
        initializeStages();
        startCurrentStage();
    }

    public void update(float delta) {
        scheduleStageTransition();

    }

    public void scheduleStageTransition() {
        int delay = MathUtils.random(STAGE_TRANSITION_COOLDOWN_MIN, STAGE_TRANSITION_COOLDOWN_MAX);
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                advanceToNextStage();
            }
        }, delay / 2000);
    }

    public void advanceToNextStage() {
        // Advance to the next stage if not already at the last stage
        if (asteroidHandler.getAsteroids().isEmpty()) {
            checkAsteroidsDestroyed();
            if(currentStageLevel.getBossAsteroids().isEmpty()) {
                System.out.println("Astroid number: " + asteroidHandler.getAsteroids());
                if (currentStageIndex < stageLevels.size() - 1) {
                    currentStageIndex++;
                    startCurrentStage();
                } else {
                    gameWon = true;
                    //handle game completion
                }
            }
        }
    }

    public void drawGameWonMessage(SpriteBatch spriteBatch, BitmapFont font) {
        if (gameWon) {
            font.draw(spriteBatch, "You have won!", Gdx.graphics.getWidth() / 2 -150, Gdx.graphics.getHeight() /2 );
        }

    }

    public void initializeStages() {
        int initialAsteroidCount = 1;
        for(int i = 1; i <= MAX_STAGES; i++) {
            int asteroidCount = initialAsteroidCount + ((i-1) * 2); //increment asteroids by 2 each stage
            int UFOCount = determineUFOCount(i);
            int bossCount = determineBossCount(i); //determine how many bosses
            int bossHealth = 500;

            StageLevel stageLevel = new StageLevel(i, asteroidCount, UFOCount, bossCount, bossHealth);
            stageLevels.add(stageLevel);
            for (int j = 0; j < bossCount; j++) {
                Vector2 bossSpawnPosition = new Vector2(MathUtils.random(500,500), MathUtils.random(550,650));
                BossAsteroid bossAsteroid = new BossAsteroid(bossSpawnPosition, 3, playerShip, bossHealth);
                bossAsteroid.setPlayerShip(playerShip);
                stageLevel.addBossAsteroid(bossAsteroid);
            }
        }
    }

    private int determineBossCount(int stageIndex) {
        if (stageIndex % 19 == 0) {
            return 3;
        } else if (stageIndex % 13 == 0) {
            return 2;
        } else if (stageIndex % 7 == 0) {
            return 1;
        } else {
            return 1;
        }
    }
    private int determineUFOCount(int stageIndex) {
        if (stageIndex > 50) {
            return 4;
        } else if (stageIndex > 20) {
            return 3;
        } else if (stageIndex > 10) {
            return 2;
        } else if (stageIndex > 5) {
            return 1;
        } else {
            return 0;
        }
    }

    private void startCurrentStage() {
        currentStageLevel = stageLevels.get(currentStageIndex);
        asteroidHandler.setAsteroidsPerSpawn(currentStageLevel.getAsteroidCount());
        asteroidHandler.startSpawning();
        ufoHandler.setUFOsPerSpawn(currentStageLevel.getUFOCount());
        ufoHandler.startSpawning();

    }




    private void checkAsteroidsDestroyed() {
        if (currentStageLevel != null && asteroidHandler.getAsteroids().isEmpty()) {
            boolean allBossesDestroyed = currentStageLevel.getBossAsteroids().isEmpty();
            if (!allBossesDestroyed && !bossAsteroidsSpawned) {
                startBossAsteroids();
                bossAsteroidsSpawned = true;
            }
        }
    }

    private void startBossAsteroids() {
        for (BossAsteroid bossAsteroid : currentStageLevel.getBossAsteroids()) {
            asteroidHandler.spawnBossAsteroid(bossAsteroid);
        }
    }

    public StageLevel getCurrentStage() {
        return currentStageLevel;
    }

    public int getCurrentStageIndex() {return currentStageIndex;}

    public void resetStageLevels() {
        currentStageIndex = 0;
    }

    public void setCurrentStageIndex(int currentStageIndex) {
        this.currentStageIndex = currentStageIndex;
    }
}

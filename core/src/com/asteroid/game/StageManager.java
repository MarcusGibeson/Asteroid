package com.asteroid.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;
import java.util.List;


public class StageManager {
    private List<Stage> stages;
    private int currentStageIndex;
    private Stage currentStage;
    private AsteroidHandler asteroidHandler;
    PlayerShip playerShip;
    public boolean gameWon = false;
    private static final int STAGE_TRANSITION_COOLDOWN_MIN = 2000;
    private static final int STAGE_TRANSITION_COOLDOWN_MAX = 3000;
    private boolean bossAsteroidsSpawned = false;
    private static final float MAX_STAGES = 50;


    public StageManager(AsteroidHandler asteroidHandler, PlayerShip playerShip) {
        this.asteroidHandler = asteroidHandler;
        this.playerShip = playerShip;
        this.stages = new ArrayList<>();
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
            if(currentStage.getBossAsteroids().isEmpty()) {
                System.out.println("Astroid number: " + asteroidHandler.getAsteroids());
                if (currentStageIndex < stages.size() - 1) {
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
        for(int i = 7; i <= MAX_STAGES; i++) {
            int asteroidCount = initialAsteroidCount + ((i-1) * 2); //increment asteroids by 2 each stage
            int bossCount = determineBossCount(i); //determine how many bosses
            int bossHealth = 500;

            Stage stage = new Stage(i, asteroidCount, bossCount, bossHealth);
            stages.add(stage);
            for (int j = 0; j < bossCount; j++) {
                Vector2 bossSpawnPosition = new Vector2(MathUtils.random(500,500), MathUtils.random(550,650));
                BossAsteroid bossAsteroid = new BossAsteroid(bossSpawnPosition, 3, playerShip, bossHealth);
                bossAsteroid.setPlayerShip(playerShip);
                stage.addBossAsteroid(bossAsteroid);
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
            return 0;
        }
    }

    private void startCurrentStage() {
        currentStage = stages.get(currentStageIndex);
        asteroidHandler.setAsteroidsPerSpawn(currentStage.getAsteroidCount());
        asteroidHandler.startSpawning();
    }




    private void checkAsteroidsDestroyed() {
        if (currentStage != null && asteroidHandler.getAsteroids().isEmpty()) {
            boolean allBossesDestroyed = currentStage.getBossAsteroids().isEmpty();
            if (!allBossesDestroyed && !bossAsteroidsSpawned) {
                startBossAsteroids();
                bossAsteroidsSpawned = true;
            }
        }
    }

    private void startBossAsteroids() {
        for (BossAsteroid bossAsteroid : currentStage.getBossAsteroids()) {
            asteroidHandler.spawnBossAsteroid(bossAsteroid);
        }
    }

    public Stage getCurrentStage() {
        return currentStage;
    }
}

package com.asteroid.game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;

public class AsteroidHandler {
    private List<Asteroid> asteroids;
    private PlayerShip playerShip;
    private ShapeRenderer shapeRenderer;
    private ScoreHandler scoreHandler;
    final static int SPAWN_COOLDOWN_MIN = 2000;
    final static int SPAWN_COOLDOWN_MAX = 3000;

    public AsteroidHandler(PlayerShip playerShip, ShapeRenderer shapeRenderer, ScoreHandler scoreHandler) {
        this.playerShip = playerShip;
        this.asteroids = new ArrayList<>();
        this.shapeRenderer = shapeRenderer;
        this.scoreHandler = scoreHandler;
        // Start spawning asteroids immediately
        scheduleSpawn();
    }

    public void spawnAsteroid() {
        // Generate random spawn node and tier
        int spawnNode = MathUtils.random(Asteroid.spawnCoordinates.length - 1);
        int tier = MathUtils.random(1, 3);
        // Create new asteroid and add it to the list
        asteroids.add(new Asteroid(spawnNode, tier, playerShip));
    }

    public void scheduleSpawn() {
        int delay = MathUtils.random(SPAWN_COOLDOWN_MIN, SPAWN_COOLDOWN_MAX);
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                spawnAsteroid();
                scheduleSpawn(); // Reschedule spawning
            }
        }, delay / 1000f);
    }

    public void update(float delta) {
        handleCollisions(); // Check for collisions and handle them
        // Update asteroid positions
        for (Asteroid asteroid : asteroids) {
            asteroid.update(delta);
        }
    }

//    private void handleCollisions() {
//        for (Asteroid asteroid : asteroids) {
//            asteroid.detectCollision(); // Delegate collision detection to Asteroid class
//        }
//    }

    //refactored to make some code reusable for Collision Handler
    private void handleCollisions() {
         Iterator<Asteroid> iterator = asteroids.iterator();
         List<Asteroid> asteroidsToAdd = new ArrayList<>(); //created new list of asteroids
        while (iterator.hasNext()) {
            Asteroid asteroid = iterator.next();
            detectCollision(asteroid);
            if (asteroid.isHitByBullet()) {
                handleHitAsteroid(asteroid, asteroidsToAdd);
                int scoreToAdd = getScoreForTier(asteroid.getTierLevel());
                scoreHandler.increaseScore(scoreToAdd);
                iterator.remove(); // Remove the asteroid from the list

            }
        }
        asteroids.addAll(asteroidsToAdd); //added them here after asteroid is removed
    }

    private void detectCollision(Asteroid asteroid) {
        asteroid.detectCollision();
    }

    public void handleHitAsteroid(Asteroid asteroid, List<Asteroid> asteroidsToAdd) {
        if(asteroid.getTierLevel() > 1) {
            splitAsteroid(asteroid, asteroidsToAdd);
        }
    }

    public void splitAsteroid(Asteroid asteroid, List<Asteroid> asteroidsToAdd) {
        Vector2 asteroidPosition = asteroid.getPosition();
        int newTierLevel = asteroid.getTierLevel();
        for (int i = 0; i < 2; i++) {
            asteroidsToAdd.add(new Asteroid(asteroidPosition, newTierLevel, playerShip));
        }
    }

    public List<Asteroid> getAsteroids() {
        return asteroids;
    }
    private int getScoreForTier(int tier) {
        switch(tier) {
            case 1:
                return 10;
            case 2:
                return 20;
            case 3:
                return 30;
            default:
                return 0;
        }
    }

    public void render() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        for (Asteroid asteroid : asteroids) {
            asteroid.draw(shapeRenderer); // Draw each asteroid
        }
        shapeRenderer.end();
    }
}
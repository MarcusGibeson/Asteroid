package com.asteroid.game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Timer;

public class AsteroidHandler {
    private List<Asteroid> asteroids;
    private PlayerShip playerShip;
    private ShapeRenderer shapeRenderer;
    final static int SPAWN_COOLDOWN_MIN = 2000;
    final static int SPAWN_COOLDOWN_MAX = 3000;

    public AsteroidHandler(PlayerShip playerShip, ShapeRenderer shapeRenderer) {
        this.playerShip = playerShip;
        this.asteroids = new ArrayList<>();
        this.shapeRenderer = shapeRenderer;
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

    private void handleCollisions() {
        Iterator<Asteroid> iterator = asteroids.iterator();
        while (iterator.hasNext()) {
            Asteroid asteroid = iterator.next();
            asteroid.detectCollision();
            if (asteroid.isHitByBullet()) {
                iterator.remove(); // Remove the asteroid from the list
                if (asteroid.getTier() > 1) {
                    // If the asteroid is not the smallest tier, split it into smaller asteroids
                    for (int i = 0; i < 2; i++) {
                        asteroids.add(new Asteroid(asteroid.getPosition(), asteroid.getTier() - 1, playerShip));
                    }
                }
            }
        }
    }

    public void render() {
        for (Asteroid asteroid : asteroids) {
            asteroid.draw(shapeRenderer); // Draw each asteroid
        }
        shapeRenderer.end();
    }
}
package com.asteroid.game.Controllers;

import com.asteroid.game.objects.PlayerShip;
import com.asteroid.game.objects.UFOShip;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class UFOHandler {
    private List<UFOShip> ufoShips;
    private PlayerShip playerShip;

    private ShapeRenderer shapeRenderer;
    private int ufosPerSpawn;
    private float respawnTimer;
    private boolean isWaitingToRespawn;
    private static final float RESPAWN_DELAY = 20;
    private static final float SPAWN_DELAY = 3;
    private float spawnDelayTimer = SPAWN_DELAY;


    public UFOHandler(PlayerShip playerShip, ShapeRenderer shapeRenderer) {
        this.playerShip = playerShip;
        this.ufoShips = new ArrayList<>();
        this.shapeRenderer = shapeRenderer;
        this.respawnTimer = 0;
        isWaitingToRespawn = false;
    }

    public void update(float delta) {
        if (spawnDelayTimer > 0) {
            spawnDelayTimer -= delta;
            return;
        }
        for (UFOShip ufoShip : ufoShips) {
            if (!ufoShip.isDestroyed()) {
                if (isOutOfBounds(ufoShip) && !isWaitingToRespawn) {
                    destroyUFO(ufoShip);
                }
                if (isWaitingToRespawn) {
                    respawnTimer -= delta;
                    if (respawnTimer <= 0) {
                        //respawn ufo off screen
                        spawnOffScreen(ufoShip);
                        isWaitingToRespawn = false;
                    }
                }
                ufoShip.update(delta);

            } else {
                respawnUFO(ufoShip);

            }
            ufoShip.updateBullets(delta);
        }
    }

    public void startSpawning() {
        spawnUFOs(ufosPerSpawn);
    }


    public void draw(ShapeRenderer shapeRenderer) {
        for (UFOShip ufoShip: ufoShips) {
            ufoShip.draw(shapeRenderer);
        }
    }

    public void drawBullets(ShapeRenderer shapeRenderer) {
        for (UFOShip ufoShip : ufoShips){
            ufoShip.drawBullets(shapeRenderer);
        }
}
    public void setUFOsPerSpawn(int ufosPerSpawn) {
        this.ufosPerSpawn = ufosPerSpawn;
    }

    private void spawnUFOs(int count) {
        for(int i = 0; i < count; i++) {
            spawnUfo();
        }
    }

    private void spawnUfo() {
        UFOShip ufo = new UFOShip(0,0, playerShip);
        ufoShips.add(ufo);
        spawnOffScreen(ufo);

    }

    public void destroyUFO(UFOShip ufoShip) {
        ufoShip.setDestroyed(true);
    }

    public void respawnUFO(UFOShip ufoShip) {
        respawnTimer = RESPAWN_DELAY;
        isWaitingToRespawn = true;
        ufoShip.setDestroyed(false);

    }

    public void spawnOffScreen(UFOShip ufoShip) {
        //Randomly select a side of the screen to spawn the UFO
        int side = MathUtils.random(4); //0 top, 1 bottom, 2 left, 3 right
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        Vector2 position = new Vector2();
        Vector2 velocity = new Vector2();


        //Initialize UFO position off-screen
        float spawnX = 0, spawnY = 0;
        switch(side) {
            case 0: //Top
                spawnX = MathUtils.random(0, screenWidth);
                spawnY = screenHeight;
                break;
            case 1: //Bottom
                spawnX = MathUtils.random(0, screenWidth);
                spawnY = 0;
                break;
            case 2: //Left
                spawnX = 0;
                spawnY = MathUtils.random(0, screenHeight);
                break;
            case 3: //Right
                spawnX = screenWidth;
                spawnY = MathUtils.random(0, screenHeight);
                break;
        }

        //Set UFO position
        position.set(spawnX, spawnY);

        //Calculate velocity towards the center of the screen
        float centerX = MathUtils.random(0, screenWidth);
        float centerY = MathUtils.random(0, screenHeight);
        velocity.set(centerX - spawnX, centerY - spawnY).nor().scl(ufoShip.getSpeed());

        //Set UFO position and velocity
        ufoShip.setPosition(position);
        ufoShip.setVelocity(velocity);
    }

    private boolean isOutOfBounds(UFOShip ufo) {
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        return ufo.getPosition().x < 0 || ufo.getPosition().x > screenWidth || ufo.getPosition().y < 0 || ufo.getPosition().y > screenHeight;
    }

    public List<UFOShip> getUfoShips() {
        return ufoShips;
    }

}

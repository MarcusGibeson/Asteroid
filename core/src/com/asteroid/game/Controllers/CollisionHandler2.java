package com.asteroid.game.Controllers;

import static com.asteroid.game.objects.Bullet.BULLET_RADIUS;

import com.asteroid.game.objects.Bullet;
import com.asteroid.game.objects.PlayerShip;
import com.asteroid.game.objects.PowerUp;
import com.asteroid.game.objects.UFOShip;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.List;

public class CollisionHandler2 {
    ScoreHandler scoreHandler;
    public CollisionHandler2(ScoreHandler scoreHandler) {
        this.scoreHandler = scoreHandler;
    }

    public void update(PlayerShip playerShip, UFOHandler ufoHandler, AsteroidHandler asteroidHandler, List<PowerUp> powerUps) {
        handleAsteroidCollisions(asteroidHandler);
        handlePlayerBulletCollisions(playerShip, asteroidHandler, ufoHandler);
        handleCometCollisions(playerShip, asteroidHandler);
        handlePlayerShipCollisions(playerShip, asteroidHandler, ufoHandler);
        handlePowerUpCollisions(playerShip, powerUps);
        handleKillAuraCollisions(playerShip, asteroidHandler, ufoHandler);

    }

    //REGION OBJECT COLLISION HANDLING
    //asteroid
    private void handleAsteroidCollisions(AsteroidHandler asteroidHandler) {

    }

    //Player Bullet
    private void handlePlayerBulletCollisions(PlayerShip playerShip, AsteroidHandler asteroidHandler) {

    }

    //Comet
    private void handleCometCollisions(PlayerShip playerShip, AsteroidHandler asteroidHandler) {

    }

    //Player ship
    private void handlePlayerShipCollisions(PlayerShip playerShip, AsteroidHandler asteroidHandler, UFOHandler ufoHandler) {

    }

    //PowerUp
    private void handlePowerUpCollisions(PlayerShip playerShip, List<PowerUp> powerUps) {

    }

    //Kill Aura
    private void handleKillAuraCollisions(PlayerShip playerShip, AsteroidHandler asteroidHandler, UFOHandler ufoHandler) {

    }

    //REGION COLLISION CHECKING METHODS

    public static boolean checkUFOBulletPlayerShipCollision(UFOHandler ufoHandler, PlayerShip playerShip) {
        Rectangle playerShipRectangle = playerShip.getCollisionRectangle();
        for (UFOShip ufoShip : ufoHandler.getUfoShips()) {
            for (Bullet bullet : ufoShip.getBullets()) {
                Circle bulletCircle = new Circle(bullet.getPosition(), BULLET_RADIUS);
                if (Intersector.overlaps(bulletCircle, playerShipRectangle)) {
                    return true;
                }
            }
        }
        return false;
    }




}

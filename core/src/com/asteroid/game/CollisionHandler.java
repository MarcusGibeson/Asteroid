package com.asteroid.game;

import static com.asteroid.game.Bullet.BULLET_RADIUS;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.List;

public class CollisionHandler {

    ScoreHandler scoreHandler;
    public CollisionHandler(ScoreHandler scoreHandler) {
        this.scoreHandler = scoreHandler;
    }

    public void update(PlayerShip playerShip, UFOShip ufo, List<PowerUp> powerUps) {
        if (!ufo.isDestroyed() && !playerShip.isPlayerDead()) {
            if(checkPlayerShipUFOCollision(playerShip, ufo)) {
                playerShip.handleCollision();
                ufo.destroy();
            }
        }

        if (!ufo.isDestroyed()) {
            if(checkPlayerBulletUFOCollision(playerShip, ufo)) {
                ufo.destroy();
                scoreHandler.increaseScore(100);
            }
        }

        if (!playerShip.isPlayerDead()) {
            if(checkUFOBulletPlayerShipCollision(ufo, playerShip)) {
                playerShip.handleCollision();

            }
        }

        if (checkPlayerShipBulletUFOBulletCollision(playerShip, ufo)) {
           List<Bullet> playerShipBullets = playerShip.getBullets();
           List<Bullet> ufoBullets = ufo.getBullets();

           for (int i = 0; i < playerShipBullets.size(); i++) {
               Bullet playerBullet = playerShipBullets.get(i);
               Circle playerBulletCircle = new Circle(playerBullet.getPosition(), playerBullet.BULLET_RADIUS);

               for (int j = 0; j < ufoBullets.size(); j++) {
                   Bullet ufoBullet = ufoBullets.get(j);
                   Circle ufoBulletCircle = new Circle(ufoBullet.getPosition(), ufoBullet.BULLET_RADIUS);

                   if (Intersector.overlaps(playerBulletCircle, ufoBulletCircle)) {
                       playerShipBullets.remove(playerBullet);
                       ufoBullets.remove(ufoBullet);
                       i--;
                       break;
                   }
               }
           }
       }

        if (checkPowerUpPlayerCollision(playerShip, powerUps)){
            //if powerUps isn't empty
            if (!powerUps.isEmpty()){
                //for each powerUp on the screen
                for (PowerUp powerUp : powerUps){
                    //if it's touching the ship
                    if (powerUp.isTouchingShip()){
                        //apply it
                        powerUp.applyToShip(playerShip);
                    }
                }
            }
        }
    }

    //Method to check collision between UFO bullets and player ship
    public static boolean checkUFOBulletPlayerShipCollision(UFOShip ufo, PlayerShip playerShip) {
        for (Bullet bullet : ufo.getBullets()) {
            Circle bulletCircle = new Circle(bullet.getPosition(), BULLET_RADIUS);
            Rectangle playerShipRectangle = playerShip.getCollisionRectangle();
            if (Intersector.overlaps(bulletCircle, playerShipRectangle)) {
                //collision detected
                return true;

            }
        }
        return false;
    }

    //Method to check collision between Player ship bullets and UFO
    public static boolean checkPlayerBulletUFOCollision(PlayerShip playerShip, UFOShip ufo) {
        for (Bullet bullet : playerShip.getBullets()) {
            Circle bulletCircle = new Circle(bullet.getPosition(), BULLET_RADIUS);
            Rectangle ufoRectangle = ufo.getCollisionRectangle();
            if (Intersector.overlaps(bulletCircle, ufoRectangle)) {
                //collision detected
                return true;
            }
        }
        return false;
    }

    //Method to check collision between Player ship and UFO
    public static boolean checkPlayerShipUFOCollision(PlayerShip playerShip, UFOShip ufo) {
        Rectangle playerShipRectangle = playerShip.getCollisionRectangle();
        Rectangle ufoRectangle = ufo.getCollisionRectangle();
        if (Intersector.overlaps(playerShipRectangle, ufoRectangle)) {
            //collision detected
            return true;
        }
        return false;
    }

    //Method to check collision
//    public static boolean checkPlayerShipAsteroidCollision(PlayerShip playerShip, List<Asteroid> asteroids) {
//
//    }

    public static boolean checkPlayerShipBulletUFOBulletCollision(PlayerShip playerShip, UFOShip ufo) {
        List<Bullet> playerShipBullets = playerShip.getBullets();
        List<Bullet> ufoBullets = ufo.getBullets();

        for (Bullet playerBullet : playerShipBullets) {
            Circle playerBulletCircle = new Circle(playerBullet.getPosition(), BULLET_RADIUS);

            for (Bullet ufoBullet : ufoBullets) {
                Circle ufoBulletCircle = new Circle(ufoBullet.getPosition(), BULLET_RADIUS);

                if(Intersector.overlaps(playerBulletCircle, ufoBulletCircle)) {
                    //collision detected
                    return true;
                }
            }
        }
        return false;
    }

    //method to check collision between the ship and any active powerups
    public static boolean checkPowerUpPlayerCollision(PlayerShip playerShip, List<PowerUp> powerUps) {
        Rectangle playerShipRectangle = playerShip.getCollisionRectangle();
        if (!powerUps.isEmpty()) {
            for (PowerUp powerUp : powerUps) {
                Rectangle powerUpRectangle = powerUp.getCollisionRectangle();
                if (Intersector.overlaps(playerShipRectangle, powerUpRectangle)) {
                    powerUp.setTouchingShip(true);
                    return true; // Collision detected, return true
                }
            }
        }
        return false; // No collision detected, return false
    }
}

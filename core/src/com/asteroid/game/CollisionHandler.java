package com.asteroid.game;

import static com.asteroid.game.BossAsteroid.BOSS_RADIUS;
import static com.asteroid.game.Bullet.BULLET_RADIUS;
import static com.asteroid.game.Comet.COMET_RADIUS;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CollisionHandler {

    ScoreHandler scoreHandler;
    public CollisionHandler(ScoreHandler scoreHandler) {
        this.scoreHandler = scoreHandler;
    }

    public void update(PlayerShip playerShip, UFOShip ufo, BossAsteroid boss, AsteroidHandler asteroidHandler, List<PowerUp> powerUps) {

        //Asteroids bumping into each other
        if(checkAsteroidCollisionWithAnotherAsteroid(asteroidHandler)) {
            List<Asteroid> asteroids = asteroidHandler.getAsteroids();
            for (int i = 0; i < asteroids.size(); i++) {
                Asteroid asteroid1 = asteroids.get(i);
                Circle asteroid1Circle = new Circle(asteroid1.getPosition(), asteroid1.getRadius());
                for (int j = i + 1; j < asteroids.size(); j++) {
                    Asteroid asteroid2 = asteroids.get(j);
                    Circle asteroid2Circle = new Circle(asteroid2.getPosition(), asteroid2.getRadius());
                    if(Intersector.overlaps(asteroid2Circle, asteroid1Circle)) {
                        asteroidHandler.checkImpactResolution(asteroid1, asteroid2);
                    }

                }
            }
            asteroidHandler.removeMarkedAsteroids(asteroids);
        }

        //Boss asteroid getting shot by player ship bullet
        if (!boss.checkIsDestroyed() && !playerShip.isPlayerDead()) {
            if(checkPlayerShipBulletBossAsteroidCollision(playerShip, boss)) {
                boss.takeDamage(1);
            }
        }

        //Comet getting shot by player ship bullet
        if(checkPlayerShipBulletCometCollision(playerShip, boss)) {
            List<Bullet> playerShipBullets = playerShip.getBullets();
            List<Comet> comets = boss.getComets();
            for (int i = 0; i < playerShipBullets.size(); i++) {
                Bullet playerBullet = playerShipBullets.get(i);
                Circle playerBulletCircle = new Circle(playerBullet.getPosition(), playerBullet.BULLET_RADIUS);

                for (int j = 0; j < comets.size(); j++) {
                    Comet comet = comets.get(j);
                    Circle cometCircle = new Circle(comet.getPosition(), comet.COMET_RADIUS);
                    if (Intersector.overlaps(playerBulletCircle, cometCircle)) {
                        playerShipBullets.remove(playerBullet);
                        comets.remove(comet);
                        i--;
                        break;
                    }
                }
            }
        }

        //Comet colliding with player
        if(checkPlayerShipCometCollision(playerShip, boss)) {
            Rectangle playerShipRectangle = playerShip.getCollisionRectangle();
            List<Comet> comets = boss.getComets();
            for(int i = 0; i < comets.size(); i++) {
                Comet comet = comets.get(i);
                Circle cometCircle = new Circle(comet.getPosition(), comet.COMET_RADIUS);
                if(Intersector.overlaps(cometCircle, playerShipRectangle)) {
                    comets.remove(comet);
//                    playerShip.handleCollision(); //this
                }
            }
        }

        //Asteroid colliding with player
        if(!playerShip.isPlayerDead()){
            if(checkPlayerShipAsteroidCollision(playerShip, asteroidHandler)) {
                List<Asteroid> asteroidsToAdd = new ArrayList<>();
                Rectangle playerShipRectangle = playerShip.getCollisionRectangle();
                List<Asteroid> asteroids = asteroidHandler.getAsteroids();

                for(int i = 0; i < asteroids.size(); i++) {
                    Asteroid asteroid = asteroids.get(i);
                    Circle asteriodCircle = new Circle(asteroid.getPosition(), asteroid.getRadius());
                    if(Intersector.overlaps(asteriodCircle, playerShipRectangle)) {
                        asteroidHandler.handleHitAsteroid(asteroid, asteroidsToAdd);
                        asteroids.remove(asteroid);
//                        playerShip.handleCollision(); //this
                    }
                }
                asteroids.addAll(asteroidsToAdd);
            }
        }

        //Player ship and Boss Asteroid colliding
        if(!boss.checkIsDestroyed() && !playerShip.isPlayerDead()){
            if(checkPlayerShipBossAsteroidCollision(playerShip, boss)) {
                playerShip.handleCollision(); //this
                boss.takeDamage(25);
            }
        }

        //Player ship and UFO collidiing
        if (!ufo.isDestroyed() && !playerShip.isPlayerDead()) {
            if(checkPlayerShipUFOCollision(playerShip, ufo)) {
//                playerShip.handleCollision();
                ufo.destroy();
            }
        }
        //UFO getting shot by player ship
        if (!ufo.isDestroyed()) {
            if(checkPlayerBulletUFOCollision(playerShip, ufo)) {
                ufo.destroy();
                scoreHandler.increaseScore(100);
            }
        }

        //UFO and Player ship colliding
        if (!playerShip.isPlayerDead()) {
            if(checkUFOBulletPlayerShipCollision(ufo, playerShip)) {
//                playerShip.handleCollision();

            }
        }

        //Player ship getting shot by UFO
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

        //Player touching a power up
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

        //kill aura and asteroids colliding
        if (checkKillAuraCollisionsWithAsteroids(playerShip, asteroidHandler)){
            Circle killAuraCircle = playerShip.getKillAuraCircle();
            List<Asteroid> asteroidList = asteroidHandler.getAsteroids();

            Iterator<Asteroid> iterator = asteroidList.iterator();
            while (iterator.hasNext()) {
                Asteroid asteroid = iterator.next();
                Circle asteroidCircle = new Circle(asteroid.getPosition(), asteroid.getRadius());
                if (Intersector.overlaps(killAuraCircle, asteroidCircle)) {
                    iterator.remove();
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

    //Method to check collision between player and asteroid
    public static boolean checkPlayerShipAsteroidCollision(PlayerShip playerShip, AsteroidHandler asteroidHandler) {
        Rectangle playerShipRectangle = playerShip.getCollisionRectangle();
        List<Asteroid> asteroids = asteroidHandler.getAsteroids();
        for(Asteroid asteroid : asteroids) {
            Circle asteroidCircle = new Circle(asteroid.getPosition(), asteroid.getRadius());
            if(Intersector.overlaps(asteroidCircle, playerShipRectangle)) {
                //collision detected
                return true;
            }
        }
        return false;
    }

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

    //Method to check collision between Comet and the player ship
    public static boolean checkPlayerShipCometCollision(PlayerShip playerShip, BossAsteroid boss) {
        Rectangle playerShipRectangle = playerShip.getCollisionRectangle();
        List<Comet> comets = boss.getComets();
        for (Comet comet : comets) {
            Circle cometCircle = new Circle(comet.getPosition(), COMET_RADIUS);
            if(Intersector.overlaps(cometCircle, playerShipRectangle)) {
                //collision detected
                return true;
            }
        }
        return false;
    }

    //Method to check collision between player's bullet and comet
    public static boolean checkPlayerShipBulletCometCollision(PlayerShip playerShip, BossAsteroid boss) {
        List<Bullet> bullets = playerShip.getBullets();
        List<Comet> comets = boss.getComets();
        for (Comet comet : comets) {
            Circle cometCircle = new Circle(comet.getPosition(), COMET_RADIUS);
            for (Bullet bullet : bullets) {
                Circle bulletCircle = new Circle(bullet.getPosition(), BULLET_RADIUS);
                if(Intersector.overlaps(bulletCircle,cometCircle)) {
                    //collision detected
                    return true;
                }
            }
        }
        return false;
    }


    //check collision between player's bullet and Boss asteroid
    public static boolean checkPlayerShipBulletBossAsteroidCollision(PlayerShip playerShip, BossAsteroid boss) {
        List<Bullet> bullets = playerShip.getBullets();
        Circle bossCircle = new Circle(boss.getPosition(), BOSS_RADIUS);
        for(Bullet bullet : bullets) {
            Circle bulletCircle = new Circle(bullet.getPosition(), BULLET_RADIUS);
            if(Intersector.overlaps(bulletCircle, bossCircle)) {
                //collision detected
                return true;
            }
        }
        return false;
    }

    //check collision between player's ship and boss asteroid
    public static boolean checkPlayerShipBossAsteroidCollision(PlayerShip playerShip, BossAsteroid boss) {
        Rectangle playerShipRectangle = playerShip.getCollisionRectangle();
        Circle bossCircle = new Circle(boss.getPosition(), BOSS_RADIUS);
        if (Intersector.overlaps(bossCircle, playerShipRectangle)) {
            //collision detected
            return true;
        }
        return false;
    }

    //check collision between Asteroids
    public static boolean checkAsteroidCollisionWithAnotherAsteroid(AsteroidHandler asteroidHandler) {
        List<Asteroid> asteroids = asteroidHandler.getAsteroids();
        for (Asteroid asteroid1 : asteroids) {
            for (Asteroid asteroid2 : asteroids) {
                Circle asteroid1Circle = new Circle(asteroid1.getPosition(), asteroid1.getRadius());
                Circle asteroid2Circle = new Circle(asteroid2.getPosition(), asteroid2.getRadius());
                if (Intersector.overlaps(asteroid1Circle, asteroid2Circle)) {
                    //collision detected
                    return true;
                }
            }
        }
        return false;
    }

    //check collision between the ship and any active powerups
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

    //check collision between a kill aura circle and asteroids
    public static boolean checkKillAuraCollisionsWithAsteroids(PlayerShip playerShip, AsteroidHandler asteroidHandler){
        Circle killAuraCircle = playerShip.getKillAuraCircle();
        List<Asteroid> asteroidList = asteroidHandler.getAsteroids();

        if (playerShip.getCurrentPowerUpType() != null){
            if (playerShip.getCurrentPowerUpType() == PowerUp.Type.KILL_AURA){
                for (Asteroid asteroid : asteroidList){
                    Circle asteroidCircle = new Circle(asteroid.getPosition(), asteroid.getRadius());
                    if(Intersector.overlaps(asteroidCircle, killAuraCircle)) {
                        //collision detected
                        return true;
                    }
                }
            } else {
                return false;
            }
        }
        return false;
    }

    //check collision between kill aura circle and ufo
    public static boolean checkKillAuraCollisionsWithUFOs(PlayerShip playerShip, UFOShip ufo){
        Circle killAuraCircle = playerShip.getKillAuraCircle();
        Rectangle ufoRectangle = ufo.getCollisionRectangle();

        if (playerShip.getCurrentPowerUpType() != null){
            if (playerShip.getCurrentPowerUpType() == PowerUp.Type.KILL_AURA){
                return Intersector.overlaps(killAuraCircle, ufoRectangle);
            }
        }
        return false;
    }

}

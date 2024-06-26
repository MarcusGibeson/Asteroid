package com.asteroid.game.Controllers;

import static com.asteroid.game.objects.BossAsteroid.BOSS_RADIUS;
import static com.asteroid.game.objects.Bullet.BULLET_RADIUS;
import static com.asteroid.game.objects.Comet.COMET_RADIUS;

import com.asteroid.game.objects.*;
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

    public void update(PlayerShip playerShip, UFOHandler ufoHandler, AsteroidHandler asteroidHandler, List<PowerUp> powerUps) {

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
        if(checkPlayerShipBulletBossAsteroidCollision(playerShip, asteroidHandler)) {
            List<Bullet> playerShipBullets = playerShip.getBullets();
            List<BossAsteroid> bossAsteroids = asteroidHandler.getBossAsteroids();
            for (int i = 0; i < bossAsteroids.size(); i++) {
                BossAsteroid boss = bossAsteroids.get(i);
                Circle bossCircle = new Circle(boss.getPosition(), boss.getRadius());
                for (int j = 0; j < playerShipBullets.size(); j++) {
                    Bullet playerBullet = playerShipBullets.get(j);
                    Circle playerBulletCircle = new Circle(playerBullet.getPosition(), playerBullet.BULLET_RADIUS);
                    if (Intersector.overlaps(playerBulletCircle, bossCircle)) {
                        boss.takeDamage(1);
                        if(boss.getCurrentHealth() <= 0) {
                            boss.setToRemove(true);
                        }
                    }
                }
            }
            asteroidHandler.removeMarkedBosses(bossAsteroids);
        }


        //Comet getting shot by player ship bullet
        if(!playerShip.isPlayerDead()){
            if(checkPlayerShipBulletCometCollision(playerShip, asteroidHandler)) {
                List<BossAsteroid> bossAsteroids = asteroidHandler.getBossAsteroids();
                List<Bullet> playerShipBullets = playerShip.getBullets();
                for(BossAsteroid boss : bossAsteroids) {
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
            }
        }


        //Comet colliding with player
        if(!playerShip.isPlayerDead()){
            if(checkPlayerShipCometCollision(playerShip, asteroidHandler)) {
                List<BossAsteroid> bossAsteroids = asteroidHandler.getBossAsteroids();
                Rectangle playerShipRectangle = playerShip.getCollisionRectangle();
                for (BossAsteroid boss : bossAsteroids) {
                    List<Comet> comets = boss.getComets();
                    for(int i = 0; i < comets.size(); i++) {
                        Comet comet = comets.get(i);
                        Circle cometCircle = new Circle(comet.getPosition(), comet.COMET_RADIUS);
                        if(Intersector.overlaps(cometCircle, playerShipRectangle)) {
                            comets.remove(comet);
                            playerShip.handleCollision();
                        }
                    }
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
                        playerShip.handleCollision();
                    }
                }
                asteroids.addAll(asteroidsToAdd);
            }
        }

        //Player ship and Boss Asteroid colliding
        if(!playerShip.isPlayerDead()){
            if(checkPlayerShipBossAsteroidCollision(playerShip, asteroidHandler)) {
                List<BossAsteroid> bossAsteroids = asteroidHandler.getBossAsteroids();
                Rectangle playerShipRectangle = playerShip.getCollisionRectangle();
                for (int i = 0; i < bossAsteroids.size(); i++) {
                    BossAsteroid boss = bossAsteroids.get(i);
                    Circle bossCircle = new Circle(boss.getPosition(), boss.getRadius());
                    if (Intersector.overlaps(bossCircle, playerShipRectangle)) {
                        playerShip.handleCollision();
                        boss.takeDamage(25);
                        if(boss.getCurrentHealth() <= 0) {
                            boss.setToRemove(true);

                        }
                    }
                }
                asteroidHandler.removeMarkedBosses(bossAsteroids);
            }
        }



        //Player ship and UFO collidiing
        for (UFOShip ufo : ufoHandler.getUfoShips()) {
            if (!ufo.isDestroyed() && !playerShip.isPlayerDead()) {
                if(checkPlayerShipUFOCollision(playerShip, ufoHandler)) {
                    Rectangle ufoRectangle = ufo.getCollisionRectangle();
                    Rectangle playerShipRectangle = playerShip.getCollisionRectangle();
                    if (Intersector.overlaps(playerShipRectangle, ufoRectangle)) {
                        playerShip.handleCollision();
                        ufo.destroy();
                    }
                }
            }
        }


        //UFO getting shot by player ship
        for (UFOShip ufo : ufoHandler.getUfoShips()) {
            if (!ufo.isDestroyed()) {
                if(checkPlayerBulletUFOCollision(playerShip, ufoHandler)) {
                    List<Bullet> playerShipBullets = playerShip.getBullets();
                    Rectangle ufoRectangle = ufo.getCollisionRectangle();
                    for (int i = 0; i < playerShipBullets.size(); i++) {
                        Bullet playerBullet = playerShipBullets.get(i);
                        Circle playerBulletCircle = new Circle(playerBullet.getPosition(), playerBullet.BULLET_RADIUS);
                        if (Intersector.overlaps(playerBulletCircle, ufoRectangle)) {
                            ufo.destroy();
                            scoreHandler.increaseScore(100);
                            playerShipBullets.remove(playerBullet);
                        }
                    }
                }
            }
        }


        //UFO and Player ship colliding
        if (!playerShip.isPlayerDead()) {
            for (UFOShip ufo : ufoHandler.getUfoShips()) {
                if(checkUFOBulletPlayerShipCollision(ufoHandler, playerShip)) {
                    List<Bullet> ufoBullets = ufo.getBullets();
                    Rectangle playerShipRectangle = playerShip.getCollisionRectangle();

                    for (int i = 0; i < ufoBullets.size(); i++) {
                        Bullet ufoBullet = ufoBullets.get(i);
                        Circle ufoBulletCircle = new Circle(ufoBullet.getPosition(), ufoBullet.BULLET_RADIUS);
                        if(Intersector.overlaps(ufoBulletCircle, playerShipRectangle)) {
                            playerShip.handleCollision();
                            ufoBullets.remove(ufoBullet);
                        }

                    }
                }
            }

        }

        //Player ship getting shot by UFO
       if (checkPlayerShipBulletUFOBulletCollision(playerShip, ufoHandler)) {
           List<Bullet> playerShipBullets = playerShip.getBullets();
           for (UFOShip ufoShip : ufoHandler.getUfoShips()) {
               List<Bullet> ufoBullets = ufoShip.getBullets();

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

       }

        //Player touching a power up
        if (checkPowerUpPlayerCollision(playerShip, powerUps)) {
            Iterator<PowerUp> iterator = powerUps.iterator(); //turning it into a modifiable object
            //while it isn't empty
            while (iterator.hasNext()) {
                //powerup is the next powerup in the list
                PowerUp powerUp = iterator.next();
                //if it's touching the ship
                if (powerUp.isTouchingShip()) {
                    //apply it
                    powerUp.applyToShip(playerShip);
                    //remove it
                    iterator.remove();
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

        if (checkKillAuraCollisionsWithUFOs(playerShip, ufoHandler)) {
            Circle killAuraCircle = playerShip.getKillAuraCircle();
            for (UFOShip ufo : ufoHandler.getUfoShips()) {
                Rectangle ufoRectangle = ufo.getCollisionRectangle();
                if (Intersector.overlaps(killAuraCircle, ufoRectangle)) {
                    ufo.setDestroyed(true);
                    scoreHandler.increaseScore(100);
                }
            }
        }


    }

    //Method to check collision between UFO bullets and player ship
    public static boolean checkUFOBulletPlayerShipCollision(UFOHandler ufoHandler, PlayerShip playerShip) {
        for (UFOShip ufoShip : ufoHandler.getUfoShips()) {
            for (Bullet bullet : ufoShip.getBullets()) {
                Circle bulletCircle = new Circle(bullet.getPosition(), BULLET_RADIUS);
                Rectangle playerShipRectangle = playerShip.getCollisionRectangle();
                if (Intersector.overlaps(bulletCircle, playerShipRectangle)) {
                    //collision detected
                    return true;
                }
            }
        }
        return false;
    }

    //Method to check collision between Player ship bullets and UFO
    public static boolean checkPlayerBulletUFOCollision(PlayerShip playerShip, UFOHandler ufoHandler) {
        for (Bullet bullet : playerShip.getBullets()) {
            Circle bulletCircle = new Circle(bullet.getPosition(), BULLET_RADIUS);
            for (UFOShip ufoShip : ufoHandler.getUfoShips()) {
                Rectangle ufoRectangle = ufoShip.getCollisionRectangle();
                if (Intersector.overlaps(bulletCircle, ufoRectangle)) {
                    //collision detected
                    return true;
                }
            }

        }
        return false;
    }

    //Method to check collision between Player ship and UFO
    public static boolean checkPlayerShipUFOCollision(PlayerShip playerShip, UFOHandler ufoHandler) {
        Rectangle playerShipRectangle = playerShip.getCollisionRectangle();
        for (UFOShip ufoShip : ufoHandler.getUfoShips()) {
            Rectangle ufoRectangle = ufoShip.getCollisionRectangle();
            if (Intersector.overlaps(playerShipRectangle, ufoRectangle)) {
                //collision detected
                return true;
            }
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

    public static boolean checkPlayerShipBulletUFOBulletCollision(PlayerShip playerShip, UFOHandler ufoHandler) {
        List<Bullet> playerShipBullets = playerShip.getBullets();
        for (UFOShip ufo : ufoHandler.getUfoShips()) {
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
        }
        return false;
    }

    //Method to check collision between Comet and the player ship
    public static boolean checkPlayerShipCometCollision(PlayerShip playerShip, AsteroidHandler asteroidHandler) {
        List<BossAsteroid> bossAsteroids = asteroidHandler.getBossAsteroids();
        Rectangle playerShipRectangle = playerShip.getCollisionRectangle();
        for(BossAsteroid boss : bossAsteroids) {
            List<Comet> comets = boss.getComets();
            for (Comet comet : comets) {
                Circle cometCircle = new Circle(comet.getPosition(), COMET_RADIUS);
                if(Intersector.overlaps(cometCircle, playerShipRectangle)) {
                    //collision detected
                    return true;
                }
            }
        }

        return false;
    }

    //Method to check collision between player's bullet and comet
    public static boolean checkPlayerShipBulletCometCollision(PlayerShip playerShip, AsteroidHandler asteroidHandler) {
        List<BossAsteroid> bossAsteroids = asteroidHandler.getBossAsteroids();
        List<Bullet> bullets = playerShip.getBullets();
        for(BossAsteroid boss : bossAsteroids) {
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
        }

        return false;
    }





    //check collision between player's bullet and Boss asteroid
    public static boolean checkPlayerShipBulletBossAsteroidCollision(PlayerShip playerShip, AsteroidHandler asteroidHandler) {
        List<BossAsteroid> bossAsteroids = asteroidHandler.getBossAsteroids();
        List<Bullet> bullets = playerShip.getBullets();
        for(BossAsteroid boss : bossAsteroids) {
            Circle bossCircle = new Circle(boss.getPosition(), BOSS_RADIUS);
            for(Bullet bullet : bullets) {
                Circle bulletCircle = new Circle(bullet.getPosition(), BULLET_RADIUS);
                if(Intersector.overlaps(bulletCircle, bossCircle)) {
                    //collision detected
                    return true;
                }
            }
        }
        return false;
    }

    //check collision between player's ship and boss asteroid
    public static boolean checkPlayerShipBossAsteroidCollision(PlayerShip playerShip, AsteroidHandler asteroidHandler) {
        Rectangle playerShipRectangle = playerShip.getCollisionRectangle();
        List<BossAsteroid> bossAsteroids = asteroidHandler.getBossAsteroids();
        for(BossAsteroid boss : bossAsteroids) {
            Circle bossCircle = new Circle(boss.getPosition(), BOSS_RADIUS);
            if (Intersector.overlaps(bossCircle, playerShipRectangle)) {
                //collision detected
                return true;
            }
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
        // No collision detected, return false
        if (!playerShip.isTouchingPowerUp()) {
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
        }
        return false;
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
    public static boolean checkKillAuraCollisionsWithUFOs(PlayerShip playerShip, UFOHandler ufoHandler){
        Circle killAuraCircle = playerShip.getKillAuraCircle();
        for (UFOShip ufo : ufoHandler.getUfoShips()) {
            Rectangle ufoRectangle = ufo.getCollisionRectangle();

            if (playerShip.getCurrentPowerUpType() != null){
                if (playerShip.getCurrentPowerUpType() == PowerUp.Type.KILL_AURA){
                    return Intersector.overlaps(killAuraCircle, ufoRectangle);
                }
            }
        }

        return false;
    }

}

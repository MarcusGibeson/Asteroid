package com.asteroid.game;

import static com.asteroid.game.BossAsteroid.BOSS_RADIUS;
import static com.asteroid.game.Bullet.BULLET_RADIUS;
import static com.asteroid.game.Comet.COMET_RADIUS;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.List;

public class CollisionHandler {

    ScoreHandler scoreHandler;
    public CollisionHandler(ScoreHandler scoreHandler) {
        this.scoreHandler = scoreHandler;
    }

    public void update(PlayerShip playerShip, UFOShip ufo, BossAsteroid boss) {


        if (!boss.checkIsDestroyed() && !playerShip.isPlayerDead()) {
            if(checkPlayerShipBulletBossAsteroidCollision(playerShip, boss)) {
                //playerShip.handleCollision();
                boss.takeDamage(50);
            }
        }

        if (!ufo.isDestroyed() && !playerShip.isPlayerDead()) {
            if(checkPlayerShipUFOCollision(playerShip, ufo)) {
//                playerShip.handleCollision();
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
//                playerShip.handleCollision();

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

    public static boolean checkPlayerShipBossAsteroidCollision(PlayerShip playerShip, BossAsteroid boss) {
        Rectangle playerShipRectangle = playerShip.getCollisionRectangle();
        Circle bossCircle = new Circle(boss.getPosition(), BOSS_RADIUS);
        if (Intersector.overlaps(bossCircle, playerShipRectangle)) {
            //collision detected
            return true;
        }
        return false;
    }

}

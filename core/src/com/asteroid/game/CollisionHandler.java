package com.asteroid.game;

import static com.asteroid.game.Bullet.BULLET_RADIUS;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.List;

public class CollisionHandler {


    public void update(PlayerShip playerShip, UFOShip ufo) {
        if (!ufo.isDestroyed() && !playerShip.isPlayerDead()) {
            if(checkPlayerShipUFOCollision(playerShip, ufo)) {
//                playerShip.handleCollision();
                ufo.destroy();
            }
        }

        if (!ufo.isDestroyed()) {
            if(checkPlayerBulletUFOCollision(playerShip, ufo)) {
                ufo.destroy();
                //add point logic here
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
}

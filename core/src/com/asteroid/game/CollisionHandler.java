package com.asteroid.game;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.List;

public class CollisionHandler {


    public void update(PlayerShip playerShip, UFOShip ufo) {
        if(checkPlayerShipUFOCollision(playerShip, ufo)) {
            playerShip.destroy();
            ufo.destroy();

            //trigger respawn logic of playership here

        }

        if(checkPlayerBulletUFOCollision(playerShip, ufo)) {
            ufo.destroy();
            //add point logic here
        }

        if(checkUFOBulletPlayerShipCollision(ufo, playerShip)) {
            playerShip.destroy();
            //trigger respawn logic - i lied, respawn logic is in boolean inside classes

        }


        //not functional yet
        if (checkPlayerShipBulletUFOBulletCollision(playerShip, ufo)) {
            //remove player bullet
            List<Bullet> playerShipBullets = playerShip.getBullets();
            for (int i = 0; i < playerShipBullets.size(); i++) {
                Bullet playerBullet = playerShipBullets.get(i);
                if(checkPlayerShipBulletUFOBulletCollision(playerShip, ufo)) {
                    playerShipBullets.remove(playerBullet);
                    i--;
                }
            }

            //Remove ufo bullet
            List<Bullet> ufoBullets = ufo.getBullets();
            for (int i = 0; i < ufoBullets.size(); i++) {
                Bullet ufoBullet = ufoBullets.get(i);
                if(checkPlayerShipBulletUFOBulletCollision(playerShip, ufo)) {
                    ufoBullets.remove(ufoBullet);
                    i--;
                }
            }
        }
    }

    //Method to check collision between UFO bullets and player ship
    public static boolean checkUFOBulletPlayerShipCollision(UFOShip ufo, PlayerShip playerShip) {
        for (Bullet bullet : ufo.getBullets()) {
            Circle bulletCircle = new Circle(bullet.getPosition(), Bullet.BULLET_RADIUS);
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
            Circle bulletCircle = new Circle(bullet.getPosition(), Bullet.BULLET_RADIUS);
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
        for (Bullet playerBullet : playerShip.getBullets()) {
            Circle playerBulletCircle = new Circle(playerBullet.getPosition(), playerBullet.BULLET_RADIUS);
            for (Bullet ufoBullet : ufo.getBullets()) {
                Circle ufoBulletCircle = new Circle(ufoBullet.getPosition(), ufoBullet.BULLET_RADIUS);
                if (Intersector.overlaps(playerBulletCircle, ufoBulletCircle)) {
                    // collision detected
                    return true;
                }
            }
        }
        return false; // no collision detected
    }
}

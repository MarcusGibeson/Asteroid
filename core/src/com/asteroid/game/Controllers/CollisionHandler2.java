package com.asteroid.game.Controllers;

import static com.asteroid.game.objects.BossAsteroid.BOSS_RADIUS;
import static com.asteroid.game.objects.Bullet.BULLET_RADIUS;
import static com.asteroid.game.objects.Comet.COMET_RADIUS;

import com.asteroid.game.objects.Asteroid;
import com.asteroid.game.objects.BossAsteroid;
import com.asteroid.game.objects.Bullet;
import com.asteroid.game.objects.Comet;
import com.asteroid.game.objects.PlayerShip;
import com.asteroid.game.objects.PowerUp;
import com.asteroid.game.objects.UFOShip;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Iterator;
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
    private void handlePlayerBulletCollisions(PlayerShip playerShip, AsteroidHandler asteroidHandler, UFOHandler ufoHandler) {

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

    public static boolean checkPlayerBulletUFOCollision(PlayerShip playerShip, UFOHandler ufoHandler) {
        for (Bullet bullet : playerShip.getBullets()) {
            Circle bulletCircle = new Circle(bullet.getPosition(), BULLET_RADIUS);
            for (UFOShip ufoShip : ufoHandler.getUfoShips()) {
                if (Intersector.overlaps(bulletCircle, ufoShip.getCollisionRectangle())) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean checkPlayerShipUFOCollision(PlayerShip playerShip, UFOHandler ufoHandler) {
        Rectangle playerShipRectangle = playerShip.getCollisionRectangle();
        for (UFOShip ufoShip : ufoHandler.getUfoShips()) {
            if (Intersector.overlaps(playerShipRectangle, ufoShip.getCollisionRectangle())) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkPlayerShipBulletUFOBulletCollision(PlayerShip playerShip, UFOHandler ufoHandler) {
        List<Bullet> playerShipBullets = playerShip.getBullets();
        for (UFOShip ufo : ufoHandler.getUfoShips()) {
            for (Bullet ufoBullet : ufo.getBullets()) {
                Circle ufoBulletCircle = new Circle(ufoBullet.getPosition(), BULLET_RADIUS);
                for (Bullet playerBullet : playerShipBullets) {
                    Circle playerBulletCircle = new Circle(playerBullet.getPosition(), BULLET_RADIUS);
                    if (Intersector.overlaps(playerBulletCircle, ufoBulletCircle)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean checkPlayerShipAsteroidCollision(PlayerShip playerShip, AsteroidHandler asteroidHandler) {
        Rectangle playerShipRectangle = playerShip.getCollisionRectangle();
        for (Asteroid asteroid : asteroidHandler.getAsteroids()) {
            if (checkPolygonRectangleCollision(asteroid.getPolygon(), playerShipRectangle)) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkPlayerShipCometCollision(PlayerShip playerShip, AsteroidHandler asteroidHandler) {
        Rectangle playerShipRectangle = playerShip.getCollisionRectangle();
        for (BossAsteroid boss : asteroidHandler.getBossAsteroids()) {
            for (Comet comet : boss.getComets()) {
                Circle cometCircle = new Circle(comet.getPosition(), COMET_RADIUS);
                if (Intersector.overlaps(cometCircle, playerShipRectangle)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean checkPlayerShipBulletAsteroidCollision(PlayerShip playerShip, AsteroidHandler asteroidHandler) {
        for (Asteroid asteroid : asteroidHandler.getAsteroids()) {
            for (Bullet bullet : playerShip.getBullets()) {
                if (checkPolygonCircleCollision(asteroid.getPolygon(), new Circle(bullet.getPosition(), BULLET_RADIUS))) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean checkPlayerShipBulletBossAsteroidCollision(PlayerShip playerShip, AsteroidHandler asteroidHandler) {
        for (BossAsteroid boss : asteroidHandler.getBossAsteroids()) {
            for (Bullet bullet : playerShip.getBullets()) {
                if (Intersector.overlaps(new Circle(bullet.getPosition(), BULLET_RADIUS), new Circle(boss.getPosition(), BOSS_RADIUS))) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean checkPlayerShipBulletCometCollision(PlayerShip playerShip, AsteroidHandler asteroidHandler) {
        for (BossAsteroid boss : asteroidHandler.getBossAsteroids()) {
            for (Bullet bullet : playerShip.getBullets()) {
                for (Comet comet : boss.getComets()) {
                    if (Intersector.overlaps(new Circle(bullet.getPosition(), BULLET_RADIUS), new Circle(comet.getPosition(), COMET_RADIUS))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    public static boolean checkPlayerShipBossAsteroidCollision(PlayerShip playerShip, AsteroidHandler asteroidHandler) {
        for (BossAsteroid boss : asteroidHandler.getBossAsteroids()) {
            if (Intersector.overlaps(new Circle(boss.getPosition(), BOSS_RADIUS), playerShip.getCollisionRectangle())) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkPowerUpPlayerCollision(PlayerShip playerShip, List<PowerUp> powerUps) {
        Rectangle playerShipRectangle = playerShip.getCollisionRectangle();
        for (PowerUp powerUp : powerUps) {
            Rectangle powerUpRectangle = powerUp.getCollisionRectangle();
            if (Intersector.overlaps(powerUpRectangle, playerShipRectangle)) {
                powerUp.setTouchingShip(true);
                return true;
            }
        }
        return false;
    }

    public static boolean checkAsteroidCollisionWithAnotherAsteroid(AsteroidHandler asteroidHandler) {
        List<Asteroid> asteroids = asteroidHandler.getAsteroids();
        for (int i = 0; i < asteroids.size(); i++) {
            Asteroid asteroid1 = asteroids.get(i);
            Polygon asteroidPolygon1 = asteroid1.getPolygon();
            for (int j = i + 1; j < asteroids.size(); j++) {
                Asteroid asteroid2 = asteroids.get(j);
                Polygon asteroidPolygon2 = asteroid2.getPolygon();
                if (Intersector.overlapConvexPolygons(asteroidPolygon1, asteroidPolygon2)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean checkKillAuraCollisionsWithAsteroids(PlayerShip playerShip, AsteroidHandler asteroidHandler) {
        Circle killAuraCircle = playerShip.getKillAuraCircle();
        for (Asteroid asteroid : asteroidHandler.getAsteroids()) {
            if (Intersector.overlaps(killAuraCircle, new Circle(asteroid.getPosition(), asteroid.getRadius()))) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkKillAuraCollisionsWithUFOs(PlayerShip playerShip, UFOHandler ufoHandler) {
        Circle killAuraCircle = playerShip.getKillAuraCircle();
        for (UFOShip ufo : ufoHandler.getUfoShips()) {
            if (Intersector.overlaps(killAuraCircle, ufo.getCollisionRectangle())) {
                return true;
            }
        }
        return false;
    }
    //REGION ADDITIONAL METHODS
    private void handleBulletAsteroidCollision(List<Bullet> playerShipBullets, List<Asteroid> asteroids) {
        for (Bullet bullet : playerShipBullets) {
            Circle bulletCircle = new Circle(bullet.getPosition(), BULLET_RADIUS);
            Iterator<Asteroid> iterator = asteroids.iterator();
            while (iterator.hasNext()) {
                Asteroid asteroid = iterator.next();
                if (checkPolygonCircleCollision(asteroid.getPolygon(), bulletCircle)) {
                    bullet.setToRemove(true);
//                    asteroid.takeDamage(1);
//                    if(asteroid.isDestroyed()) {
//                        iterator.remove();
//                        scoreHandler.increaseScore(10);
//                    }
                }
            }
        }
    }

    private void handleBulletBossAsteroidCollision(List<Bullet> playerShipBullets, List<BossAsteroid> bosses) {
        for (Bullet bullet : playerShipBullets) {
            Circle bulletCircle = new Circle(bullet.getPosition(), BULLET_RADIUS);
            Iterator<BossAsteroid> iterator = bosses.iterator();
            while (iterator.hasNext()) {
                BossAsteroid boss = iterator.next();
                if (checkPolygonCircleCollision(boss.getPolygon(), bulletCircle)) {
                    bullet.setToRemove(true);
                    boss.takeDamage(1);
                    if (boss.checkIsDestroyed()) {
                        scoreHandler.increaseScore(100);
                    }
                }
            }
        }
    }



    //REGION COLLISION SHAPE CHECKS

    private static boolean checkPolygonRectangleCollision(Polygon polygon, Rectangle rectangle) {
        Polygon rectanglePolygon = new Polygon(new float[] {
           rectangle.x, rectangle.y,
           rectangle.x + rectangle.width, rectangle.y,
           rectangle.x + rectangle.width, rectangle.y + rectangle.height,
           rectangle.x, rectangle.y + rectangle.height
        });
        return Intersector.overlapConvexPolygons(polygon, rectanglePolygon);
    }

    private static boolean checkPolygonCircleCollision(Polygon polygon, Circle circle) {
        //Get vertices of polygon
        float [] vertices = polygon.getTransformedVertices();
        Vector2 center = new Vector2(circle.x, circle.y);
        float squareRadius = circle.radius * circle.radius;

        //Check if any of the vertices are inside the circle
        for (int i = 0; i < vertices.length; i += 2) {
            Vector2 vertex = new Vector2(vertices[i], vertices[i+1]);
            if (vertex.dst2(center) < squareRadius) {
                return true;
            }
        }

        //Check if any of the edges are overlapping with the circle
        for (int i = 0; i < vertices.length; i += 2) {
            Vector2 start = new Vector2(vertices[i], vertices[i+1]);
            Vector2 end = new Vector2(vertices[(i + 2) % vertices.length], vertices[(i + 3) % vertices.length]);
            if (Intersector.intersectSegmentCircle(start, end, center, circle.radius * circle.radius)) {
                return true;
            }
        }

        //Check if the center of the circle is inside the polygon
        if (polygon.contains(circle.x, circle.y)) {
            return true;
        }

        return false;
    }
}

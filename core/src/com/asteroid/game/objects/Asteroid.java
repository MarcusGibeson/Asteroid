package com.asteroid.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Asteroid {
    //region **CONSTANTS**
    public Vector2 position, velocity, polygonCenter;
    private int tier;
    private int tierLevel;
    private float width;
    private float height;
    private List<Vector2> spawnNodes;
    private boolean hitByBullet;
    private PlayerShip playerShip;
    public boolean toRemove;
    private float asteroidMultiplier;
    private int asteroidType;
    public float[] polygonVertices;

//    public static final float SMALL_ASTEROID_RADIUS = 20;
//    public static final float MEDIUM_ASTEROID_RADIUS = 80;
//    public static final float LARGE_ASTEROID_RADIUS = 160;
//
//    private static final float SCREEN_HEIGHT = 720f;
//    private static final float SCREEN_WIDTH = 1280f;
//    private static final float MAX_SPEED = 5f;
    private float asteroidRadius;
    // Defining spawn node coordinates so they can easily be assigned

    public static final int[][] spawnCoordinates = {
            //region spawn node coords
            {160, 719},
            {480, 719},
            {800, 719},
            {1120, 719},
            {320, 1},
            {640, 1},
            {960, 1},
            {1, 90},
            {1, 270},
            {1, 450},
            {1, 630},
            {1279, 180},
            {1279, 360},
            {1279, 540}
            //endregion
    };
    //endregion

    //Main constructor for parent asteroids
    public Asteroid(int node, int tier, PlayerShip playerShip) {
        this.playerShip = playerShip;
        hitByBullet = false;
        this.spawnNodes = new ArrayList<>();
        for (int[] coord : spawnCoordinates) {
            spawnNodes.add(new Vector2(coord[0], coord[1]));
        }
        this.position = new Vector2(spawnNodes.get(node));
        this.velocity = new Vector2();
        assignTierParameters(tier);
        toRemove = false;
        asteroidType = MathUtils.random(1,3);
        polygonCenter = new Vector2(position.x + 20 * asteroidMultiplier, position.y + 20 * asteroidMultiplier);
        assignPolygonVertices(asteroidType);
    }

    //Constructor for child/sibling asteroids
    public Asteroid(Vector2 parentPosition, int parentTier, PlayerShip playerShip, int parentType){
        this.playerShip = playerShip;
        if (parentTier <= 1){
            return;
        }
        this.position = new Vector2(parentPosition.x + randomNonZeroValue(-100,100), parentPosition.y + randomNonZeroValue(-100,100));
        this.velocity = new Vector2();
        int childTier = parentTier - 1;
        this.tier = childTier;
        assignTierParameters(childTier);
        asteroidType = parentType;
        toRemove = false;
        polygonCenter = new Vector2(position.x + 20 * asteroidMultiplier, position.y + 20 * asteroidMultiplier);
        assignPolygonVertices(asteroidType);
    }

    //region **GETTERS AND SETTERS**
    public float getRadius() {
        return asteroidRadius;
    }
    public Vector2 getVelocity() {return velocity;}
    public void setVelocity(Vector2 velocity) {
        this.velocity.set(velocity);
    }
    public int getAsteroidType () {return asteroidType;}
    public void setPosition(Vector2 position) {
        this.position = position;
    }
    public int getTierLevel() {return tierLevel;} //changed to tierLevel to keep consistent
    public Vector2 getPosition() {return position;}

    public boolean isHitByBullet() {return hitByBullet;}

    public boolean isToRemove() {
        return toRemove;
    }
    public void setToRemove(boolean toRemove) {
        this.toRemove = toRemove;
    }
    //endregion

    public void update(float delta) {
        System.out.println("delta: " + delta);
        System.out.println("Current position: " + position);
        System.out.println("Current velocity: " + velocity);
        // Update position based on velocity
        position.add(velocity.x, velocity.y);

        // Update center based on new position
        polygonCenter.set(position.x + 20 * asteroidMultiplier, position.y + 20 * asteroidMultiplier);

        // Apply off-screen wrapping
        loopOffScreenMovement();

        // Update vertices position based on velocity
        for (int i = 0; i < polygonVertices.length; i += 2) {
            polygonVertices[i] += velocity.x * delta;
            polygonVertices[i + 1] += velocity.y * delta;
        }

        // Apply rotation around the new center
        handleRotation(polygonVertices, polygonCenter);
    }

    public void draw(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.polygon(polygonVertices);

    }

    public void assignTierParameters(int tier){
        switch(tier){
            case 1: //Small asteroid
                height = 40;
                width = 40;
                setVelocity(new Vector2(randomNonZeroValue(-4, 4), randomNonZeroValue(-4,4)));
                tierLevel = 1;
//                asteroidRadius = SMALL_ASTEROID_RADIUS;
                asteroidMultiplier = 1;
                break;
            case 2: //Medium asteroid
                height = 160;
                width = 160;
                setVelocity(new Vector2(randomNonZeroValue(-2,2), randomNonZeroValue(-2,2)));
                tierLevel = 2;
//                asteroidRadius = MEDIUM_ASTEROID_RADIUS;
                asteroidMultiplier = 4;
                break;
            case 3: //Large asteroid
                height = 320;
                width = 320;
                setVelocity(new Vector2(randomNonZeroValue(-1,1), randomNonZeroValue(-1,1)));
                tierLevel = 3;
//                asteroidRadius = LARGE_ASTEROID_RADIUS;
                asteroidMultiplier = 8;
                break;
            default:
                throw new IllegalArgumentException("Invalid tier value: " + tier);
        }
    }

    private float randomNonZeroValue(int minValue, int maxValue) {
        float randomValue;
        do {
            randomValue = MathUtils.random(minValue, maxValue);
        } while (randomValue == 0); // Keep generating until a non-zero value is obtained
        return randomValue;
    }

    //yes I did just copy your loop method from player class lol
    public void loopOffScreenMovement() {
        //changes position based on location so ship remains on screen
        if (polygonCenter.x < 0) {
            polygonCenter.x = Gdx.graphics.getWidth();
        } else if (polygonCenter.x > Gdx.graphics.getWidth() + width) {
            polygonCenter.x = 0; //moves ship to left side of screen if exits right
        }
        if (polygonCenter.y < 0) {
            polygonCenter.y = Gdx.graphics.getHeight(); //moves ship to top side of screen if exits bottom
        } else if (polygonCenter.y > Gdx.graphics.getHeight()){
            polygonCenter.y = 0; //moves ship to bottom of screen if exits top
        }
    }

    public void detectCollision(){
        List<Bullet> bullets = playerShip.getBullets();
        if (!bullets.isEmpty()){
            Iterator<Bullet> iterator = bullets.iterator();
            while (iterator.hasNext()){
                Bullet bullet = iterator.next();
                if (intersects(bullet.getPosition())){
                    hitByBullet = true;
                    iterator.remove();
                    break;
                }
            }
        }
    }



    private boolean intersects(Vector2 bulletPosition){
        // Calculate distance between bullet and asteroid center
        float distance = position.dst(bulletPosition);
        // Check if distance is less than the sum of their radii
        return distance < (width / 2 + 2);
    }





    private void handleRotation(float[] vertices, Vector2 center) {
        float angle = 2.0f;
        float cos = MathUtils.cosDeg(angle);
        float sin = MathUtils.sinDeg(angle);

        for (int i = 0; i < vertices.length; i += 2) {
            float x = vertices[i];
            float y = vertices[i + 1];

            float translatedX = x - center.x;
            float translatedY = y - center.y;

            float rotatedX = translatedX * cos - translatedY * sin;
            float rotatedY = translatedX * sin + translatedY * cos;

            vertices[i] = rotatedX + center.x;
            vertices[i + 1] = rotatedY + center.y;
        }
    }

    public void assignPolygonVertices(int asteroidType){
        //region cross asteroid spawn coordinates
        final float[] crossCoordinates = {
                position.x, position.y,
                position.x + 10*asteroidMultiplier, position.y - 2*asteroidMultiplier,
                position.x + 13*asteroidMultiplier, position.y - 13*asteroidMultiplier,
                position.x + 26*asteroidMultiplier, position.y - 13*asteroidMultiplier,
                position.x + 26*asteroidMultiplier, position.y - 2*asteroidMultiplier,
                position.x + 38*asteroidMultiplier, position.y,
                position.x + 40*asteroidMultiplier, position.y + 13*asteroidMultiplier,
                position.x + 27*asteroidMultiplier, position.y + 15*asteroidMultiplier,
                position.x + 26*asteroidMultiplier, position.y + 26*asteroidMultiplier,
                position.x + 13*asteroidMultiplier, position.y + 24*asteroidMultiplier,
                position.x + 11*asteroidMultiplier, position.y + 14*asteroidMultiplier,
                position.x, position.y + 13*asteroidMultiplier
        };
        //endregion

        //region skull-type spawn coordinates
        final float[] skullCoordinates = {
                position.x, position.y,
                position.x + 10*asteroidMultiplier, position.y,
                position.x + 10*asteroidMultiplier, position.y - 15*asteroidMultiplier,
                position.x + 30*asteroidMultiplier, position.y - 15*asteroidMultiplier,
                position.x + 30*asteroidMultiplier, position.y,
                position.x + 40*asteroidMultiplier, position.y,
                position.x + 40*asteroidMultiplier, position.y + 10*asteroidMultiplier,
                position.x + 30*asteroidMultiplier, position.y + 25*asteroidMultiplier,
                position.x + 10*asteroidMultiplier, position.y + 25*asteroidMultiplier,
                position.x, position.y + 10*asteroidMultiplier
        };
        //endregion

        //region weird type spawn coordinates (shield? idk)
        final float[] weirdShieldCoordinates = {
                position.x, position.y,
                position.x + 20*asteroidMultiplier, position.y + 12*asteroidMultiplier,
                position.x + 40*asteroidMultiplier, position.y,
                position.x + 40*asteroidMultiplier, position.y + 25*asteroidMultiplier,
                position.x + 30*asteroidMultiplier, position.y + 40*asteroidMultiplier,
                position.x + 20*asteroidMultiplier, position.y + 40*asteroidMultiplier,
                position.x, position.y + 25*asteroidMultiplier
        };
        //endregion

        switch(asteroidType){
            case 1:
                polygonVertices = crossCoordinates;
                break;
            case 2:
                polygonVertices = skullCoordinates;
                break;
            case 3:
                polygonVertices = weirdShieldCoordinates;
                break;
            default:
                System.out.println("debug text, defaulting instead of passing asteroidType parameter");
                polygonVertices = crossCoordinates;
                break;
        }
    }
}

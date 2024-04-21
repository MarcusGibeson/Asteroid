package com.asteroid.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Asteroid {
    private Vector2 position, velocity;
    private int tier;
    private float width, height;
    private List<Vector2> spawnNodes;
    private boolean hitByBullet;
    private PlayerShip playerShip;
    private UFOShip ufo;
    private static final float SCREEN_HEIGHT = Gdx.graphics.getHeight();
    private static final float SCREEN_WIDTH = Gdx.graphics.getWidth();
    private static final float MAX_SPEED = 2f;

    // Defining spawn node coordinates so they can easily be assigned
    static final int[][] spawnCoordinates = {
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
    };



    //Main constructor for parent asteroids
    public Asteroid(int node, int tier, PlayerShip playerShip, UFOShip ufo) {
        this.playerShip = playerShip;
        this.ufo = ufo;
        this.tier = tier;
        hitByBullet = false;
        this.spawnNodes = new ArrayList<>();
        for (int[] coord : spawnCoordinates) {
            spawnNodes.add(new Vector2(coord[0], coord[1]));
        }
        this.position = spawnNodes.get(node);
        assignTierParameters(tier);
        System.out.println("Tier upon initialize: " + tier);
    }

    //Constructor for child/sibling asteroids
    public Asteroid(Vector2 parentPosition, int parentTier, PlayerShip playerShip, UFOShip ufo){
        this.playerShip = playerShip;
        this.ufo = ufo;
        if (parentTier <= 1){
            return;
        }
        this.position = new Vector2(parentPosition.x + MathUtils.random(-10,10), parentPosition.y + MathUtils.random(-10,10));
        int childTier = parentTier - 1;
        this.tier = childTier;
        assignTierParameters(childTier);
    }

    public void update(float delta) {
        //Update asteroid's position
        position.x += velocity.x;
        position.y += velocity.y;

        loopOffScreenMovement();
    }

    private float[] calculateSmallAsteroidVertices() {
        //Define custom points relative to the center of the asteroid
        float[] customPoints = {
                -20, 10, //Vertex 1 (x,y)
                20, 10,
                30, -10,
                10,-20,
                -10, -30,
                -30, -20
        };

        //Calculate the total number of vertices
        int numberOfVertices = customPoints.length / 2;

        //Initialize array to store vertices
        float[] vertices = new float[numberOfVertices * 2];

        //Copy custom points to vertices array
        System.arraycopy(customPoints, 0, vertices, 0, customPoints.length);
        return vertices;
    }

    public void draw(ShapeRenderer shapeRenderer) {

//        shapeRenderer.circle(position.x, position.y, width/2);
        switch(tier) {
            case 1:
                drawSmallAsteroid(shapeRenderer);
                break;
            case 2:
              drawSmallAsteroid(shapeRenderer);
                break;
            case 3:
              drawSmallAsteroid(shapeRenderer);
                break;
            default:
                throw new IllegalArgumentException("Invalid tier value " + tier);
        }
    }


    public void assignTierParameters(int tier){
        switch(tier){
            case 1: //Small asteroid
//                health = 1;
                height = 40;
                width = 40;
                velocity = new Vector2(4,4);
                break;
            case 2: //Medium asteroid
//                health = 2;
                height = 160;
                width = 160;
                velocity = new Vector2(2,2);
                break;
            case 3: //Large asteroid
//                health = 3;
                height = 300;
                width = 300;
                velocity = new Vector2(1,1);
                break;
            default:
                throw new IllegalArgumentException("Invalid tier value: " + tier);
        }
    }

    public void drawSmallAsteroid(ShapeRenderer shapeRenderer) {
        shapeRenderer.begin();
        //calculate custom vertices for the asteroid
        float[] vertices = calculateSmallAsteroidVertices();

        //draw the asteroid using the custom vertices
        shapeRenderer.polygon(vertices);

        shapeRenderer.end();

    }

    //yes I did just copy your loop method from player class lol
    public void loopOffScreenMovement() {
        if (position.x < 0) {
            position.x = Gdx.graphics.getWidth(); //moves asteroid to right side of screen if exits left
        } else if (position.x > Gdx.graphics.getWidth()) {
            position.x = 0; //moves asteroid to left side of screen if exits right
        }
        if (position.y < 0) {
            position.y = Gdx.graphics.getHeight(); //moves asteroid to top side of screen if exits bottom
        } else if (position.y > Gdx.graphics.getHeight()) {
            position.y = 0; //moves asteroid to bottom of screen if exits top
        }
    }

    public void detectCollision(){
        List<Bullet> bullets = playerShip.getBullets();
        bullets.addAll(ufo.getBullets());
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

    public int getTier() {return tier;}
    public Vector2 getPosition() {return position;}
    public boolean isHitByBullet(){return hitByBullet;}
}

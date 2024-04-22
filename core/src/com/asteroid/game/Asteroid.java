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
    float speed = 100;
    private static final float SCREEN_HEIGHT = 720f;
    private static final float SCREEN_WIDTH = 1280f;
    private static final float MAX_SPEED = 5f;

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
    public Asteroid(int x, int y, int tier, PlayerShip playerShip) {
        this.tier = tier;
        assignTierParameters(tier);
        this.position = new Vector2(x, y);
        this.playerShip = playerShip;

        hitByBullet = false;
//        this.spawnNodes = new ArrayList<>();
//        for (int[] coord : spawnCoordinates) {
//            spawnNodes.add(new Vector2(coord[0], coord[1]));
//        }
//        spawnOffScreen(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

//        this.position = spawnNodes.get(node);

    }

//    //Constructor for child/sibling asteroids
//    public Asteroid(Vector2 parentPosition, int parentTier, PlayerShip playerShip){
//        this.playerShip = playerShip;
//        if (parentTier <= 1){
//            return;
//        }
//        this.position = new Vector2(parentPosition.x + MathUtils.random(-10,10), parentPosition.y + MathUtils.random(-10,10));
//        int childTier = parentTier - 1;
//        this.tier = childTier;
//        assignTierParameters(childTier);
//    }

    public void update(float delta) {
        //Update asteroid's position
        position.x += velocity.x;
        position.y += velocity.y;

        loopOffScreenMovement();
    }

    public void draw(ShapeRenderer shapeRenderer) {

//        shapeRenderer.circle(position.x, position.y, width/2);

        switch(tier) {
            case 1:
                drawSmallAsteroid(shapeRenderer);
                break;
            case 2:
                drawMediumAsteroid(shapeRenderer);
                break;
            case 3:
                drawLargeAsteroid(shapeRenderer);
                break;
            default:
                throw new IllegalArgumentException("Invalid tier value" + tier);
        }
    }

    public void assignTierParameters(int tier){
        switch(tier){
            case 1: //Small asteroid
//                health = 1;
                height = 40;
                width = 40;
                this.velocity = new Vector2(4,4);
                break;
            case 2: //Medium asteroid
//                health = 2;
                height = 160;
                width = 160;
                this.velocity = new Vector2(2,2);
                break;
            case 3: //Large asteroid
//                health = 3;
                height = 300;
                width = 300;
                this.velocity = new Vector2(1,1);
                break;
            default:
                throw new IllegalArgumentException("Invalid tier value: " + tier);
        }
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

    public void spawnOffScreen(float screenWidth, float screenHeight) {
        //Randomly select a side of the screen to spawn the UFO
        int side = MathUtils.random(4); //0 top, 1 bottom, 2 left, 3 right

        //Initialize UFO position off-screen
        float spawnX = 0, spawnY = 0;
        switch(side) {
            case 0: //Top
                spawnX = MathUtils.random(0, screenWidth);
                spawnY = screenHeight;
                break;
            case 1: //Bottom
                spawnX = MathUtils.random(0, screenWidth);
                spawnY = 0;
                break;
            case 2: //Left
                spawnX = 0;
                spawnY = MathUtils.random(0, screenHeight);
                break;
            case 3: //Right
                spawnX = screenWidth;
                spawnY = MathUtils.random(0, screenHeight);
                break;
        }

        //Set UFO position
        position.set(spawnX, spawnY);

        //Calculate velocity towards the center of the screen
        float centerX = MathUtils.random(0, screenWidth);
        float centerY = MathUtils.random(0, screenHeight);
        velocity.set(centerX - spawnX, centerY - spawnY).nor().scl(speed);
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

    public int getTier() {return tier;}
    public Vector2 getPosition() {return position;}
    public boolean isHitByBullet(){return hitByBullet;}


    private float[] calculateSmallAsteroidVertices() {
        //Define points relative to center of the asteroid
        float[] smallPoints = {
                -20, 10,
                20, 10,
                30, -10,
                10, -20,
                -10, -30,
                -30, -20
        };

        int numberOfVertices = smallPoints.length / 2;
        float[] vertices = new float[numberOfVertices * 2];
        System.arraycopy(smallPoints, 0, vertices, 0, smallPoints.length);
        return vertices;
    }
    private float[] calculateMediumAsteroidVertices() {
        //Define points relative to center of the asteroid
        float[] mediumPoints = {
                -90, 0,    // Vertex 1
                -80, 30,   // Vertex 2
                -60, 60,   // Vertex 3
                -30, 90,   // Vertex 4
                0, 100,    // Vertex 5
                30, 90,    // Vertex 6
                60, 60,    // Vertex 7
                80, 30,    // Vertex 8
                90, 0,     // Vertex 9
                80, -30,   // Vertex 10
                60, -60,   // Vertex 11
                30, -90,   // Vertex 12
                0, -100,   // Vertex 13
                -30, -90,  // Vertex 14
                -60, -60,  // Vertex 15
                -80, -30,  // Vertex 16
        };

        int numberOfVertices = mediumPoints.length / 2;
        float[] vertices = new float[numberOfVertices * 2];
        System.arraycopy(mediumPoints, 0, vertices, 0, mediumPoints.length);
        return vertices;
    }
    private float[] calculateLargeAsteroidVertices() {
        //Define points relative to center of the asteroid
        float[] largePoints = {
                -80, 0,    // Vertex 1
                -75, 25,   // Vertex 2
                -65, 50,   // Vertex 3
                -50, 75,   // Vertex 4
                -25, 90,   // Vertex 5
                0, 100,    // Vertex 6
                25, 90,    // Vertex 7
                50, 75,    // Vertex 8
                65, 50,    // Vertex 9
                75, 25,    // Vertex 10
                80, 0,     // Vertex 11
                75, -25,   // Vertex 12
                65, -50,   // Vertex 13
                50, -75,   // Vertex 14
                25, -90,   // Vertex 15
                0, -100,   // Vertex 16
                -25, -90,  // Vertex 17
                -50, -75,  // Vertex 18
                -65, -50,  // Vertex 19
                -75, -25,  // Vertex 20
        };

        int numberOfVertices = largePoints.length / 2;
        float[] vertices = new float[numberOfVertices * 2];
        System.arraycopy(largePoints, 0, vertices, 0, largePoints.length);
        return vertices;
    }

    public void drawSmallAsteroid(ShapeRenderer shapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.WHITE);

        float[] vertices = calculateSmallAsteroidVertices();
        shapeRenderer.polygon(vertices);
        shapeRenderer.end();
    }

    public void drawMediumAsteroid(ShapeRenderer shapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.WHITE);

        float[] vertices = calculateMediumAsteroidVertices();
        shapeRenderer.polygon(vertices);
        shapeRenderer.end();
    }

    public void drawLargeAsteroid(ShapeRenderer shapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.WHITE);

        float[] vertices = calculateLargeAsteroidVertices();
        shapeRenderer.polygon(vertices);
        shapeRenderer.end();
    }
}

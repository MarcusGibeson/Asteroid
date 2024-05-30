package com.asteroid.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;

public class PowerUp {
    public enum Type {
        RAPID_FIRE,
        PULSE_SHOT,
        WAVE_SHOT,
        KILL_AURA,
        MULTI_SHOT,
        INVULN
    }

    private boolean isTouchingShip;
    private Type type;
    private static final long EFFECT_DURATION = 15;//measured in seconds, it's 15s total
    private static final long SPAWN_COOLDOWN = 45; //45s cooldown between spawns
    private long startTime;
    public Vector2 dimensions = new Vector2(40,40);
    public Vector2 position;
    float[] shieldVertices;


    public PowerUp (){
        setRandomType(); //Set a random power-up upon initialization
        randomizePosition(); //Set a random location upon initialization
        this.isTouchingShip = false;
    }

    public PowerUp(Type type) {
        this.type = type;
        this.position = new Vector2(0,0);
        this.isTouchingShip = false;
        //region Defining vertices and values for custom powerup decals
        shieldVertices = new float[] {
                position.x + 20, position.y + 5, //bottom of shield
                position.x + 5, position.y + 15, //bottom left
                position.x + 5, position.y + 35, //top left
                position.x + 20, position.y + 30, //top middle (the dip)
                position.x + 35, position.y + 35, //top right
                position.x + 35, position.y + 15 //bottom right
        };
        //endregion
    }

    public Type getType() {return type;}
    public void setType(Type type) {this.type = type;}
    public float getEffectDuration() {return EFFECT_DURATION;}
    public void setTouchingShip(boolean touchingShip) {isTouchingShip = touchingShip;}
    public boolean isTouchingShip() {return isTouchingShip;}
    public float getSpawnCooldown() {return SPAWN_COOLDOWN;}
    public void setPosition(float x, float y) {
        position.set(x,y);
    }

    public void setRandomType(){
        this.type = Type.values()[MathUtils.random(Type.values().length - 1)];
    }

    public void applyToShip(PlayerShip player){
        player.setTouchingPowerUp(true);
        player.setCurrentPowerUpType(getType());

        //Remove power ups after 15 seconds and set the current powerup to null
        Timer.schedule(new Timer.Task(){
            @Override
            public void run() {
                player.setTouchingPowerUp(false);
                player.setCurrentPowerUpType(null);
            }
        }, EFFECT_DURATION);
    }

    public void draw(ShapeRenderer shapeRenderer){
        shapeRenderer.setAutoShapeType(true);
        shapeRenderer.begin();
        switch (getType()) {
            case RAPID_FIRE:
                float largeCircleRadius = 17;
                float largeCircleX = position.x + 20;
                float largeCircleY = position.y + 20;

                int numSmallCircles = 8;
                float smallCircleRadius = 3;

                shapeRenderer.setColor(Color.RED);
                shapeRenderer.rect(position.x, position.y, dimensions.x, dimensions.y);
                shapeRenderer.circle(largeCircleX, largeCircleY, largeCircleRadius);

                float distanceBetweenSmallCircles = 360f/numSmallCircles;
                for (int i = 0; i < numSmallCircles; i++){
                    float angle = i * distanceBetweenSmallCircles;
                    float smallCircleX = largeCircleX + (largeCircleRadius - 5) * MathUtils.cosDeg(angle);
                    float smallCircleY = largeCircleY + (largeCircleRadius - 5) * MathUtils.sinDeg(angle);

                    shapeRenderer.circle(smallCircleX, smallCircleY, smallCircleRadius);
                }
                break;
            case PULSE_SHOT:
                //region defining a triangle to represent the player ship
                float triangleTipX = position.x + 20;
                float triangleTipY = position.y + 16;

                float triangleLeftX = position.x + 15;
                float triangleLeftY = position.y + 4;

                float triangleRightX = position.x + 25;
                float triangleRightY = position.y + 4;
                //endregion

                shapeRenderer.setColor(Color.YELLOW);
                shapeRenderer.rect(position.x, position.y, dimensions.x, dimensions.y);

                shapeRenderer.triangle(triangleTipX, triangleTipY, triangleLeftX, triangleLeftY, triangleRightX, triangleRightY);
                shapeRenderer.circle(position.x + 20, position.y + 20, 2);
                shapeRenderer.circle(position.x + 20, position.y + 26, 2);
                shapeRenderer.circle(position.x + 20, position.y + 32, 2);
                break;
            case WAVE_SHOT: //this one is a doozy
                //region defining a triangle to represent the player ship (yes, i had to copy this again)
                float triangleTipX1 = position.x + 20;
                float triangleTipY1 = position.y + 16;

                float triangleLeftX2 = position.x + 15;
                float triangleLeftY2 = position.y + 4;

                float triangleRightX3 = position.x + 25;
                float triangleRightY3 = position.y + 4;
                //endregion
                shapeRenderer.setColor(Color.PURPLE);
                shapeRenderer.rect(position.x, position.y, dimensions.x, dimensions.y);

                shapeRenderer.triangle(triangleTipX1, triangleTipY1, triangleLeftX2, triangleLeftY2, triangleRightX3, triangleRightY3);

                drawArcSegment(shapeRenderer, position.y + 22, 15, 12);
                drawArcSegment(shapeRenderer, position.y + 18, 12, 12);
                drawArcSegment(shapeRenderer, position.y + 14, 9, 12);
                break;
            case KILL_AURA:
                //region defining constants
                float centerX = position.x + 20;
                float centerY = position.y + 20;
                float killCircleRadius = 17f;

                float shipX1 = position.x + 20;
                float shipY1 = position.y + 26;
                float shipX2 = position.x + 15;
                float shipY2 = position.y + 14;
                float shipX3 = position.x + 25;
                float shipY3 = position.y + 14;
                //endregion

                shapeRenderer.setColor(Color.ORANGE);
                shapeRenderer.rect(position.x, position.y, dimensions.x, dimensions.y);

                shapeRenderer.circle(centerX, centerY, killCircleRadius);
                shapeRenderer.triangle(shipX1, shipY1, shipX2, shipY2, shipX3, shipY3);
                break;
            case MULTI_SHOT:
                //region defining constants
                float triangleTipX11 = position.x + 20;
                float triangleTipY11 = position.y + 16;

                float triangleLeftX22 = position.x + 15;
                float triangleLeftY22 = position.y + 4;

                float triangleRightX33 = position.x + 25;
                float triangleRightY33 = position.y + 4;
                //endregion

                shapeRenderer.setColor(Color.GREEN);
                shapeRenderer.rect(position.x, position.y, dimensions.x, dimensions.y);
                shapeRenderer.triangle(triangleTipX11, triangleTipY11, triangleLeftX22, triangleLeftY22, triangleRightX33, triangleRightY33);

                shapeRenderer.circle(position.x + 20, position.y + 24, 3);
                shapeRenderer.circle(position.x + 11, position.y + 24, 3);
                shapeRenderer.circle(position.x + 29, position.y + 24, 3);

                shapeRenderer.circle(position.x + 20, position.y + 34, 3);
                shapeRenderer.circle(position.x + 11, position.y + 34, 3);
                shapeRenderer.circle(position.x + 29, position.y + 34, 3);

                break;
            case INVULN:
                shapeRenderer.setColor(new Color(135 / 255f, 206 / 255f, 250 / 255f, 1)); //getting light blue
                shapeRenderer.rect(position.x, position.y, dimensions.x, dimensions.y);
                shapeRenderer.polygon(shieldVertices);
                break;
            default:
                shapeRenderer.setColor(Color.PINK);
                shapeRenderer.rect(position.x, position.y, dimensions.x, dimensions.y);
        }

        shapeRenderer.end();
    }

    public Rectangle getCollisionRectangle() {
        return new Rectangle(position.x, position.y, dimensions.x, dimensions.y);
    }

    public void randomizePosition(){
        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();

        int randomX = MathUtils.random(100, screenWidth - 100);
        int randomY = MathUtils.random(60, screenHeight - 60);

        this.position = new Vector2(randomX, randomY);

        //region Defining vertices and values for custom powerup decals
        shieldVertices = new float[] {
                position.x + 20, position.y + 5, //bottom of shield
                position.x + 5, position.y + 15, //bottom left
                position.x + 5, position.y + 35, //top left
                position.x + 20, position.y + 30, //top middle (the dip)
                position.x + 35, position.y + 35, //top right
                position.x + 35, position.y + 15 //bottom right
        };
        //endregion
    }

    public void drawArcSegment (ShapeRenderer shapeRenderer, float centerY,
                                float radius, int numSegments){
        float centerX = position.x + 20;
        float startAngle = 150f;
        float endAngle = 30f;

        float angleStep = (endAngle - startAngle) / numSegments; //determining how many line segments need to be drawn to recreate an arc
        for (int i = 0; i < numSegments; i++) {
            //determine the x,y of the start and end point of the line segment
            float angle1 = startAngle + i * angleStep;
            float angle2 = startAngle + (i + 1) * angleStep;

            //convert it to radians
            float radians1 = MathUtils.degreesToRadians * angle1;
            float radians2 = MathUtils.degreesToRadians * angle2;

            //plot it
            float x1 = centerX + radius * MathUtils.cos(radians1);
            float y1 = centerY + radius * MathUtils.sin(radians1);
            float x2 = centerX + radius * MathUtils.cos(radians2);
            float y2 = centerY + radius * MathUtils.sin(radians2);
            shapeRenderer.line(x1, y1, x2, y2);
        }
    }
}

package com.asteroid.game.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Comet {
    public static final float COMET_SPEED = 400.0f;
    private Vector2 velocity;
    private Vector2 position;
    public static final float COMET_RADIUS = 10;
    private float guideDuration = 0.5f;
    private float guideTimer = 0.0f;
    private float tailDuration = 3f;
    private float tailTimer = 0.0f;
    private boolean drawTail = true;
    private Vector2 previousPosition;
    private float tailAlpha = 2.0f;

    private int maxHealth = 1;
    private int currentHealth;
    private boolean isDestroyed;

    PlayerShip playerShip;
    ShapeRenderer shapeRenderer;

    public Comet(Vector2 position, Vector2 playerShipPosition, PlayerShip playerShip) {
        this.playerShip = playerShip;
        this.position = position;
        velocity = playerShipPosition.cpy().sub(position).nor().scl(COMET_SPEED);
        shapeRenderer = new ShapeRenderer();
        this.previousPosition = new Vector2(position.x, position.y);
        this.isDestroyed = false;
        this.currentHealth = maxHealth;
    }

    public void update(float delta, Vector2 playerShipPosition) {
        if(!isDestroyed) {
            if(!playerShip.isPlayerDead()){
                if(guideTimer < guideDuration) {
                    // Calculate the direction from the comet to the player ship
                    Vector2 direction = playerShipPosition.cpy().sub(position).nor();
                    velocity = playerShip.getPosition().cpy().sub(position).nor().scl(COMET_SPEED);
                    guideTimer += delta;

                    // Calculate the rotation angle
                    float rotation = direction.angleDeg();

                    // Update the position of the comet
                    position.add(velocity.x * delta, velocity.y * delta);
                    // Update the rotation of the comet if it's moving
                    if (velocity.len() > 0) {
                        rotation = rotation - 45; // Adjust the rotation as needed
                    }

                    // Draw the comet with rotation using ShapeRenderer
                    shapeRenderer.setAutoShapeType(true);
                    shapeRenderer.begin();
                    shapeRenderer.identity();
                    shapeRenderer.translate(position.x, position.y, 0);
                    shapeRenderer.rotate(0, 0, 1, rotation); // Rotate around the Z axis
                    shapeRenderer.circle(0, 0, COMET_RADIUS);

                    previousPosition.set(position);

                    //Draw the tail if it should be visible

                    shapeRenderer.end();
                } else {
                    //After guide duration, resume flying straight
                    velocity = velocity.nor().scl(COMET_SPEED);
                    position.add(velocity.x * delta, velocity.y * delta);
                }

            }else {
                velocity = velocity.nor().scl(COMET_SPEED);
                position.add(velocity.x * delta, velocity.y * delta);
            }
            tailTimer += delta;
            if (tailTimer >= tailDuration) {
                drawTail = false;
            }

        }

    }

    public void draw(ShapeRenderer shapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        if(drawTail && tailTimer < tailDuration) {
            shapeRenderer.setColor(Color.RED.r, Color.RED.g, Color.RED.b, tailAlpha);
            shapeRenderer.rectLine(position.x, position.y, previousPosition.x, previousPosition.y, 1); // Adjust line width as needed

        } else {
            drawTail = false;
        }
        //Main body of comet
        shapeRenderer.setColor(Color.GRAY);
        shapeRenderer.circle(position.x, position.y, COMET_RADIUS);

        //Tail of Comet
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rectLine(position.x, position.y, position.x - COMET_RADIUS , position.y, -COMET_RADIUS );


        shapeRenderer.end();

    }

    public void takeDamage(int damage) {
        currentHealth -= damage;
        if (currentHealth < 0) {
            currentHealth = 0;
            isDestroyed = true;
        }
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }

    public Vector2 getPosition() {
        return position;
    }
}

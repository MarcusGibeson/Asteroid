package com.asteroid.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Comet {
    public static final float COMET_SPEED = 500.0f;
    private Vector2 velocity;
    private Vector2 position;
    private float radius = 10;

    private float guideDuration = 0.5f;
    private float guideTimer = 0.0f;



    PlayerShip playerShip;
    ShapeRenderer shapeRenderer;

    public Comet(Vector2 position, Vector2 playerShipPosition, PlayerShip playerShip) {
        this.playerShip = playerShip;
        this.position = position;
        this.radius = 10.0f;
        velocity = playerShipPosition.cpy().sub(position).nor().scl(COMET_SPEED);
        shapeRenderer = new ShapeRenderer();
    }

    public void update(float delta, Vector2 playerShipPosition) {
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
                shapeRenderer.circle(0, 0, radius);
                shapeRenderer.end();
            } else {
                //After guide duration, resume flying straight
                velocity = velocity.nor().scl(COMET_SPEED);
                position.add(velocity.x * delta, velocity.y * delta);
            }
        }
    }

    public void draw(ShapeRenderer shapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        //Main body of comet
        shapeRenderer.setColor(Color.GRAY);
        shapeRenderer.circle(position.x, position.y, radius);

        //Tail of Comet
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rectLine(position.x, position.y, position.x - radius , position.y, - radius );

        shapeRenderer.end();

    }

}

package com.asteroid.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class PlayerShip {
    private Vector2 position;
    private float rotation;
    private final float width = 20; // Adjust as needed
    private final float height = 30; // Adjust as needed
    private static final float MAX_SPEED = 5f;
    private static final float ROTATION_SPEED = 3f;

    public PlayerShip(float x, float y) {
        position = new Vector2(x, y);
        rotation = 0;
        System.out.println("PlayerShip rotation is: " + rotation);
    }

    public void update() {
        // Add logic here to update ship position based on user input or game mechanics
        handleInput();
        loopOffScreenMovement();
    }

    public void draw(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.identity();
        shapeRenderer.translate(position.x, position.y, 0);
        shapeRenderer.rotate(0, 0, 1, rotation);

        // Draw ship body (triangle)
        shapeRenderer.triangle(
                -height / 2, -width / 2,  // Bottom-left corner
                -height / 2, width / 2,   // Top-left corner
                height / 2, 0             // Right-middle point
        );

        // draw additional details like lines for wings, etc.
        //right wing
        shapeRenderer.line(-width / 2, 0, -height / 2, -width);
        shapeRenderer.line(-width / 2, -height / 2, width / 2, -height / 2);

        //left wing
        shapeRenderer.line(-width / 2, 0, -height / 2, width);
        shapeRenderer.line(-width / 2, height / 2, width / 2, height / 2);


        // Reset transformation matrix
        shapeRenderer.identity();
    }

    public void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
            // Move forward in the direction the ship is facing (upwards)
            float xSpeed = MAX_SPEED * MathUtils.cosDeg(rotation); // Use cos for x component
            float ySpeed = MAX_SPEED * MathUtils.sinDeg(rotation); // Use sin for y component
            position.x += xSpeed;
            position.y += ySpeed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            //Rotate left
            rotation += ROTATION_SPEED;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            //rotate right
            rotation -= ROTATION_SPEED;
        }
    }

    public void loopOffScreenMovement() {
        //changes position based on location so ship remains on screen
        if (position.x < 0) {
            position.x = Gdx.graphics.getWidth(); //moves ship to right side of screen if exits left
        } else if (position.x > Gdx.graphics.getWidth()) {
            position.x = 0; //moves ship to left side of screen if exits right
        }
        if (position.y < 0) {
            position.y = Gdx.graphics.getHeight(); //moves ship to top side of screen if exits bottom
        } else if (position.y > Gdx.graphics.getHeight()) {
            position.y = 0; //moves ship to bottom of screen if exits top
        }
    }
    // Getter and setter methods for position and rotation can be added as needed
}
package com.asteroid.game;

import static com.asteroid.game.Bullet.BULLET_RADIUS;
import static com.asteroid.game.Bullet.BULLET_SPEED;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class PlayerShip {
    private Vector2 position;
    private float rotation;
    private final float width = 20; // Adjust as needed
    private final float height = 30; // Adjust as needed
    private static final float MAX_SPEED = 5f;
    private static final float ROTATION_SPEED = 3f;

    private float shotCooldownTimer = 0f;
    private static final float SHOT_COOLDOWN = 0.5f;

    private List<Bullet> bullets;

    public PlayerShip(float x, float y) {
        position = new Vector2(x, y);
        rotation = 0;
        System.out.println("PlayerShip rotation is: " + rotation);
        bullets = new ArrayList<>();

    }

    public void update(float delta) {


        // Add logic here to update ship position based on user input or game mechanics
        handleInput();
        loopOffScreenMovement();
        updateBullets(delta);
        updateCooldownTimer(delta);
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
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            if (canShoot()) {
                //shoot bullet
                shoot();
                resetCooldownTimer();
            }

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

    private void updateBullets(float delta) {
        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            bullet.update(delta);
            if (bullet.getPosition().x < 0 || bullet.getPosition().x > Gdx.graphics.getWidth() || bullet.getPosition().y < 0 || bullet.getPosition().y > Gdx.graphics.getHeight()) {
                //remove bullets that go out of bounds
                bullets.remove(i);
            }
        }
    }

    public void drawBullets(ShapeRenderer shapeRenderer) {
        for(Bullet bullet: bullets) {
            bullet.draw(shapeRenderer);
        }
    }

    public void shoot() {
        Vector2 bulletDirection = new Vector2(MathUtils.cosDeg(rotation), MathUtils.sinDeg(rotation));
        bullets.add(new Bullet(new Vector2(position), bulletDirection, BULLET_SPEED, BULLET_RADIUS, Color.WHITE));
    }

    private boolean canShoot() {
        return shotCooldownTimer <= 0;
    }

    private void resetCooldownTimer() {
        shotCooldownTimer = SHOT_COOLDOWN;
    }

    private void updateCooldownTimer(float delta) {
        shotCooldownTimer -= delta;
        if (shotCooldownTimer < 0) {
            shotCooldownTimer = 0;
        }
    }
}
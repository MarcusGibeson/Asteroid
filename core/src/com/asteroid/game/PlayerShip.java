package com.asteroid.game;

import static com.asteroid.game.Bullet.BULLET_RADIUS;
import static com.asteroid.game.Bullet.BULLET_SPEED;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class PlayerShip {
    private final Vector2 position;
    private final Vector2 velocity;
    private float rotation;
    private final float width = 20; // Adjust as needed
    private final float height = 30; // Adjust as needed
    private static final float MAX_SPEED = 5f;
    private static final float ACCELERATION = 0.1f;
    private static final float ROTATION_SPEED = 3f;
    private static final float FRICTION = 0.01f;

    private float shotCooldownTimer = 0f;
    private static final float SHOT_COOLDOWN = 0.25f;

    private final List<Bullet> bullets;
    private final Sound shootingSound;
    private final Sound movingForwardSound;
    private final JetFireEffect jetFireEffect;



    public PlayerShip(float x, float y) {
        position = new Vector2(x, y);
        velocity = new Vector2();
        rotation = 0;
        System.out.println("PlayerShip rotation is: " + rotation);
        bullets = new ArrayList<>();
        jetFireEffect = new JetFireEffect(Color.ORANGE);
        shootingSound = Gdx.audio.newSound(Gdx.files.internal("Audio/Bullet_single.mp3"));
        movingForwardSound = Gdx.audio.newSound(Gdx.files.internal("Audio/Ship_Thrusters.mp3"));
    }

    public void update(float delta) {
        //Update ship's position based on velocity
        position.x += velocity.x;
        position.y += velocity.y;

        if (isAccelerating) {
            jetFireEffect.update(delta);
        }
        // Add logic here to update ship position based on user input or game mechanics
        handleInput();
        loopOffScreenMovement();
        updateBullets(delta);
        updateCooldownTimer(delta);
    }

    public void draw(ShapeRenderer shapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
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

        // draw lines for wings
        //right wing
        shapeRenderer.line(-width / 2, 0, -height / 2, -width);
        shapeRenderer.line(-width / 2, -height / 2, width / 2, -height / 2);

        //left wing
        shapeRenderer.line(-width / 2, 0, -height / 2, width);
        shapeRenderer.line(-width / 2, height / 2, width / 2, height / 2);


        // Reset transformation matrix
        shapeRenderer.identity();
        shapeRenderer.end();
        if (isAccelerating) {
            //Draw the jet fire effect while accelerating
            float offsetX = -MathUtils.cosDeg(rotation) * 20;
            float offsetY = -MathUtils.sinDeg(rotation) * 20;
            Vector2 firePosition = new Vector2(position.x + offsetX, position.y + offsetY);
            jetFireEffect.draw(shapeRenderer, firePosition, rotation);
        }
    }

    //boolean flag to keep track of whether the key is pressed
    private boolean isAccelerating = false;
    public void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
            if(!isAccelerating) {
                // Move forward in the direction the ship is facing (upwards)
                isAccelerating = true;
                movingForwardSound.loop();

            }
            accelerate();

        } else {
            //Key is not pressed
            if (isAccelerating) {
                isAccelerating = false;
                movingForwardSound.stop();
            }
            decelerate();
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

    private void accelerate() {
        //Calculate acceleration based on ship's rotation
        float accelerationX = ACCELERATION * MathUtils.cosDeg(rotation);
        float accelerationY = ACCELERATION * MathUtils.sinDeg(rotation);

        //Apply acceleration to velocity
        velocity.x += accelerationX;
        velocity.y += accelerationY;

        //Limit velocity to maximum speed
        velocity.limit(MAX_SPEED);
    }

    private void decelerate() {
        //Gradually reduce velocity (simulate deceleration)
        velocity.scl(1 - FRICTION);

        //Clamp velocity to zero if it becomes too small
        if (velocity.len2() < 0.1f) {
            velocity.setZero();
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

        shootingSound.play();
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
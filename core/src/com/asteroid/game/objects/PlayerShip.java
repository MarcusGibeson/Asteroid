package com.asteroid.game.objects;

import static com.asteroid.game.objects.Bullet.BULLET_RADIUS;
import static com.asteroid.game.objects.Bullet.BULLET_SPEED;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;
import java.util.List;

public class PlayerShip {
    private Vector2 position;
    private final Vector2 velocity;
    private float rotation;
    private final float width = 20; // Adjust as needed
    private final float height = 30; // Adjust as needed
    private static final float MAX_SPEED = 10f;
    private static final float ACCELERATION = 0.1f;
    private static final float ROTATION_SPEED = 3f;
    private static final float FRICTION = 0.01f;

    private static final int MAX_HEALTH = 1;

    private float shotCooldownTimer = 0f;
    private static float shotCooldown = 0.25f;
    private int pulseCount = 0;





    private final List<Bullet> bullets;
    private final Sound shootingSound;
    private final Sound movingForwardSound;
    private final Sound shipExplosion;
    private final JetFireEffect jetFireEffect;
    private float volume = 0.25f;

    private boolean isDestroyed;
    private boolean isInvulnerable;

    private int health;
    private int lives;
    private boolean isPlayerDead = false;
    private Vector2 respawnPosition;
    private boolean respawnRequested = false;
    private boolean isRespawning = false;

    private boolean isTouchingPowerUp;
    private PowerUp.Type currentPowerUpType;



    public PlayerShip(float x, float y,int initialHealth, int initialLives) {
        position = new Vector2(x, y);
        this.health = initialHealth;
        this.lives = initialLives;
        this.respawnPosition = new Vector2((float) Gdx.graphics.getWidth() / 2, (float) Gdx.graphics.getHeight() / 2);
        velocity = new Vector2();
        rotation = 0;
        bullets = new ArrayList<>();
        jetFireEffect = new JetFireEffect(Color.ORANGE);
        shootingSound = Gdx.audio.newSound(Gdx.files.internal("Audio/Bullet_single.mp3"));
        movingForwardSound = Gdx.audio.newSound(Gdx.files.internal("Audio/Ship_Thrusters.mp3"));
        shipExplosion = Gdx.audio.newSound(Gdx.files.internal("Audio/ship_explosion.mp3"));
        isDestroyed = false;
    }

    public void update(float delta) {
        //if the current power up type is invulnerability, turn off the collision function
        if (getCurrentPowerUpType() == PowerUp.Type.INVULN){
            setInvulnerable(true);
        } else {
            setInvulnerable(false);
        }

        if (isPlayerDead && Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            respawnRequested = true;
        }
        if (respawnRequested && !isRespawning) {
            respawn();
        }
        if (!isDestroyed) {
            //Update ship's position based on velocity
            position.x += velocity.x;
            position.y += velocity.y;

            if (isAccelerating) {
                jetFireEffect.update(delta);
            }
            // Add logic here to update ship position based on user input or game mechanics
            handleInput();
            loopOffScreenMovement();
            updateCooldownTimer(delta);
        }
        updateBullets(delta);
    }



    public void draw(ShapeRenderer shapeRenderer) {
        if (!isDestroyed) {
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
            shapeRenderer.line(-width / 2, 0, -height / 2, -width );
            shapeRenderer.line(-width / 2, -height / 2, width / 2, -height / 2);

            //left wing
            shapeRenderer.line(-width / 2, 0, -height / 2, width );
            shapeRenderer.line(-width / 2, height / 2, width / 2, height / 2);


            // Reset transformation matrix
            shapeRenderer.identity();

            //draw the kill aura for the powerup
            if (getCurrentPowerUpType() != null){
                if (getCurrentPowerUpType() == PowerUp.Type.KILL_AURA){
                    shapeRenderer.setColor(Color.ORANGE);
                    shapeRenderer.circle(position.x, position.y, 200);
                }
            }
            shapeRenderer.end();
            if (isAccelerating) {
                //Draw the jet fire effect while accelerating
                float offsetX = -MathUtils.cosDeg(rotation) * 20;
                float offsetY = -MathUtils.sinDeg(rotation) * 20;
                Vector2 firePosition = new Vector2(position.x + offsetX, position.y + offsetY);
                jetFireEffect.draw(shapeRenderer, firePosition, rotation);
            }


        }

    }

    public void handleCollision() {
        if (!isInvulnerable){
            health--;
            if (health <= 0) {
                destroy();
                isPlayerDead = true;
            }
        }
    }

    public boolean isPlayerDead() {
        return isPlayerDead;
    }

    public void respawn() {

        if (lives > 0) {
            lives--;
            setPosition(respawnPosition);
            isDestroyed = false;
            health = getMaxHealth();
            isPlayerDead = false;
            respawnRequested = false;
            isRespawning = false;

        } else {
            //game over logic

        }
    }

    public void resetLives() {
        lives = 5;
    }

    public void drawRespawnMessage(SpriteBatch spriteBatch, BitmapFont font) {
        if (isPlayerDead) {
            font.draw(spriteBatch, "You have died, press Enter to continue. You have " + (lives - 1) + " lives remaining.", Gdx.graphics.getWidth() / 2 -150, Gdx.graphics.getHeight() /2 );
        }
    }
    private int getMaxHealth() {
        return MAX_HEALTH;
    }

    public void destroy() {

        isDestroyed = true;
        movingForwardSound.stop();
        shipExplosion.play();
    }



    public boolean isTouchingPowerUp() {return isTouchingPowerUp;}
    public void setTouchingPowerUp(boolean touchingPowerUp) {isTouchingPowerUp = touchingPowerUp;}
    public PowerUp.Type getCurrentPowerUpType() {return currentPowerUpType;}
    public void setCurrentPowerUpType(PowerUp.Type currentPowerUpType) {this.currentPowerUpType = currentPowerUpType;}

    public boolean getInvulnerable() {return isInvulnerable;}
    public void setInvulnerable(boolean vulnerability) {this.isInvulnerable = vulnerability;}
    public static float getShotCooldown() {return shotCooldown;}
    public static void setShotCooldown(float shotCooldown) {PlayerShip.shotCooldown = shotCooldown;}
    public int getHealth() {
        return health;
    }
    public int getLives() {
        return lives;
    }
    public void setPosition(Vector2 position) {
        this.position = position;
    }
    public Vector2 getPosition() {
        return position;
    }
    public Rectangle getCollisionRectangle() {
        return new Rectangle(position.x, position.y, width, height);
    }
    public Circle getKillAuraCircle(){
        return new Circle(position.x, position.y, 200);
    }
    public List<Bullet> getBullets() {
        return bullets;
    }

    //boolean flag to keep track of whether the key is pressed
    private boolean isAccelerating = false;
    public void handleInput() {
        if (!isDestroyed) {
            if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
                if(!isAccelerating) {
                    // Move forward in the direction the ship is facing (upwards)
                    isAccelerating = true;
                    movingForwardSound.loop(volume);
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
//            if (Gdx.input.isKeyPressed(Input.Keys.NUM_1)){
//                setTouchingPowerUp(true);
//                setCurrentPowerUpType(PowerUp.Type.RAPID_FIRE);
//            }
//            if (Gdx.input.isKeyPressed(Input.Keys.NUM_2)){
//                setTouchingPowerUp(true);
//                setCurrentPowerUpType(PowerUp.Type.PULSE_SHOT);
//            }
//            if (Gdx.input.isKeyPressed(Input.Keys.NUM_3)){
//                setTouchingPowerUp(true);
//                setCurrentPowerUpType(PowerUp.Type.WAVE_SHOT);
//            }
//            if (Gdx.input.isKeyPressed(Input.Keys.NUM_4)){
//                setTouchingPowerUp(true);
//                setCurrentPowerUpType(PowerUp.Type.KILL_AURA);
//            }
//            if (Gdx.input.isKeyPressed(Input.Keys.NUM_5)){
//                setTouchingPowerUp(true);
//                setCurrentPowerUpType(PowerUp.Type.MULTI_SHOT);
//            }
//            if (Gdx.input.isKeyPressed(Input.Keys.NUM_6)){
//                setTouchingPowerUp(true);
//                setCurrentPowerUpType(PowerUp.Type.INVULN);
//            }
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
        } else if (position.x > Gdx.graphics.getWidth() +width) {
            position.x = 0; //moves ship to left side of screen if exits right
        }
        if (position.y < 0) {
            position.y = Gdx.graphics.getHeight(); //moves ship to top side of screen if exits bottom
        } else if (position.y > Gdx.graphics.getHeight()){
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
        if (isTouchingPowerUp) {
            float originalCooldown = getShotCooldown();
            Vector2 bulletDirection = new Vector2(MathUtils.cosDeg(rotation), MathUtils.sinDeg(rotation));
            switch (getCurrentPowerUpType()) {
                case RAPID_FIRE: //rapidly shooting bullets
                    //setting the shot cooldown to about 60% faster
                    setShotCooldown(0.1f);
                    bullets.add(new Bullet(new Vector2(position), bulletDirection, BULLET_SPEED, BULLET_RADIUS, Color.WHITE));
                    shootingSound.play();
                    break;
                case PULSE_SHOT: //shooting bullets in sets of three
                    handlePulseShooting();
                    break;
                case WAVE_SHOT: //shooting a dense wave of bullets with a large delay between them
                    float waveCooldown = 1f;
                    setShotCooldown(waveCooldown);
                    for (int i = -5; i < 5; i++){
                        Vector2 newBulletDirection = new Vector2(MathUtils.cosDeg(rotation + i * 4), MathUtils.sinDeg(rotation + i * 4));
                        bullets.add(new Bullet(new Vector2(position), newBulletDirection, BULLET_SPEED, BULLET_RADIUS, Color.WHITE));
                        shootingSound.play();
                    }
                    break;
                case KILL_AURA: //area around player that damages every couple of seconds or so
                    break;
                case MULTI_SHOT: //shoot bullets like normal but in a dense wave of three (not to be confused with wave shot, as it's slower but way more bullets)
                    setShotCooldown(0.25f);
                    Vector2 bulletPosition = getPosition();
                    // Calculate the base direction vector based on the ship's rotation angle
                    Vector2 baseDirection = new Vector2(MathUtils.cosDeg(rotation), MathUtils.sinDeg(rotation));

                    // Define the distance between the center bullet and the side bullets
                    float distance = 15; // Adjust this value as needed

                    // Calculate the offsets for the side bullets based on the ship's rotation angle
                    float xOffsetLeft = -MathUtils.sinDeg(rotation) * distance;
                    float yOffsetLeft = MathUtils.cosDeg(rotation) * distance;

                    float xOffsetRight = MathUtils.sinDeg(rotation) * distance;
                    float yOffsetRight = -MathUtils.cosDeg(rotation) * distance;

                    // Create the center bullet at the ship's position
                    bullets.add(new Bullet(
                            bulletPosition,
                            baseDirection, // Same direction for all bullets
                            BULLET_SPEED,
                            BULLET_RADIUS,
                            Color.WHITE
                    ));

                    // Create the left bullet with the calculated offset
                    bullets.add(new Bullet(
                            new Vector2(bulletPosition.x + xOffsetLeft, bulletPosition.y + yOffsetLeft),
                            baseDirection, // Same direction for all bullets
                            BULLET_SPEED,
                            BULLET_RADIUS,
                            Color.WHITE
                    ));

                    // Create the right bullet with the calculated offset
                    bullets.add(new Bullet(
                            new Vector2(bulletPosition.x + xOffsetRight, bulletPosition.y + yOffsetRight),
                            baseDirection, // Same direction for all bullets
                            BULLET_SPEED,
                            BULLET_RADIUS,
                            Color.WHITE
                    ));
                    shootingSound.play();
                    break;

                default: //in case the bad juju happens
                    setShotCooldown(0.25f);
                    System.out.println("you shouldn't be seeing this message anyways but hey debug is a thing");
                    bullets.add(new Bullet(new Vector2(position), bulletDirection, BULLET_SPEED, BULLET_RADIUS, Color.WHITE));

                    shootingSound.play();
                    break;
            }
        } else {
            setShotCooldown(0.25f);
            Vector2 bulletDirection = new Vector2(MathUtils.cosDeg(rotation), MathUtils.sinDeg(rotation));
            bullets.add(new Bullet(new Vector2(position), bulletDirection, BULLET_SPEED, BULLET_RADIUS, Color.WHITE));

            shootingSound.play();
        }
    }

    private boolean canShoot() {
        return shotCooldownTimer <= 0;
    }

    private void resetCooldownTimer() {
        shotCooldownTimer = shotCooldown;
    }

    private void updateCooldownTimer(float delta) {
        shotCooldownTimer -= delta;
        if (shotCooldownTimer < 0) {
            shotCooldownTimer = 0;
        }
    }

    private void handlePulseShooting(){
        setShotCooldown(0.4f);
        Timer.schedule(new Timer.Task(){
            @Override
            public void run() {
                // Perform action every 0.05 seconds
                if(pulseCount < 3) {
                    Vector2 bulletDirection = new Vector2(MathUtils.cosDeg(rotation), MathUtils.sinDeg(rotation));
                    bullets.add(new Bullet(new Vector2(position), bulletDirection, BULLET_SPEED, BULLET_RADIUS, Color.WHITE));
                    shootingSound.play();
                    System.out.println("Performing action " + (pulseCount + 1));
                    pulseCount++;
                } else {
                    // After performing the action three times, wait for 0.3 seconds
                    Timer.schedule(new Timer.Task(){
                        @Override
                        public void run() {
                            // Reset action count after cooldown
                            pulseCount = 0;
                        }
                    }, 0.3f);
                    this.cancel(); // Cancel the current task
                }
            }
        }, 0, 0.05f);
    }
}
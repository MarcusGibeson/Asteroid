package com.asteroid.game.objects;

import com.asteroid.game.objects.Bullet;
import com.asteroid.game.objects.PlayerShip;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class UFOShip {

    private Vector2 position;
    private Vector2 velocity;
    private float rotation;



    //shaperenderer variables
    private final float width = 30;
    private final float height = 15;
    private final float bodyWidth = 25;
    private final float bodyHeight = 10;
    private final float cockpitWidth = 10;
    private final float cockpitHeight = 10;
    private final float cockpitRadius = 10;
    private final float wingWidth = 15;
    private final float wingHeight = 5;

    //respawn variables and constant
    private float respawnTimer = 0;
    private boolean isWaitingToRespawn = false;
    private static final float RESPAWN_DELAY = 20;
    private PlayerShip playerShip;

    //shooting variables and constant
    private float shootTimer = 0;
    private static final float SHOOT_INTERVAL = 1.5f; //adjust how many seconds between shots
    private final List<Bullet> bullets;
    private final Sound bulletUFO;
    private final Sound ufoExplosion;
    private boolean isDestroyed;
    float speed = 100;

    public UFOShip(float x, float y, PlayerShip playerShip) {
        this.position = new Vector2(x, y);
        this.rotation = 0;
        this.velocity = new Vector2(0,0);
        this.playerShip = playerShip;
        bullets = new ArrayList<>();
        bulletUFO = Gdx.audio.newSound(Gdx.files.internal("Audio/Bullet_UFO.mp3"));
        ufoExplosion = Gdx.audio.newSound(Gdx.files.internal("Audio/ufo_explosion.mp3"));
        isDestroyed = false;
    }

    public void update(float delta) {
        if (!isDestroyed) {
            //Update position of the UFO based on its velocity
            position.x += velocity.x * delta;
            position.y += velocity.y * delta;

            shootTimer += delta;
            if (!playerShip.isPlayerDead()) {
                if (shootTimer >= SHOOT_INTERVAL) {
                    shoot();
                    bulletUFO.play();
                    shootTimer = 0;
                }
            }
        }
        updateBullets(delta);
    }

    public void draw(ShapeRenderer shapeRenderer) {
        if(!isDestroyed) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            //Draw UFO body
            shapeRenderer.setColor(Color.WHITE);
            shapeRenderer.identity();
            shapeRenderer.translate(position.x, position.y, 0);
            shapeRenderer.rotate(0,0,1, rotation );
            shapeRenderer.rect(-bodyWidth /2, -bodyHeight /2,
                    bodyWidth, bodyHeight);

            //Draw UFO cockpit
            shapeRenderer.setColor(Color.WHITE);
            shapeRenderer.arc(0, cockpitHeight /2, cockpitRadius, 0, 180);

            //Draw UFO wings
            shapeRenderer.setColor(Color.WHITE);
            shapeRenderer.rect(-wingWidth /2, -bodyHeight /2, wingWidth, wingHeight);



            //Draw triangular shapes on each side
            float triangleBase = bodyWidth * 0.2f;

            // Left triangle
            shapeRenderer.triangle(-bodyWidth / 2, bodyHeight / 2,
                    -bodyWidth / 2, -bodyHeight / 2,
                    -bodyWidth / 2 - triangleBase, -bodyHeight / 2);

            // Right triangle
            shapeRenderer.triangle(bodyWidth / 2, bodyHeight / 2,
                    bodyWidth / 2, -bodyHeight / 2,
                    bodyWidth / 2 + triangleBase, -bodyHeight / 2);



            //Reset transformation matrix
            shapeRenderer.identity();
            shapeRenderer.end();
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

    private void shoot() {
        //Calculate direction towards player ship
        Vector2 direction = new Vector2(playerShip.getPosition()).sub(position).nor();
        // Create a new bullet
        Bullet bullet = new Bullet(new Vector2(position), direction, Bullet.BULLET_SPEED, Bullet.BULLET_RADIUS, Color.RED);
        bullets.add(bullet);

    }

    //destroy flag
    public void destroy() {
        ufoExplosion.play();
        isDestroyed = true;

    }
    public boolean isDestroyed() {
        return isDestroyed;
    }
    //Movement function










    //getters and setters
    public Vector2 getPosition() {
        return position;
    }
    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public Vector2 getVelocity() {
        return velocity;
    }
    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    public float getRotation() {
        return rotation;
    }
    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public List<Bullet> getBullets() {
        return bullets;
    }
    public Rectangle getCollisionRectangle() {
        return new Rectangle(position.x, position.y, width, height);
    }

    public boolean getIsDestroyed() {
        return isDestroyed;
    }
    public void setDestroyed(boolean destroyed) {
        this.isDestroyed = destroyed;
    }

    public float getSpeed() {
        return  speed;
    }

}

package com.asteroid.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;


public class Bullet {

    private Vector2 position;
    private Vector2 velocity;
    private float radius;
    private Color color;
    private static final float BULLET_LIFESPAN = 1.5f;

    public static final float BULLET_SPEED = 500;
    public static final float BULLET_RADIUS = 2;
    private float lifespanTimer;

    private Vector2 initialVelocity;

    public Bullet(Vector2 position, Vector2 direction, float speed, float radius, Color color)  {
        this.position = new Vector2(position);
        this.velocity = new Vector2(direction.nor().scl(speed)); //Normalize direction and scale by speed
        this.radius = radius;
        this.color = color;
        this.lifespanTimer = BULLET_LIFESPAN;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getVelocity() { return  velocity;
    }

    public boolean isExpired() {
        return lifespanTimer <=0;
    }

    public void update(float delta) {
        position.add(velocity.x * delta, velocity.y * delta); // Update position based on velocity

        lifespanTimer -= delta;
        handleScreenWrapping();
    }

    public void draw(ShapeRenderer shapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(color);
        shapeRenderer.circle(position.x, position.y, radius);
        shapeRenderer.end();
    }

    private void handleScreenWrapping() {
        if (position.x < 0) {
            position.x = Gdx.graphics.getWidth();
        } else if (position.x > Gdx.graphics.getWidth()) {
            position.x = 0;
        }
        if(position.y < 0) {
            position.y = Gdx.graphics.getHeight();
        } else if (position.y > Gdx.graphics.getHeight()) {
            position.y = 0;
        }
    }


}

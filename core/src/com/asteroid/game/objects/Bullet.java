package com.asteroid.game.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;


public class Bullet {

    private Vector2 position;
    private Vector2 velocity;
    private float radius;
    private Color color;

    public static final float BULLET_SPEED = 500;
    public static final float BULLET_RADIUS = 2;

    public Bullet(Vector2 position, Vector2 direction, float speed, float radius, Color color)  {
        this.position = new Vector2(position);
        this.velocity = new Vector2(direction).nor().scl(speed); //Normalize direction and scale by speed
        this.radius = radius;
        this.color = color;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void update(float delta) {
        position.add(velocity.x * delta, velocity.y * delta); // Update position based on velocity
    }

    public void draw(ShapeRenderer shapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(color);
        shapeRenderer.circle(position.x, position.y, radius);
        shapeRenderer.end();
    }
}

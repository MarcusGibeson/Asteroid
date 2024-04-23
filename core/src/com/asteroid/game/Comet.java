package com.asteroid.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Comet {
    public static final float COMET_SPEED = 100.0f;
    private Vector2 velocity;
    private Vector2 position;
    private float radius;

    PlayerShip playerShip;

    public Comet(Vector2 position, Vector2 playerShipPosition, PlayerShip playerShip) {
        this.playerShip = playerShip;
        this.position = position;
        this.radius = 10.0f;
        velocity = playerShipPosition.cpy().sub(position).nor().scl(COMET_SPEED);
    }

    public void update(float delta) {
        position.add(velocity.x * delta, velocity.y * delta);
        velocity = playerShip.getPosition().cpy().sub(position).nor().scl(COMET_SPEED);
    }

    public void draw(ShapeRenderer shapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        //Main body of comet
        shapeRenderer.setColor(Color.GRAY);
        shapeRenderer.circle(position.x, position.y, radius);

        //Tail of Comet
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rectLine(position.x, position.y, position.x - radius * 2, position.y, - radius * 2);

        shapeRenderer.end();

    }

}

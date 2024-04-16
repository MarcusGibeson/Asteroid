package com.asteroid.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

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

    public UFOShip(float x, float y) {
        this.position = new Vector2(x, y);
        this.rotation = 0;
        this.velocity = new Vector2(0,0);
    }

    public void update(float delta) {
        //Update position of the UFO based on its velocity
        position.x += velocity.x * delta;
        position.y += velocity.y * delta;

    }

    public void draw(ShapeRenderer shapeRenderer) {
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
        shapeRenderer.arc(0, bodyHeight /2, cockpitRadius, 0, 180);

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
}

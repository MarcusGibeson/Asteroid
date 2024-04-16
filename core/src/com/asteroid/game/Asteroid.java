package com.asteroid.game;

import com.badlogic.gdx.math.Vector2;

public class Asteroid {
    private Vector2 position, velocity;
    private int health;
    private float rotation, width, height;

    private static final float MAX_SPEED = 5f; //Smallest asteroid size should be roughly the same speed as the player, adjust if needed

    public Asteroid(Vector2 position, Vector2 velocity) {
        this.position = position;
        this.velocity = velocity;
        //test
    }
}

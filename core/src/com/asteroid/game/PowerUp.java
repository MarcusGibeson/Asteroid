package com.asteroid.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;

public class PowerUp {
    public enum Type {
        RAPID_FIRE,
        PULSE_SHOT,
        WAVE_SHOT,
        KILL_AURA,
        MULTI_SHOT,
        INVULN
    }

    private boolean isTouchingShip;
    private Type type;
    private static final long EFFECT_DURATION = 15;//measured in seconds, it's 15s total
    private static final long SPAWN_COOLDOWN = 45; //45s cooldown between spawns
    private long startTime;
    public Vector2 dimensions = new Vector2(60,60);
    public Vector2 position;


    public PowerUp (Vector2 position){
        setRandomType(); //Set a random power-up upon initialization
        this.position = position;
        this.isTouchingShip = false;
    }

    public Type getType() {return type;}
    public void setType(Type type) {this.type = type;}
    public float getEffectDuration() {return EFFECT_DURATION;}
    public void setTouchingShip(boolean touchingShip) {isTouchingShip = touchingShip;}
    public boolean isTouchingShip() {return isTouchingShip;}
    public float getSpawnCooldown() {return SPAWN_COOLDOWN;}

    public void setRandomType(){
        this.type = Type.values()[MathUtils.random(Type.values().length - 1)];
    }

    public void applyToShip(PlayerShip player){
        player.setTouchingPowerUp(true);
        player.setCurrentPowerUpType(getType());

        //Remove power ups after 15 seconds and set the current powerup to null
        Timer.schedule(new Timer.Task(){
            @Override
            public void run() {
                player.setTouchingPowerUp(false);
                player.setCurrentPowerUpType(null);
            }
        }, EFFECT_DURATION);
    }

    public void draw(ShapeRenderer shapeRenderer){
        shapeRenderer.setColor(Color.PINK);
        shapeRenderer.rect(position.x, position.y, dimensions.x, dimensions.y);
    }

    public Rectangle getCollisionRectangle() {
        return new Rectangle(position.x, position.y, dimensions.x, dimensions.y);
    }
}

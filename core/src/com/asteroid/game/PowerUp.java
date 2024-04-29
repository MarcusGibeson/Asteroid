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
    private static final long EFFECT_DURATION = 15000;//measured in milliseconds, it's 15s total
    private static final long SPAWN_COOLDOWN = 45000; //45s cooldown between spawns
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
        float originalCooldown = PlayerShip.getShotCooldown(); //storing original cooldown timer to be reset after the powerup wears off
        switch(getType()){
            case RAPID_FIRE:
                //setting the shot cooldown to about 60% faster
                PlayerShip.setShotCooldown(0.1f);

                //scheduling it to reset the cooldown after it wears off
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        PlayerShip.setShotCooldown(originalCooldown);
                    }
                }, getEffectDuration());
                break;
            case PULSE_SHOT:
                // Set the speed of the rapid fire pulse shots
                float pulseCooldown = 0.05f;

                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        // Set the shot cooldown to the rapid fire speed
                        PlayerShip.setShotCooldown(pulseCooldown);

                        // Sets a cooldown that resets every 3 shots worth of time
                        Timer.schedule(new Timer.Task() {
                            @Override
                            public void run() {
                                // Reset the shot cooldown to the original value
                                PlayerShip.setShotCooldown(originalCooldown);
                            }
                        }, 0.15f);
                    }
                }, 0, 0.25f);
                break;
            case WAVE_SHOT:
                break;
            case KILL_AURA:
                break;
            case MULTI_SHOT:
                break;
            case INVULN:
                break;
            default:
                break;
        }
    }

    public void draw(ShapeRenderer shapeRenderer){
        shapeRenderer.setColor(Color.PINK);
        shapeRenderer.rect(position.x, position.y, dimensions.x, dimensions.y);
    }

    public Rectangle getCollisionRectangle() {
        return new Rectangle(position.x, position.y, dimensions.x, dimensions.y);
    }
}

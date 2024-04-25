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
        TIME_SLOW,
        KILL_AURA,
        MULTI_SHOT,
        INVULN
    }

    private boolean isTouchingShip;
    private Type type;
    private static final long DURATION = 15000;//measured in milliseconds, it's 15s total
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
    public float getDuration() {return DURATION;}
    public void setTouchingShip(boolean touchingShip) {isTouchingShip = touchingShip;}
    public boolean isTouchingShip() {return isTouchingShip;}

    public void setRandomType(){
        this.type = Type.values()[MathUtils.random(Type.values().length - 1)];
    }

    public void applyToShip(PlayerShip player){
        switch(getType()){
            case RAPID_FIRE:
                //storing original cooldown timer to be reset after the powerup wears off
                float originalCooldown = PlayerShip.getShotCooldown();

                //setting the shot cooldown to about 60% faster
                PlayerShip.setShotCooldown(0.1f);

                //scheduling it to reset the cooldown after it wears off
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        PlayerShip.setShotCooldown(originalCooldown);
                    }
                }, getDuration());
                break;
            case PULSE_SHOT:
                break;
            case WAVE_SHOT:
                break;
            case TIME_SLOW:
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

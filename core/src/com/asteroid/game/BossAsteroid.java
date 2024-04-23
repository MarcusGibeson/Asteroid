package com.asteroid.game;

import static com.asteroid.game.Comet.COMET_SPEED;

import com.badlogic.gdx.math.Vector2;

import java.util.List;

public class BossAsteroid extends Asteroid{
    Vector2 position;
    private static final float BOSS_SPEED = 2.0f;
    private static final int BOSS_HEALTH = 100;
    private static final float COMET_SHOOT_RANGE = 200;
    private static final float COMET_SHOOT_DELAY = 3;
    private float timeSinceLastComet = 0;

    PlayerShip playerShip;
    List<Comet> comets;

    private int maxHealth;
    private int currentHealth;

    public BossAsteroid(Vector2 position, PlayerShip playerShip) {
        super(position, BOSS_HEALTH, playerShip);
    }

    public void update(float delta) {
        super.update(delta);

        //Update time since last comet
        timeSinceLastComet += delta;

        //Check if the boss should shoot a comet based on range and delay
        if (shouldShootComet() && timeSinceLastComet >= COMET_SHOOT_DELAY) {
            shootComet();
        }
    }

    private boolean shouldShootComet() {
        //Check if player is with range
        float distanceToPlayer = playerShip.getPosition().dst(getPosition());
        if (distanceToPlayer < COMET_SHOOT_RANGE) {
            //Check for delay from last comet
            if (timeSinceLastComet > COMET_SHOOT_DELAY) {
                //ConditionCheck
                if(playerShip.getHealth() > 0) {
                    return true;
                }

            }
        }
        return false;
    }

    private void shootComet() {
        //create new comet
        Vector2 playerShipPosition = playerShip.getPosition();
        Vector2 cometDirection = playerShipPosition.cpy().sub(position).nor();
        Vector2 cometVelocity = cometDirection.scl(COMET_SPEED);

        //Create a new comet at the position of the boss asteroid
        Comet comet = new Comet(position.cpy(), cometVelocity);

        //Add the comet to the list of comets
        comets.add(comet);
        timeSinceLastComet = 0;
    }

}

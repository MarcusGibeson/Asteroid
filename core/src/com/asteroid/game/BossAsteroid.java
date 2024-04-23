package com.asteroid.game;

import static com.asteroid.game.Comet.COMET_SPEED;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BossAsteroid extends Asteroid{

    private final float width = 300;
    private final float height = 150;
    private static final float BOSS_SPEED = 2.0f;
    private static final int BOSS_HEALTH = 100;
    private static final float COMET_SHOOT_RANGE = 200;
    private static final float COMET_SHOOT_DELAY = 3;
    private float timeSinceLastComet = 0;

    PlayerShip playerShip;
    List<Comet> comets;

    private int maxHealth;
    private int currentHealth;

    public BossAsteroid(Vector2 position, int tierLevel, PlayerShip playerShip, int maxHealth) {
        super(position, tierLevel, playerShip);
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
        this.comets = new ArrayList<>();
    }

    public void update(float delta) {
        super.update(delta);
        //Update time since last comet
        timeSinceLastComet += delta;
        updateComets(delta);
        //Check if the boss should shoot a comet based on range and delay
        if (shouldShootComet() && timeSinceLastComet >= COMET_SHOOT_DELAY) {
            shootComet();
        }
    }

    public void updateComets(float delta) {
        //Iterate through list of comets
        Iterator<Comet> iterator = comets.iterator();
        while(iterator.hasNext()) {
            Comet comet = iterator.next();
            comet.update(delta);
        }
    }

    public void setPlayerShip(PlayerShip playerShip) {
        this.playerShip = playerShip;
    }

    //Method to decrease asteroid's health
    public void takeDamage(int damage) {
        currentHealth -= damage;
        if (currentHealth < 0) {
            currentHealth = 0;
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
        Vector2 cometDirection = playerShipPosition.cpy().sub(getPosition()).nor();
        Vector2 cometVelocity = cometDirection.scl(COMET_SPEED);

        //Create a new comet at the position of the boss asteroid
        Comet comet = new Comet(getPosition().cpy(), cometVelocity, playerShip);

        //Add the comet to the list of comets
        comets.add(comet);
        timeSinceLastComet = 0;
    }


    //Draw method
    @Override
    public void draw(ShapeRenderer shapeRenderer) {
        shapeRenderer.setAutoShapeType(true);
        shapeRenderer.begin();
        //Draw boss asteroid with glow effect
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.set(ShapeRenderer.ShapeType.Line);
        shapeRenderer.circle(getPosition().x, getPosition().y, width / 2 + 5);

        //Draw boss asteroid
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.circle(getPosition().x, getPosition().y, width / 2);

        //Draw the health bar
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(getPosition().x - width / 2, getPosition().y + height /2 +5, width *(currentHealth / (float)maxHealth), 5);
        shapeRenderer.end();
    }

    public void drawComets(ShapeRenderer shapeRenderer) {
        for (Comet comet : comets) {
            comet.draw(shapeRenderer);
        }
    }
}

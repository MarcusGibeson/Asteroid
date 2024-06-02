package com.asteroid.game.objects;

import static com.asteroid.game.objects.Comet.COMET_SPEED;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BossAsteroid extends Asteroid {

    public static final float BOSS_RADIUS = 150.0f;
    private final float width = 300;
    private final float height = 150;
    private static final float BOSS_SPEED = 2.0f;
    private static final int BOSS_HEALTH = 100;
    private static final float COMET_SHOOT_RANGE = 1000;
    private static final float COMET_SHOOT_DELAY = 2;
    private float timeSinceLastComet = 0;
    public boolean toRemove;

    PlayerShip playerShip;
    List<Comet> comets;

    private int maxHealth;
    private int currentHealth;
    private boolean isDestroyed;

    public float[] polygonVertices;
    private float rotationAngle;
    private float rotationSpeed;
    private float asteroidMultiplier;
    private int asteroidType;


    public BossAsteroid(Vector2 position, int tierLevel, PlayerShip playerShip, int maxHealth) {
        super(position, tierLevel, playerShip, 3);
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
        this.comets = new ArrayList<>();
        this.isDestroyed = false;
        toRemove = false;

        this.rotationAngle = 0;
        this.rotationSpeed = MathUtils.random(-50f, 50f);
        this.asteroidMultiplier = 6;
        this.asteroidType = MathUtils.random(1, 3);
        assignBossPolygonVertices(asteroidType);
    }

    public void update(float delta) {
        if(!isDestroyed) {
            super.update(delta);
            updatePolygonVertices();
            rotationAngle += rotationSpeed * delta;


            //Update time since last comet
            timeSinceLastComet += delta;
            //Check if the boss should shoot a comet based on range and delay
            if (shouldShootComet() && timeSinceLastComet >= COMET_SHOOT_DELAY) {
                shootComet();
            }
        }
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public void updateComets(float delta) {
        //Iterate through list of comets
        Iterator<Comet> iterator = comets.iterator();
        while(iterator.hasNext()) {
            Comet comet = iterator.next();
            comet.update(delta, this.playerShip.getPosition());
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
            isDestroyed = true;
        }
    }

    public boolean checkIsDestroyed() {
        return isDestroyed;
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
        Vector2 perpendicularDirection = new Vector2(-cometDirection.y, cometDirection.x);
        Vector2 cometPosition1 = getPosition().cpy().add(perpendicularDirection.scl(10));
        Vector2 cometPosition2 = getPosition().cpy().sub(perpendicularDirection.scl(10));
        Vector2 cometVelocity = cometDirection.scl(COMET_SPEED);

        //Create a new comet at the position of the boss asteroid
        Comet comet = new Comet(cometPosition1, cometVelocity, playerShip);
        Comet comet2 = new Comet(cometPosition2, cometVelocity, playerShip);

        //Add the comet to the list of comets
        comets.add(comet);
        comets.add(comet2);
        timeSinceLastComet = 0;
    }


    //Draw method
    @Override
    public void draw(ShapeRenderer shapeRenderer) {
        if(!isDestroyed) {
            shapeRenderer.setAutoShapeType(true);
            shapeRenderer.begin();
            //Draw boss asteroid with glow effect
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.set(ShapeRenderer.ShapeType.Line);
            shapeRenderer.polygon(polygonVertices);

            //Draw boss asteroid
            shapeRenderer.setColor(Color.WHITE);
            shapeRenderer.polygon(polygonVertices);

            //Draw the health bar
            shapeRenderer.setColor(Color.GREEN);
            shapeRenderer.rect(getPosition().x - width / 2, getPosition().y + height /2 +5, width *(currentHealth / (float)maxHealth), 5);
            shapeRenderer.end();
        }

    }

    public void drawComets(ShapeRenderer shapeRenderer) {
        for (Comet comet : comets) {
            comet.draw(shapeRenderer);
        }
    }

    // Assign polygon vertices based on the boss asteroid's shape
    public void assignBossPolygonVertices(int asteroidType) {
        switch (asteroidType) {
            case 1:
                polygonVertices = new float[] {
                        -40, -14, -22, -22, -18, -40, 22, -40,
                        24, -22, 40, -12, 40, 14, 26, 24,
                        22, 40, -20, 38, -24, 26, -40, 22
                };
                break;
            case 2:
                polygonVertices = new float[] {
                        -40, -14, -20, -16, -22, -40, 22, -40,
                        20, -16, 40, -14, 38, 18, 24, 40,
                        -24, 40, -38, 18
                };
                break;
            case 3:
                polygonVertices = new float[] {
                        -40, -40, 0, -14, 40, -40, 40, 6,
                        0, 40, -40, 8
                };
                break;
            default:
                throw new IllegalArgumentException("Invalid asteroid type: " + asteroidType);
        }

        // Scale vertices
        for (int i = 0; i < polygonVertices.length; i++) {
            polygonVertices[i] *= asteroidMultiplier;
        }

        updatePolygonVertices();
    }

    private void updatePolygonVertices() {
        // Calculate the updated vertices positions based on the current position
        float centerX = position.x;
        float centerY = position.y;

        float[] baseVertices = getBaseVerticesForAsteroidType(asteroidType);

        for (int i = 0; i < baseVertices.length; i += 2) {
            float x = baseVertices[i] * asteroidMultiplier;
            float y = baseVertices[i + 1] * asteroidMultiplier;

            //Apply rotation transformation
            float rotatedX = x * MathUtils.cosDeg(rotationAngle) - y * MathUtils.sinDeg(rotationAngle);
            float rotatedY = x * MathUtils.sinDeg(rotationAngle) + y * MathUtils.cosDeg(rotationAngle);


            polygonVertices[i] = centerX + rotatedX;
            polygonVertices[i + 1] = centerY + rotatedY;
        }
    }

    private float[] getBaseVerticesForAsteroidType(int type) {
        switch (type) {
            case 1:
                return new float[] {
                        -20, -7, -11, -11, -9, -20, 11, -20,
                        12, -11, 20, -6, 20, 7, 13, 12,
                        11, 20, -10, 19, -12, 13, -20, 11
                };
            case 2:
                return new float[] {
                        -20, -7, -10, -8, -11, -20, 11, -20,
                        10, -8, 20, -7, 19, 9, 12, 20,
                        -12, 20, -19, 9
                };
            case 3:
                return new float[] {
                        -20, -20, 0, -7, 20, -20, 20, 3,
                        0, 20, -20, 4
                };
            default:
                throw new IllegalArgumentException("Invalid asteroid type: " + type);
        }
    }

    //getters
    public List<Comet> getComets() {
        return comets;
    }
    public Vector2 getPosition() {
        return position;
    }

    public boolean isToRemove() {
        return toRemove;
    }

    public void setToRemove(boolean toRemove) {
        this.toRemove = toRemove;
    }
}

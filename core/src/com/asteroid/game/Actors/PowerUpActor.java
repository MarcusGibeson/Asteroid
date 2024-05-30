package com.asteroid.game.Actors;

import com.asteroid.game.objects.PowerUp;
import com.asteroid.game.screens.HowToPlayScreen;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class PowerUpActor extends Actor {

    private PowerUp powerUp;
    private ShapeRenderer shapeRenderer;
    private String description;
    private HowToPlayScreen howToPlayScreen;

    public PowerUpActor(PowerUp powerUp, ShapeRenderer shapeRenderer, String description, HowToPlayScreen howToPlayScreen) {
        this.powerUp = powerUp;
        this.shapeRenderer = shapeRenderer;
        this.description = description;
        this.howToPlayScreen = howToPlayScreen;

        setBounds(0, 0, 50, 50);

        addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                String description = howToPlayScreen.getDescription(powerUp.getType());
                howToPlayScreen.setDescription(description);
                return true;
            }
        });
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        powerUp.draw(shapeRenderer);
    }
}

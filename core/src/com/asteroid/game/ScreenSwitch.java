package com.asteroid.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ScreenSwitch extends Game {
    public SpriteBatch batch;

    @Override
    public void create() {
        batch = new SpriteBatch();
        setScreen(new MainMenuScreen(this, batch));
    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
    }

    public void switchToAsteroidXtreme() {
        setScreen(new AsteroidXtreme((ScreenSwitch) Gdx.app.getApplicationListener(), batch));
    }

    public void switchToMainMenu() {
        setScreen(new MainMenuScreen((ScreenSwitch) Gdx.app.getApplicationListener(), batch));
    }
}

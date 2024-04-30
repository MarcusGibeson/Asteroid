package com.asteroid.game;

import com.badlogic.gdx.Game;
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
        setScreen(new AsteroidXtreme());
    }
}

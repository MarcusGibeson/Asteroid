package com.asteroid.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MainMenuScreen implements Screen {
    private AsteroidXtreme game;
    private Stage stage;
    private Texture backgroundTexture;
    SpriteBatch batch;

    public MainMenuScreen(AsteroidXtreme game, SpriteBatch batch) {
        this.game = game;
        this.stage = new Stage(new ScreenViewport());
        this.backgroundTexture = new Texture("backgroundTexture.jpg");
        Gdx.input.setInputProcessor(stage);
        batch = new SpriteBatch();
        setupMenu();
    }
    private void setupMenu() {
        //Add UI elements to the stage
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        //Clear the screen
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Render background texture
        game.batch.begin();
        game.batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.batch.end();

        //Render the stage UI
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width,height,true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        backgroundTexture.dispose();
        stage.dispose();
        batch.dispose();
    }

    //other screen methods like pause, resume, dispose
}

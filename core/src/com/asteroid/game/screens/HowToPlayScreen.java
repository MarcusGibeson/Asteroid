package com.asteroid.game.screens;

import com.asteroid.game.objects.PowerUp;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.List;

public class HowToPlayScreen implements Screen {

    private ScreenSwitch screenSwitch;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private OrthographicCamera camera;
    private List<PowerUp> powerUps;
    private BitmapFont font;
    private int popupWidth;
    private int popupHeight;
    private Stage stage;


    public HowToPlayScreen(ScreenSwitch screenSwitch, ShapeRenderer shapeRenderer, SpriteBatch batch, List<PowerUp> powerUps) {
        this.batch = batch;
        this.screenSwitch = screenSwitch;
        this.powerUps = powerUps;
        this.shapeRenderer = shapeRenderer;
        this.font = new BitmapFont();

        this.stage = new Stage(new ScreenViewport());

        // size of pop-up
        this.popupWidth = Gdx.graphics.getWidth();
        this.popupHeight = Gdx.graphics.getHeight();

        //camera to control view
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, popupWidth, popupHeight);
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Screen currentScreen = screenSwitch.getCurrentScreen();
        if(currentScreen != null && currentScreen != this) {
            currentScreen.render(delta);
        }

        renderPopup(delta);
    }

    private void renderPopup(float delta) {
        batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0,0,0,0.7f);
        shapeRenderer.rect((popupWidth / 2), (popupHeight) / 2, popupWidth, popupHeight);
        shapeRenderer.end();

        batch.begin();
        font.draw(batch, "How to Play", popupWidth  - 40, popupHeight - 20);
        font.draw(batch, "Press esc to close", popupWidth  - 50, popupHeight - 40);
        batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            screenSwitch.setScreen(screenSwitch.getMainMenuScreen());
        }
    }

    public void update(float delta) {

    }


    @Override
    public void resize(int width, int height) {
        popupWidth = width / 2;
        popupHeight = height;
        camera.setToOrtho(false, width, height);
        camera.update();
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

    }

    public InputProcessor getStage() {
        return stage;
    }
}

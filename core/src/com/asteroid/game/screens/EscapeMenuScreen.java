package com.asteroid.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class EscapeMenuScreen implements Screen {

    private ScreenSwitch screenSwitch;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private OrthographicCamera camera;
    private Stage stage;
    private BitmapFont font;
    private TextButton resumeButton;
    private TextButton settingsButton;
    private TextButton quitButton;

    public EscapeMenuScreen(ScreenSwitch screenSwitch, SpriteBatch batch, ShapeRenderer shapeRenderer) {
        this.screenSwitch = screenSwitch;
        this.batch = batch;
        this.shapeRenderer = shapeRenderer;
        this.font = new BitmapFont();

        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        this.stage = new Stage(new ScreenViewport());

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;

        resumeButton = new TextButton("Resume", buttonStyle);
        settingsButton = new TextButton("Settings", buttonStyle);
        quitButton = new TextButton("Quit to Main menu", buttonStyle);

        //Increase button size
        float buttonWidth = 200f;
        float buttonHeight = 50f;

        //Set size of buttons
        resumeButton.setSize(buttonWidth, buttonHeight);
        settingsButton.setSize(buttonWidth, buttonHeight);
        quitButton.setSize(buttonWidth, buttonHeight);

        //position buttons
        resumeButton.setPosition(Gdx.graphics.getWidth() / 2 - resumeButton.getWidth() / 2, Gdx.graphics.getHeight() / 2 + 50);
        settingsButton.setPosition(Gdx.graphics.getWidth() /2 - settingsButton.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        quitButton.setPosition(Gdx.graphics.getWidth() /2 - quitButton.getWidth() / 2, Gdx.graphics.getHeight() / 2 - 50);

        //add buttons to stage
        stage.addActor(resumeButton);
        stage.addActor(settingsButton);
        stage.addActor(quitButton);

        //set up button listeners
        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screenSwitch.setScreen(screenSwitch.getSavedScreen());
            }
        });

        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screenSwitch.setScreen(screenSwitch.getSettingsScreen());
            }
        });

        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screenSwitch.setScreen(screenSwitch.getMainMenuScreen());
            }
        });
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0,0,0,0.7f);
        shapeRenderer.rect(0,0,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shapeRenderer.end();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        font.draw(batch, "Esc Menu", Gdx.graphics.getWidth() / 2 - 30, Gdx.graphics.getHeight() - 30);
        batch.end();

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        camera.setToOrtho(false, width, height);
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
        stage.dispose();
        font.dispose();
    }

    public InputProcessor getStage() {
        return stage;
    }
}

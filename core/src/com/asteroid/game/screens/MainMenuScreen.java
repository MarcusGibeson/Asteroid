package com.asteroid.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MainMenuScreen implements Screen {
    private ScreenSwitch screenSwitch;
    private Stage stage;
    private Texture backgroundTexture;
    SpriteBatch batch;
    private int width = Gdx.graphics.getWidth();
    private int height = Gdx.graphics.getHeight();
    ShapeRenderer shapeRenderer;

    Texture startButtonTexture = new Texture("Images/MainMenuStartButton.png");
    ImageButton startButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(startButtonTexture)));

    Texture settingsButtonTexture = new Texture("Images/MainMenuSettingsButton.png");
    ImageButton settingsButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(settingsButtonTexture)));

    Texture highScoresButtonTexture = new Texture("Images/MainMenuHighScoresButton.png");
    ImageButton highScoresButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(highScoresButtonTexture)));

    private final Sound mainMenuMusic;
    private float volume = 0.2f;

    public MainMenuScreen(ScreenSwitch screenSwitch, SpriteBatch batch) {
        this.screenSwitch = screenSwitch;
        this.stage = new Stage(new ScreenViewport());
        this.backgroundTexture = new Texture("Images/MainMenuBackground.jpg");
        shapeRenderer = new ShapeRenderer();
        mainMenuMusic = Gdx.audio.newSound(Gdx.files.internal("Audio/BackgroundMenuMusic.mp3"));
        Gdx.input.setInputProcessor(stage);
        batch = new SpriteBatch();
        setupMenu();
    }
    private void setupMenu() {
        //Add UI elements to the stage
        mainMenuMusic.loop(volume);
        startButton.setPosition(width * 3/4 +75 - startButton.getWidth() / 2, height  * 3/4  - startButton.getHeight() / 2);
        settingsButton.setPosition(width * 3/4 +75 - startButton.getWidth() / 2, height  * 3/4  - startButton.getHeight() / 2 - 200);
        highScoresButton.setPosition(width * 3/4 +75 - startButton.getWidth() / 2, height  * 3/4  - startButton.getHeight() / 2 - 400);

        //add hover effects
        addHoverEffect(startButton);
        addHoverEffect(settingsButton);
        addHoverEffect(highScoresButton);

        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screenSwitch.switchToAsteroidXtreme();
                startButton.removeListener(this);
            }
        });

        stage.addActor(startButton);
        stage.addActor(settingsButton);
        stage.addActor(highScoresButton);

    }

    private void addHoverEffect(final ImageButton button) {
        final Texture fireTexture = new Texture("Images/MenuFireSelection.png");
        final Image fireImage = new Image(new TextureRegionDrawable(new TextureRegion(fireTexture)));
        fireImage.setVisible(false);
        fireImage.setPosition(button.getX(), button.getY() + button.getHeight());

        button.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                fireImage.setVisible(true);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                fireImage.setVisible(false);
            }
        });
        stage.addActor(fireImage);
    }

    private void drawSelectedAreaOutline(ShapeRenderer shapeRenderer, ImageButton button) {
        float x = button.getX();
        float y = button.getY();
        float width = button.getWidth();
        float height = button.getHeight();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.rect(x,y,width,height);
        shapeRenderer.end();
    }


    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        //Clear the screen
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Render background texture
        screenSwitch.batch.begin();
        screenSwitch.batch.draw(backgroundTexture, 0, 0, width, height);
        screenSwitch.batch.end();

        //Render the stage UI
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();

        //Handle user input to start the game
        if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            screenSwitch.switchToGameOver();
        }

        drawSelectedAreaOutline(shapeRenderer, startButton);

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

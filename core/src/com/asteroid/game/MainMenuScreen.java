package com.asteroid.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
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

    public MainMenuScreen(ScreenSwitch screenSwitch, SpriteBatch batch) {
        this.screenSwitch = screenSwitch;
        this.stage = new Stage(new ScreenViewport());
        this.backgroundTexture = new Texture("Images/backgroundTexture.jpg");
        Gdx.input.setInputProcessor(stage);
        batch = new SpriteBatch();
        setupMenu();
    }
    private void setupMenu() {
        //Add UI elements to the stage
        Texture startButtonTexture = new Texture("Images/startButton.png");
        ImageButton startButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(startButtonTexture)));
        startButton.setPosition(width * 3/4 +75 - startButton.getWidth() / 2, height  * 3/4  - startButton.getHeight() / 2);

        //add hover effect
        final Texture startButtonSelectedTexture = new Texture("Images/startButtonSelected.png");
        final TextureRegionDrawable startButtonSelectedDrawable = new TextureRegionDrawable(new TextureRegion(startButtonSelectedTexture));
        final TextureRegionDrawable startButtonDefaultDrawable = new TextureRegionDrawable(new TextureRegion(startButtonTexture));

        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screenSwitch.switchToAsteroidXtreme();
                startButton.removeListener(this);
            }
        });
        startButton.addListener(getHoverListener(startButton, startButtonDefaultDrawable, startButtonSelectedDrawable));
        stage.addActor(startButton);

    }

    private EventListener getHoverListener(final ImageButton button, final TextureRegionDrawable defaultDrawable,final TextureRegionDrawable selectedDrawable) {
        return new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(button.isOver()) {
                    button.getStyle().imageUp = selectedDrawable;
                }else {
                    button.getStyle().imageUp = defaultDrawable;
                }
            }
        };
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
        screenSwitch.batch.begin();
        screenSwitch.batch.draw(backgroundTexture, 0, 0, width, height);
        screenSwitch.batch.end();

        //Render the stage UI
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();

        //Handle user input to start the game
        if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            screenSwitch.switchToAsteroidXtreme();
        }
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
        stage.dispose();
    }

    @Override
    public void dispose() {
        backgroundTexture.dispose();

        batch.dispose();
    }

    //other screen methods like pause, resume, dispose


}

package com.asteroid.game.screens;

import com.asteroid.game.Controllers.ScoreHandler;
import com.asteroid.game.Controllers.StageManager;
import com.asteroid.game.Controllers.UFOHandler;
import com.asteroid.game.objects.PowerUp;
import com.asteroid.game.objects.UFOShip;
import com.asteroid.game.Controllers.AsteroidHandler;
import com.asteroid.game.Controllers.CollisionHandler;
import com.asteroid.game.objects.PlayerShip;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;
import java.util.List;

public class ScreenSwitch extends Game {
    public SpriteBatch batch;
    private Screen gameOverScreen;
    private Screen mainMenuScreen;
    private AsteroidXtreme asteroidXtreme;
    private GameLoop gameLoop;

    private CollisionHandler collisionHandler;
    private PlayerShip ship;
    private AsteroidHandler asteroidHandler;
    private StageManager stageManager;
    private ScoreHandler scoreHandler;
    private ShapeRenderer shapeRenderer;
    private Sound backgroundMusic;
    private List<PowerUp> powerUps;
    private UFOHandler ufoHandler;
    private HowToPlayScreen howToPlayScreen;
    private Screen currentScreen;
    private Screen previousScreen;
    private Screen savedScreen;
    private EscapeMenuScreen escMenuScreen;
    private OrthographicCamera camera;

    private float volume = 0.05f;

    @Override
    public void create() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        scoreHandler = new ScoreHandler();
        ship = new PlayerShip((float) Gdx.graphics.getWidth() / 2, (float) Gdx.graphics.getHeight() / 2, 1, 5);
        collisionHandler = new CollisionHandler(scoreHandler);
        asteroidHandler = new AsteroidHandler(ship, shapeRenderer, scoreHandler);
        ufoHandler = new UFOHandler(ship, shapeRenderer);
        stageManager = new StageManager(asteroidHandler, ufoHandler, ship);
        powerUps = new ArrayList<>();

        asteroidXtreme = new AsteroidXtreme(this, batch, collisionHandler, ship, ufoHandler, asteroidHandler, stageManager, shapeRenderer, powerUps);
        gameLoop = new GameLoop(this, batch, asteroidXtreme, collisionHandler, ship, ufoHandler, asteroidHandler, stageManager);
        backgroundMusic = Gdx.audio.newSound(Gdx.files.internal("Audio/BackgroundGameMusic.mp3"));
        mainMenuScreen = new MainMenuScreen(this, batch);
        howToPlayScreen = new HowToPlayScreen(this, shapeRenderer, batch, powerUps);
        gameOverScreen = new GameOverScreen(this, batch, scoreHandler);
        escMenuScreen = new EscapeMenuScreen(this, batch, shapeRenderer);
        previousScreen = mainMenuScreen;
        setScreen(mainMenuScreen);

    }

    public Screen getCurrentScreen() {
        return currentScreen;
    }
    public Screen getSavedScreen() { return savedScreen;}

    public Screen getPreviousScreen() {
        return previousScreen;
    }

    public OrthographicCamera getCurrentScreenCamera() {
        if (currentScreen instanceof MainMenuScreen) {
            return ((MainMenuScreen) currentScreen).getCamera();
        } else if (currentScreen instanceof HowToPlayScreen) {
            return ((HowToPlayScreen) currentScreen).getCamera();
        }
        return null;
    }

    @Override
    public void setScreen(Screen screen) {
        currentScreen = screen;
        super.setScreen(screen);

        if (screen instanceof MainMenuScreen) {
            Gdx.input.setInputProcessor(((MainMenuScreen) screen).getStage());
        } else if (screen instanceof HowToPlayScreen) {
            Gdx.input.setInputProcessor(((HowToPlayScreen) screen).getStage());
        } else if (screen instanceof EscapeMenuScreen) {
            Gdx.input.setInputProcessor(((EscapeMenuScreen) screen).getStage());
        } else {
            Gdx.input.setInputProcessor(null);
        }
    }

    public GameLoop getGameLoop() {
        return gameLoop;
    }

    @Override
    public void render() {
        checkEscKeyPressed();
        super.render();
    }

    public void checkEscKeyPressed() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            if(getScreen() == mainMenuScreen || getScreen() == gameOverScreen) {
                return;
            }

            if(getScreen() == howToPlayScreen) {
                if (previousScreen instanceof MainMenuScreen) {
                    setScreen(previousScreen);
                } else {
                    setScreen(escMenuScreen);
                }
            }

            previousScreen = getScreen();
            if (getScreen() != escMenuScreen) {
                previousScreen = getScreen();
                savedScreen = getScreen();
                if (previousScreen instanceof AsteroidXtreme) {
                    ((AsteroidXtreme) savedScreen).pause();
                }
                setScreen(escMenuScreen);
            } else {
                if (savedScreen != null) {
                    if (savedScreen instanceof AsteroidXtreme) {
                        ((AsteroidXtreme) savedScreen).resume();
                    }
                    setScreen(savedScreen);
                } else {
                    if (previousScreen instanceof AsteroidXtreme) {
                        ((AsteroidXtreme) previousScreen).resume();
                    }
                    setScreen(previousScreen);
                }
            }
        }
    }
    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
        shapeRenderer.dispose();
        backgroundMusic.dispose();
        if (mainMenuScreen != null) mainMenuScreen.dispose();
        if (gameOverScreen != null) gameOverScreen.dispose();
        if (asteroidXtreme != null) asteroidXtreme.dispose();
        if (howToPlayScreen != null) howToPlayScreen.dispose();
    }

    public void switchToAsteroidXtreme() {
        if(getScreen() instanceof MainMenuScreen) {
            mainMenuScreen = getScreen();
            mainMenuScreen.hide();
        }
        backgroundMusic.loop(volume);
        setScreen(new AsteroidXtreme((ScreenSwitch) Gdx.app.getApplicationListener(), batch, collisionHandler, ship, ufoHandler, asteroidHandler, stageManager, shapeRenderer, powerUps));
    }

    public void switchToMainMenu() {
        if(getScreen() instanceof GameOverScreen) {
            gameOverScreen.dispose();
        } else if (getScreen() instanceof EscapeMenuScreen) {
            if(getPreviousScreen() instanceof AsteroidXtreme) {
                asteroidXtreme.dispose();
            }
        }
        if (mainMenuScreen == null) {
            mainMenuScreen = new MainMenuScreen(this, batch);
        }
        setScreen(new MainMenuScreen((ScreenSwitch) Gdx.app.getApplicationListener(), batch));
    }

    public Screen getMainMenuScreen() {
        savedScreen = null;
        return mainMenuScreen;
    }

    public Screen getSettingsScreen() {
        return howToPlayScreen;
    }

    public void switchToHowToPlayScreen() {
        previousScreen = getScreen();
        savedScreen = mainMenuScreen;
        setScreen(new HowToPlayScreen((ScreenSwitch) Gdx.app.getApplicationListener(), shapeRenderer, batch, powerUps));
    }

    public void switchToGameOver(){
        Screen currentScreen = getScreen();
        if (currentScreen instanceof AsteroidXtreme) {
            asteroidXtreme = (AsteroidXtreme) currentScreen;
            GameLoop gameLoop = asteroidXtreme.getGameLoop();
            gameLoop.stop();
            asteroidXtreme.setRenderingEnabled(false);
            asteroidXtreme.hide();
            asteroidXtreme.dispose();
            backgroundMusic.dispose();

        }
        setScreen(new GameOverScreen((ScreenSwitch) Gdx.app.getApplicationListener(), batch, scoreHandler));
        gameOverScreen.show();


    }

}

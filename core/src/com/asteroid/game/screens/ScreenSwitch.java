package com.asteroid.game.screens;

import com.asteroid.game.Controllers.ScoreHandler;
import com.asteroid.game.Controllers.StageManager;
import com.asteroid.game.Controllers.UFOHandler;
import com.asteroid.game.objects.UFOShip;
import com.asteroid.game.Controllers.AsteroidHandler;
import com.asteroid.game.Controllers.CollisionHandler;
import com.asteroid.game.objects.PlayerShip;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class ScreenSwitch extends Game {
    public SpriteBatch batch;
    private Screen gameOverScreen;
    private Screen mainMenuScreen;
    private AsteroidXtreme asteroidXtreme;
    private GameLoop gameLoop;

    private CollisionHandler collisionHandler;
    private PlayerShip ship;
    private UFOShip ufo;
    private AsteroidHandler asteroidHandler;
    private StageManager stageManager;
    private ScoreHandler scoreHandler;
    private ShapeRenderer shapeRenderer;
    private Sound backgroundMusic;
    private UFOHandler ufoHandler;

    private float volume = 0.05f;

    @Override
    public void create() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        scoreHandler = new ScoreHandler();
        ship = new PlayerShip((float) Gdx.graphics.getWidth() / 2, (float) Gdx.graphics.getHeight() / 2, 1, 1);
        collisionHandler = new CollisionHandler(scoreHandler);
        ufo = new UFOShip(200, 200, ship);
        asteroidHandler = new AsteroidHandler(ship, shapeRenderer, scoreHandler);
        ufoHandler = new UFOHandler(ship, shapeRenderer, scoreHandler);
        stageManager = new StageManager(asteroidHandler, ufoHandler, ship);

        asteroidXtreme = new AsteroidXtreme(this, batch, collisionHandler, ship, ufoHandler, asteroidHandler, stageManager, shapeRenderer);
        gameLoop = new GameLoop(this, batch, asteroidXtreme, collisionHandler, ship, ufoHandler, asteroidHandler, stageManager);
        backgroundMusic = Gdx.audio.newSound(Gdx.files.internal("Audio/BackgroundGameMusic.mp3"));
        mainMenuScreen = new MainMenuScreen(this, batch);
        gameOverScreen = new GameOverScreen(this, batch, scoreHandler);
        setScreen(mainMenuScreen);
    }

    public GameLoop getGameLoop() {
        return gameLoop;
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    public void switchToAsteroidXtreme() {

        if(getScreen() instanceof MainMenuScreen) {
            mainMenuScreen = getScreen();
            mainMenuScreen.dispose();
        }
        backgroundMusic.loop(volume);
        setScreen(new AsteroidXtreme((ScreenSwitch) Gdx.app.getApplicationListener(), batch, collisionHandler, ship, ufoHandler, asteroidHandler, stageManager, shapeRenderer));
    }

    public void switchToMainMenu() {
        if(getScreen() instanceof GameOverScreen) {
            gameOverScreen.dispose();
        }
        setScreen(new MainMenuScreen((ScreenSwitch) Gdx.app.getApplicationListener(), batch));
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

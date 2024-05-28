package com.asteroid.game.screens;

import com.asteroid.game.Controllers.UFOHandler;
import com.asteroid.game.objects.StageLevel;
import com.asteroid.game.Controllers.StageManager;
import com.asteroid.game.objects.UFOShip;
import com.asteroid.game.Controllers.AsteroidHandler;
import com.asteroid.game.Controllers.CollisionHandler;
import com.asteroid.game.Controllers.ScoreHandler;
import com.asteroid.game.objects.Asteroid;
import com.asteroid.game.objects.PlayerShip;
import com.asteroid.game.objects.PowerUp;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.awt.Shape;
import java.util.ArrayList;
import java.util.List;


public class AsteroidXtreme extends ApplicationAdapter implements Screen {
	private ShapeRenderer shapeRenderer;
	private Texture lifeTexture;
	private TextureRegion lifeRegion;

	PlayerShip ship;
	UFOShip ufo;
	CollisionHandler collisionHandler;
	SpriteBatch spriteBatch;
	BitmapFont font;
	AsteroidHandler asteroidHandler;
	ScoreHandler scoreHandler;
	UFOHandler ufoHandler;

	List<Asteroid> asteroids;
	StageLevel stageLevel;
	boolean gameOver;
	private StageManager stageManager;
	private ScreenSwitch screenSwitch;
	private GameLoop gameLoop;

	public ShapeRenderer shape;
	public SpriteBatch batch;

	private Stage stage;
	private boolean renderingEnabled = true;

	List<PowerUp> powerUps;

	public AsteroidXtreme() {

	}

	public AsteroidXtreme(ScreenSwitch screenSwitch, SpriteBatch batch, CollisionHandler collisionHandler, PlayerShip ship, UFOHandler ufoHandler, AsteroidHandler asteroidHandler, StageManager stageManager, ShapeRenderer shapeRenderer, List<PowerUp> powerUps) {
		this.spriteBatch = batch;
		this.screenSwitch = screenSwitch;
		this.stage = new Stage(new ScreenViewport());
		this.collisionHandler = collisionHandler;
		this.ship = ship;
		this.asteroidHandler = asteroidHandler;
		this.ufoHandler = ufoHandler;
		this.stageManager = stageManager;
		this.shapeRenderer = shapeRenderer;
		this.powerUps = powerUps;
		initialize();
		resetGameState();
	}
	public void initialize() {
		shape = new ShapeRenderer();
		batch = new SpriteBatch();
		scoreHandler = new ScoreHandler();
		lifeTexture = new Texture(Gdx.files.internal("Images/Player_Ship_Lives.png"));
		lifeRegion = new TextureRegion(lifeTexture);

		font = new BitmapFont();

		asteroids = asteroidHandler.getAsteroids();
		stageLevel = stageManager.getCurrentStage();
		gameOver = false;
		gameLoop = screenSwitch.getGameLoop();

		powerUps = new ArrayList<>();
		//initializing a timer to spawn new powerups
		Timer.schedule(new Timer.Task() {
			@Override
			public void run() {
				addNewPowerUp();
			}
		},10 ,30);
	}


	@Override
	public void render(float delta) {

		if (!renderingEnabled) {
			return;
		}

		if(!gameOver) {
			if (isGameOver()) {
				screenSwitch.switchToGameOver();
				return;
			}
			if (!isGameOver()) {

				gameLoop.update(delta, powerUps);

				// Clear screen
				Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

				// Draw ship

				ship.draw(shape);
				ship.drawBullets(shape);

				//Draw ufo
				ufoHandler.draw(shape);
				ufoHandler.drawBullets(shape);

				//Draw asteroids
				asteroidHandler.render();

				// Draw all power-ups
				for (PowerUp powerUp : powerUps) {
					powerUp.draw(shape);
				}

				//Respawn message
				batch.begin();
				ship.drawRespawnMessage(batch, font);
				batch.end();

				//Scoreboard
				batch.begin();
				font.draw(batch, "Score: " + scoreHandler.getScore(), 20, Gdx.graphics.getHeight()-20);
				batch.end();


				//Draw lives
				batch.begin();
				drawPlayerLives(batch);
				batch.end();

				//Stage
				batch.begin();
				font.draw(batch, "Stage: " + (stageManager.getCurrentStageIndex() + 1), Gdx.graphics.getWidth()- 100, Gdx.graphics.getHeight() - 20);
				batch.end();

				//Game won message
				batch.begin();
				stageManager.drawGameWonMessage(batch, font);
				batch.end();




			}
		}


	}

	@Override
	public void show() {
		gameLoop.start();
	}

	@Override
	public void pause() {
		gameLoop.pause();
	}

	@Override
	public void resume() {
		gameLoop.resume();
	}



	@Override
	public void hide() {
		gameLoop.stop();
	}

	@Override
	public void dispose() {
		disposeResources();
	}


	private void drawPlayerLives(SpriteBatch batch) {
		int lives = ship.getLives();
		float lifeImageWidth = lifeRegion.getRegionWidth();
		float lifeImageHeight = lifeRegion.getRegionHeight();
		float startX = 10;
		float startY = Gdx.graphics.getHeight() - 80;
		for (int i = 0; i < lives; i++) {
			batch.draw(lifeRegion, startX + i * (lifeImageWidth + 5), startY, lifeImageWidth, lifeImageHeight);
		}
	}

	public boolean isGameOver() {
		int playerLives = ship.getLives();
        if (playerLives == 0) {
			gameOver = true;
			return true;
		}
		gameOver = false;
		return false;
    }

	public void setRenderingEnabled(boolean enabled) {
		renderingEnabled = enabled;
    }

	public GameLoop getGameLoop() {
		return gameLoop;
	}

	public void disposeResources() {
		// Dispose resources in reverse order of creation
		if (font != null) {
			font.dispose();
			font = null;
		}

		if (lifeTexture != null) {
			lifeTexture.dispose();
			lifeTexture = null;
		}

		if (shape != null) {
			shape.dispose();
			shape = null;
		}

		if (batch != null) {
			batch.dispose();
			batch = null;
		}

		if(stage != null) {
			stage.dispose();
			stage = null;
		}
	}

	private void addNewPowerUp() {
		PowerUp newPowerUp = new PowerUp();
		powerUps.add(newPowerUp);
	}

	public void resetGameState() {
		gameOver = false;
		ship.resetLives();
		scoreHandler.resetScore();
		stageManager.resetStageLevels();
		asteroidHandler.resetAsteroids();
		asteroidHandler.resetBossAsteroids();
		ufoHandler.resetUFOs();
		powerUps.clear();
		stageManager.setCurrentStageIndex(-1);

	}
}

package com.asteroid.game.screens;

import com.asteroid.game.objects.StageLevel;
import com.asteroid.game.Controllers.StageManager;
import com.asteroid.game.objects.UFOShip;
import com.asteroid.game.Controllers.AsteroidHandler;
import com.asteroid.game.Controllers.CollisionHandler;
import com.asteroid.game.Controllers.ScoreHandler;
import com.asteroid.game.objects.Asteroid;
import com.asteroid.game.objects.PlayerShip;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.awt.Shape;
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

	List<Asteroid> asteroids;
	StageLevel stageLevel;
	boolean gameOver;
	private StageManager stageManager;
	private ScreenSwitch screenSwitch;
	private GameLoop gameLoop;



	private float volume = 0.05f;
	private Stage stage;
	private boolean renderingEnabled = true;

	public AsteroidXtreme() {
	}

	public AsteroidXtreme(ScreenSwitch screenSwitch, SpriteBatch batch, CollisionHandler collisionHandler, PlayerShip ship, UFOShip ufo, AsteroidHandler asteroidHandler, StageManager stageManager, ShapeRenderer shapeRenderer) {
		this.spriteBatch = batch;
		this.screenSwitch = screenSwitch;
		this.stage = new Stage(new ScreenViewport());
		this.collisionHandler = collisionHandler;
		this.ship = ship;
		this.ufo = ufo;
		this.asteroidHandler = asteroidHandler;
		this.stageManager = stageManager;
		this.shapeRenderer = shapeRenderer;
		initialize();
	}
	public void initialize() {
		scoreHandler = new ScoreHandler();
		lifeTexture = new Texture(Gdx.files.internal("Images/Player_Ship_Lives.png"));
		lifeRegion = new TextureRegion(lifeTexture);
		font = new BitmapFont();

		asteroids = asteroidHandler.getAsteroids();
		stageLevel = stageManager.getCurrentStage();
		gameOver = false;
		gameLoop = screenSwitch.getGameLoop();

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
			if (shapeRenderer != null && spriteBatch != null && font != null &&!isGameOver()) {
				gameLoop.update(delta);

				// Clear screen
				Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

				// Draw ship

				ship.draw(shapeRenderer);
				ship.drawBullets(shapeRenderer);

				//Draw ufo
				ufo.draw(shapeRenderer);
				ufo.drawBullets(shapeRenderer);

				//Draw asteroids
				asteroidHandler.render();

				//Respawn message
				spriteBatch.begin();
				ship.drawRespawnMessage(spriteBatch, font);
				spriteBatch.end();

				//Scoreboard
				spriteBatch.begin();
				font.draw(spriteBatch, "Score: " + scoreHandler.getScore(), 20, Gdx.graphics.getHeight()-20);

				//Draw lives
				drawPlayerLives(spriteBatch);
				spriteBatch.end();

				//Game won message
				spriteBatch.begin();
				stageManager.drawGameWonMessage(spriteBatch, font);
				spriteBatch.end();




			}
		}


	}

	@Override
	public void show() {

	}

	@Override
	public void hide() {
		disposeResources();
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
			spriteBatch.draw(lifeRegion, startX + i * (lifeImageWidth + 5), startY, lifeImageWidth, lifeImageHeight);
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
	}
}

package com.asteroid.game;

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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;

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

	List<Asteroid> asteroids;
	StageLevel stageLevel;

	boolean gameOver;
	private StageManager stageManager;
	private ScreenSwitch screenSwitch;
	List<PowerUp> powerUps;

	private float volume = 0.05f;

	public AsteroidXtreme() {
	}

	public AsteroidXtreme(ScreenSwitch screenSwitch, SpriteBatch batch) {
		this.spriteBatch = batch;
		this.screenSwitch = screenSwitch;

		initialize();
	}
	private void initialize() {
		shapeRenderer = new ShapeRenderer();
		scoreHandler = new ScoreHandler();
		spriteBatch = new SpriteBatch();
		lifeTexture = new Texture(Gdx.files.internal("Images/Player_Ship_Lives.png"));
		lifeRegion = new TextureRegion(lifeTexture);
		font = new BitmapFont();
		ship = new PlayerShip((float) Gdx.graphics.getWidth() / 2, (float) Gdx.graphics.getHeight() / 2, 1, 5);
		ufo = new UFOShip(200, 200, ship);
		collisionHandler = new CollisionHandler(scoreHandler);
		asteroidHandler = new AsteroidHandler(ship, shapeRenderer, scoreHandler);
		stageManager = new StageManager(asteroidHandler, ship);
		asteroids = asteroidHandler.getAsteroids();
		stageLevel = stageManager.getCurrentStage();
		gameOver = false;

		powerUps = new ArrayList<>();

		//initializing a timer to spawn new powerups
		Timer.schedule(new Timer.Task() {
			@Override
			public void run() {
				addNewPowerUp();
			}
		},5 ,5);
	}


	@Override
	public void render(float delta) {
		if (!isGameOver()) {
			System.out.println(powerUps.size());
			// Clear screen
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			//float delta = Gdx.graphics.getDeltaTime();

			//Update collision handler
			collisionHandler.update(ship, ufo, asteroidHandler, powerUps);

			// Update ship logic
			ship.update(delta);
			ufo.update(delta);

			//Update asteroids
			asteroidHandler.update(delta);



			// Draw ship
			ship.draw(shapeRenderer);
			ship.drawBullets(shapeRenderer);

			//Draw ufo
			ufo.draw(shapeRenderer);
			ufo.drawBullets(shapeRenderer);

			//Draw asteroids
			asteroidHandler.render();

			// Draw all power-ups
			for (PowerUp powerUp : powerUps) {
				powerUp.draw(shapeRenderer);
			}

			//Respawn message
			spriteBatch.begin();
			ship.drawRespawnMessage(spriteBatch, font);
			spriteBatch.end();



			//Scoreboard
			spriteBatch.begin();
			font.draw(spriteBatch, "Score: " + scoreHandler.getScore(), 20, Gdx.graphics.getHeight()-20);

			//Draw lives
			drawPlayerLives(spriteBatch);

			//Check for stage transitions and update parameters
			stageManager.update(delta);

			//Game won message
			spriteBatch.begin();
			stageManager.drawGameWonMessage(spriteBatch, font);
			spriteBatch.end();
		} else {
			//switch this to game over screen
			screenSwitch.switchToMainMenu();
		}
	}

	@Override
	public void show() {

	}

//	@Override
//	public void render(float delta) {
//
//	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {
		shapeRenderer.dispose();
		lifeTexture.dispose();
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
		spriteBatch.end();

	}

	private boolean isGameOver() {
		int playerLives = ship.getLives();
		if (playerLives == 0) {
			return true;
		}
		return false;
	}

	private void addNewPowerUp() {
		PowerUp newPowerUp = new PowerUp();
		powerUps.add(newPowerUp);
	}

}

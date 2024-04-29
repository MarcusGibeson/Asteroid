package com.asteroid.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.util.List;


public class AsteroidXtreme extends ApplicationAdapter {
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

	private StageManager stageManager;


	@Override
	public void create() {

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
	}

	@Override
	public void render() {
		// Clear screen
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		float delta = Gdx.graphics.getDeltaTime();

		//Update collision handler
		collisionHandler.update(ship, ufo, asteroidHandler);

		stageManager.update(delta);

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

		//Check for stage transitions and update parameters
		updateStageParameters();

	}

	@Override
	public void dispose() {
		shapeRenderer.dispose();
		lifeTexture.dispose();
	}


	private void updateStageParameters() {
		Stage currentStage = stageManager.getCurrentStage();
		asteroidHandler.setAsteroidsPerSpawn(currentStage.getAsteroidCount());
		asteroidHandler.setSpawnCooldown(currentStage.getSpawnCooldownMin(), currentStage.getSpawnCooldownMax());
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
}

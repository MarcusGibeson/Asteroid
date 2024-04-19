package com.asteroid.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;


public class AsteroidXtreme extends ApplicationAdapter {
	private ShapeRenderer shapeRenderer;
	PlayerShip ship;
	UFOShip ufo;
	CollisionHandler collisionHandler;
	SpriteBatch spriteBatch;
	BitmapFont font;
	AsteroidHandler asteroidHandler;


	@Override
	public void create() {
		shapeRenderer = new ShapeRenderer();
		spriteBatch = new SpriteBatch();
		font = new BitmapFont();
		ship = new PlayerShip((float) Gdx.graphics.getWidth() / 2, (float) Gdx.graphics.getHeight() / 2, 1, 5);
		ufo = new UFOShip(200, 200, ship);
		collisionHandler = new CollisionHandler();
		asteroidHandler = new AsteroidHandler(ship, shapeRenderer);
	}

	@Override
	public void render() {
		// Clear screen
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		float delta = Gdx.graphics.getDeltaTime();

		//Update collision handler
		collisionHandler.update(ship, ufo);

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
	}

	@Override
	public void dispose() {
		shapeRenderer.dispose();
	}

}

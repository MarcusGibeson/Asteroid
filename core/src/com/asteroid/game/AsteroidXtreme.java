package com.asteroid.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;


public class AsteroidXtreme extends ApplicationAdapter {
	private ShapeRenderer shapeRenderer;
	PlayerShip ship;
	ArrayList<Asteroid> asteroids;


	@Override
	public void create() {
		shapeRenderer = new ShapeRenderer();
		ship = new PlayerShip((float) Gdx.graphics.getWidth() / 2, (float) Gdx.graphics.getHeight() / 2);
		asteroids = new ArrayList<>();
	}

	@Override
	public void render() {
		// Clear screen
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		float delta = Gdx.graphics.getDeltaTime();

		// Update ship logic
		ship.update(delta);
		for (Asteroid asteroid : asteroids){
			asteroid.update(delta);
			asteroid.draw(shapeRenderer);
		}

		// Draw ship
		ship.draw(shapeRenderer);
		ship.drawBullets(shapeRenderer);


	}

	@Override
	public void dispose() {
		shapeRenderer.dispose();
	}

}

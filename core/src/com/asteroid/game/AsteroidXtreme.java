package com.asteroid.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.List;

public class AsteroidXtreme extends ApplicationAdapter {
	private ShapeRenderer shapeRenderer;
	PlayerShip ship;


	@Override
	public void create() {
		shapeRenderer = new ShapeRenderer();
		ship = new PlayerShip(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
	}

	@Override
	public void render() {
		// Clear screen
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		float delta = Gdx.graphics.getDeltaTime();

		// Update ship logic
		ship.update(delta);

		// Draw ship
		ship.draw(shapeRenderer);
		ship.drawBullets(shapeRenderer);


	}

	@Override
	public void dispose() {
		shapeRenderer.dispose();
	}

}

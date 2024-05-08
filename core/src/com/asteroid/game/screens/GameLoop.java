package com.asteroid.game.screens;

import com.asteroid.game.Controllers.AsteroidHandler;
import com.asteroid.game.Controllers.CollisionHandler;
import com.asteroid.game.Controllers.StageManager;
import com.asteroid.game.objects.PlayerShip;
import com.asteroid.game.objects.PowerUp;
import com.asteroid.game.objects.UFOShip;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.List;

public class GameLoop {

    private ScreenSwitch screenSwitch;
    private AsteroidXtreme asteroidXtreme;
    private CollisionHandler collisionHandler;
    private PlayerShip ship;
    private UFOShip ufo;
    private AsteroidHandler asteroidHandler;
    private StageManager stageManager;
    private SpriteBatch batch;
    private List<PowerUp> powerUps;

    private boolean running;

    public GameLoop(ScreenSwitch screenSwitch, SpriteBatch batch, AsteroidXtreme asteroidXtreme,
                    CollisionHandler collisionHandler, PlayerShip ship, UFOShip ufo,
                    AsteroidHandler asteroidHandler, StageManager stageManager, List<PowerUp> powerUps) {
        this.batch = batch;
        this.screenSwitch = screenSwitch;
        this.asteroidXtreme = asteroidXtreme;
        this.collisionHandler = collisionHandler;
        this.ship = ship;
        this.ufo = ufo;
        this.asteroidHandler = asteroidHandler;
        this.stageManager = stageManager;
        this.powerUps = powerUps;
        running = true;
    }

    public void start() {
        running = true;
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        float delta = 0;
        long timer = System.currentTimeMillis();
        int updates = 0;
        int frames = 0;
        while(running) {
            long now = System.nanoTime();
            delta += (now-lastTime) / ns;
            lastTime = now;
            while (delta >= 1) {
                update(delta);
                updates++;
                delta--;
            }
            frames++;

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                System.out.println("FPS: " + frames + " Ticks: " + updates);
                frames = 0;
                updates = 0;
            }

        }
    }


    public void update(float delta) {
        //Update collision handler
        collisionHandler.update(ship, ufo, asteroidHandler, powerUps);

        // Update ship logic
        ship.update(delta);
        ufo.update(delta);

        //Update asteroids
        asteroidHandler.update(delta);

        //Check for stage transitions and update parameters
        stageManager.update(delta);


    }

    public void stop() {
        running = false;
    }
}

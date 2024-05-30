package com.asteroid.game.screens;

import com.asteroid.game.Actors.PowerUpActor;
import com.asteroid.game.objects.PowerUp;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collections;

public class HowToPlayScreen implements Screen {

    private ScreenSwitch screenSwitch;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private OrthographicCamera camera;
    private List<PowerUp> powerUps;
    private BitmapFont font;
    private int width;
    private int height;
    private Stage stage;
    private Label descriptionLabel;
    private Skin skin;
    private Map<PowerUp.Type, String> powerUpDescriptions;


    public HowToPlayScreen(ScreenSwitch screenSwitch, ShapeRenderer shapeRenderer, SpriteBatch batch, List<PowerUp> powerUps) {
        this.batch = batch;
        this.screenSwitch = screenSwitch;
        this.powerUps = powerUps;
        this.shapeRenderer = shapeRenderer;
        this.font = new BitmapFont();
        this.skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

        this.stage = new Stage(new ScreenViewport());

        // size of pop-up
        this.width = Gdx.graphics.getWidth();
        this.height = Gdx.graphics.getHeight();

        //camera to control view
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, width, height);

        initializeDescriptions();
        initializePowerUps();
        setupUI();
    }

    private void setupUI() {
        //Setup UI components
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        //Power-up list
        Table powerUpTable = new Table();
        powerUpTable.top().left();
        powerUpTable.setWidth(width / 3f);
        powerUpTable.setHeight(height);

        float x = 10;
        float y = height - 50;

        for (PowerUp powerUp : powerUps) {
            PowerUpActor powerUpActor = new PowerUpActor(powerUp, shapeRenderer, powerUp.getType().name(), this);
            powerUpActor.setPosition(x, y);
            powerUp.setPosition(x,y);
            powerUpTable.add(powerUpActor).pad(10).row();

            y -= 50;
        }


        //Description label
        descriptionLabel = new Label("", skin);
        Table descriptionTable = new Table();
        descriptionTable.bottom().left();
        descriptionTable.setWidth(2 * width / 3f);
        descriptionTable.setHeight(height / 2f);
        descriptionTable.add(descriptionLabel).expand().fill().pad(10);

        table.add(powerUpTable).width(width / 3f).height(height).expand().fill();
        table.add(descriptionTable).width(2 * width / 3f).height(height / 2f).expand().fill();
    }

    public void setDescription(String description) {
        descriptionLabel.setText(description);
    }

    private void initializeDescriptions() {
        powerUpDescriptions = new HashMap<>();
        powerUpDescriptions.put(PowerUp.Type.RAPID_FIRE, "Increases your firing rate significantly.");
        powerUpDescriptions.put(PowerUp.Type.PULSE_SHOT, "Quickly fire multiple shots in a row");
        powerUpDescriptions.put(PowerUp.Type.WAVE_SHOT, "Fires shots in a wave pattern.");
        powerUpDescriptions.put(PowerUp.Type.KILL_AURA, "Creates an aura that damages nearby enemies");
        powerUpDescriptions.put(PowerUp.Type.MULTI_SHOT, "Shoots multiple projectiles at once.");
        powerUpDescriptions.put(PowerUp.Type.INVULN, "Grants temporary invunerability.");
    }

    public String getDescription(PowerUp.Type type) {
        return powerUpDescriptions.get(type);
    }

    private void initializePowerUps() {
        powerUps = new ArrayList<>();
        for (PowerUp.Type type : PowerUp.Type.values()) {
            PowerUp powerUp = new PowerUp(type);
            powerUps.add(powerUp);
        }
    }

    @SuppressWarnings("NewApi")
    private void sortPowerUps() {
        Collections.sort(powerUps, Comparator.comparing(PowerUp::getType));
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Screen currentScreen = screenSwitch.getCurrentScreen();
        if(currentScreen != null && currentScreen != this) {
            currentScreen.render(delta);
        }

        renderData(delta);
        drawSections();

        stage.act(delta);
        stage.draw();
    }

    private void renderData(float delta) {
        batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0,0,0,0.7f);
        shapeRenderer.rect((width / 2), (height) / 2, width, height);
        shapeRenderer.end();

        batch.begin();
        font.draw(batch, "How to Play", width / 2  - 40, height - 20);
        font.draw(batch, "Press esc to close", width / 2 - 50, height - 40);

        font.draw(batch, "Welcome to Asteroids Xtreme. Controls are simple:", width / 2 - 150, height - 100);

        batch.end();

    }

    private void drawSections() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1, 0, 0, 1); // Red color

        // First part: 1/3 width, 100% height
        float firstPartWidth = width / 3f;
        float firstPartHeight = height - 2;
        shapeRenderer.rect(2, 2, firstPartWidth, firstPartHeight);

        // Second part: 2/3 width, 50% height (top half)
        float secondPartWidth = 2 * width / 3f;
        float secondPartHeight = height / 2f;
        shapeRenderer.rect(firstPartWidth, secondPartHeight, secondPartWidth, secondPartHeight);

        // Third part: 2/3 width, 50% height (bottom half)
        shapeRenderer.rect(firstPartWidth, 2, secondPartWidth, secondPartHeight);

        shapeRenderer.end();
    }

    public void update(float delta) {

    }


    @Override
    public void resize(int width, int height) {
        this.width = width;
        this.height = height;
        camera.setToOrtho(false, width, height);
        camera.update();
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
        batch.dispose();
        shapeRenderer.dispose();
        font.dispose();
        skin.dispose();

    }

    public InputProcessor getStage() {
        return stage;
    }
}

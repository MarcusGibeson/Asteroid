package com.asteroid.game.screens;


import com.asteroid.game.Controllers.ScoreHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class GameOverScreen implements Screen {

    private boolean isGameOver;
    private boolean isHighScoreEntered;
    private String playerName;
    private BitmapFont font;
    private ScreenSwitch screenSwitch;
    private SpriteBatch batch;
    private ScoreHandler scoreHandler;


    private ShapeRenderer shapeRenderer;
    private GlyphLayout glyphLayout;

    public GameOverScreen(ScreenSwitch screenSwitch, SpriteBatch batch, ScoreHandler scoreHandler) {
        this.screenSwitch = screenSwitch;
        this.scoreHandler = scoreHandler;
        this.shapeRenderer = new ShapeRenderer();
        this.font = new BitmapFont(Gdx.files.internal("Fonts/default.fnt"));
        this.font.setColor(Color.RED);
        this.font.getData().setScale(2);
        this.batch = batch;
        this.isGameOver = true;
        this.isHighScoreEntered = false;
        this.playerName = "";
        this.glyphLayout = new GlyphLayout();
    }


    @Override
    public void show() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);

        batch.begin();
        if (isGameOver) {
            font.draw(batch, "Score: " + scoreHandler.getScore(), Gdx.graphics.getWidth() / 2 - 50, Gdx.graphics.getHeight() / 2 + 200);
            font.getData().setScale(3);
            font.draw(batch, "Game Over!", Gdx.graphics.getWidth() / 2 - 150, Gdx.graphics.getHeight() / 2 + 100);
            font.getData().setScale(2);
            font.draw(batch, "Enter your name: " + playerName, Gdx.graphics.getWidth() / 2 - 200, Gdx.graphics.getHeight() / 2 - 100);
            if (isHighScoreEntered) {
                font.draw(batch, "Press space to return to the main menu", Gdx.graphics.getWidth() / 4  + 50, 50);
            }
        }
        batch.end();
    }

    public void update(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            isHighScoreEntered = true;
        }

        if (isHighScoreEntered && playerName.isEmpty() && Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)) {
            Gdx.input.getTextInput(new NameInputListener(), "Enter your name ", "", "");
        }

        if (isGameOver && Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            resetGameState();
            restartGame();
        }

        handleTextInput();
    }

    private void handleTextInput() {
        for (int i = Input.Keys.A; i <= Input.Keys.Z; i ++) {
            if(Gdx.input.isKeyJustPressed(i)) {
                playerName += Input.Keys.toString(i);
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE) && playerName.length() > 0) {
            playerName = playerName.substring(0, playerName.length() - 1);
        }
    }

    public void gameOver() {
        isGameOver = true;
    }

    private void restartGame() {
        screenSwitch.switchToMainMenu();
    }

    private class NameInputListener implements Input.TextInputListener {
        @Override
        public void input(String text) {
            playerName = text;
            isHighScoreEntered = true;
        }

        @Override
        public void canceled() {
            playerName = "";
            isHighScoreEntered = false;
        }
    }

    @Override
    public void resize(int width, int height) {

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
        font.dispose();
    }

    public void resetGameState() {
        isGameOver = false;
        isHighScoreEntered = false;
        playerName = "";
    }
}

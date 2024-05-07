package com.asteroid.game.screens;

import com.asteroid.game.Controllers.ScoreHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class GameOverScreen implements Screen {

    private TextInputField inputField;
    private boolean isGameOver;
    private boolean isHighScoreEntered;
    private String playerName;
    private BitmapFont font;
    private ScreenSwitch screenSwitch;
    private Stage stage;
    private SpriteBatch batch;
    private ScoreHandler scoreHandler;
    private Label playNameLabel;

    public GameOverScreen(ScreenSwitch screenSwitch, SpriteBatch batch, ScoreHandler scoreHandler) {
        this.screenSwitch = screenSwitch;
        this.scoreHandler = scoreHandler;
        this.stage = new Stage(new ScreenViewport());
        font = new BitmapFont();
        font.setColor(Color.RED);
        font.getData().setScale(2);
        this.batch = batch;
        inputField = new TextInputField("Enter your name: ");
        isGameOver = true;
        isHighScoreEntered = false;
        playerName="";
        playNameLabel = new Label("", new Label.LabelStyle(font, Color.RED));
        playNameLabel.setAlignment(Align.left);
        playNameLabel.setPosition(300,300);
        Gdx.input.setInputProcessor(stage);
        stage.addActor(inputField);
        inputField.setFocus();
    }


    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        update();
        stage.act(delta);
        stage.draw();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if(isGameOver) {
            batch.begin();
            font.draw(batch, "Score: " + scoreHandler.getScore(), Gdx.graphics.getWidth() /2 -150, Gdx.graphics.getHeight() /2 + 150);
            font.draw(batch, "Game Over!", Gdx.graphics.getWidth() / 2 - 100, Gdx.graphics.getHeight() / 2 + 50);
            font.draw(batch, "Name: " + inputField.getPlayerName(), Gdx.graphics.getWidth() /2 -150, Gdx.graphics.getHeight() / 2);
//            if(isHighScoreEntered) {
                font.draw(batch, "Press Space to return the the main menu",Gdx.graphics.getWidth() /4 + 50, 50);
//            }
            batch.end();
        }

    }

    public void update() {
        playNameLabel.setText("Name: " + inputField.getPlayerName());
        if (isHighScoreEntered && playerName.isEmpty() && Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)) {
            Gdx.input.getTextInput(new NameInputListener(), "Enter your name", "", "");
        }
        if (isGameOver && Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            restartGame();
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
        stage.getViewport().update(width,height,true);
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
}

package com.asteroid.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class GameOverScreen implements Screen {

    private boolean isGameOver;
    private boolean isHighScoreEntered;
    private String playerName;
    private BitmapFont font;
    private ScreenSwitch screenSwitch;
    private Stage stage;

    public GameOverScreen(ScreenSwitch screenSwitch, SpriteBatch batch) {
        this.screenSwitch = screenSwitch;
        font = new BitmapFont();
        font.setColor(Color.RED);
        font.getData().setScale(2);
        isGameOver = true;
        isHighScoreEntered = false;
        playerName="";
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if(isGameOver) {
            SpriteBatch batch = screenSwitch.batch;
            batch.begin();
            font.draw(batch, "Game Over", Gdx.graphics.getWidth() / 2 - 100, Gdx.graphics.getHeight() / 2 + 50);
            font.draw(batch, "Enter your name: " + playerName, Gdx.graphics.getWidth() /2 -150, Gdx.graphics.getHeight() / 2);
            if(isHighScoreEntered) {
                font.draw(batch, "Press Enter to return the the main menu",Gdx.graphics.getWidth() / 2 - 150, Gdx.graphics.getHeight() / 2 - 50);
            }
            batch.end();
        }
    }

    public void update() {
        if (isGameOver && Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            restartGame();
        }
        if (isHighScoreEntered && playerName.isEmpty() && !Gdx.input.isPeripheralAvailable(Input.Peripheral.OnscreenKeyboard)) {
            Gdx.input.getTextInput(new NameInputListener(), "Enter your name", "", "");
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
}

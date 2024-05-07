package com.asteroid.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

public class TextInputField extends Table {
    private TextField textField;
    private Label label;
    private String playerName = "";

    public TextInputField(String labelText) {
        super();
        BitmapFont font = new BitmapFont(Gdx.files.internal("Fonts/default.fnt"));
        font.getData().setScale(2f);

        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.RED);
        label = new Label(labelText, labelStyle);

        textField = new TextField("", new TextField.TextFieldStyle(font, Color.RED, null, null, null));
        this.defaults().padRight(10);


        add(label).align(Align.right);
        add(textField).width(200).align(Align.left);

        textField.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                playerName = textField.getText();
            }
        });

        textField.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ENTER) {
                    System.out.println("Player name : " + playerName);
                    return true;
                }
                return false;
            }
        });
    }

    public void setFocus() {
        textField.getStage().setKeyboardFocus(textField);
    }
    public String getPlayerName() {
        return playerName;
    }


    public String getText() {
        return textField.getText();
    }
};
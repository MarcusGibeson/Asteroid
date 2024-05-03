package com.asteroid.game.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class JetFireEffect {
    private final int numSegments = 5;
    private final float segmentLength = 2f;
    private final float segmentWidth = 2f;
    private final float fadeSpeed = 0.1f;

    private Color color;
    private float alpha;

    public JetFireEffect (Color color) {
        this.color = color;
        this.alpha = 1f; //Start with full opacity
    }

    //pretty much just drawing each segment of the fire effect, and then makes it fade off. Rotation keeps it aligned with ship while turning
    public void draw(ShapeRenderer shapeRenderer, Vector2 position, float rotation) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(color.r, color.g, color.b, alpha);

        for (int i = 0; i < numSegments; i++) {
            float segmentAlpha = alpha * (1 - i / (float) numSegments); //Calculate alpha for each segment
            shapeRenderer.rectLine(position.x, position.y,
                                    position.x - i * segmentLength * MathUtils.cosDeg(rotation),
                                    position.y - i * segmentWidth * MathUtils.sinDeg(rotation),
                                        segmentWidth);
            shapeRenderer.setColor(color.r, color.g, color.b, segmentAlpha);
        }
        shapeRenderer.end();
    }

    public void update(float delta) {
        alpha -= fadeSpeed * delta;
        if (alpha < 0) {
            alpha = 0;
        }
    }
}




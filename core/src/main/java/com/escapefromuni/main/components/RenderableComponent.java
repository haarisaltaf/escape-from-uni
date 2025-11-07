package com.escapefromuni.main.components;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public interface RenderableComponent extends BaseComponent {
    /**
     Handles Rendering.
     */
    public void render(SpriteBatch batch);
}

package com.escapefromuni.main.components;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * Renderable Components are for any gameObjects that want to render inside the
 * Game world. It gives a gameObject that extends this class access to the render()
 * function, which is called on all RenderableComponents every frame the Game needs
 * to draw something to the screen.
 */
public interface RenderableComponent extends BaseComponent {
    /**
     * This function is called every frame that the screen refreshes. gameObjects
     * that draw to the screen should include their functionality to render inside
     * this function. It is best practice to ensure that any graphics are rendered
     * at the gameObjects position, for example if you want a sprite that renders at
     * (600,350), render the sprite at the gameObjects position and then reposition
     * the gameObject to (600,350).
     * @param batch The batch of objects which will be rendered to the screen. Items
     *              added later will be shown on top of previous objects
     */
    public void render(SpriteBatch batch);
}

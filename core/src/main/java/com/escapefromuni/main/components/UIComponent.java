package com.escapefromuni.main.components;

import com.badlogic.gdx.math.Vector2;
import com.escapefromuni.main.GameObject;

/**
 * UIComponent extends RenderableComponent and is designed for gameObjects that
 * render at a fixed point on the screen on top of all other RenderableComponents
 * e.g the GUI.
 */
public interface UIComponent extends RenderableComponent{
    /**
     * Sets the anchor point which determines where on the screen this UI component
     * will snap to. The UI component will snap to this part of the screen regardless
     * of screen size or how the screen is scaled.
     * @param newScreenPosition the new anchor point that this UIComponent will snap to.
     *                          This is a Vector value ranging from (-1,-1) to (1,1) where (-1,-1)
     *                          is the absolute bottom left of the screen and (1,1) is the top right of the screen, and
     *                          The centre of the Screen is (0,0).
     */
    public void setRelativeScreenPosition(Vector2 newScreenPosition);
    /**
     * Gets the anchor point which determines where on the screen this UI component
     * will snap to. The UI component will snap to this part of the screen regardless
     * of screen size or how the screen is scaled. This is a Vector value ranging from
     * (-1,-1) to (1,1) where (-1,-1) is the absolute bottom left of the screen and (1,1)
     * is the top right of the screen, and The centre of the Screen is (0,0).
     */
    public Vector2 getRelativeScreenPosition();

    /**
     * This function is called by Game every frame before render() is called which positions this gameObject
     * position to a fixed point on the screen depending on the relative screen position
     * @param cameraPosition the position of the camera
     * @param screenSize the size of the game window.
     * @param zoom How zoomed in the camera is
     */
    public void positionOnScreen(Vector2 cameraPosition, Vector2 screenSize,float zoom);
}

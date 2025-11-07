package com.escapefromuni.main.components;

import com.badlogic.gdx.math.Vector2;
import com.escapefromuni.main.GameObject;

public interface UIComponent extends RenderableComponent{
    public void setRelativeScreenPosition(Vector2 newScreenPosition);
    public Vector2 getRelativeScreenPosition();
    public void positionOnScreen(Vector2 cameraPosition, Vector2 screenSize);
}

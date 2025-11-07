package com.escapefromuni.main.ui;

import com.badlogic.gdx.math.Vector2;
import com.escapefromuni.main.GameObject;
import com.escapefromuni.main.components.UIComponent;

public abstract class UIElement extends GameObject implements UIComponent {
    //The relative position on screen ranging from (-1,-1) (bottom left) to (1,1) (top right)
    Vector2 relativeScreenPosition;
    public UIElement (Vector2 relativeScreenPosition){
        this.relativeScreenPosition = relativeScreenPosition;
    }
    @Override
    public Vector2 getRelativeScreenPosition() {
        return relativeScreenPosition;
    }
    @Override
    public void setRelativeScreenPosition(Vector2 screenPosition) {
        this.relativeScreenPosition = screenPosition;
    }

    @Override
    public void positionOnScreen(Vector2 cameraPosition, Vector2 screenSize,float zoom) {
        position.set(cameraPosition.x + relativeScreenPosition.x * screenSize.x * 0.5f * zoom,
                cameraPosition.y + relativeScreenPosition.y * screenSize.y * 0.5f * zoom);
    }
}

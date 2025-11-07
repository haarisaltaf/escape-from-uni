package com.escapefromuni.main.components;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * The CameraController gameObject uses this component to update the cameras viewpoint
 * before any other objects are rendered. This will only be called by what
 * the Game determines is the 'active' camera, and only one camera can be
 * active at a time.
 */
public interface CameraComponent extends BaseComponent{
    /**
     * Updates the camera's projection to the next frame.
     * @param spriteBatch the spriteBatch to project to.
     */
    public void updateCamera(SpriteBatch spriteBatch);
    public Vector2 getCameraPosition();
    public OrthographicCamera getCamera();
}

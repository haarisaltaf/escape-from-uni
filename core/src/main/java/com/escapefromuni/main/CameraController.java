package com.escapefromuni.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.escapefromuni.main.components.CameraComponent;
import com.escapefromuni.main.components.RenderableComponent;

/**
 * A gameGbject which makes the camera which follows a given target.
 */
public class CameraController extends GameObject implements CameraComponent {
    OrthographicCamera camera;
    GameObject target;
    public void SetTarget(GameObject target) {
        this.target = target;
    }
    public OrthographicCamera GetCamera()
    {
        return camera;
    }
    @Override
    public void start() {
        super.start();
        camera = new OrthographicCamera();
        camera.setToOrtho(false,1080,720);
        camera.position.set(0, 0, 0);
        camera.update();
    }
    @Override
    public void update(float delta) {
        position.set(target.position);
    }

    @Override
    public void updateCamera(SpriteBatch batch) {
        camera.position.set(position.x,position.y,0);
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            System.out.println("zoom in");
            camera.zoom += 0.02f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.E)) {
            camera.zoom -= 0.02f;
        }
        camera.update();
        batch.setProjectionMatrix(camera.combined);
    }

    @Override
    public Vector2 getCameraPosition() {
        return position;
    }

    @Override
    public OrthographicCamera getCamera() {
        return camera;
    }

    public CameraController (GameObject target) {
        this.target = target;
    }
}

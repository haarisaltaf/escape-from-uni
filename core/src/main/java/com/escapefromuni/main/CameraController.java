package com.escapefromuni.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.escapefromuni.main.components.RenderableComponent;

public class CameraController extends GameObject implements RenderableComponent {
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
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            System.out.println("zoom in");
            camera.zoom += 0.02f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.E)) {
            camera.zoom -= 0.02f;
        }
    }

    @Override
    public void render(SpriteBatch batch, Vector2 cameraPosition) {
        camera.position.set(target.position.x,target.position.y,0);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
    }
    public CameraController (GameObject target) {
        this.target = target;
    }
}

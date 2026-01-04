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
    
    // Variables to control nausea effect
    float nauseaTimer = 0f;
    private float amplitude = 20f; // How much the camera moves
    private float frequency = 5f; // How fast it oscillates

    // Variable to control zoom using Binoculars item
    int numOfBinoculars = 0;

    /**
     * Set's the target gameObject that this CameraController should track.
     * @param target the gameObject to follow.
     */
    public void SetTarget(GameObject target) {
        this.target = target;
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
        // Camera zooms out when binoculars are picked up
        camera.zoom += numOfBinoculars * 0.1;

        // Nausea Effect
        if (nauseaTimer > 0) {
        nauseaTimer -= delta;
        // Uses Sine wave to oscillate x position
        float sway = (float) Math.sin(nauseaTimer * frequency) * amplitude;
        position.x += sway;
        // Uses Cosine wave to oscillate y position
        float bob = (float) Math.cos(nauseaTimer * frequency * 2) * (amplitude / 2);
        position.y += bob;
        // Camera Rotation for nausea
        float rotation = (float) Math.sin(nauseaTimer * frequency * 0.5) * 2f;
        camera.up.set(0, 1, 0);
        camera.rotate(rotation);
        // Camera zoom for nausea
        float zoom = (float) Math.sin(nauseaTimer * frequency/2 + 45) / 10;
        camera.zoom += zoom;
        }
    }

    @Override
    public void updateCamera(SpriteBatch batch) {
        camera.position.set(position.x,position.y,0);
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
    public void nausea() {
        nauseaTimer = 10f;
    }
    
    public void binoculars() {
        numOfBinoculars += 1;
    }
}

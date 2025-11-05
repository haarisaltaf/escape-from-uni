package com.escapefromuni.main;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.escapefromuni.main.components.RenderableComponent;

public class GameMap extends GameObject implements RenderableComponent {
    TiledMap TestMap;
    OrthogonalTiledMapRenderer renderer;
    OrthographicCamera camera;

    /**
     * Create a renderer object to load the map.
     * Utilises the game camera created at the start of the program
     */
    public GameMap(CameraController cameraController){
        TestMap = new TmxMapLoader().load("TestMap.tmx");
        renderer = new OrthogonalTiledMapRenderer(TestMap,2.25f);
        camera = cameraController.GetCamera();
        renderer.setView(camera);
    }

    /**
     * Simple method to dispose of the renderer at the end of the program
     */
    @Override
    public void dispose(){
        renderer.dispose();
    }

    @Override
    public void render(SpriteBatch batch, Vector2 cameraPosition) {
        renderer.setView(camera);
        renderer.render();
    }
}

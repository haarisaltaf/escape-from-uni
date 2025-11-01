package com.escapefromuni.main;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class GameMap {
    TiledMap TestMap;
    OrthogonalTiledMapRenderer renderer;

    /**
     * Create a renderer object to load the map.
     * Utilises the game camera created at the start of the program
     */
    public GameMap(OrthographicCamera GameCamera){
        TestMap = new TmxMapLoader().load("TestMap.tmx");
        renderer = new OrthogonalTiledMapRenderer(TestMap,2.25f);
        renderer.setView(GameCamera);
    }

    /**
     * Simple method used to call for the map to render.
     */
    public void RenderMap(){
        renderer.render();
    }

    /**
     * Simple method to dispose of the renderer at the end of the program
     */
    public void dispose(){
        renderer.dispose();
    }

}

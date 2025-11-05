package com.escapefromuni.main;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.escapefromuni.main.components.RenderableComponent;
import java.util.ArrayList;
import java.util.List;

public class GameMap extends GameObject implements RenderableComponent {
    TiledMap TestMap;
    OrthogonalTiledMapRenderer renderer;
    OrthographicCamera camera;
    ArrayList<Rectangle> CollisonMap = new ArrayList<>(150);

    /**
     * Create a renderer object to load the map.
     * Utilises the game camera created at the start of the program
     */
    public GameMap(CameraController cameraController){
        TestMap = new TmxMapLoader().load("TestMap.tmx");
        generateCollisionMap();
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

    public void generateCollisionMap(){
        TiledMapTileLayer collisionLayer = (TiledMapTileLayer)this.TestMap.getLayers().get("Collision");
        for(int x = 0; x <15 ; x++){
            for(int y = 0; y<10; y++){
                TiledMapTileLayer.Cell cell = collisionLayer.getCell(x,y);
                //Ignore blank (invalid) tiles
                if (cell == null) continue;
                // The tile indicating the collision map has ID = 5
                if(cell.getTile().getId() == 5){
                    //todo: find out why you have to add offset
                    CollisonMap.add(new Rectangle(x*72f,y*72f ,72f,72f));
                }
            }
        }
    }

}

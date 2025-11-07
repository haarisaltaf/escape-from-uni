package com.escapefromuni.main;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.escapefromuni.main.components.CameraComponent;
import com.escapefromuni.main.components.CollisionComponent;
import com.escapefromuni.main.components.RenderableComponent;
import java.util.ArrayList;

public class GameMap extends GameObject implements RenderableComponent, CollisionComponent {
    TiledMap TestMap;
    OrthogonalTiledMapRenderer renderer;
    CameraComponent cameraComponent;
    ArrayList<Rectangle> CollisionMap = new ArrayList<>(150);

    /**
     * Create a renderer object to load the map.
     * Utilises the game camera created at the start of the program
     */
    public GameMap(CameraComponent cameraComponent){
        TestMap = new TmxMapLoader().load("TestMap.tmx");
        generateCollisionMap();
        renderer = new OrthogonalTiledMapRenderer(TestMap,4f);
        this.cameraComponent = cameraComponent;
        renderer.setView(cameraComponent.getCamera());
    }

    /**
     * Simple method to dispose of the renderer at the end of the program
     */
    @Override
    public void dispose(){
        renderer.dispose();
    }

    @Override
    public void render(SpriteBatch batch) {
        renderer.setView(cameraComponent.getCamera());
        renderer.render();
    }

    public void generateCollisionMap(){
        TiledMapTileLayer collisionLayer = (TiledMapTileLayer)this.TestMap.getLayers().get("Collision");
        for(int x = 0; x <collisionLayer.getWidth() ; x++){
            for(int y = 0; y<collisionLayer.getHeight(); y++){
                TiledMapTileLayer.Cell cell = collisionLayer.getCell(x,y);
                //Ignore blank (invalid) tiles
                if (cell == null) continue;
                // The tile indicating the collision map has ID = 5
                if(cell.getTile().getId() == 1){
                    CollisionMap.add(new Rectangle(x*128,y*128 ,128,128));
                }
            }
        }
    }

    @Override
    public CollisionLayer getCollisionLayer() {
        return CollisionLayer.WALL;
    }

    @Override
    public Boolean isCollidingWith(Rectangle hitboxCheck) {
        for(Rectangle rect : CollisionMap) {
            if (rect.overlaps(hitboxCheck)){
                return true;
            }
        }
        return false;
    }

    @Override
    public Boolean isCollisionEnabled() {
        return true;
    }
}

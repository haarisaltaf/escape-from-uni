package com.escapefromuni.main;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.escapefromuni.main.components.CameraComponent;
import com.escapefromuni.main.components.CollisionComponent;
import com.escapefromuni.main.components.RenderableComponent;
import java.util.ArrayList;

public class GameMap extends GameObject implements RenderableComponent, CollisionComponent {
    public static TiledMap TestMap;
    OrthogonalTiledMapRenderer renderer;
    CameraComponent cameraComponent;
    public static ArrayList<Rectangle> CollisionMap = new ArrayList<>(150);
    public static boolean KeyWallExists = true;

    /**
     * Create a renderer object to load the map.
     * Utilises the game camera created at the start of the program
     */
    public GameMap(CameraComponent cameraComponent) {
        TestMap = new TmxMapLoader().load("TestMap.tmx");

        addCollisionTileAtWorld(TestMap, 3492f, 1054f, 34, 4f, CollisionMap);
        addCollisionTileAtWorld(TestMap, 3620f, 1054f, 34, 4f, CollisionMap);
        addCollisionTileAtWorld(TestMap, 3748f, 1054f, 34, 4f, CollisionMap);
        addCollisionTileAtWorld(TestMap, 3876f, 1054f, 34, 4f, CollisionMap);
        generateCollisionMap();
        renderer = new OrthogonalTiledMapRenderer(TestMap, 4f);
        this.cameraComponent = cameraComponent;
        renderer.setView(cameraComponent.getCamera());
    }

    /**
     * Simple method to dispose of the renderer at the end of the program
     */
    @Override
    public void dispose() {
        renderer.dispose();
    }

    @Override
    public void render(SpriteBatch batch) {
        renderer.setView(cameraComponent.getCamera());
        renderer.render();
    }

    /**
     * Generates the collision that map uses for e.g. walls.
     * Tiles in the "Collision" Tiled layer with ID 1 are considered
     * 'Collision Tiles' with a square hitbox overlapping the entire tile
     */
    public void generateCollisionMap() {
        TiledMapTileLayer collisionLayer = (TiledMapTileLayer) this.TestMap.getLayers().get("Collision");
        for (int x = 0; x < collisionLayer.getWidth(); x++) {
            for (int y = 0; y < collisionLayer.getHeight(); y++) {
                TiledMapTileLayer.Cell cell = collisionLayer.getCell(x, y);
                // Ignore blank (invalid) tiles
                if (cell == null)
                    continue;
                // The tile indicating the collision map has ID = 5
                System.out.println(cell.getTile().getId());
                if (cell.getTile().getId() == 1) {
                    System.out.println("Adding new rect");
                    CollisionMap.add(new Rectangle(x * 128, y * 128, 128, 128));
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
        for (Rectangle rect : CollisionMap) {
            if (rect.overlaps(hitboxCheck)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Boolean isCollisionEnabled() {
        return true;
    }

    public static void addCollisionTileAtWorld(
            TiledMap map,
            float worldX,
            float worldY,
            int tileIdInWallsTileset,
            float renderScale,
            ArrayList<Rectangle> collisionMap) {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get("Map");
        if (layer == null) {
            System.out.println("Layer 'Map' NOT FOUND");
            return;
        }

        float tw = layer.getTileWidth();
        float th = layer.getTileHeight();

        int tileX = (int) (worldX / (tw * renderScale));
        int tileY = (int) (worldY / (th * renderScale));

        if (tileX < 0 || tileY < 0 || tileX >= layer.getWidth() || tileY >= layer.getHeight()) {
            System.out.println("OUT OF BOUNDS");
            return;
        }

        TiledMapTileSet walls = map.getTileSets().getTileSet("Walls");
        if (walls == null) {
            System.out.println("Tileset 'Walls' NOT FOUND");
            return;
        }

        TiledMapTile tile = walls.getTile(tileIdInWallsTileset);
        if (tile == null) {
            System.out.println("No tile found in 'Walls' for id=" + tileIdInWallsTileset);
            return;
        }

        TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
        cell.setTile(tile);
        layer.setCell(tileX, tileY, cell);

        TiledMapTileLayer collisionLayer = (TiledMapTileLayer) map.getLayers().get("Collision");

        if (collisionLayer == null) {
            System.out.println("Collision layer NOT FOUND");
            return;
        }

        TiledMapTileSet collisionTileset = map.getTileSets().getTileSet("Collision");

        if (collisionTileset == null) {
            System.out.println("Collision tileset NOT FOUND");
            return;
        }

        TiledMapTile collisionTile = collisionTileset.getTile(1);

        if (collisionTile == null) {
            System.out.println("Collision tile ID 1 NOT FOUND");
            return;
        }

        TiledMapTileLayer.Cell collisionCell = new TiledMapTileLayer.Cell();
        collisionCell.setTile(collisionTile);

        collisionLayer.setCell(tileX, tileY, collisionCell);

        var rect = new Rectangle(tileX * 128, tileY * 128, 128, 128);
        collisionMap.add(rect);

        System.out.println("Placed tile + collision at " + tileX + "," + tileY);
    }

    public static boolean isPlayerNearAnyPlacedWall(
            Rectangle playerHitbox,
            float nearDistance) {

        // HARD-CODED WALL RECTS
        Rectangle[] placedWallRects = new Rectangle[] {
                new Rectangle(3492f, 1054f, 128f, 128f),
                new Rectangle(3620f, 1054f, 128f, 128f),
                new Rectangle(3748f, 1054f, 128f, 128f),
                new Rectangle(3876f, 1054f, 128f, 128f)
        };

        Rectangle expanded = new Rectangle();

        for (Rectangle wall : placedWallRects) {
            expanded.set(
                    wall.x - nearDistance,
                    wall.y - nearDistance,
                    wall.width + nearDistance * 2f,
                    wall.height + nearDistance * 2f);

            if (expanded.overlaps(playerHitbox)) {
                return true;
            }
        }

        return false;
    }

    public static void removeHardcodedPlacedWalls() {
        if (TestMap == null) {
            return;
        }

        float[][] POS = {
                { 3492f, 1054f },
                { 3620f, 1054f },
                { 3748f, 1054f },
                { 3876f, 1054f }
        };

        TiledMapTileLayer mapLayer = (TiledMapTileLayer) TestMap.getLayers().get("Map");
        TiledMapTileLayer collisionLayer = (TiledMapTileLayer) TestMap.getLayers().get("Collision");
        if (mapLayer == null || collisionLayer == null) {
            return;
        }

        float tw = mapLayer.getTileWidth();
        float th = mapLayer.getTileHeight();

        for (float[] p : POS) {
            float worldX = p[0], worldY = p[1];

            int tileX = (int) (worldX / (tw * 4f));
            int tileY = (int) (worldY / (th * 4f));

            // remove cells
            mapLayer.setCell(tileX, tileY, null);
            collisionLayer.setCell(tileX, tileY, null);

            // remove rects matching this tile
            float rx = tileX * tw * 4f;
            float ry = tileY * th * 4f;
            float rw = tw * 4f;
            float rh = th * 4f;

            CollisionMap.removeIf(r -> r.x == rx && r.y == ry && r.width == rw && r.height == rh);
        }

        KeyWallExists = false;
    }
    
    public static boolean DoesKeyWallExists() {
        return KeyWallExists;
    }

}

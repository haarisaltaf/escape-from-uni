package com.escapefromuni.main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import java.util.List;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    Player player;
    GameMap map;
    OrthographicCamera GameCamera;
    //TODO: move player into this list (and all future GameObjects)
    List<GameObject> gameObjects;
    //TODO: add separate list of GameObjects with renderable component etc.
    @Override
    public void create() {
        batch = new SpriteBatch();
        // Define a camera which the game will be seen through. This is currently centered around (0,0)
        GameCamera = new OrthographicCamera();
        GameCamera.setToOrtho(false,540,360);
        // The TileMap provided requires a camera to function.
        map = new GameMap(GameCamera);
        //TODO: Iterate through every GameObject and run the start() function on them
        player = new Player(new Vector2(0,0));
        player.start();
    }

    @Override
    public void render() {
        //TODO: Iterate through every GameObject with the RenderableComponent and run render()
        //TODO: Camera system / Camera position
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        batch.begin();
        map.RenderMap();
        player.update(Gdx.graphics.getDeltaTime());
        if(Gdx.input.isKeyPressed(Input.Keys.F)){
            player.speedUp();
        }
        //TODO: Change cameraPosition to a GameObject with a CameraComponent to control easier
        Vector2 cameraPosition = new Vector2(-Gdx.graphics.getWidth() / 2f,-Gdx.graphics.getHeight() / 2f);
        player.render(batch,cameraPosition);
        batch.end();
    }

    @Override
    public void dispose() {
        //TODO: run dispose() on every GameObject in gameObjects
        player.dispose();
        batch.dispose();
        map.dispose();
    }
}

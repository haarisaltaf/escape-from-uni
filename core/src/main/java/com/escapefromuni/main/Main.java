package com.escapefromuni.main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.escapefromuni.main.components.RenderableComponent;

import java.util.ArrayList;
import java.util.List;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    Sprite titleImage;
    Sprite playButton;
    //The camera which is active and rendering the scene
    CameraController activeCamera;
    String gameState = "title";
    List<GameObject> gameObjects;
    List<RenderableComponent> renderableComponents;
    GameMap Map;
    @Override
    public void create() {
        batch = new SpriteBatch();
        gameObjects = new ArrayList<GameObject>();
        renderableComponents = new ArrayList<RenderableComponent>();
        //Create the Main Menu UI and position it
        titleImage = new Sprite(new Texture(Gdx.files.internal("titleGraphic.png")));
        titleImage.setPosition((Gdx.graphics.getWidth() - titleImage.getTexture().getWidth()) / 2f, (Gdx.graphics.getHeight() * 1.5f - titleImage.getTexture().getHeight()) / 2f);
        playButton = new Sprite(new Texture(Gdx.files.internal("playButton.png")));
        playButton.setPosition((Gdx.graphics.getWidth() - playButton.getTexture().getWidth()) / 2f, (Gdx.graphics.getHeight() / 2f - playButton.getTexture().getHeight()) / 2f);
        //Create the Game World:
        //Define a camera and add it to GameObjects
        activeCamera = new CameraController(null);
        addGameObject(activeCamera);

        //Add the player
        var player = new Player(new Vector2(100,100));
        addGameObject(player);
        activeCamera.SetTarget(player);
        //Add the Map
        Map = new GameMap(activeCamera);
        addGameObject(new GameMap(activeCamera));
        addGameObject(new GameTimer(player));
        addGameObject(new Collectible(new Vector2(200,200),player));
        addGameObject(new Collectible(new Vector2(400,200),player));
    }

    @Override
    public void render() {
        if (gameState.equals("title")) {
            //TODO: hardcoded to default screen size, fix later
            if (Gdx.input.getX() >= 340 && Gdx.input.getX() <= 740 && 
                Gdx.input.getY() >= 440 && Gdx.input.getY() <= 640 && 
                Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                    gameState = "main";
            }
            ScreenUtils.clear(1f, 1f, 1f, 1f);
            batch.begin();
            titleImage.draw(batch);
            playButton.draw(batch);
            batch.end();
        }
        else if (gameState.equals("main")) {
            ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
            //cache the delta time
            float deltaTime = Gdx.graphics.getDeltaTime();
            //Update all gameObjects
            for (var gameObject : gameObjects) {
                if(gameObject.getClass().equals(Player.class)){
                    ((Player) gameObject).update(deltaTime,Map);
                }else{
                    gameObject.update(deltaTime);
                }

            }
            //set view to active camera
            batch.setProjectionMatrix(activeCamera.GetCamera().combined);
            //For all gameObjects that render, render them
            batch.begin();
            for (var renderable : renderableComponents) {
                //todo cameraPosition is obsolete, which is why its (0,0)
                renderable.render(batch,new Vector2(0,0));
            }
            batch.end();
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        //dispose every gameObject
        for (var gameObject : gameObjects) {
            gameObject.dispose();
        }
    }

    /**
     * Adds a gameObject to the game world and runs the start() procedure.
     * @param gameObject The gameobject to add.
     * @return the gameObject added if successful, null if not.
     */
    //todo: return null if fail
    public GameObject addGameObject(GameObject gameObject){
        gameObjects.add(gameObject);
        //If the object has a renderableComponent, then add it to the list of renderableComponents
        if (gameObject instanceof RenderableComponent) {
            renderableComponents.add((RenderableComponent) gameObject);
        }
        gameObject.start();
        return gameObject;
    }

}

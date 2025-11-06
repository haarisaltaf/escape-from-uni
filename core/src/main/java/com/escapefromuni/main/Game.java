package com.escapefromuni.main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.escapefromuni.main.components.CollisionComponent;
import com.escapefromuni.main.components.RenderableComponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Game extends ApplicationAdapter {
    private SpriteBatch batch;
    Sprite titleImage;
    Sprite playButton;
    Sprite pausedSprite;
    //The camera which is active and rendering the scene
    static CameraController activeCamera;
    static String gameState = "title";
    static List<GameObject> gameObjects;
    static List<RenderableComponent> renderableComponents;
    static HashMap<CollisionComponent.CollisionLayer,List<CollisionComponent>> collidingComponents;
    GameMap Map;
    Player player;
    @Override
    public void create() {
        batch = new SpriteBatch();
        gameObjects = new ArrayList<>();
        renderableComponents = new ArrayList<>();
        collidingComponents = new HashMap<>();
        //Create the Main Menu UI and position it
        titleImage = new Sprite(new Texture(Gdx.files.internal("titleGraphic.png")));
        titleImage.setPosition((Gdx.graphics.getWidth() - titleImage.getTexture().getWidth()) / 2f, (Gdx.graphics.getHeight() * 1.5f - titleImage.getTexture().getHeight()) / 2f);
        playButton = new Sprite(new Texture(Gdx.files.internal("playButton.png")));
        playButton.setPosition((Gdx.graphics.getWidth() - playButton.getTexture().getWidth()) / 2f, (Gdx.graphics.getHeight() / 2f - playButton.getTexture().getHeight()) / 2f);
        pausedSprite = new Sprite(new Texture(Gdx.files.internal("pausedGraphic.png")));
        pausedSprite.setPosition((Gdx.graphics.getWidth() - pausedSprite.getTexture().getWidth()) / 2f, (Gdx.graphics.getHeight() * 1.5f - pausedSprite.getTexture().getHeight()) / 2f);
        //Create the Game World:
        //Define a camera and add it to GameObjects
        activeCamera = new CameraController(null);
        addGameObject(activeCamera);
        //Add the player
        player = new Player(new Vector2(100,100));
        addGameObject(player);
        activeCamera.SetTarget(player);
        //Add the Map
        Map = new GameMap(activeCamera);
        addGameObject(new GameMap(activeCamera));
        addGameObject(new GameTimer(player));
        addGameObject(new SpeedCollectible(new Vector2(200,200)));
        addGameObject(new SpeedCollectible(new Vector2(400,200)));
        addGameObject(new NPC(new Vector2(800,600)));
        addGameObject(new Key(new Vector2(1600,600)));
    }

    @Override
    public void render() {
        if (gameState.equals("title")) {
            //Detect clicking on the play button
            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                if (Gdx.input.getX() >= playButton.getX() && Gdx.input.getX() <= (playButton.getX() + playButton.getWidth())
                &&  Gdx.input.getY() >= (Gdx.graphics.getHeight() - (playButton.getY() + playButton.getHeight())) && Gdx.input.getY() <= (Gdx.graphics.getHeight() - playButton.getY())) {
                    gameState = "main";
                }
            }
            ScreenUtils.clear(1f, 1f, 1f, 1f);
            batch.begin();
            titleImage.draw(batch);
            playButton.draw(batch);
            batch.end();
        }
        else if (gameState.equals("main")) {
            if (Gdx.input.isKeyJustPressed(111)) {
                gameState = "paused";
            }
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
        else if (gameState.equals("paused")) {
            if (Gdx.input.isKeyJustPressed(111)) {
                gameState = "main";
            } 
            ScreenUtils.clear(1f, 1f, 1f, 1f);
            batch.begin();
            pausedSprite.setPosition(player.position.x - 200,player.position.y);
            pausedSprite.draw(batch);
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
    public static Boolean isCollidingWithLayer(Rectangle hitbox, CollisionComponent.CollisionLayer type){
        if (!collidingComponents.containsKey(type)) return false;
        for(CollisionComponent collider : collidingComponents.get(type)){
            if (!collider.getCollisionLayer().equals(type)) throw new IllegalArgumentException("Layer cannot change after added to game.");
            if (collider.isCollisionEnabled() && collider.isCollidingWith(hitbox)){
                return true;
            }
        }
        return false;
    }
    public static ArrayList<GameObject> getAllCollidingObjects(Rectangle hitbox, CollisionComponent.CollisionLayer type){
        if (!collidingComponents.containsKey(type)) return null;
        ArrayList<GameObject> overlappingGameObjects = new ArrayList<>();
        for(CollisionComponent collider : collidingComponents.get(type)){
            if (!collider.getCollisionLayer().equals(type)) throw new IllegalArgumentException("Layer cannot change after added to game.");
            if (collider.isCollisionEnabled() && collider.isCollidingWith(hitbox)){
                overlappingGameObjects.add((GameObject) collider);
            }
        }
        return overlappingGameObjects;
    }
    public static GameObject getFirstCollidingObjectInLayer(Rectangle hitbox, CollisionComponent.CollisionLayer type){
        if (!collidingComponents.containsKey(type)) return null;
        for(CollisionComponent collider : collidingComponents.get(type)){
            if (!collider.getCollisionLayer().equals(type)) throw new IllegalArgumentException("Layer cannot change after added to game.");
            if (collider.isCollisionEnabled() && collider.isCollidingWith(hitbox)){
                return (GameObject) collider;
            }
        }
        return null;
    }
    /**
     * Adds a gameObject to the game world and runs the start() procedure.
     * @param gameObject The gameObject to add.
     * @return the gameObject added if successful, null if not.
     */
    //todo: return null if fail
    public GameObject addGameObject(GameObject gameObject){
        gameObjects.add(gameObject);
        //If the object has a renderableComponent, then add it to the list of renderableComponents
        if (gameObject instanceof RenderableComponent renderObject) {
            renderableComponents.add(renderObject);
        }
        //If the object has a collisionComponent, then add it to the relevant dictionary of colliders depending on the objects layer
        if (gameObject instanceof CollisionComponent collisionObject){
            var layer = collisionObject.getCollisionLayer();
            if (!collidingComponents.containsKey(layer)){
                collidingComponents.put(layer,new ArrayList<>());
            }
            collidingComponents.get(layer).add(collisionObject);
        }
        gameObject.start();
        return gameObject;
    }

}

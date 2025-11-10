package com.escapefromuni.main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.escapefromuni.main.collectables.Key;
import com.escapefromuni.main.collectables.SpeedCollectable;
import com.escapefromuni.main.components.CameraComponent;
import com.escapefromuni.main.components.CollisionComponent;
import com.escapefromuni.main.components.RenderableComponent;
import com.escapefromuni.main.components.UIComponent;
import com.escapefromuni.main.ui.GameMessageHandler;
import com.escapefromuni.main.ui.GameTimer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Game extends ApplicationAdapter {
    private SpriteBatch batch;
    Sprite titleImage;
    Sprite playButton;
    Sprite controlsHelp;
    Sprite pausedSprite;
    Sprite quitButton;
    Sprite winImage;
    Sprite loseImage;
    Music music;
    static GameTimer timer;
    //The camera which is active and rendering the scene
    private static CameraComponent activeCamera;
    public static GameState gameState = GameState.MENU;
    static List<GameObject> gameObjects = new ArrayList<>();
    static List<RenderableComponent> renderableComponents = new ArrayList<>();
    static List<UIComponent> uiComponents = new ArrayList<>();
    static HashMap<CollisionComponent.CollisionLayer, List<CollisionComponent>> collidingComponents = new HashMap<>();

    @Override
    public void create() {
        batch = new SpriteBatch();
        //Create the Main Menu UI and position it
        titleImage = new Sprite(new Texture(Gdx.files.internal("titleGraphic.png")));
        titleImage.setPosition((Gdx.graphics.getWidth() - titleImage.getTexture().getWidth()) / 2f, (Gdx.graphics.getHeight() * 1.5f - titleImage.getTexture().getHeight()) / 2f);
        playButton = new Sprite(new Texture(Gdx.files.internal("playButton.png")));
        playButton.setPosition((Gdx.graphics.getWidth() - playButton.getTexture().getWidth()) / 2f, (Gdx.graphics.getHeight() / 2f - playButton.getTexture().getHeight()) / 2f);
        controlsHelp = new Sprite(new Texture(Gdx.files.internal("controlsHelp.png")));
        controlsHelp.setPosition((Gdx.graphics.getWidth() - controlsHelp.getTexture().getWidth()) / 2f, (Gdx.graphics.getHeight() - controlsHelp.getTexture().getHeight()) / 2f + 50);
        pausedSprite = new Sprite(new Texture(Gdx.files.internal("pausedGraphic.png")));
        pausedSprite.setPosition((Gdx.graphics.getWidth() - pausedSprite.getTexture().getWidth()) / 2f, (Gdx.graphics.getHeight() * 1.5f - pausedSprite.getTexture().getHeight()) / 2f);
        quitButton = new Sprite(new Texture(Gdx.files.internal("quitButton.png")));
        quitButton.setPosition((Gdx.graphics.getWidth() - quitButton.getTexture().getWidth()) / 2f, (Gdx.graphics.getHeight() / 2f - quitButton.getTexture().getHeight()) / 2f);
        winImage = new Sprite(new Texture(Gdx.files.internal("winImage.png")));
        winImage.setPosition((Gdx.graphics.getWidth() - winImage.getTexture().getWidth()) / 2f, (Gdx.graphics.getHeight() * 1.5f - winImage.getTexture().getHeight()) / 2f + 60);
        loseImage = new Sprite(new Texture(Gdx.files.internal("loseImage.png")));
        loseImage.setPosition((Gdx.graphics.getWidth() - loseImage.getTexture().getWidth()) / 2f, (Gdx.graphics.getHeight() * 1.5f - loseImage.getTexture().getHeight()) / 2f + 60);
        //Create the Game World:
        //Define a camera and add it to GameObjects
        var camera = new CameraController(null);
        addGameObject(camera);
        SetActiveCamera(camera);
        //Add the player
        var player = new Player(new Vector2(256, 256));
        addGameObject(player);
        camera.SetTarget(player);
        //Add the Map
        addGameObject(new GameMap(camera));
        addGameObject(new GameTimer(new Vector2(-0.9f, 0.9f)));
        addGameObject(new GameMessageHandler(new Vector2(0,0.2f)));
        addGameObject(new SpeedCollectable(new Vector2(400, 400)));
        addGameObject(new SpeedCollectable(new Vector2(800, 200)));
        addGameObject(new SpeedCollectable(new Vector2(1200, 200)));
        addGameObject(new SpeedCollectable(new Vector2(1600, 200)));
        addGameObject(new SpeedCollectable(new Vector2(2000, 200)));
        addGameObject(new SpeedCollectable(new Vector2(2400, 200)));
        addGameObject(new SpeedCollectable(new Vector2(2800, 200)));
        addGameObject(new NPC(new Vector2(1400, 1000),"npc1.png","key"));
        addGameObject(new Key(new Vector2(2000, 1000)));
        addGameObject(new Key(new Vector2(2300, 1000)));
        addGameObject(new Key(new Vector2(2600, 1000)));
        addGameObject(new Key(new Vector2(2900, 1000)));

        //Set up music
        music = Gdx.audio.newMusic(Gdx.files.internal("Music/Dungeon.wav"));
        music.setLooping(true);
        music.setVolume(0.5f);
        music.play();
    }

    @Override
    public void render() {
        switch (gameState) {
            case MENU:
                activeCamera.getCamera().zoom = 1;
                activeCamera.getCamera().position.set(Gdx.graphics.getWidth()/2f,Gdx.graphics.getHeight()/2f,0);
                activeCamera.getCamera().update();
                batch.setProjectionMatrix(activeCamera.getCamera().combined);
                //TODO: Change these to UIElements
                //Detect clicking on the play button
                if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                    if (Gdx.input.getX() >= playButton.getX() && Gdx.input.getX() <= (playButton.getX() + playButton.getWidth())
                    &&  Gdx.input.getY() >= (Gdx.graphics.getHeight() - (playButton.getY() + playButton.getHeight())) && Gdx.input.getY() <= (Gdx.graphics.getHeight() - playButton.getY())) {
                        gameState = GameState.PLAYING;
                        GameMessageHandler.ShowMessage("WASD to move\nESC to pause",3);
                    }
                }
                ScreenUtils.clear(1f, 1f, 1f, 1f);
                batch.begin();
                titleImage.draw(batch);
                playButton.draw(batch);
                controlsHelp.draw(batch);
                batch.end();
                break;
            case PLAYING:

                if (Gdx.input.isKeyJustPressed(111)) {
                    gameState = GameState.PAUSED;
                }
                ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
                //cache the delta time
                float deltaTime = Gdx.graphics.getDeltaTime();
                //Update all gameObjects
                for (var gameObject : gameObjects) {
                    gameObject.update(deltaTime);
                }
                //set view to active camera
                //batch.setProjectionMatrix(activeCamera.GetCamera().combined);
                activeCamera.updateCamera(batch);
                //For all gameObjects that render, render them
                batch.begin();
                for (var renderable : renderableComponents) {
                    renderable.render(batch);
                }
                //Render the UI components after the rest so they are on top
                Vector2 screenSize = new Vector2(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
                for(var ui : uiComponents){
                    ui.positionOnScreen(activeCamera.getCameraPosition(),screenSize,activeCamera.getCamera().zoom);
                    ui.render(batch);
                }
                batch.end();
                break;
            case PAUSED:
                //If escape key pressed
                if (Gdx.input.isKeyJustPressed(111)) {
                    gameState = GameState.PLAYING;
                }
                if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                    System.out.println(Gdx.input.getY());
                    //Super hardcoded, MUST adjust if screen resized
                    if (Gdx.input.getX() >= 340 && Gdx.input.getX() <= 740
                    &&  Gdx.input.getY() >= 410 && Gdx.input.getY() <= 610) {
                        Gdx.app.exit();
                    }
                }
                ScreenUtils.clear(1f, 1f, 1f, 1f);
                batch.begin();
                //TODO: Change these to UIElements
                pausedSprite.setPosition(activeCamera.getCameraPosition().x - 200, activeCamera.getCameraPosition().y + 50);
                pausedSprite.draw(batch);
                quitButton.setPosition(activeCamera.getCameraPosition().x - 200, activeCamera.getCameraPosition().y - 250);
                quitButton.draw(batch);
                batch.end();
                break;
            case WIN:
                if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                    System.out.println(Gdx.input.getY());
                    //Super hardcoded, MUST adjust if screen resized
                    if (Gdx.input.getX() >= 340 && Gdx.input.getX() <= 740
                    &&  Gdx.input.getY() >= 440 && Gdx.input.getY() <= 640) {
                        Gdx.app.exit();
                    }
                }
                activeCamera.getCamera().zoom = 1;
                activeCamera.getCamera().position.set(Gdx.graphics.getWidth()/2f,Gdx.graphics.getHeight()/2f,0);
                activeCamera.getCamera().update();
                batch.setProjectionMatrix(activeCamera.getCamera().combined);
                ScreenUtils.clear(1f, 1f, 1f, 1f);
                batch.begin();
                winImage.draw(batch);
                quitButton.draw(batch);
                batch.end();
                break;
            case LOSE:
                if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                    System.out.println(Gdx.input.getY());
                    //Super hardcoded, MUST adjust if screen resized
                    if (Gdx.input.getX() >= 340 && Gdx.input.getX() <= 740
                    &&  Gdx.input.getY() >= 440 && Gdx.input.getY() <= 640) {
                        Gdx.app.exit();
                    }
                }
                activeCamera.getCamera().zoom = 1;
                activeCamera.getCamera().position.set(Gdx.graphics.getWidth()/2f,Gdx.graphics.getHeight()/2f,0);
                activeCamera.getCamera().update();
                batch.setProjectionMatrix(activeCamera.getCamera().combined);
                ScreenUtils.clear(1f, 1f, 1f, 1f);
                batch.begin();
                loseImage.draw(batch);
                quitButton.draw(batch);
                batch.end();
                break;
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
     * Checks a rectangle hitbox against the array of all hitboxes inside a certain collision layer, and then
     * returns true if there are any overlapping hitboxes
     * @param hitbox A rectangle which is compared against every hitbox inside a layer
     * @param type The type of layer which is checked
     * @return True if a CollisionComponent's hitbox overlaps the input, false otherwise.
     */
    public static Boolean isCollidingWithLayer(Rectangle hitbox, CollisionComponent.CollisionLayer type) {
        if (!collidingComponents.containsKey(type)) return false;
        for (CollisionComponent collider : collidingComponents.get(type)) {
            if (!collider.getCollisionLayer().equals(type))
                throw new IllegalArgumentException("Layer cannot change after added to game.");
            if (collider.isCollisionEnabled() && collider.isCollidingWith(hitbox)) {
                return true;
            }
        }
        return false;
    }
    /**
     * Returns a list of all overlapping hitboxes inside a certain collision layer, from
     * a given Rectangle hitbox which is used to compare against.
     * @param hitbox A rectangle which is compared against every hitbox inside the layer
     * @param type The type of layer which is checked
     * @return An arrayList of GameObjects including all CollisionComponents that collided with the hitbox.
     */
    public static ArrayList<GameObject> getAllCollidingObjects(Rectangle hitbox, CollisionComponent.CollisionLayer type) {
        if (!collidingComponents.containsKey(type)) return null;
        ArrayList<GameObject> overlappingGameObjects = new ArrayList<>();
        for (CollisionComponent collider : collidingComponents.get(type)) {
            if (!collider.getCollisionLayer().equals(type))
                throw new IllegalArgumentException("Layer cannot change after added to game.");
            if (collider.isCollisionEnabled() && collider.isCollidingWith(hitbox)) {
                overlappingGameObjects.add((GameObject) collider);
            }
        }
        return overlappingGameObjects;
    }
    /**
     * Returns the first overlapping hitbox inside a certain collision layer, from
     * a given Rectangle hitbox which is used to compare against. This function is
     * useful if you know that a layer will only contain one gameObject, i.e. the
     * Player layer (should) only be used for the player class.
     * @param hitbox A rectangle which is compared against every hitbox inside the layer
     * @param type The type of layer which is checked
     * @return The CollisionComponent that collided with the hitbox.
     */
    public static GameObject getFirstCollidingObjectInLayer(Rectangle hitbox, CollisionComponent.CollisionLayer type) {
        if (!collidingComponents.containsKey(type)) return null;
        for (CollisionComponent collider : collidingComponents.get(type)) {
            if (!collider.getCollisionLayer().equals(type))
                throw new IllegalArgumentException("Layer cannot change after added to game.");
            if (collider.isCollisionEnabled() && collider.isCollidingWith(hitbox)) {
                return (GameObject) collider;
            }
        }
        return null;
    }

    /**
     * Set's the camera that the Game is actively using as the main game camera.
     * Changing the active camera could be useful for cutscenes etc. There can
     * only be one active camera at a time.
     * @param newActiveCamera The new CameraComponent which replaces the old active camera.
     */
    public static void SetActiveCamera(CameraComponent newActiveCamera){
        activeCamera = newActiveCamera;
    }
    /**
     * Returns the camera that the Game is actively using as the main game camera.
     * @return The new CameraComponent which replaces the old active camera.
     */
    public static CameraComponent GetActiveCamera(){
        return activeCamera;
    }

    /**
     * Adds a gameObject to the game world and runs the start() procedure.
     *
     * @param gameObject The gameObject to add.
     */
    public void addGameObject(GameObject gameObject) {
        gameObjects.add(gameObject);
        //If the object is UI
        if (gameObject instanceof UIComponent uiObject) {
            uiComponents.add(uiObject);
        }
        //Otherwise, If the object has a renderableComponent, then add it to the list of renderableComponents
        else if (gameObject instanceof RenderableComponent renderObject) {
            renderableComponents.add(renderObject);
        }
        //If the object has a collisionComponent, then add it to the relevant dictionary of colliders depending on the objects layer
        if (gameObject instanceof CollisionComponent collisionObject) {
            var layer = collisionObject.getCollisionLayer();
            if (!collidingComponents.containsKey(layer)) {
                collidingComponents.put(layer, new ArrayList<>());
            }
            collidingComponents.get(layer).add(collisionObject);
        }
        if(gameObject.getClass() == GameTimer.class){
            timer = (GameTimer)gameObject;
        }
        gameObject.start();
    }
    public enum GameState{
        MENU, PLAYING, PAUSED, WIN, LOSE
    }

    public static GameTimer getTimer(){
        return timer;
    }

}

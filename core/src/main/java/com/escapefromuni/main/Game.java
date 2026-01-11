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
import com.escapefromuni.main.collectables.TimeStop;
import com.escapefromuni.main.collectables.SpeedCollectable;
import com.escapefromuni.main.collectables.NauseaCollectable;
import com.escapefromuni.main.collectables.DeathCollectable;
import com.escapefromuni.main.collectables.TeleportCollectable;
import com.escapefromuni.main.collectables.SlowCollectable;
import com.escapefromuni.main.collectables.BinocularsItem;
import com.escapefromuni.main.components.CameraComponent;
import com.escapefromuni.main.components.CollisionComponent;
import com.escapefromuni.main.components.RenderableComponent;
import com.escapefromuni.main.components.UIComponent;
import com.escapefromuni.main.ui.GameMessageHandler;
import com.escapefromuni.main.ui.GameTimer;
import com.escapefromuni.main.ui.Leaderboard;
import com.escapefromuni.main.ui.Achievements;
import com.escapefromuni.main.ui.NameInput;
import com.escapefromuni.main.ui.ScoreManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all
 * platforms.
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
    HashMap<String, Vector2> locations = defineLocations();

    private Stage stage;
    private TextField nameInput;
    private boolean showingInput = true;

    public static GameTimer timer;
    // The camera which is active and rendering the scene
    private static CameraComponent activeCamera;
    public static GameState gameState = GameState.MENU;
    static List<GameObject> gameObjects = new ArrayList<>();
    static List<RenderableComponent> renderableComponents = new ArrayList<>();
    static List<UIComponent> uiComponents = new ArrayList<>();
    static HashMap<CollisionComponent.CollisionLayer, List<CollisionComponent>> collidingComponents = new HashMap<>();

    // Leaderboard
    private BitmapFont font;
    public Leaderboard leaderboard = new Leaderboard();
    public String name = "longboi";
    public Achievements achievements = new Achievements();
    HashMap<String, Boolean> achievementsList = achievements.getAchievements();
    public String top5;
    // SCORING
    public ScoreManager scoreManager = new ScoreManager();
    private float score;
    private Boolean isFinished = false;
    private static Player player = null;

    @Override
    public void create() {
        batch = new SpriteBatch();
        // Create the Main Menu UI and position it
        titleImage = new Sprite(new Texture(Gdx.files.internal("titleGraphic.png")));
        titleImage.setPosition((Gdx.graphics.getWidth() - titleImage.getTexture().getWidth()) / 2f,
                (Gdx.graphics.getHeight() * 1.5f - titleImage.getTexture().getHeight()) / 2f);
        playButton = new Sprite(new Texture(Gdx.files.internal("playButton.png")));
        playButton.setPosition((Gdx.graphics.getWidth() - playButton.getTexture().getWidth()) / 2f,
                (Gdx.graphics.getHeight() / 2f - playButton.getTexture().getHeight()) / 2f);

        // adding leaderboard
        font = new BitmapFont();
        font.setColor(0, 0, 0, 1);
        font.getData().setScale(2f);
        leaderboard.init();
        achievements.init();

        // text/ name input
        stage = new Stage();
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        nameInput = new TextField("", skin);
        nameInput.setPosition(Gdx.graphics.getWidth() / 3f, 10);
        nameInput.setSize(350, 40);

        nameInput.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                if (c == '\n' || c == '\r') {
                    name = textField.getText();
                    showingInput = false;
                    System.out.println(name);
                }
            }
        });

        stage.addActor(nameInput);

        controlsHelp = new Sprite(new Texture(Gdx.files.internal("controlsHelp.png")));
        controlsHelp.setPosition((Gdx.graphics.getWidth() - controlsHelp.getTexture().getWidth()) / 2f,
                (Gdx.graphics.getHeight() - controlsHelp.getTexture().getHeight()) / 2f + 50);
        pausedSprite = new Sprite(new Texture(Gdx.files.internal("pausedGraphic.png")));
        pausedSprite.setPosition((Gdx.graphics.getWidth() - pausedSprite.getTexture().getWidth()) / 2f,
                (Gdx.graphics.getHeight() * 1.5f - pausedSprite.getTexture().getHeight()) / 2f);
        quitButton = new Sprite(new Texture(Gdx.files.internal("quitButton.png")));
        quitButton.setPosition((Gdx.graphics.getWidth() - quitButton.getTexture().getWidth()) / 2f,
                (Gdx.graphics.getHeight() / 2f - quitButton.getTexture().getHeight()) / 2f);
        winImage = new Sprite(new Texture(Gdx.files.internal("winImage.png")));
        winImage.setPosition((Gdx.graphics.getWidth() - winImage.getTexture().getWidth()) / 2f,
                (Gdx.graphics.getHeight() * 1.5f - winImage.getTexture().getHeight()) / 2f + 60);
        loseImage = new Sprite(new Texture(Gdx.files.internal("loseImage.png")));
        loseImage.setPosition((Gdx.graphics.getWidth() - loseImage.getTexture().getWidth()) / 2f,
                (Gdx.graphics.getHeight() * 1.5f - loseImage.getTexture().getHeight()) / 2f + 60);
        // Create the Game World:
        // Define a camera and add it to GameObjects
        var camera = new CameraController(null);
        addGameObject(camera);
        SetActiveCamera(camera);
        // Add the player
        player = new Player(locations.get("player_start"));
        player.setAchievements(achievements);
        addGameObject(player);
        camera.SetTarget(player);
        // Add the Map
        addGameObject(new GameMap(camera));
        addGameObject(new GameTimer(new Vector2(-0.9f, 0.9f)));
        addGameObject(new GameMessageHandler(new Vector2(0, 0.2f)));
        addGameObject(new TimeStop(locations.get("timestop_top_right"), achievements));

        addGameObject(new SpeedCollectable(locations.get("speed_north_west")));
        addGameObject(new SpeedCollectable(locations.get("speed_near_start")));
        addGameObject(new SpeedCollectable(locations.get("speed_middle_entrance")));
        addGameObject(new SpeedCollectable(locations.get("speed_middle_north")));
        addGameObject(new SpeedCollectable(locations.get("speed_west")));
        addGameObject(new SpeedCollectable(locations.get("speed_east")));
        addGameObject(new SpeedCollectable(locations.get("speed_middle_west")));
        addGameObject(new BinocularsItem(locations.get("binoculars"), achievements));

        addGameObject(new NPC(locations.get("npc_central_hub"), "npc1.png", "key"));

        addGameObject(new Key(locations.get("key_1")));
        addGameObject(new Key(locations.get("key_2")));
        addGameObject(new Key(locations.get("key_3")));
        addGameObject(new Key(locations.get("key_4")));

        addGameObject(new NauseaCollectable(locations.get("nausea"), achievements));
        addGameObject(new DeathCollectable(locations.get("death")));
        // addGameObject(new TeleportCollectable(locations.get("tp")));
        addGameObject(new SlowCollectable(locations.get("tp")));

        // Set up music
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
                activeCamera.getCamera().position.set(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f, 0);
                activeCamera.getCamera().update();
                batch.setProjectionMatrix(activeCamera.getCamera().combined);
                // TODO: Change these to UIElements

                // Detect clicking on the play button
                if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                    if (Gdx.input.getX() >= playButton.getX()
                            && Gdx.input.getX() <= (playButton.getX() + playButton.getWidth())
                            && Gdx.input
                                    .getY() >= (Gdx.graphics.getHeight() - (playButton.getY() + playButton.getHeight()))
                            && Gdx.input.getY() <= (Gdx.graphics.getHeight() - playButton.getY())) {
                        gameState = GameState.PLAYING;
                        GameMessageHandler.ShowMessage("WASD to move\nESC to pause", 3);
                    }
                }
                ScreenUtils.clear(1f, 1f, 1f, 1f);
                top5 = leaderboard.getTopFive();
                batch.begin();
                titleImage.draw(batch);
                playButton.draw(batch);
                controlsHelp.draw(batch);
                font.draw(batch, top5, 10, 700);
                batch.end();
                if (showingInput) {
                    Gdx.input.setInputProcessor(stage);
                    stage.setKeyboardFocus(nameInput);
                    stage.act();
                    stage.draw();
                }
                break;

            case PLAYING:
                if (Gdx.input.isKeyJustPressed(111)) {
                    gameState = GameState.PAUSED;
                }
                ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
                // cache the delta time
                float deltaTime = Gdx.graphics.getDeltaTime();
                // Update all gameObjects
                for (var gameObject : gameObjects) {
                    gameObject.update(deltaTime);
                }
                // set view to active camera
                // batch.setProjectionMatrix(activeCamera.GetCamera().combined);
                activeCamera.updateCamera(batch);
                // For all gameObjects that render, render them
                batch.begin();
                for (var renderable : renderableComponents) {
                    renderable.render(batch);
                }
                // Render the UI components after the rest so they are on top
                Vector2 screenSize = new Vector2(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                for (var ui : uiComponents) {
                    ui.positionOnScreen(activeCamera.getCameraPosition(), screenSize, activeCamera.getCamera().zoom);
                    ui.render(batch);
                }
                batch.end();
                break;
            case PAUSED:
                // If escape key pressed
                if (Gdx.input.isKeyJustPressed(111)) {
                    gameState = GameState.PLAYING;
                }
                if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                    // System.out.println(Gdx.input.getY());
                    // Super hardcoded, MUST adjust if screen resized
                    if (Gdx.input.getX() >= 340 && Gdx.input.getX() <= 740
                            && Gdx.input.getY() >= 410 && Gdx.input.getY() <= 610) {
                        Gdx.app.exit();
                    }
                }
                ScreenUtils.clear(1f, 1f, 1f, 1f);
                batch.begin();
                // TODO: Change these to UIElements
                pausedSprite.setPosition(activeCamera.getCameraPosition().x - 200,
                        activeCamera.getCameraPosition().y + 50);
                pausedSprite.draw(batch);
                quitButton.setPosition(activeCamera.getCameraPosition().x - 200,
                        activeCamera.getCameraPosition().y - 250);
                quitButton.draw(batch);
                batch.end();
                break;
            case WIN:
                if (!isFinished) {
                    score = scoreManager.calculateScore(achievements.getAchieved(), timer.GetTime(), true);
                    leaderboard.appendToLeaderboard(name, score);
                    isFinished = true;
                }
                if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                    System.out.println(Gdx.input.getY());
                    // Super hardcoded, MUST adjust if screen resized
                    if (Gdx.input.getX() >= 340 && Gdx.input.getX() <= 740
                            && Gdx.input.getY() >= 440 && Gdx.input.getY() <= 640) {
                        Gdx.app.exit();
                    }
                }
                String time = GameTimer.GetTimeString(timer.GetTime());
                activeCamera.getCamera().zoom = 1;
                activeCamera.getCamera().position.set(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f, 0);
                activeCamera.getCamera().update();
                batch.setProjectionMatrix(activeCamera.getCamera().combined);
                ScreenUtils.clear(0.25f, 0.75f, 0f, 1f);
                batch.begin();
                winImage.draw(batch);
                quitButton.draw(batch);
                GameMessageHandler.ShowMessage("Time left : " + time, 1);
                GameMessageHandler.instance.positionOnScreen(new Vector2(0, 0),
                        new Vector2(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()), 1);
                GameMessageHandler.instance.render(batch);
                batch.end();
                break;
            case LOSE:
                if (!isFinished) {
                    score = scoreManager.calculateScore(achievements.getAchieved(), timer.GetTime(), false);
                    leaderboard.appendToLeaderboard(name, score);
                    isFinished = true;
                }
                if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                    System.out.println(Gdx.input.getY());
                    // WARNING: Super hardcoded, MUST adjust if screen resized
                    if (Gdx.input.getX() >= 340 && Gdx.input.getX() <= 740
                            && Gdx.input.getY() >= 440 && Gdx.input.getY() <= 640) {
                        Gdx.app.exit();
                    }
                }
                activeCamera.getCamera().zoom = 1;
                activeCamera.getCamera().position.set(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f, 0);
                activeCamera.getCamera().update();
                batch.setProjectionMatrix(activeCamera.getCamera().combined);
                ScreenUtils.clear(1f, 1f, 1f, 1f);
                batch.begin();
                loseImage.draw(batch);
                quitButton.draw(batch);
                font.draw(batch, "CURRENT SCORE: " + score, 10, 700);
                batch.end();
                break;
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        stage.dispose();
        // dispose every gameObject
        for (var gameObject : gameObjects) {
            gameObject.dispose();
        }
    }

    /**
     * Checks a rectangle hitbox against the array of all hitboxes inside a certain
     * collision layer, and then
     * returns true if there are any overlapping hitboxes
     * 
     * @param hitbox A rectangle which is compared against every hitbox inside a
     *               layer
     * @param type   The type of layer which is checked
     * @return True if a CollisionComponent's hitbox overlaps the input, false
     *         otherwise.
     */
    public static Boolean isCollidingWithLayer(Rectangle hitbox, CollisionComponent.CollisionLayer type) {
        if (!collidingComponents.containsKey(type))
            return false;
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
     * Returns a list of all overlapping hitboxes inside a certain collision layer,
     * from
     * a given Rectangle hitbox which is used to compare against.
     * 
     * @param hitbox A rectangle which is compared against every hitbox inside the
     *               layer
     * @param type   The type of layer which is checked
     * @return An arrayList of GameObjects including all CollisionComponents that
     *         collided with the hitbox.
     */
    public static ArrayList<GameObject> getAllCollidingObjects(Rectangle hitbox,
            CollisionComponent.CollisionLayer type) {
        if (!collidingComponents.containsKey(type))
            return null;
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
     * 
     * @param hitbox A rectangle which is compared against every hitbox inside the
     *               layer
     * @param type   The type of layer which is checked
     * @return The CollisionComponent that collided with the hitbox.
     */
    public static GameObject getFirstCollidingObjectInLayer(Rectangle hitbox, CollisionComponent.CollisionLayer type) {
        if (!collidingComponents.containsKey(type))
            return null;
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
     * 
     * @param newActiveCamera The new CameraComponent which replaces the old active
     *                        camera.
     */
    public static void SetActiveCamera(CameraComponent newActiveCamera) {
        activeCamera = newActiveCamera;
    }

    /**
     * Returns the camera that the Game is actively using as the main game camera.
     * 
     * @return The new CameraComponent which replaces the old active camera.
     */
    public static CameraComponent GetActiveCamera() {
        return activeCamera;
    }

    public static Player getPlayer() {
        return player;
    }

    /**
     * Adds a gameObject to the game world and runs the start() procedure.
     *
     * @param gameObject The gameObject to add.
     */
    public void addGameObject(GameObject gameObject) {
        gameObjects.add(gameObject);
        // If the object is UI
        if (gameObject instanceof UIComponent uiObject) {
            uiComponents.add(uiObject);
        }
        // Otherwise, If the object has a renderableComponent, then add it to the list
        // of renderableComponents
        else if (gameObject instanceof RenderableComponent renderObject) {
            renderableComponents.add(renderObject);
        }
        // If the object has a collisionComponent, then add it to the relevant
        // dictionary of colliders depending on the objects layer
        if (gameObject instanceof CollisionComponent collisionObject) {
            var layer = collisionObject.getCollisionLayer();
            if (!collidingComponents.containsKey(layer)) {
                collidingComponents.put(layer, new ArrayList<>());
            }
            collidingComponents.get(layer).add(collisionObject);
        }
        if (gameObject.getClass() == GameTimer.class) {
            timer = (GameTimer) gameObject;
        }
        gameObject.start();
    }

    public enum GameState {
        MENU, PLAYING, PAUSED, WIN, LOSE
    }

    public static GameTimer getTimer() {
        return timer;
    }

    private static HashMap<String, Vector2> defineLocations() {
        HashMap<String, Vector2> locations = new HashMap<>();
        // Player Start Location
        locations.put("player_start", new Vector2(4090, 500));

        // Power-ups -- speed names are meant to just give a relative idea for which is
        // which on the map
        locations.put("speed_north_west", new Vector2(512, 1920));
        locations.put("speed_west", new Vector2(768, 2432));
        locations.put("speed_near_start", new Vector2(2950, 1280));
        locations.put("speed_middle_entrance", new Vector2(3584, 7040));
        locations.put("speed_middle_west", new Vector2(4352, 7296));
        locations.put("speed_middle_north", new Vector2(3072, 7296));
        locations.put("speed_east", new Vector2(7680, 6784));

        locations.put("binoculars", new Vector2(2750, 600));

        locations.put("timestop_top_right", new Vector2(6850, 1308));

        // NPC
        locations.put("npc_central_hub", new Vector2(4096, 4096));

        // Keys
        locations.put("key_1", new Vector2(2000, 1000));
        locations.put("key_2", new Vector2(2300, 1000));
        locations.put("key_3", new Vector2(2600, 1000));
        locations.put("key_4", new Vector2(2900, 1000));

        // Debuffs
        locations.put("nausea", new Vector2(2500, 400));
        locations.put("death", new Vector2(2750, 400));

        // Hidden Events
        locations.put("tp", new Vector2(4090, 800));

        return locations;
    }
}

package com.escapefromuni.main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.escapefromuni.main.collectables.Key;
import com.escapefromuni.main.collectables.TimeStop;
import com.escapefromuni.main.collectables.SpeedCollectable;
import com.escapefromuni.main.collectables.NauseaCollectable;
import com.escapefromuni.main.collectables.DeathCollectable;
import com.escapefromuni.main.collectables.SlowCollectable;
import com.escapefromuni.main.collectables.BinocularsItem;
import com.escapefromuni.main.collectables.TeleportCollectable;
import com.escapefromuni.main.collectables.C4;
import com.escapefromuni.main.components.CameraComponent;
import com.escapefromuni.main.components.CollisionComponent;
import com.escapefromuni.main.components.RenderableComponent;
import com.escapefromuni.main.components.UIComponent;
import com.escapefromuni.main.ui.GameMessageHandler;
import com.escapefromuni.main.ui.GameTimer;
import com.escapefromuni.main.ui.Leaderboard;
import com.escapefromuni.main.ui.Achievements;
import com.escapefromuni.main.ui.ScoreManager;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

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
    Sprite enterName;
    private Texture background;

    Music music;
    HashMap<String, Vector2> locations = defineLocations();

    private Stage stage;
    private TextField nameInput;
    private boolean showingInput = true;

    public static GameTimer timer;
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
    public String top5;

    // SCORING
    public ScoreManager scoreManager = new ScoreManager();
    private float score;
    private Boolean isFinished = false;

    private static Player player = null;

    @Override
    public void create() {
        batch = new SpriteBatch();

	background = new Texture("background.png");

        // Create menu sprites
        titleImage = new Sprite(new Texture(Gdx.files.internal("titleGraphic.png")));
        playButton = new Sprite(new Texture(Gdx.files.internal("playButton.png")));
        controlsHelp = new Sprite(new Texture(Gdx.files.internal("controlsHelp.png")));
        controlsHelp.setScale(1.5f);
        pausedSprite = new Sprite(new Texture(Gdx.files.internal("pausedGraphic.png")));
        quitButton = new Sprite(new Texture(Gdx.files.internal("quitButton.png")));
        winImage = new Sprite(new Texture(Gdx.files.internal("winImage.png")));
        loseImage = new Sprite(new Texture(Gdx.files.internal("loseImage.png")));
        enterName = new Sprite(new Texture(Gdx.files.internal("Enter-Your-Name.png")));
        enterName.setScale(0.5f);

        // adding leaderboard
        font = new BitmapFont();
        font.setColor(1, 1, 1, 1);
        font.getData().setScale(2f);
        leaderboard.init();
        achievements.init();

        // text/ name input
        stage = new Stage();
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        nameInput = new TextField("", skin);
        nameInput.setSize(350, 40);

        nameInput.setTextFieldListener((textField, c) -> {
            if (c == '\n' || c == '\r') {
                name = textField.getText();
                showingInput = false;
                System.out.println(name);
            }
        });

        stage.addActor(nameInput);

        // Create the Game World:
        var camera = new CameraController(null);
        addGameObject(camera);
        SetActiveCamera(camera);

        // Layout UI once using current window size
        layoutMenu(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

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
        addGameObject(new TimeStop(locations.get("timestop_hidden_room"), achievements));

        addGameObject(new SpeedCollectable(locations.get("speed_north_west")));
        addGameObject(new SpeedCollectable(locations.get("speed_near_start")));
        addGameObject(new SpeedCollectable(locations.get("speed_near_npc")));
        // addGameObject(new SpeedCollectable(locations.get("speed_middle_entrance")));
        addGameObject(new SpeedCollectable(locations.get("speed_middle_north")));
        addGameObject(new SpeedCollectable(locations.get("speed_west")));
        // addGameObject(new SpeedCollectable(locations.get("speed_east")));
        addGameObject(new SpeedCollectable(locations.get("speed_north_east")));
        // addGameObject(new SpeedCollectable(locations.get("speed_middle_west")));
        addGameObject(new SpeedCollectable(locations.get("speed_north_west")));
        addGameObject(new SpeedCollectable(locations.get("speed_middle_west_2")));
        addGameObject(new SpeedCollectable(locations.get("speed_middle_west_3")));
        addGameObject(new SpeedCollectable(locations.get("speed_middle_west_4")));
        addGameObject(new SpeedCollectable(locations.get("speed_middle_west_5")));

        addGameObject(new BinocularsItem(locations.get("binoculars"), achievements));
        addGameObject(new BinocularsItem(locations.get("binoculars_near_npc"), achievements));
        addGameObject(new BinocularsItem(locations.get("binoculars_2"), achievements));
        addGameObject(new BinocularsItem(locations.get("binoculars_3"), achievements));
        addGameObject(new BinocularsItem(locations.get("binoculars_4"), achievements));
        addGameObject(new BinocularsItem(locations.get("binoculars_5"), achievements));
        addGameObject(new BinocularsItem(locations.get("binoculars_6"), achievements));
        addGameObject(new BinocularsItem(locations.get("binoculars_7"), achievements));

        addGameObject(new NPC(locations.get("npc_central_hub"), "npc1.png", "key"));
        addGameObject(new NPC(locations.get("Bomb_Sign"), "BombSign.png", "bomb"));
        addGameObject(new NPC(locations.get("Glasses_Sign"), "GlassesSign.png", "glasses"));


        addGameObject(new Key(locations.get("key_1")));
        addGameObject(new Key(locations.get("key_2")));
        // addGameObject(new Key(locations.get("key_3")));
        // addGameObject(new Key(locations.get("key_4")));
        // addGameObject(new Key(locations.get("key_5")));
        // addGameObject(new Key(locations.get("key_6")));
        // addGameObject(new Key(locations.get("key_7")));

        addGameObject(new C4(locations.get("c4")));
        addGameObject(new C4(locations.get("c4_east")));

        addGameObject(new NauseaCollectable(locations.get("nausea"), achievements));
        addGameObject(new NauseaCollectable(locations.get("nausea_2"), achievements));
        addGameObject(new NauseaCollectable(locations.get("nausea_3"), achievements));
        addGameObject(new NauseaCollectable(locations.get("nausea_4"), achievements));
        addGameObject(new NauseaCollectable(locations.get("nausea_5"), achievements));
        addGameObject(new NauseaCollectable(locations.get("nausea_6"), achievements));
        addGameObject(new NauseaCollectable(locations.get("nausea_7"), achievements));
        addGameObject(new NauseaCollectable(locations.get("nausea_8"), achievements));
        addGameObject(new NauseaCollectable(locations.get("nausea_9"), achievements));
        addGameObject(new NauseaCollectable(locations.get("nausea_10"), achievements));
        addGameObject(new NauseaCollectable(locations.get("nausea_11"), achievements));
        addGameObject(new NauseaCollectable(locations.get("nausea_12"), achievements));

        

        addGameObject(new DeathCollectable(locations.get("death")));
        addGameObject(new DeathCollectable(locations.get("death_2")));
        addGameObject(new DeathCollectable(locations.get("death_3")));
        addGameObject(new DeathCollectable(locations.get("death_4")));
        addGameObject(new DeathCollectable(locations.get("death_5")));
        addGameObject(new DeathCollectable(locations.get("death_6")));
        addGameObject(new DeathCollectable(locations.get("death_7")));
        addGameObject(new DeathCollectable(locations.get("death_8")));

        addGameObject(new TeleportCollectable(locations.get("tp")));
        
        addGameObject(new SlowCollectable(locations.get("slow_1")));
        addGameObject(new SlowCollectable(locations.get("slow_2")));
        addGameObject(new SlowCollectable(locations.get("slow_3")));
        addGameObject(new SlowCollectable(locations.get("slow_4")));
        addGameObject(new SlowCollectable(locations.get("slow_5")));
        addGameObject(new SlowCollectable(locations.get("slow_6")));
        addGameObject(new SlowCollectable(locations.get("slow_7")));
        addGameObject(new SlowCollectable(locations.get("slow_8")));
        addGameObject(new SlowCollectable(locations.get("slow_9")));
        addGameObject(new SlowCollectable(locations.get("slow_10")));
        addGameObject(new SlowCollectable(locations.get("slow_11")));


        // Set up music
        music = Gdx.audio.newMusic(Gdx.files.internal("Music/Dungeon.wav"));
        music.setLooping(true);
        music.setVolume(0.5f);
        music.play();
    }

    /**
     * Reposition UI sprites (and the name input) based on the current window size.
     */
    private void layoutMenu(int width, int height) {
        titleImage.setPosition(
                (width - titleImage.getTexture().getWidth()) / 2f,
                (height * 1.5f - titleImage.getTexture().getHeight()) / 2f);

        playButton.setPosition(
                (width - playButton.getTexture().getWidth()) / 2f,
                (height / 2f - playButton.getTexture().getHeight()) / 2f);

        controlsHelp.setPosition(
                (width - controlsHelp.getTexture().getWidth()) / 2f,
                (height - controlsHelp.getTexture().getHeight()) / 2f - 100f);

        pausedSprite.setPosition(
                (width - pausedSprite.getTexture().getWidth()) / 2f,
                (height * 1.5f - pausedSprite.getTexture().getHeight()) / 2f);

        quitButton.setPosition(
                (width - quitButton.getTexture().getWidth()) / 2f,
                (height / 2f - quitButton.getTexture().getHeight()) / 2f);

        winImage.setPosition(
                (width - winImage.getTexture().getWidth()) / 2f,
                (height * 1.5f - winImage.getTexture().getHeight()) / 2f + 60);

        loseImage.setPosition(
                (width - loseImage.getTexture().getWidth()) / 2f,
                (height * 1.5f - loseImage.getTexture().getHeight()) / 2f + 60);

        nameInput.setPosition(width / 3f, 10);
	
	enterName.setPosition(5, 5);
    }

    @Override
    public void resize(int width, int height) {
        if (stage != null) {
            stage.getViewport().update(width, height, true);
        }

        if (activeCamera != null) {
            activeCamera.getCamera().viewportWidth = width;
            activeCamera.getCamera().viewportHeight = height;
            activeCamera.getCamera().update();
        }

        layoutMenu(width, height);
    }

    @Override
    public void render() {
        switch (gameState) {
            case MENU: {
                int w = Gdx.graphics.getWidth();
                int h = Gdx.graphics.getHeight();

                activeCamera.getCamera().zoom = 1f;
                activeCamera.getCamera().position.set(w / 2f, h / 2f, 0);
                activeCamera.getCamera().update();
                batch.setProjectionMatrix(activeCamera.getCamera().combined);

                // Convert click from screen coords -> world coords, so it works at any
                // resolution/aspect
                Vector3 mouseWorld = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
                activeCamera.getCamera().unproject(mouseWorld);

                if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                    if (playButton.getBoundingRectangle().contains(mouseWorld.x, mouseWorld.y)) {
                        gameState = GameState.PLAYING;
                        GameMessageHandler.ShowMessage("WASD to move\nESC to pause", 3);
                    }
                }

                ScreenUtils.clear(1f, 1f, 1f, 1f);
                top5 = leaderboard.getTopFive();

                batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                titleImage.draw(batch);
                playButton.draw(batch);
                controlsHelp.draw(batch);
		enterName.draw(batch);
                font.draw(batch, top5, 10, 700);
                batch.end();

                if (showingInput) {
                    Gdx.input.setInputProcessor(stage);
                    stage.setKeyboardFocus(nameInput);
                    stage.act();
                    stage.draw();
                }
                break;
            }

            case PLAYING: {
                if (Gdx.input.isKeyJustPressed(111)) {
                    gameState = GameState.PAUSED;
                }
                ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

                float deltaTime = Gdx.graphics.getDeltaTime();
                for (var gameObject : gameObjects) {
                    gameObject.update(deltaTime);
                }

                activeCamera.updateCamera(batch);

                batch.begin();
                for (var renderable : renderableComponents) {
                    renderable.render(batch);
                }

                Vector2 screenSize = new Vector2(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                for (var ui : uiComponents) {
                    ui.positionOnScreen(activeCamera.getCameraPosition(), screenSize, activeCamera.getCamera().zoom);
                    ui.render(batch);
                }
                batch.end();
                break;
            }

            case PAUSED: {
                if (Gdx.input.isKeyJustPressed(111)) {
                    gameState = GameState.PLAYING;
                }

                quitButton.setPosition(
                        activeCamera.getCameraPosition().x - 200,
                        activeCamera.getCameraPosition().y - 250);

                Vector3 mouse = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
                activeCamera.getCamera().unproject(mouse);

                if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                    if (quitButton.getBoundingRectangle().contains(mouse.x, mouse.y)) {
                        Gdx.app.exit();
                    }
                }

                ScreenUtils.clear(1f, 1f, 1f, 1f);

                batch.begin();
                pausedSprite.setPosition(activeCamera.getCameraPosition().x - 200,
                        activeCamera.getCameraPosition().y + 50);
                pausedSprite.draw(batch);

                quitButton.draw(batch);
                batch.end();

                break;
            }

            case WIN: {
                if (!isFinished) {
                    score = scoreManager.calculateScore(achievements.getAchieved(), timer.GetTime(), true);
                    leaderboard.appendToLeaderboard(name, score);
                    isFinished = true;
                }

                String time = GameTimer.GetTimeString(timer.GetTime());

                activeCamera.getCamera().zoom = 1f;
                activeCamera.getCamera().position.set(
                        Gdx.graphics.getWidth() / 2f,
                        Gdx.graphics.getHeight() / 2f,
                        0);
                activeCamera.getCamera().update();
                batch.setProjectionMatrix(activeCamera.getCamera().combined);

                float cx = activeCamera.getCamera().position.x;
                float cy = activeCamera.getCamera().position.y;

                quitButton.setPosition(cx - quitButton.getWidth() / 2f, cy - 250f);

                Vector3 mouse = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
                activeCamera.getCamera().unproject(mouse);

                // Click quit
                if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                    if (quitButton.getBoundingRectangle().contains(mouse.x, mouse.y)) {
                        Gdx.app.exit();
                    }
                }

                ScreenUtils.clear(0.25f, 0.75f, 0f, 1f);

                batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                winImage.draw(batch);
                quitButton.draw(batch);

                GameMessageHandler.ShowMessage("Time left : " + time, 1);
                GameMessageHandler.instance.positionOnScreen(
                        new Vector2(0, 0),
                        new Vector2(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()),
                        1);
                GameMessageHandler.instance.render(batch);
                font.draw(batch, top5 + "\nCurrentScore: " + (int) score, 10, 700);
                batch.end();

                break;
            }

            case LOSE: {
                if (!isFinished) {
                    score = scoreManager.calculateScore(achievements.getAchieved(), timer.GetTime(), false);
                    leaderboard.appendToLeaderboard(name, score);
                    isFinished = true;
                }

                activeCamera.getCamera().zoom = 1f;
                activeCamera.getCamera().position.set(
                        Gdx.graphics.getWidth() / 2f,
                        Gdx.graphics.getHeight() / 2f,
                        0);
                activeCamera.getCamera().update();
                batch.setProjectionMatrix(activeCamera.getCamera().combined);

                quitButton.setPosition(
                        activeCamera.getCameraPosition().x - 200,
                        activeCamera.getCameraPosition().y - 250);

                Vector3 mouse = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
                activeCamera.getCamera().unproject(mouse);

                if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                    if (quitButton.getBoundingRectangle().contains(mouse.x, mouse.y)) {
                        Gdx.app.exit();
                    }
                }

                ScreenUtils.clear(1f, 1f, 1f, 1f);

                batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                loseImage.draw(batch);
                font.draw(batch, top5 + "\nCurrentScore: " + (int) score, 10, 700);
                quitButton.draw(batch);
                batch.end();
                break;
            }
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        stage.dispose();
	background.dispose();
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
            if (collider.isCollisionEnabled() && collider.isCollidingWith(hitbox))
                return true;
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

        if (gameObject instanceof UIComponent uiObject) {
            uiComponents.add(uiObject);
        } else if (gameObject instanceof RenderableComponent renderObject) {
            renderableComponents.add(renderObject);
        }

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

        // Power-ups
        locations.put("speed_north_west", new Vector2(442, 1848));
        locations.put("speed_west", new Vector2(768, 2432));
        locations.put("speed_near_start", new Vector2(2950, 1280));
        // locations.put("speed_middle_entrance", new Vector2(3584, 7040));
        // locations.put("speed_middle_west", new Vector2(4352, 7296));
        locations.put("speed_middle_north", new Vector2(3072, 7296));
        // locations.put("speed_east", new Vector2(7680, 6784));
        locations.put("speed_near_npc", new Vector2(3614, 2286));
        locations.put("speed_north_east", new Vector2(7178, 5731));
        locations.put("speed_north_west", new Vector2(2365, 6050));
        locations.put("speed_middle_west_2", new Vector2(829, 4925));
        locations.put("speed_middle_west_3", new Vector2(941, 6192));
        locations.put("speed_middle_west_4", new Vector2(1207, 6192));
        locations.put("speed_middle_west_5", new Vector2(1792, 6192));

        locations.put("binoculars", new Vector2(7363, 1069));
        locations.put("binoculars_near_npc", new Vector2(4512, 3162));
        locations.put("binoculars_2", new Vector2(1345, 3502));
        locations.put("binoculars_3", new Vector2(301, 4650));
        locations.put("binoculars_4", new Vector2(1842, 5420));
        locations.put("binoculars_5", new Vector2(701, 6452));
        locations.put("binoculars_6", new Vector2(701, 6452));
        locations.put("binoculars_7", new Vector2(1206, 6957));

        locations.put("timestop_top_right", new Vector2(6850, 1308));
        locations.put("timestop_hidden_room", new Vector2(7167, 3460));
        // NPC
        locations.put("npc_central_hub", new Vector2(4096, 4096));
        locations.put("Bomb_Sign", new Vector2(4675, 804));
        locations.put("Glasses_Sign", new Vector2(3427, 988));


        // Keys
        locations.put("key_1", new Vector2(2658, 6925));
        locations.put("key_2", new Vector2(7000, 6640));
        // locations.put("key_3", new Vector2(1222, 4643));
        // locations.put("key_4", new Vector2(316, 2969));
        // locations.put("key_5", new Vector2(5292, 4018));
        // locations.put("key_6", new Vector2(4929, 1196));
        // locations.put("key_7", new Vector2(2832, 6568));

        // C4
        locations.put("c4", new Vector2(3648, 293));
        locations.put("c4_east", new Vector2(7859, 5119));

        // Debuffs
        locations.put("nausea", new Vector2(1474, 1704));
        locations.put("nausea_2", new Vector2(6599, 1072));
        locations.put("nausea_3", new Vector2(7721, 2343));
        locations.put("nausea_4", new Vector2(6590, 1985));
        locations.put("nausea_5", new Vector2(6859, 3756));
        locations.put("nausea_6", new Vector2(7736, 3512));
        locations.put("nausea_7", new Vector2(7485, 4686));
        locations.put("nausea_8", new Vector2(6853, 5406));
        locations.put("nausea_9", new Vector2(7486, 5406));
        locations.put("nausea_10", new Vector2(7735, 5939));
        locations.put("nausea_11", new Vector2(7485, 4686));
        locations.put("nausea_12", new Vector2(6859, 6184));
        
        locations.put("death", new Vector2(1866, 314));
        locations.put("death_2", new Vector2(189, 5681));
        locations.put("death_3", new Vector2(826, 4507));
        locations.put("death_4", new Vector2(7141, 2880));
        locations.put("death_5", new Vector2(8012, 2096));
        locations.put("death_6", new Vector2(7173, 2852));
        locations.put("death_7", new Vector2(7611, 4255));
        locations.put("death_8", new Vector2(7993, 5423));

        locations.put("slow_1", new Vector2(2488, 1325));
        locations.put("slow_2", new Vector2(1701, 1475));
        locations.put("slow_3", new Vector2(1477, 2727));
        locations.put("slow_4", new Vector2(4958, 232));
        locations.put("slow_5", new Vector2(5804, 660));
        locations.put("slow_6", new Vector2(6082, 1325));
        locations.put("slow_7", new Vector2(6201, 2100));
        locations.put("slow_8", new Vector2(7143, 2879));
        locations.put("slow_8", new Vector2(7466, 3759));
        locations.put("slow_9", new Vector2(7957, 3246));
        locations.put("slow_10", new Vector2(8001, 4532));
        locations.put("slow_11", new Vector2(6893, 5870));

        // Hidden Events
        locations.put("tp", new Vector2(4183, 943));

        return locations;
    }
}

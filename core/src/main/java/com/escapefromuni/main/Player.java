package com.escapefromuni.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.escapefromuni.main.collectables.C4;
import com.escapefromuni.main.collectables.Collectable;
import com.escapefromuni.main.collectables.Item;
import com.escapefromuni.main.components.CollisionComponent;
import com.escapefromuni.main.components.RenderableComponent;
import com.escapefromuni.main.ui.GameMessageHandler;
import com.escapefromuni.main.ui.Achievements;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

public class Player extends GameObject implements RenderableComponent, CollisionComponent {

    // Texture and Sprite class used from the libGDX library.
    Texture idleTexture;
    Sprite playerSprite;
    // Player speed attribute makes it possible to alter speed during the game.
    boolean hasKey = false;
    float baseSpeed = 700f;
    float speed = baseSpeed;
    // How fast the player's speed returns to normal
    float speedRecovery = 45f;
    Rectangle hitbox;
    ArrayList<Item> items = new ArrayList<>();
    Texture[] animation = new Texture[] {
            new Texture(Gdx.files.internal("Player/player_3.png")),
            new Texture(Gdx.files.internal("Player/player_4.png")),
            new Texture(Gdx.files.internal("Player/player_5.png")),
            new Texture(Gdx.files.internal("Player/player_6.png"))
    };
    float animation_speed = 4f;
    float animation_frame = 0;
    // Event counter
    Set<String> events = new HashSet<String>();
    Achievements achievements;
    HashMap<Integer, Vector2> teleportLocations = defineTeleportLocations();

    int keyCounter = 0;

    public Player(Vector2 position, float rotation, String playerTexturePath) {
        super(position, rotation);
        this.idleTexture = new Texture(Gdx.files.internal(playerTexturePath));
    }

    public Player(Vector2 position, float rotation) {
        super(position, rotation);
        // Player texture defaults to placeholder player.png
        idleTexture = new Texture(Gdx.files.internal("Player/player_2.png"));
    }

    public Player(Vector2 position) {
        super(position);
    }

    public void setAchievements(Achievements achievementSys) {
        achievements = achievementSys;
    }

    public void start() {
        // Generates a Sprite object using the player.png texture
        idleTexture = new Texture(Gdx.files.internal("Player/player_2.png"));
        playerSprite = new Sprite(idleTexture);
        float rectX = position.x;
        float rectY = position.y;
        this.hitbox = new Rectangle(rectX, rectY, playerSprite.getWidth(), playerSprite.getHeight());
        playerSprite.setScale(4);
    }

    @Override
    public void update(float deltaTime) {
        // if (Gdx.input.isKeyPressed(Input.Keys.R)) {
        //     position.set(4090, 2800);
        // }
        // Speed slowly returns to the base movement speed
        if (speed > baseSpeed) {
            speed = Math.max(speed - deltaTime * speedRecovery, baseSpeed);
        } else if (speed < baseSpeed) {
            speed = Math.min(speed + deltaTime * speedRecovery, baseSpeed);
        }
        // If you drink too much coffee/soda, you get the unexpected event of a 'sugar
        // crash' where your speed slows down
        if (speed > (baseSpeed + 300f)) {
            speed = 0;
            // GameMessageHandler.ShowMessage("Sugar Crash!",5);
            achievements.achieveAchievement("Sugar Crash!!");
            events.add("SugarCrash");
        }
        float targetZoom = 0.75f + (speed / baseSpeed) * 0.25f;
        Game.GetActiveCamera().getCamera().zoom += (targetZoom - Game.GetActiveCamera().getCamera().zoom) * 0.25f;

        // Pick up collectibles
        ArrayList<GameObject> pickups = Game.getAllCollidingObjects(hitbox, CollisionLayer.COLLECTIBLE);
        assert pickups != null;
        for (var pickup : pickups) {
            if (pickup instanceof Collectable collectable) {
                collectable.pickup(this);
            }
            if (pickup instanceof Item item) {
                items.add(item);
            }
        }

        if (hasKey(Game.getPlayer()) && GameMap.isPlayerNearAnyPlacedWall(hitbox, 80f) && GameMap.DoesKeyWallExists()) {
            GameMap.removeHardcodedPlacedWalls();
            removeKey();

        }

        if (C4.doesPlayerHaveBomb() && GameMap.isPlayerNearBombableWall(hitbox, 80f) && GameMap.DoesBombWallExists()) {

            if (!Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                GameMessageHandler.ShowMessage("Press E to bomb the wall ", 3);
            } else {
                GameMessageHandler.StopMessage();
                for (int i = 0; i < items.size(); i++) {
                    Item item = items.get(i);
                    if (item instanceof C4 c4) {
                        items.remove(i);
                        c4.placeBomb(c4, hitbox);
                        break;
                    }
                }
            }
        }
        // normalise to have consistent speed regardless of direction, and then scale by
        // move speed and time
        Vector2 desiredVelocity = getInputVector().nor().scl(speed * deltaTime);
        // Velocity after accounting for collision with walls
        Vector2 resolvedVelocity = CollideWithWalls(desiredVelocity);
        // The resultant velocity is applied to the player
        position.add(resolvedVelocity.x, resolvedVelocity.y);
        // Handle animation
        if (resolvedVelocity.len2() <= 0.01) {
            playerSprite.setTexture(idleTexture);
        } else {
            animation_frame += deltaTime * animation_speed * (speed / baseSpeed);
            if (Math.floor(animation_frame) >= animation.length) {
                animation_frame = 0;
            }
            playerSprite.setTexture(animation[(int) Math.floor(animation_frame)]);
        }
        playerSprite.setFlip(resolvedVelocity.x < 0, false);
        // Simulate "drag" on items using velocity
        displayItems(resolvedVelocity.x);
        setCount();
    }

    public Vector2 CollideWithWalls(Vector2 desiredVelocity) {
        Vector2 newVelocity = new Vector2(desiredVelocity.x, desiredVelocity.y);
        // Check the hitbox a frame forwards in the X direction. If this caused a
        // collision, cancel the X velocity
        hitbox.setPosition(position.x + desiredVelocity.x - hitbox.getWidth() / 2f,
                position.y - hitbox.getHeight() / 2f);
        if (Game.isCollidingWithLayer(hitbox, CollisionLayer.WALL)) {
            newVelocity.x = 0;
        }
        // Check the hitbox a frame forwards in the Y direction. If this caused a
        // collision, cancel the Y velocity
        hitbox.setPosition(position.x - hitbox.getWidth() / 2f,
                position.y + desiredVelocity.y - hitbox.getHeight() / 2f);
        if (Game.isCollidingWithLayer(hitbox, CollisionLayer.WALL)) {
            newVelocity.y = 0;
        }
        return newVelocity;
    }

    @Override
    public void render(SpriteBatch batch) {
        playerSprite.setPosition(position.x - playerSprite.getWidth() / 2f,
                position.y - playerSprite.getWidth() / 2f + 40f);
        playerSprite.draw(batch);

        System.out.println("X: " + position.x + " Y: " + position.y);
    }

    /**
     * Get the desired direction that the player wants to move in from using the
     * arrow keys or WASD.
     *
     * @return A vector representing the players desired move direction, that has a
     *         magnitude of <=1.
     */
    public Vector2 getInputVector() {
        Vector2 direction = new Vector2();
        // Check which keys are being pressed on each frame and move the sprite
        // accordingly.
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            direction.add(1, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            direction.add(-1, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
            direction.add(0, 1);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
            direction.add(0, -1);
        }
        return direction;
    }

    /**
     * Speeds the player up temporarily.
     */
    public void speedUp() {
        speed += 100f;
        achievements.achieveAchievement("Sugar Rush!");
        events.add("SpdUP");
    }

    // Teleports the player to a random location
    public void teleportRandom() {
        position.set(teleportLocations.get((int) (Math.random() * 7 + 1))); // generates random number from 1-7
        events.add("TP");
    }

    // Slows down the player temporarily
    public void slowDown() {
        speed = 0;
        events.add("SpdDOWN");
    }

    public void giveKey() {
        keyCounter += 1;
        this.hasKey = true;
        events.add("GetKey");
    }

    public void removeKey() {
        for (int i = 0; i < items.size(); i++) {
            if ("key".equals(items.get(i).itemType)) {
                items.get(i).setAsNotExist();
                items.remove(i);
                break;
            }
        }
        keyCounter -= 1;
        if (keyCounter == 0) {
            this.hasKey = false;
        }
    }

    public static boolean hasKey(Player player) {
        return player.hasKey;
    }

    public void displayItems(float drag) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i) instanceof C4) {
                items.get(i).position.set(position.x - 15 - drag * i * 2.9f, position.y + 30);
            } else {
                items.get(i).position.set(position.x - 15 - drag * i * 2.9f, position.y + 55 + i * 20);
            }
        }
    }

    public void AddItem(Item newItem) {
        items.add(newItem);
    }

    public boolean HasItem(String itemType) {
        for (int i = 0; i < items.size(); i++) {
            if (Objects.equals(items.get(i).itemType, itemType)) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param itemType
     * @return
     */
    public boolean TakeItem(String itemType) {
        int itemToRemove = -1;
        events.add("TakeItem");
        for (int i = 0; i < items.size(); i++) {
            if (Objects.equals(items.get(i).itemType, itemType)) {
                itemToRemove = i;
                break;
            }
        }
        if (itemToRemove != -1) {
            items.remove(itemToRemove);
            return true;
        } else {
            return false;
        }
    }

    public void setCount() {
        Game.getTimer().setEventCounter(this.events.size());
    }

    @Override
    public CollisionLayer getCollisionLayer() {
        return CollisionLayer.PLAYER;
    }

    @Override
    public Boolean isCollidingWith(Rectangle collisionCheck) {
        return hitbox.overlaps(collisionCheck);
    }

    @Override
    public Boolean isCollisionEnabled() {
        return true;
    }

    private static HashMap<Integer, Vector2> defineTeleportLocations() {
        HashMap<Integer, Vector2> teleportLocations = new HashMap<>();

        teleportLocations.put(1, new Vector2(4090, 500)); // Spawn
        teleportLocations.put(2, new Vector2(2560, 7040)); // Top Left (Key Room)
        teleportLocations.put(3, new Vector2(448, 3840)); // Middle Left
        teleportLocations.put(4, new Vector2(6848, 4096)); // Middle Right
        teleportLocations.put(5, new Vector2(7808, 6656)); // Top Right (Key Room)
        teleportLocations.put(6, new Vector2(6848, 1920)); // Bottom Right
        teleportLocations.put(7, new Vector2(1280, 1216)); // Bottom Left
        return teleportLocations;
    }
}

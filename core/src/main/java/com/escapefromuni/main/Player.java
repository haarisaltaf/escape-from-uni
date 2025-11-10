package com.escapefromuni.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.escapefromuni.main.collectables.Collectable;
import com.escapefromuni.main.collectables.Item;
import com.escapefromuni.main.components.CollisionComponent;
import com.escapefromuni.main.components.RenderableComponent;
import com.escapefromuni.main.ui.GameMessageHandler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Player extends GameObject implements RenderableComponent, CollisionComponent {

    // Texture and Sprite class used from the libGDX library.
    Texture idleTexture;
    Sprite playerSprite;
    // Player speed attribute makes it possible to alter speed during the game.
    boolean hasKey = false;
    float baseSpeed = 300f;
    float speed = baseSpeed;
    //How fast the player's speed returns to normal
    float speedRecovery = 25f;
    Rectangle hitbox;
    ArrayList<Item> items = new ArrayList<>();
    Texture[] animation = new Texture[]{
            new Texture(Gdx.files.internal("Player/player_3.png")),
            new Texture(Gdx.files.internal("Player/player_4.png")),
            new Texture(Gdx.files.internal("Player/player_5.png")),
            new Texture(Gdx.files.internal("Player/player_6.png"))
    };
    float animation_speed = 4f;
    float animation_frame = 0;
    // Event counter
    Set<String> events = new HashSet<String>();

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
        //Speed slowly returns to the base movement speed
        if (speed > baseSpeed){
            speed = Math.max(speed - deltaTime * speedRecovery,baseSpeed);
        }else if (speed < baseSpeed){
            speed = Math.min(speed + deltaTime * speedRecovery,baseSpeed);
        }
        //If you drink too much coffee/soda, you get the unexpected event of a 'sugar crash' where your speed slows down
        if (speed > baseSpeed * 2.5f){
            speed = 0;
            GameMessageHandler.ShowMessage("Sugar Crash!",5);
            events.add("SugarCrash");
        }
        float targetZoom = 0.75f + (speed / baseSpeed) * 0.25f;
        Game.GetActiveCamera().getCamera().zoom += (targetZoom - Game.GetActiveCamera().getCamera().zoom) * 0.25f;

        //Pick up collectibles
        ArrayList<GameObject> pickups = Game.getAllCollidingObjects(hitbox,CollisionLayer.COLLECTIBLE);
        assert pickups != null;
        for (var pickup : pickups){
            if (pickup instanceof Collectable collectable){
                collectable.pickup(this);
            }
            if (pickup instanceof Item item){
                items.add(item);
            }
        }

        //normalise to have consistent speed regardless of direction, and then scale by move speed and time
        Vector2 desiredVelocity = getInputVector().nor().scl(speed * deltaTime);
        //Velocity after accounting for collision with walls
        Vector2 resolvedVelocity = CollideWithWalls(desiredVelocity);
        //The resultant velocity is applied to the player
        position.add(resolvedVelocity.x, resolvedVelocity.y);
        //Handle animation
        if (resolvedVelocity.len2() <= 0.01){
            playerSprite.setTexture(idleTexture);
        }else{
            animation_frame += deltaTime * animation_speed * (speed / baseSpeed);
            if (Math.floor(animation_frame) >= animation.length){
                animation_frame = 0;
            }
            playerSprite.setTexture(animation[(int)Math.floor(animation_frame)]);
        }
        playerSprite.setFlip(resolvedVelocity.x < 0,false);
        //Simulate "drag" on items using velocity
        displayItems(resolvedVelocity.x);
        setCount();
    }
    public Vector2 CollideWithWalls(Vector2 desiredVelocity){
        Vector2 newVelocity = new Vector2(desiredVelocity.x,desiredVelocity.y);
        //Check the hitbox a frame forwards in the X direction. If this caused a collision, cancel the X velocity
        hitbox.setPosition(position.x + desiredVelocity.x - hitbox.getWidth() / 2f, position.y - hitbox.getHeight() / 2f);
        if (Game.isCollidingWithLayer(hitbox, CollisionLayer.WALL)) {
            newVelocity.x = 0;
        }
        //Check the hitbox a frame forwards in the Y direction. If this caused a collision, cancel the Y velocity
        hitbox.setPosition(position.x - hitbox.getWidth() / 2f, position.y + desiredVelocity.y - hitbox.getHeight() / 2f);
        if (Game.isCollidingWithLayer(hitbox, CollisionLayer.WALL)) {
            newVelocity.y = 0;
        }
        return newVelocity;
    }

    @Override
    public void render(SpriteBatch batch) {
        playerSprite.setPosition(position.x - playerSprite.getWidth() / 2f, position.y - playerSprite.getWidth() / 2f - 12f);
        playerSprite.draw(batch);
    }

    /**
     * Get the desired direction that the player wants to move in from using the arrow keys or WASD.
     *
     * @return A vector representing the players desired move direction, that has a magnitude of <=1.
     */
    public Vector2 getInputVector() {
        Vector2 direction = new Vector2();
        // Check which keys are being pressed on each frame and move the sprite accordingly.
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
        events.add("SpdUP");
    }

    public void giveKey() {
        this.hasKey = true;
        events.add("GetKey");
    }

    public static boolean hasKey() {
        System.out.println("TODO: Detect if has key");
        //return this.hasKey;
        return false;
    }
    public void displayItems(float drag){
        for (int i = 0; i < items.size(); i++) {
            items.get(i).position.set(position.x - drag * i * 0.4f,position.y + i * 20);
        }
    }
    public void AddItem(Item newItem){
        items.add(newItem);
    }
    public boolean HasItem(String itemType){
        for (int i = 0; i < items.size(); i++) {
            if (Objects.equals(items.get(i).itemType, itemType)){
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
    public boolean TakeItem(String itemType){
        int itemToRemove = -1;
        events.add("TakeItem");
        for (int i = 0; i < items.size(); i++) {
            if (Objects.equals(items.get(i).itemType, itemType)){
                itemToRemove = i;
                break;
            }
        }
        if (itemToRemove != -1){
            items.remove(itemToRemove);
            return true;
        }else{
            return false;
        }
    }

    public void setCount(){
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
}

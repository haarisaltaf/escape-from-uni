package com.escapefromuni.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.escapefromuni.main.components.CollisionComponent;
import com.escapefromuni.main.components.RenderableComponent;

import java.util.ArrayList;

public class Player extends GameObject implements RenderableComponent, CollisionComponent {

    // Texture and Sprite class used from the libGDX library.
    Texture playerTexture;
    Sprite playerSprite;
    // Player speed attribute makes it possible to alter speed during the game.
    boolean sugarCrash = false;
    boolean hasKey = false;
    boolean wonGame = false;
    float playerSpeed = 200f;
    float speedTimer = 0;
    Rectangle hitbox;

    public Player(Vector2 position, float rotation, String playerTexturePath) {
        super(position, rotation);
        this.playerTexture = new Texture(Gdx.files.internal(playerTexturePath));
    }

    public Player(Vector2 position, float rotation) {
        super(position, rotation);
        // Player texture defaults to placeholder player.png
        playerTexture = new Texture(Gdx.files.internal("player.png"));
    }

    public Player(Vector2 position) {
        super(position);
    }

    public void start() {
        // Generates a Sprite object using the player.png texture
        playerTexture = new Texture(Gdx.files.internal("player.png"));
        playerSprite = new Sprite(playerTexture);
        float rectX = position.x;
        float rectY = position.y;
        this.hitbox = new Rectangle(rectX, rectY, playerSprite.getWidth(), playerSprite.getHeight());
    }

    public void update(float deltaTime, GameMap Map) {
        // Check that the player has time left on their speed-up.
        if (speedTimer > 0) {
            speedTimer -= deltaTime;
        }
        // If player is out of time reset the speed and ensure counter is at 0.
        else if (speedTimer <= 0) {
            speedTimer = 0;
            playerSpeed = 200f;
            sugarCrash = false;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.F)) {
            speedUp();
        }

        //Pick up collectibles
        ArrayList<GameObject> pickups = Game.getAllCollidingObjects(hitbox,CollisionLayer.COLLECTIBLE);
        assert pickups != null;
        for (var pickup : pickups){
            if (pickup instanceof Collectible collectible){
                collectible.pickup(this);
            }
        }

        //normalise to have consistent speed regardless of direction, and then scale by move speed and time
        Vector2 desiredVelocity = getInputVector().nor().scl(playerSpeed * deltaTime);
        //Velocity after accounting for collision with walls
        Vector2 resolvedVelocity = CollideWithWalls(desiredVelocity);
        //The resultant velocity is applied to the player
        position.add(resolvedVelocity.x, resolvedVelocity.y);
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
    public void render(SpriteBatch batch, Vector2 cameraPosition) {
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
     * Speeds the player up for 30 seconds.
     */
    public void speedUp() {
        speedTimer += 30;
        if (speedTimer > 45 || this.sugarCrash) {
            this.sugarCrash = true;
            playerSpeed = 100f;
        } else {
            playerSpeed = 300f;
        }

    }

    public void giveKey() {
        this.hasKey = true;
    }

    public boolean getKey() {
        return this.hasKey;
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

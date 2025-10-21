package com.escapefromuni.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.math.Vector2;
import com.escapefromuni.main.components.PhysicsComponent;
import com.escapefromuni.main.components.RenderableComponent;

public class Player extends GameObject implements RenderableComponent {

    // Texture and Sprite class used from the libGXD library.
    Texture playerTexture;
    Sprite playerSprite;
    // Player speed attribute makes it possible to alter speed during the game.
    float playerSpeed = 200f;
    float speedTimer = 0;

    public Player(Vector2 position, float rotation, String playerTexturePath) {
        super(position, rotation);
        this.playerTexture = new Texture(Gdx.files.internal(playerTexturePath));;
    }

    public Player(Vector2 position,float rotation) {
        super(position,rotation);
        // Player texture defaults to placeholder player.png
        playerTexture = new Texture(Gdx.files.internal("player.png"));
    }
    public Player(Vector2 position) {
        super(position);
        // Player texture defaults to placeholder player.png
        playerTexture = new Texture(Gdx.files.internal("player.png"));
    }

    public void start(){
        // Generates a Sprite object using the player.png texture
        playerSprite = new Sprite(playerTexture);
    }

    public void update(float deltaTime) {
        // Check that the player has time left on their speed-up.
        if(speedTimer > 0){
            speedTimer -= deltaTime;
        }
        // If player is out of time reset the speed and ensure counter is at 0.
        else if(speedTimer <= 0){
            speedTimer = 0;
            playerSpeed = 200f;
        }
        Vector2 result = getDesiredDirection().scl(playerSpeed * deltaTime);
        position.add(result.x, result.y);
    }
    @Override
    public void render(SpriteBatch batch,Vector2 cameraPosition) {
        playerSprite.setPosition(position.x - cameraPosition.x - playerSprite.getWidth() / 2f, position.y - cameraPosition.y - playerSprite.getWidth() / 2f);
        playerSprite.draw(batch);
    }
    /**
     * Get the desired direction that the player wants to move in from using the arrow keys or WASD.
     * @return A vector representing the players desired move direction, that has a magnitude of <=1.
     */
    public Vector2 getDesiredDirection(){
        Vector2 direction = new Vector2();
        // Check which keys are being pressed on each frame and move the sprite accordingly.
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            direction.add(1,0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            direction.add(-1,0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
            direction.add(0,1);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
            direction.add(0,-1);
        }
        //Normalises the direction (ensures a consistent magnitude of 1 regardless of desired direction)
        //This is done to avoid situations where the player could move faster by going diagonal (read: Pythagorean Theorem)
        return direction.nor();
    }

    /**
     * Speeds the player up for 30 seconds.
     */
    public void speedUp(){
        speedTimer += 30;
        playerSpeed = 300f;
    }
}

package com.escapefromuni.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Player {

    // Texture and Sprite class used from the libGXD library.
    Texture playerTexture;
    Sprite playerSprite;
    // Player speed attribute makes it possible to alter speed during the game.
    float playerSpeed = 200f;
    float speedTimer = 0;

    public Player(){
        // Player texture currently hardcoded as player.png as there is only one texture
        playerTexture = new Texture(Gdx.files.internal("player.png"));
        // Generates a Sprite object using the player.png texture
        playerSprite = new Sprite(playerTexture);
    }

    public void movePlayer(){
        // Using the dt function makes the speed of the player constant on different hardware.
        float dt = Gdx.graphics.getDeltaTime();
        // Check that the player has time left on their speed-up.
        if(speedTimer > 0){
            speedTimer -= dt;
        }
        // If player is out of time reset the speed and ensure counter is at 0.
        else if(speedTimer <= 0){
            speedTimer = 0;
            playerSpeed = 200f;
        }
        // Check which keys are being pressed on each frame and move the sprite accordingly.
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            playerSprite.translateX(playerSpeed * dt);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            playerSprite.translateX(-playerSpeed * dt);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
            playerSprite.translateY(playerSpeed * dt);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
            playerSprite.translateY(-playerSpeed*dt);
        }

    }

    public void speedUp(){
        speedTimer += 30;
        playerSpeed = 3000f;
    }

}

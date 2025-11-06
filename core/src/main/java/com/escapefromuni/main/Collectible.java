package com.escapefromuni.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.escapefromuni.main.components.RenderableComponent;

public class Collectible extends GameObject implements RenderableComponent {

    Texture collecitibleTexture;
    Sprite collectibleSprite;
    Rectangle hitbox;
    Player player;
    boolean active = true;

    public Collectible(Vector2 givenPosition,Player player){
        position = givenPosition;
        this.collecitibleTexture = new Texture(Gdx.files.internal("defaultCollectible.png"));
        this.collectibleSprite = new Sprite(collecitibleTexture);
        hitbox = new Rectangle(position.x, position.y,collectibleSprite.getWidth(),collectibleSprite.getHeight());
        this.player = player;
    }

    @Override
    public void render(SpriteBatch batch, Vector2 cameraPosition) {
        if(active){
            collectibleSprite.setPosition(position.x, position.y);
            collectibleSprite.draw(batch);
        }

    }

    @Override
    public void update(float deltaTime) {
        if(this.hitbox.overlaps(player.hitbox) && this.active){
            this.active = false;
            player.speedUp();
        }
    }
}

package com.escapefromuni.main.collectables;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.escapefromuni.main.GameObject;
import com.escapefromuni.main.Player;
import com.escapefromuni.main.components.CollisionComponent;
import com.escapefromuni.main.components.RenderableComponent;

public abstract class Collectable extends GameObject implements RenderableComponent, CollisionComponent {
    String imagePath = "defaultCollectible.png";
    Texture collecitibleTexture;
    Sprite collectibleSprite;
    Rectangle hitbox;
    boolean active = true;

    public Collectable(Vector2 position){
        this.position = position;
    }
    public Collectable(Vector2 position, String imagePath){
        this.position = position;
        this.imagePath = imagePath;
    }

    @Override
    public void start() {
        this.collecitibleTexture = new Texture(Gdx.files.internal(imagePath));
        this.collectibleSprite = new Sprite(collecitibleTexture);
        collectibleSprite.setScale(4);
        hitbox = new Rectangle(position.x, position.y,collectibleSprite.getWidth(),collectibleSprite.getHeight());
    }

    @Override
    public void render(SpriteBatch batch) {
        if(active){
            collectibleSprite.setPosition(position.x, position.y);
            collectibleSprite.draw(batch);
        }

    }

    /**
     *
     * @param player Player object
     * Disables the collectible once the player has collected.
     */
    public void pickup(Player player){
        active = false;
    }

    @Override
    public CollisionLayer getCollisionLayer() {
        return CollisionLayer.COLLECTIBLE;
    }

    @Override
    public Boolean isCollidingWith(Rectangle hitboxCheck) {
        return hitbox.overlaps(hitboxCheck);
    }

    @Override
    public Boolean isCollisionEnabled() {
        //only collide when object is active
        return active;
    }
}

package com.escapefromuni.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.escapefromuni.main.components.CollisionComponent;
import com.escapefromuni.main.components.RenderableComponent;

public class Collectible extends GameObject implements RenderableComponent, CollisionComponent {

    String imagePath = "defaultCollectible.png";
    Texture collecitibleTexture;
    Sprite collectibleSprite;
    Rectangle hitbox;
    //Player player;
    boolean active = true;

    public Collectible(Vector2 givenPosition,Player player){
        position = givenPosition;
        //this.player = player;
    }

    @Override
    public void start() {
        this.collecitibleTexture = new Texture(Gdx.files.internal(imagePath));
        this.collectibleSprite = new Sprite(collecitibleTexture);
        hitbox = new Rectangle(position.x, position.y,collectibleSprite.getWidth(),collectibleSprite.getHeight());
    }

    @Override
    public void render(SpriteBatch batch, Vector2 cameraPosition) {
        if(active){
            collectibleSprite.setPosition(position.x, position.y);
            collectibleSprite.draw(batch);
        }

    }
    public void pickup(Player player){
        active = false;
    }

//    @Override
//    public void update(float deltaTime) {
//        if(this.hitbox.overlaps(player.hitbox) && this.active){
//            this.active = false;
//            player.speedUp();
//        }
//    }

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

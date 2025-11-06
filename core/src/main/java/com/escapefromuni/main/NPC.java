package com.escapefromuni.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.escapefromuni.main.components.CollisionComponent;
import com.escapefromuni.main.components.RenderableComponent;

public class NPC extends GameObject implements RenderableComponent, CollisionComponent {

    Texture texture;
    Sprite sprite;
    Rectangle hitbox;

    public NPC(Vector2 position){
        this.position = position;
        texture = new Texture(Gdx.files.internal("professor.png"));
        sprite = new Sprite(texture);
        hitbox = new Rectangle(position.x, position.y, texture.getWidth(), texture.getHeight());
    }

    @Override
    public void update(float deltaTime) {
        if(Game.isCollidingWithLayer(hitbox,CollisionLayer.PLAYER)){
            if(Player.hasKey()){
                // make player win the game
                System.out.println("You win");
                //player.wonGame = true;
            }else{
                System.out.println("You need the key");
            }
        }
    }

    @Override
    public void render(SpriteBatch batch, Vector2 cameraPosition) {
        sprite.setPosition(position.x, position.y);
        sprite.draw(batch);
    }

    @Override
    public CollisionLayer getCollisionLayer() {
        return CollisionLayer.NPC;
    }

    @Override
    public Boolean isCollidingWith(Rectangle hitboxCheck) {
        return hitbox.overlaps(hitboxCheck);
    }

    @Override
    public Boolean isCollisionEnabled() {
        return true;
    }
}

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
    String requiredItem;

    public NPC(Vector2 position,String textureDir,String requiredItem){
        this.position = position;
        texture = new Texture(Gdx.files.internal(textureDir));
        sprite = new Sprite(texture);
        sprite.setScale(4);
        hitbox = new Rectangle(position.x, position.y, texture.getWidth(), texture.getHeight());
        this.requiredItem = requiredItem;
    }

    @Override
    public void update(float deltaTime) {
        GameObject playerObject = Game.getFirstCollidingObjectInLayer(hitbox,CollisionLayer.PLAYER);
        if(playerObject != null){
            if (playerObject instanceof Player player){
                boolean success =  player.TakeItem(requiredItem);
                if (success){
                    Game.gameState = Game.GameState.WIN;
                }else{
                    System.out.println("You need the " + requiredItem);
                }
            }else{
                throw new RuntimeException("Unexpected item in the bagging area");
            }
        }
    }

    @Override
    public void render(SpriteBatch batch) {
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

package com.escapefromuni.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.escapefromuni.main.components.CollisionComponent;
import com.escapefromuni.main.components.RenderableComponent;
import com.escapefromuni.main.ui.GameMessageHandler;

public class NPC extends GameObject implements RenderableComponent, CollisionComponent {

    // Stores the texture and sprit for the npc
    // Stores the hitbox of the npc
    // Stores the item needed to interract with the npc
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

    /**
     * Update the NPC to check if it has been interracted with.
     * @param deltaTime : The time in ms since the last update
     */
    @Override
    public void update(float deltaTime) {
        GameObject playerObject = Game.getFirstCollidingObjectInLayer(hitbox,CollisionLayer.PLAYER);
        if(playerObject != null){
            if (playerObject instanceof Player player){
                boolean success =  player.TakeItem(requiredItem);
                if (success){
                    Game.gameState = Game.GameState.WIN;
                }else{
                    GameMessageHandler.ShowMessage("You need the " + requiredItem,4);
                }
            }else{
                throw new RuntimeException("Collided with object on player layer that is not of type Player.");
            }
        }
    }

    // Renderer for the npc
    @Override
    public void render(SpriteBatch batch) {
        sprite.setPosition(position.x, position.y);
        sprite.draw(batch);
    }

    //Returns the collsion layer of the npc
    @Override
    public CollisionLayer getCollisionLayer() {
        return CollisionLayer.NPC;
    }

    //Returns true if the given hitbox collides with npc hitbox
    @Override
    public Boolean isCollidingWith(Rectangle hitboxCheck) {
        return hitbox.overlaps(hitboxCheck);
    }

    //Returns true if the hitbox has collision
    @Override
    public Boolean isCollisionEnabled() {
        return true;
    }
}

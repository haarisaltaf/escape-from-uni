package com.escapefromuni.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.escapefromuni.main.components.RenderableComponent;

public class NPC extends GameObject implements RenderableComponent {

    Texture npcTexture;
    Sprite npcSprite;
    Rectangle npcHitbox;
    Player player;

    public NPC(Vector2 position, Player player){
        this.position = position;
        npcTexture = new Texture(Gdx.files.internal("professor.png"));
        npcSprite = new Sprite(npcTexture);
        npcHitbox = new Rectangle(position.x, position.y, npcTexture.getWidth(),npcTexture.getHeight());
        this.player =player;
    }

    @Override
    public void update(float deltaTime) {
        if(npcHitbox.overlaps(player.hitbox)){
            if(player.getKey()){
                // make player win the game
                System.out.println("You win");
                player.wonGame = true;
            }else{
                System.out.println("You need the key");
            }
        }
    }

    @Override
    public void render(SpriteBatch batch, Vector2 cameraPosition) {
        npcSprite.setPosition(position.x, position.y);
        npcSprite.draw(batch);
    }
}

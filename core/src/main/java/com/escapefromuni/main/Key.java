package com.escapefromuni.main;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.escapefromuni.main.components.RenderableComponent;

public class Key extends Collectible implements RenderableComponent {


    public Key(Vector2 givenPosition, Player player) {
        super(givenPosition, player);
        super.imagePath = "Key.png";
    }

    @Override
    public void update(float deltaTime) {
        if(this.hitbox.overlaps(player.hitbox)){
            player.giveKey();
            this.active = false;
        }
    }

    @Override
    public void render(SpriteBatch batch, Vector2 cameraPosition) {
        if(active){
            collectibleSprite.setPosition(position.x, position.y);
        }else if(!active){
            collectibleSprite.setPosition(player.position.x,player.position.y);
        }
        collectibleSprite.draw(batch);
    }

}

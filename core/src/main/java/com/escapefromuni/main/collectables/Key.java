package com.escapefromuni.main.collectables;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.escapefromuni.main.Player;
import com.escapefromuni.main.components.RenderableComponent;

public class Key extends Item implements RenderableComponent {
    Player holder;

    public Key(Vector2 givenPosition) {
        super(givenPosition,"glasses.png","key");
    }
    public Key(Vector2 givenPosition,String imagePath) {
        super(givenPosition,imagePath,"key");
    }
    @Override
    public void pickup(Player player){
        super.pickup(player);
        player.giveKey();
        holder = player;
    }
    @Override
    public void render(SpriteBatch batch) {
//        if(active){
//            collectibleSprite.setPosition(position.x, position.y);
//        }else{
//            collectibleSprite.setPosition(holder.position.x,holder.position.y);
//        }
        collectibleSprite.setPosition(position.x,position.y);
        collectibleSprite.draw(batch);
    }

}

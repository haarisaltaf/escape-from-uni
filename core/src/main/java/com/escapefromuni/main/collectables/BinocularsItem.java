package com.escapefromuni.main.collectables;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.escapefromuni.main.Player;
import com.escapefromuni.main.components.RenderableComponent;
import com.escapefromuni.main.Game;

public class BinocularsItem extends Item implements RenderableComponent {
    Player holder;
    public BinocularsItem(Vector2 givenPosition) {
        super(givenPosition,"Binoculars.png","binoculars");
    }
    public BinocularsItem(Vector2 givenPosition,String imagePath) {
        super(givenPosition,imagePath,"binoculars");
    }
    @Override
    public void pickup(Player player){
        super.pickup(player);
        Game.GetActiveCamera().binoculars();
        holder = player;
    }
    @Override
    public void render(SpriteBatch batch) {
 //       if(active){
//            collectibleSprite.setPosition(position.x, position.y);
//        }else{
//            collectibleSprite.setPosition(holder.position.x,holder.position.y);
//        }
        collectibleSprite.setPosition(position.x + 3,position.y + 3);
        collectibleSprite.draw(batch);
    }

}

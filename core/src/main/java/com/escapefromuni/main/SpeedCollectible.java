package com.escapefromuni.main;

import com.badlogic.gdx.math.Vector2;

public class SpeedCollectible extends Collectible{
    public SpeedCollectible(Vector2 position, String imagePath) {
        super(position, imagePath);
    }

    public SpeedCollectible(Vector2 position) {
        super(position);
    }

    @Override
    public void pickup(Player player){
        super.pickup(player);
        player.speedUp();
    }
}

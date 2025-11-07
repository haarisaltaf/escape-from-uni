package com.escapefromuni.main.collectables;

import com.badlogic.gdx.math.Vector2;
import com.escapefromuni.main.Player;

public class SpeedCollectable extends Collectable {
    public SpeedCollectable(Vector2 position, String imagePath) {
        super(position, imagePath);
    }

    public SpeedCollectable(Vector2 position) {
        super(position);
    }

    @Override
    public void pickup(Player player){
        super.pickup(player);
        player.speedUp();
    }
}

package com.escapefromuni.main.collectables;

import com.badlogic.gdx.math.Vector2;
import com.escapefromuni.main.Player;

public class SlowCollectable extends Collectable {
    public SlowCollectable(Vector2 position, String imagePath) {
        super(position, imagePath);
    }

    public SlowCollectable(Vector2 position) {
        super(position,"BearTrap.png");
    }

    @Override
    public void pickup(Player player){
        super.pickup(player);
        player.slowDown();
    }
}

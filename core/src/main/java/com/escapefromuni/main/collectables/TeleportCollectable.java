package com.escapefromuni.main.collectables;

import com.badlogic.gdx.math.Vector2;
import com.escapefromuni.main.Player;

public class TeleportCollectable extends Collectable {
    public TeleportCollectable(Vector2 position, String imagePath) {
        super(position, imagePath);
    }

    public TeleportCollectable(Vector2 position) {
        super(position,"RandomTeleport.png");
    }

    @Override
    public void pickup(Player player){
        super.pickup(player);
        player.teleportRandom();
    }
}

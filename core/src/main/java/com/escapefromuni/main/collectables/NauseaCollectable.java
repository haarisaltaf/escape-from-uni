package com.escapefromuni.main.collectables;

import com.badlogic.gdx.math.Vector2;
import com.escapefromuni.main.Player;
import com.escapefromuni.main.CameraController;
import com.escapefromuni.main.Game;



public class NauseaCollectable extends Collectable {
    
    public NauseaCollectable(Vector2 position, String imagePath) {
        super(position, imagePath);
    }

    public NauseaCollectable(Vector2 position) {
        super(position,"defaultCollectible.png");
    }

    @Override
    public void pickup(Player player){
        super.pickup(player);
        Game.GetActiveCamera().nausea();
    }
}

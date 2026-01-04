package com.escapefromuni.main.collectables;

import com.badlogic.gdx.math.Vector2;
import com.escapefromuni.main.Player;
import com.escapefromuni.main.Game;



public class DeathCollectable extends Collectable {
    
    public DeathCollectable(Vector2 position, String imagePath) {
        super(position, imagePath);
    }

    public DeathCollectable(Vector2 position) {
        super(position,"Death.png");
    }

    @Override
    public void pickup(Player player){
        super.pickup(player);
        Game.gameState = Game.GameState.LOSE;
    }
}

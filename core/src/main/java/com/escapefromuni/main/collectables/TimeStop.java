package com.escapefromuni.main.collectables;

import com.badlogic.gdx.math.Vector2;
import com.escapefromuni.main.Player;
import com.escapefromuni.main.Game;

public class TimeStop extends Collectable {
    public TimeStop(Vector2 position, String imagePath) {
        super(position, imagePath);
    }

    public TimeStop(Vector2 position) {
        super(position,"TimeStop.png");
    }

    @Override
    public void pickup(Player player){
        super.pickup(player);
	Game.timer.addTime(5f);
    }
}

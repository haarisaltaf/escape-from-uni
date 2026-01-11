package com.escapefromuni.main.collectables;

import com.badlogic.gdx.math.Vector2;
import com.escapefromuni.main.Player;
import com.escapefromuni.main.Game;
import com.escapefromuni.main.ui.Achievements;

public class TimeStop extends Collectable {
    Achievements achievements = null;

    public TimeStop(Vector2 position, String imagePath) {
        super(position, imagePath);
    }

    public TimeStop(Vector2 position) {
        super(position,"TimeStop.png");
    }

    public TimeStop(Vector2 position, Achievements achievementsP) {
        super(position,"TimeStop.png");
	achievements = achievementsP;
	}

    @Override
    public void pickup(Player player){
        super.pickup(player);
	Game.timer.addTime(5f);
	if (achievements != null) {
		achievements.achieveAchievement("THE WORLD.");
	}
    }
}

package com.escapefromuni.main.collectables;

import com.badlogic.gdx.math.Vector2;
import com.escapefromuni.main.Player;
import com.escapefromuni.main.CameraController;
import com.escapefromuni.main.Game;
import com.escapefromuni.main.ui.Achievements;


public class NauseaCollectable extends Collectable {

    Achievements achievements = null;

    public NauseaCollectable(Vector2 givenPosition, String imagePath) {
        super(givenPosition, imagePath);
    }

    public NauseaCollectable(Vector2 givenPosition) {
        super(givenPosition,"banana.png");
    }

    public NauseaCollectable(Vector2 givenPosition, Achievements achievementsP) {
        super(givenPosition,"banana.png");
	achievements = achievementsP;
    }

    @Override
    public void pickup(Player player){
        super.pickup(player);
        Game.GetActiveCamera().nausea();
	if (achievements != null) {
		achievements.achieveAchievement("nana.png");
	}
    }
}

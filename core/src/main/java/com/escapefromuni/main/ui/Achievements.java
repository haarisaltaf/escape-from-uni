
package com.escapefromuni.main.ui;

import com.escapefromuni.main.ui.GameMessageHandler;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

public class Achievements {

	HashMap<String, Boolean> ACHIEVEMENTS = new HashMap<String, Boolean>();

	public void init() {
		ACHIEVEMENTS.put("Sugar Rush!", false);
		ACHIEVEMENTS.put("Sugar Crash!!", false);
		ACHIEVEMENTS.put("All-Purpose Telescope", false);
		ACHIEVEMENTS.put("Contacts", false);
		ACHIEVEMENTS.put("nana.png", false);
		ACHIEVEMENTS.put("THE WORLD.", false);
	}

	public HashMap<String, Boolean> getAchievements() {
		return ACHIEVEMENTS;
	}

	public Collection<Boolean> getAchieved() {
		return ACHIEVEMENTS.values();
	}

	public Boolean achieveAchievement(String achievement) {
		if (ACHIEVEMENTS.get(achievement) == false) {
			// TODO: FIX FOR SCREEN POSITION
			GameMessageHandler.ShowMessage("ACHIEVEMENT GET!\n" + achievement,5); 
			ACHIEVEMENTS.put(achievement, true);
			return true;
		} else if (ACHIEVEMENTS.get(achievement) == true){
			System.out.println("Already Achieved");
			return false;
		} else {
			System.out.println("Ensure correct achievement name is used.");
			return false;
		}
	}

	public void testAchievement() {
		HashMap<String, Boolean> all = getAchievements();
		if (achieveAchievement("Contacts")) {
			System.out.println("Achieved Successfully");
		} else {
			System.out.println("Failed to Achieve");
		}
	}
}



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
}


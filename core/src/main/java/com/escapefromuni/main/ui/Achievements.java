
package com.escapefromuni.main.ui;

import com.escapefromuni.main.ui.GameMessageHandler;
import java.util.Arrays;

public class Achievements {

	String[] ACHIEVEMENTS = {
		"Sugar Rush!",
		"Sugar Crash!!",
		"Glasses on Glasses!",
		"KEYYYYYYYYY",
		"The End?",
	};

	public String[] getAchievements() {
		return ACHIEVEMENTS;
	}

	public Boolean achieveAchievement(String achievement) {
		if (Arrays.asList(ACHIEVEMENTS).contains(achievement)) {
			// TODO: FIX FOR SCREEN POSITION
			GameMessageHandler.ShowMessage(achievement,5); 
			return true;
		} else {
			System.out.println("Ensure correct achievement name is used.");
			return false;
		}
	}

	public void testAchievement() {
		String[] all = getAchievements();
		if (achieveAchievement(all[0])) {
			System.out.println("Achieved Successfully");
		} else {
			System.out.println("Failed to Achieve");
		}
	}
}



package com.escapefromuni.ui;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Collection;
import java.util.ArrayList;
import java.util.HashMap;

import com.escapefromuni.main.ui.Achievements;

public class AchievementsTest {
	
    private Achievements achievements = new Achievements();
    private Achievements emptyAchievement = new Achievements();
    private HashMap<String, Boolean> achievementsTest;
    private Collection<Boolean> allNotAchieved;
    private HashMap<String, Boolean> ACHIEVEMENTS;

    @BeforeEach
    void setup() {
	achievements.init();
	emptyAchievement.init();
    }
	
    @Test
	void testGetAchievements() {
		achievementsTest = new HashMap<String, Boolean>();
		achievementsTest.put("Sugar Rush!", false);
		achievementsTest.put("Sugar Crash!!", false);
		achievementsTest.put("All-Purpose Telescope", false);
		achievementsTest.put("Contacts", false);
		achievementsTest.put("nana.png", false);
		achievementsTest.put("THE WORLD.", false);
		assertEquals(achievements.getAchievements(), achievementsTest);
	}


    @Test
	void testGetAchieved() {
		assertEquals(
		    new ArrayList<>(achievements.getAchieved()), 
		    new ArrayList<>(emptyAchievement.getAchieved())
		);
	}
}

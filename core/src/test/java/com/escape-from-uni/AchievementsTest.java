package com.escapefromuni.ui;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Collection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Arrays;

import com.escapefromuni.main.ui.Achievements;
import com.escapefromuni.main.ui.GameMessageHandler;

public class AchievementsTest {

	private Achievements achievements;
	private HashMap<String, Boolean> expectedAchievements;

	@BeforeEach
	void setup() {
	    achievements = new Achievements();
	    achievements.init();

	    // expectedAchievemetns = initial state/ no achievements
	    expectedAchievements = new HashMap<>();
	    expectedAchievements.put("Sugar Rush!", false);
	    expectedAchievements.put("Sugar Crash!!", false);
	    expectedAchievements.put("All-Purpose Telescope", false);
	    expectedAchievements.put("Contacts", false);
	    expectedAchievements.put("nana.png", false);
	    expectedAchievements.put("THE WORLD.", false);
	}

	@Test
	void testInitCreatesAllAchievements() {
	    HashMap<String, Boolean> result = achievements.getAchievements();
	    assertEquals(6, result.size());
	}

	@Test
	void testInitAllAchievementsStartFalse() {
	    Collection<Boolean> achieved = achievements.getAchieved();
	    for (Boolean value : achieved) {
		assertFalse(value);
	    }
	}

	@Test
	void testInitContainsExpectedAchievementNames() {
	    HashMap<String, Boolean> result = achievements.getAchievements();

	    assertTrue(result.containsKey("Sugar Rush!"));
	    assertTrue(result.containsKey("Sugar Crash!!"));
	    assertTrue(result.containsKey("All-Purpose Telescope"));
	    assertTrue(result.containsKey("Contacts"));
	    assertTrue(result.containsKey("nana.png"));
	    assertTrue(result.containsKey("THE WORLD."));
	}

	@Test
	void testInitCalledTwiceDoesNotDuplicate() {
	    achievements.init();
	    assertEquals(6, achievements.getAchievements().size());
	}


	@Test
	void testGetAchievements() {
	    assertEquals(expectedAchievements, achievements.getAchievements());
	}

	@Test
	void testGetAchievementsNotEmpty() {
	    assertNotNull(achievements.getAchievements());
	}

	@Test
	void testGetAchievedStartAllFalse() {
	    Collection<Boolean> achievedAchievements = achievements.getAchieved();

	    ArrayList<Boolean> expected = new ArrayList<>(Arrays.asList(
		false, false, false, false, false, false));

	    assertEquals(new ArrayList<>(expected).size(),
		new ArrayList<>(achievedAchievements).size());

	    for (Boolean isAchieved : achievedAchievements) {
		assertFalse(isAchieved);
	    }
	}

	@Test
	void testGetAchievedReturnsCorrectCount() {
	    assertEquals(6, achievements.getAchieved().size());
	}

	@Test
	void testGetAchievedMarksAsAchieved() {
	    try (MockedStatic<GameMessageHandler> mockedHandler = 
		    Mockito.mockStatic(GameMessageHandler.class)) {
		
		achievements.achieveAchievement("Sugar Rush!");
		
		Collection<Boolean> achieved = achievements.getAchieved();
		assertTrue(achieved.contains(true));
	    }
	}

	@Test
	void testAchieveAchievementReturnsFalseIfAlreadyAchieved() {
	    try (MockedStatic<GameMessageHandler> mockedHandler = 
		    Mockito.mockStatic(GameMessageHandler.class)) {
		
		achievements.achieveAchievement("Sugar Rush!"); 
		Boolean result = achievements.achieveAchievement("Sugar Rush!");
		assertFalse(result);
	    }
	}

	@Test
	void testAchieveAchievementDoesNotAffectOtherAchievements() {
	    try (MockedStatic<GameMessageHandler> mockedHandler = 
		    Mockito.mockStatic(GameMessageHandler.class)) {
		
		achievements.achieveAchievement("Sugar Rush!");
		
		assertFalse(achievements.getAchievements().get("Sugar Crash!!"));
		assertFalse(achievements.getAchievements().get("Contacts"));
		assertFalse(achievements.getAchievements().get("nana.png"));
	    }
	}

	@Test
	void testAchieveAchievementCallsMessageHandlerCorrectly() {
	    try (MockedStatic<GameMessageHandler> mockedHandler = 
		    Mockito.mockStatic(GameMessageHandler.class)) {
		
		achievements.achieveAchievement("Sugar Rush!");
		
		mockedHandler.verify(() -> GameMessageHandler.ShowMessage("ACHIEVEMENT GET!\nSugar Rush!", 5));
	    }
	}

	@Test
	void testAchieveAchievementDoesNotCallMessageForAlreadyAchieved() {
	    try (MockedStatic<GameMessageHandler> mockedHandler = 
		    Mockito.mockStatic(GameMessageHandler.class)) {
		
		achievements.achieveAchievement("Sugar Rush!");
		mockedHandler.clearInvocations();
		achievements.achieveAchievement("Sugar Rush!");
		
		mockedHandler.verify(() -> GameMessageHandler.ShowMessage(anyString(), anyInt()), never());
	    }
	}

	@Test
	void testAchieveAllAchievements() {
	    try (MockedStatic<GameMessageHandler> mockedHandler = 
		    Mockito.mockStatic(GameMessageHandler.class)) {
		
		achievements.achieveAchievement("Sugar Rush!");
		achievements.achieveAchievement("Sugar Crash!!");
		achievements.achieveAchievement("All-Purpose Telescope");
		achievements.achieveAchievement("Contacts");
		achievements.achieveAchievement("nana.png");
		achievements.achieveAchievement("THE WORLD.");
		
		for (Boolean value : achievements.getAchieved()) {
		    assertTrue(value);
		}
	    }
	}

	@Test
	void testAchieveSugarRush() {
	    try (MockedStatic<GameMessageHandler> mockedHandler = 
		    Mockito.mockStatic(GameMessageHandler.class)) {
		
		assertTrue(achievements.achieveAchievement("Sugar Rush!"));
		assertTrue(achievements.getAchievements().get("Sugar Rush!"));
	    }
	}

	@Test
	void testAchieveSugarCrash() {
	    try (MockedStatic<GameMessageHandler> mockedHandler = 
		    Mockito.mockStatic(GameMessageHandler.class)) {
		
		assertTrue(achievements.achieveAchievement("Sugar Crash!!"));
		assertTrue(achievements.getAchievements().get("Sugar Crash!!"));
	    }
	}

	@Test
	void testAchieveAllPurposeTelescope() {
	    try (MockedStatic<GameMessageHandler> mockedHandler = 
		    Mockito.mockStatic(GameMessageHandler.class)) {
		
		assertTrue(achievements.achieveAchievement("All-Purpose Telescope"));
		assertTrue(achievements.getAchievements().get("All-Purpose Telescope"));
	    }
	}

	@Test
	void testAchieveContacts() {
	    try (MockedStatic<GameMessageHandler> mockedHandler = 
		    Mockito.mockStatic(GameMessageHandler.class)) {
		
		assertTrue(achievements.achieveAchievement("Contacts"));
		assertTrue(achievements.getAchievements().get("Contacts"));
	    }
	}

	@Test
	void testAchieveNanaPng() {
	    try (MockedStatic<GameMessageHandler> mockedHandler = 
		    Mockito.mockStatic(GameMessageHandler.class)) {
		
		assertTrue(achievements.achieveAchievement("nana.png"));
		assertTrue(achievements.getAchievements().get("nana.png"));
	    }
	}

	@Test
	void testAchieveTheWorld() {
	    try (MockedStatic<GameMessageHandler> mockedHandler = 
		    Mockito.mockStatic(GameMessageHandler.class)) {
		
		assertTrue(achievements.achieveAchievement("THE WORLD."));
		assertTrue(achievements.getAchievements().get("THE WORLD."));
	    }
	}
}

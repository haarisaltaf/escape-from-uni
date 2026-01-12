package com.escapefromuni.ui;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class GameTimerTest {

	private GameTimerLogic gameTimer;
	
	@BeforeEach
	void setup() {
		gameTimer = new GameTimerLogic(300f);
	}
	
	@Test
	void testConstructorInitialTime() {
		GameTimerLogic timer = new GameTimerLogic(300f);
		assertEquals(300f, timer.GetTime());
	}
	
	@Test
	void testConstructorNegativeTime() {
		GameTimerLogic timer = new GameTimerLogic(-50f);
		assertEquals(-50f, timer.GetTime());
	    }

	@Test
	void testConstructorLargeTime() {
		GameTimerLogic timer = new GameTimerLogic(9999f);
		assertEquals(9999f, timer.GetTime());
	}

	
	@Test
	void testGetTimeString() {
		assertEquals("05:00", GameTimerLogic.GetTimeString(300f));
		assertEquals("01:30", GameTimerLogic.GetTimeString(90f));
		assertEquals("00:45", GameTimerLogic.GetTimeString(45f));
	}
	
	@Test
	void testGetTimeStringZeroAndNegative() {
		assertEquals("00:00", GameTimerLogic.GetTimeString(0f));
		assertEquals("00:00", GameTimerLogic.GetTimeString(-1f));
		assertEquals("00:00", GameTimerLogic.GetTimeString(-100f));
	}
	
	@Test
	void testGetTimeStringLargeValues() {
		assertEquals("10:30", GameTimerLogic.GetTimeString(630f));
		assertEquals("12:34", GameTimerLogic.GetTimeString(754f));
		assertEquals("59:59", GameTimerLogic.GetTimeString(3599f));
	}
	
	@Test
	void testGetTimeFloorsFloats() {
		assertEquals("01:30", GameTimerLogic.GetTimeString(90.5f));
		assertEquals("01:30", GameTimerLogic.GetTimeString(90.9f));
	    }
	
	@Test
	void testUpdateDecrementsTime() {
		float initialTime = gameTimer.GetTime();
		gameTimer.update(1f);
		assertEquals(initialTime - 1f, gameTimer.GetTime());
	}
	
	@Test
	void testUpdateWithZeroTimeChange() {
		float initialTime = gameTimer.GetTime();
		gameTimer.update(0f);
		assertEquals(initialTime, gameTimer.GetTime());
	}
	
	@Test
	void testUpdateRepeated() {
		gameTimer.update(10f);
		gameTimer.update(20f);
		gameTimer.update(30f);
		assertEquals(240f, gameTimer.GetTime());  // 300 - 60 = 240
	}

	@Test
	void testUpdateNegativeTime() {
		gameTimer.update(350f);
		assertTrue(gameTimer.GetTime() < 0);
	    }

	@Test
	void testUpdateWithSmallChange() {
		float initialTime = gameTimer.GetTime();
		gameTimer.update(0.016f);  // ~60fps frame time
		assertEquals(initialTime - 0.016f, gameTimer.GetTime(), 0.001f);
	    }


	@Test
	void testAddTimePositive() {
		float initialTime = gameTimer.GetTime();
		gameTimer.addTime(5f);
		assertEquals(initialTime + 5f, gameTimer.GetTime());
	}

	@Test
	void testAddTimeNegative() {
		float initialTime = gameTimer.GetTime();
		gameTimer.addTime(-10f);
		assertEquals(initialTime - 10f, gameTimer.GetTime());
	}

	@Test
	void testAddTimeZero() {
		float initialTime = gameTimer.GetTime();
		gameTimer.addTime(0f);
		assertEquals(initialTime, gameTimer.GetTime());
	}


	@Test
	void testTimerPausedStartsFalse() {
		GameTimerLogic freshTimer = new GameTimerLogic(300f);
		assertFalse(freshTimer.getTimerPaused());
	}

	@Test
	void testSetTimerPausedTrue() {
		gameTimer.setTimerPaused(true);
		assertTrue(gameTimer.getTimerPaused());
	}

	@Test
	void testSetTimerPausedFalse() {
		gameTimer.setTimerPaused(true);
		gameTimer.setTimerPaused(false);
		assertFalse(gameTimer.getTimerPaused());
	}
}

class GameTimerLogic {
	/* Game Timer Logic class that is purely for debugging without having
	 * to implement some weird workaround for BitmapFont so just copied the
	 * logic itself to test directly.
	 * */
	private float time;
	private int eventCounter = 0;
	private boolean timerPaused = false;

	public GameTimerLogic(float startTime) {
		time = startTime;
	}

	/**
	* If the game time drops below zero the GameState is set to lose.
	* 
	* @param deltaTime : The time in ms since the last update
	*/
	public void update(float deltaTime) {
		if (!timerPaused) {
			time -= deltaTime;
		}
	}

	/**
	* Converts a time given in seconds to minutes and seconds instead.
	* 
	* @param timeSeconds The input time in seconds.
	* @return A formatted string in the form MM:SS
	*/
	public static String GetTimeString(float timeSeconds) {
		if (timeSeconds <= 0) { return "00:00"; }

		float minutes = (timeSeconds % 3600) / 60;
		float seconds = timeSeconds % 60;

		return String.format("%02d:%02d", (int) Math.floor(minutes), (int) Math.floor(seconds));
	}

	// Returns the time left on the timer
	public float GetTime() {
		return time;
	}

	// Adds 5 seconds to timer
	public void addTime(float seconds) {
		time += seconds;
	}

	// Sets the event counter
	public void setEventCounter(int eventCounter) {
		this.eventCounter = eventCounter;
	}

	public void setTimerPaused(boolean isPaused) {
		timerPaused = isPaused;
	}

	public Boolean getTimerPaused() {
		return timerPaused;
	}
}

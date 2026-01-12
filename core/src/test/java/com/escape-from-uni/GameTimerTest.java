package com.escapefromuni.ui;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class GameTimerTest {
    private GameTimerLogic gameTimer = new GameTimerLogic(300f);

    @Test
    void testGetTimeString() {
        assertEquals("05:00", gameTimer.GetTimeString(300f));
        assertEquals("00:00", gameTimer.GetTimeString(0f));
        assertEquals("10:30", gameTimer.GetTimeString(630f));
        assertEquals("00:45", gameTimer.GetTimeString(45f));
        assertEquals("12:34", gameTimer.GetTimeString(754f));
        assertEquals("00:00", gameTimer.GetTimeString(-90f));
    }

    @Test
    void testAddTime() {
        float initialTime = gameTimer.GetTime();
        gameTimer.addTime(5f);
        assertEquals(initialTime + 5f, gameTimer.GetTime());

        initialTime = gameTimer.GetTime();
        gameTimer.addTime(0f);
        assertEquals(initialTime + 0f, gameTimer.GetTime());

        initialTime = gameTimer.GetTime();
        gameTimer.addTime(-10f);
        assertEquals(initialTime - 10f, gameTimer.GetTime());
    }

    @Test
    void testUpdateTimer() {
        float initialTime = gameTimer.GetTime();
        gameTimer.update(1f);
        assertEquals(initialTime - 1f, gameTimer.GetTime());

        gameTimer.update(2.5f);
        assertEquals(initialTime - 3.5f, gameTimer.GetTime());
    }

    @Test
    void testPauseTimer() {
        gameTimer.setTimerPaused(true);
        gameTimer.update(1f);
        assertEquals(gameTimer.getTimerPaused(), true);

        gameTimer.setTimerPaused(false);
        gameTimer.update(1f);
        assertEquals(gameTimer.getTimerPaused(), false);
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
    	time -= deltaTime;
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

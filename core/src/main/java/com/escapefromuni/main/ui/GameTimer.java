package com.escapefromuni.main.ui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.escapefromuni.main.Game;

public class GameTimer extends UIElement {
    // Store the game time and event counter
    // Creates a font used to output time
    float time;
    BitmapFont font;
    int eventCounter = 0;
    boolean timerPaused = false;

    public GameTimer(Vector2 relativeScreenPosition) {
        super(relativeScreenPosition);
        time = 300;
        font = new BitmapFont();
    }

    /**
     * If the game time drops below zero the GameState is set to lose.
     * 
     * @param deltaTime : The time in ms since the last update
     */
    @Override
    public void update(float deltaTime) {
        if (Game.gameState == Game.GameState.PLAYING && timerPaused == false) {
            time -= deltaTime;
            if (time < 0) {
                Game.gameState = Game.GameState.LOSE;
            }
        }
    }

    /**
     * Converts a time given in seconds to minutes and seconds instead.
     * 
     * @param timeSeconds The input time in seconds.
     * @return A formatted string in the form MM:SS
     */
    public static String GetTimeString(float timeSeconds) {
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

    // Pause timer
    public void pauseTimer(float seconds) {
        System.out.println("We here");
        timerPaused = true;
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                timerPaused = false;
            }
        }, seconds);
    }

    // Render the timer
    @Override
    public void render(SpriteBatch batch) {
        font.draw(batch, "Time Left : " + GetTimeString(time) + "\n Events : " + eventCounter, position.x, position.y);
    }

    // Set position on screen
    @Override
    public void positionOnScreen(Vector2 cameraPosition, Vector2 screenSize, float zoom) {
        super.positionOnScreen(cameraPosition, screenSize, zoom);
        font.getData().setScale(3 * zoom);
    }

    // Sets the event counter
    public void setEventCounter(int eventCounter) {
        this.eventCounter = eventCounter;
    }

}

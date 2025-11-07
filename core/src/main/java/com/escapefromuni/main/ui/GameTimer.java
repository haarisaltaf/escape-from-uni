package com.escapefromuni.main.ui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.escapefromuni.main.Game;

public class GameTimer extends UIElement {

    float time;
    BitmapFont font;

    public GameTimer(Vector2 relativeScreenPosition){
        super(relativeScreenPosition);
        time = 300;
        font = new BitmapFont();
    }

    @Override
    public void update(float deltaTime){
        if(Game.gameState == Game.GameState.PLAYING) {
            time -= deltaTime;
        } else if (time < 0){
            System.out.println("Time Up");
        }
    }
    public static String GetTimeString(float timeSeconds){
        float hours = timeSeconds / 3600;
        float minutes = (timeSeconds % 3600) / 60;
        float seconds = timeSeconds % 60;
        float milliSeconds = timeSeconds % 1f;
        if (hours > 1){
            return String.format("%02d:%02d:%02d:%02d", (int)Math.floor(hours), (int)Math.floor(minutes), (int)Math.floor(seconds),(int)Math.floor(milliSeconds * 100));
        }else{
            return String.format("%02d:%02d:%02d", (int)Math.floor(minutes), (int)Math.floor(seconds),(int)Math.floor(milliSeconds * 100));
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        font.draw(batch,"Time : " + GetTimeString(time), position.x,position.y);
    }
    @Override
    public void positionOnScreen(Vector2 cameraPosition, Vector2 screenSize,float zoom) {
        super.positionOnScreen(cameraPosition,screenSize,zoom);
        font.getData().setScale(3 * zoom);
    }
}

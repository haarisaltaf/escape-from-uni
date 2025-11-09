package com.escapefromuni.main.ui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.escapefromuni.main.Game;

public class GameMessageHandler extends UIElement{
    BitmapFont font;
    static GameMessageHandler instance;
    float time;
    String message;
    Boolean visible = false;

    public GameMessageHandler(Vector2 relativeScreenPosition){
        super(relativeScreenPosition);
        instance = this;
        font = new BitmapFont();
    }

    @Override
    public void update(float deltaTime){
        if(Game.gameState == Game.GameState.PLAYING) {
            time -= deltaTime;
            if (time < 0){
                visible = false;
            }
        }
    }
    public static void ShowMessage(String message,float time){
        instance.time = time;
        instance.message = message;
        instance.visible = true;
    }

    @Override
    public void render(SpriteBatch batch) {
        if (visible)
            font.draw(batch,message, position.x,position.y);
    }
    @Override
    public void positionOnScreen(Vector2 cameraPosition, Vector2 screenSize,float zoom) {
        super.positionOnScreen(cameraPosition,screenSize,zoom);
        font.getData().setScale(3 * zoom);
    }
}

package com.escapefromuni.main.ui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.escapefromuni.main.Game;

public class GameMessageHandler extends UIElement{
    BitmapFont font;
    public static GameMessageHandler instance;
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

    /**
     * Displays a message onto the screen. This can be good for simple text messages or
     * output that you want to display onto the screen. This will also overwrite any
     * previous messages that are currently displaying on screen but haven't disappeared
     * yet.
     * @param message A standard string text message.
     * @param time How long the message remains on screen, in seconds.
     */
    public static void ShowMessage(String message,float time){
        instance.time = time;
        instance.message = message;
        instance.visible = true;
    }

    public static void StopMessage() {
        instance.visible = false;
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

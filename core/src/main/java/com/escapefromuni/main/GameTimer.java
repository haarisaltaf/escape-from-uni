package com.escapefromuni.main;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.escapefromuni.main.components.RenderableComponent;

import javax.swing.text.NumberFormatter;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class GameTimer extends GameObject implements RenderableComponent {

    float time;
    BitmapFont font;
    Player Player;
    NumberFormat formatter = new DecimalFormat("0.00");

    public GameTimer(Player player){
        time = 0;
        font = new BitmapFont();
        Player = player;
    }

    @Override
    public void update(float deltaTime){
        time += deltaTime;
    }


    @Override
    public void render(SpriteBatch batch, Vector2 cameraPosition) {
        font.draw(batch,"Time : " + String.format("%.2f",time),Player.playerSprite.getX() - 520,Player.playerSprite.getY() + 360);
    }

}

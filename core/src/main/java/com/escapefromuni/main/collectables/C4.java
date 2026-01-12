package com.escapefromuni.main.collectables;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.escapefromuni.main.Game;
import com.escapefromuni.main.GameMap;
import com.escapefromuni.main.Player;
import com.escapefromuni.main.components.RenderableComponent;

public class C4 extends Item implements RenderableComponent {
    Player holder;
    static boolean bombPickedUp = false;
    static boolean shouldRender = true;
    public C4(Vector2 givenPosition) {
        super(givenPosition, "Binoculars.png", "C4");
    }

    @Override
    public void pickup(Player player) {
        super.pickup(player);
        bombPickedUp = true;
        holder = player;
    }

    @Override
    public void render(SpriteBatch batch) {
        if (!shouldRender) {
            return;
        }
        collectibleSprite.setPosition(position.x, position.y);
        collectibleSprite.draw(batch);
    }

    public static boolean doesPlayerHaveBomb() {
        return bombPickedUp;
    }

    public void placeBomb(C4 bomb, Rectangle playerHitbox) {
        if (bomb == null) {
            return;
        }

        bomb.position.set(4748f, 851f);
        bombPickedUp = false;

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                GameMap.removeBombWall();
                shouldRender = false;
                if (GameMap.isPlayerNearBombableWall(playerHitbox, 80f)) {
                    Game.gameState = Game.GameState.LOSE;
                }
            }
        }, 3, 0, 0);
    }

}

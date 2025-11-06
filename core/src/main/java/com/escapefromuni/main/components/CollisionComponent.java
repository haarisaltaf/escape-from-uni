package com.escapefromuni.main.components;

import com.badlogic.gdx.math.Rectangle;

public interface CollisionComponent extends BaseComponent{
    public CollisionLayer getCollisionLayer();
    public Boolean isCollidingWith(Rectangle hitboxCheck);
    public Boolean isCollisionEnabled();
    public enum CollisionLayer{
        NONE,WALL,COLLECTIBLE, NPC, PLAYER
    }
}

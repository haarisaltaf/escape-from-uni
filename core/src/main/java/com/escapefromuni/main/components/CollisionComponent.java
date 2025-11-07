package com.escapefromuni.main.components;

import com.badlogic.gdx.math.Rectangle;

/**
 * GameObjects which implement CollisionComponent have hitboxes that can be detected
 * by other GameObjects for functionality such as walls or triggers. Each collision
 * component must have a layer assigned to it which specifies which type of checks
 * the object is used for. For example, anything that the player is supposed to collide
 * with (i.e. stop when touching) would have a CollisionComponent in the WALL layer.
 */
public interface CollisionComponent extends BaseComponent{
    /**
     * Returns the collision layer that this object belongs to. When the Game makes a query
     * for overlapping objects belonging a certain layer, If this CollisionComponent is inside
     * that layer then isCollidingWith() will be called given collision is enabled.
     * There is currently no functionality for CollisionLayer to change once it's added to
     * the Game, this function must return a single value which doesn't change.
     * @return The CollisionLayer that this object belongs to.
     */
    public CollisionLayer getCollisionLayer();

    /**
     * Calculates whether a Rectangle hitbox overlaps with this CollisionComponent.
     * @param hitboxCheck The hitbox which this CollisionComponent needs to check against.
     * @return A boolean value, True meaning that the gameObject is overlapping the
     * hitboxCheck, and False if the hitbox is not touching the gameObject.
     */
    public Boolean isCollidingWith(Rectangle hitboxCheck);

    /**
     * Is Collision enabled on this object. If collision is not enabled, then when the
     * Game makes a query for any overlapping objects, the gameObject will not be checked.
     * @return A boolean value representing whether this object should be checked for collision
     * or not.
     */
    public Boolean isCollisionEnabled();
    public enum CollisionLayer{
        NONE,WALL,COLLECTIBLE, NPC, PLAYER
    }
}

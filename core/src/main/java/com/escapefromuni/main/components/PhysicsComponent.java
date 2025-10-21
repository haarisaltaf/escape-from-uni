package com.escapefromuni.main.components;

import com.badlogic.gdx.math.Vector2;

/**
 * GameObjects that need to keep track of velocity and acceleration will have a PhysicsComponent
 */
//TODO: This is just a placeholder / Temporary to demonstrate components
public interface PhysicsComponent extends BaseComponent {
    Vector2 velocity = new Vector2();
    Vector2 acceleration = new Vector2();

    /**
     * Resolves a step of the physics calculations
     */
    public void step();
}

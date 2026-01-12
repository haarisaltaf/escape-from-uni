package com.escapefromuni.main;

import com.badlogic.gdx.math.Vector2;
import com.escapefromuni.main.components.BaseComponent;

public abstract class GameObject{

    /**
     * The objects position in 2D.
     */
    public Vector2 position;
    /**
     * The objects rotation in (degrees?)
     */
    public float rotation;
	/**
	Called when this GameObject is initialised.
	*/
	public void start(){}
    /**
	Updates the GameObject for a single frame.
	@param deltaTime : The time in ms since the last update
	*/
	public void update(float deltaTime){}

    /**
     * Handles functionality once this GameObject is disposed.
     */
    public void dispose(){}

    /**
     * Checks whether this GameObject extends a particular component type
     * @return returns True if this GameObject has the component, false otherwise.
     * @param <C> The component type, which must extend from BaseComponent.
     */
    public <C extends BaseComponent> boolean HasComponent(){
        return BaseComponent.class.isAssignableFrom(this.getClass());
    }
    public GameObject(){
        position = new Vector2(0,0);
        rotation = 0;
    }
    public GameObject(Vector2 position, float rotation){
        if ((0 > position.x || position.x > 8192) || (0 > position.y || position.y> 8192)) {
            position = new Vector2(0,0);
        }
        this.position = position;
        this.rotation = rotation;
    }
    public GameObject(Vector2 position){
        if ((0 > position.x || position.x > 8192) || (0 > position.y || position.y> 8192)) {
            position = new Vector2(0,0);
        }
        this.position = position;
        this.rotation = 0;
    }
}
public abstract class GameObject{
	/**
	Called when this GameObject is initialised.
	*/
	public abstract void start();
	/**
	Updates the GameObject for a single frame.
	@param deltaTime : The time since the last update
	*/
	public abstract void update(float deltaTime);
	/**
	Destroys this GameObject.
	*/
	public abstract void destroy();
	/**
	Handles Input.
	*/
	public abstract void input();
	/**
	Handles Rendering.
	*/
	public abstract void render();
}
package com.supergreenowl.blobables.framework;

/**
 * State of a screen.
 * @author luke
 *
 */
public abstract class ScreenState {

	protected final StateScreen screen;
	
	public ScreenState(StateScreen s) {
		screen =  s;
	}
	
	public abstract void pause();
	
	public abstract void resume();
	
	public abstract void update(long deltaTime);
	
	public abstract void present(long deltaTime);

	public abstract void dispose();
	
}

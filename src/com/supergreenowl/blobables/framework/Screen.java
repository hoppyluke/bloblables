package com.supergreenowl.blobables.framework;

public abstract class Screen {
	
	public final Game game;
	
	public Screen(Game g) {
		game = g;
	}
	
	public abstract void update(long deltaTime);
	
	public abstract void present(long deltaTime);
	
	public abstract void pause();
	
	public abstract void resume();
	
	public abstract void dispose();

}

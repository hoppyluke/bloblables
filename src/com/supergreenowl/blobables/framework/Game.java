package com.supergreenowl.blobables.framework;

public interface Game {

	public UserInput getInput();
	
	public FileIO getIO();
	
	public Graphics getGraphics();
	
	public Screen getStartupScreen();
	
	public Screen getScreen();
	
	public void setScreen(Screen s);
	
}

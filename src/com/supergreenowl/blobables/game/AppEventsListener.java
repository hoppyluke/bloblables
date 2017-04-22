package com.supergreenowl.blobables.game;

public interface AppEventsListener {

	/**
	 * Returns the app to the main menu.
	 */
	public void onGoToMenu();
	
	/**
	 * Starts a new game.
	 * @param colours Colour for each player in the game.
	 */
	public void onStartGame(byte[] colours);
	
}

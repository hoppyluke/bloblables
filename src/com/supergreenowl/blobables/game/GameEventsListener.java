package com.supergreenowl.blobables.game;

/**
 * Responds to game life-cycle events.
 * @author luke
 *
 */
public interface GameEventsListener {

	public void onGameOver(boolean playerWon, int score, int gameWidth);
	
	public void onReplay();
	
	public void onPause(int gameWidth);
	
	public void onResume();
	
	public void onBack();

}
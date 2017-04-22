package com.supergreenowl.blobables.game;

public interface MenuEventsListener {

	/**
	 * Event to transition menu to the help screen.
	 */
	public void onHelp();
	
	/**
	 * Event to transition menu to the home screen.
	 */
	public void onHome();
	
	/**
	 * Event to transition menu to quick play setup screen.
	 */
	public void onQuickPlay();
	
}

package com.supergreenowl.blobables.game;

import com.supergreenowl.blobables.framework.Game;

public class ScoreManager {

	private static final String HIGH_SCORE = "highscore";	
	
	private ScoreManager()
	{
		// Private to prevent instantiation.
	}
	
	/**
	 * Gets the high score.
	 * @param g Game to access score settings.
	 * @return High score.
	 */
	public static int getHighScore(Game g) {
		return g.getIO().getPreference(HIGH_SCORE, 0);
	}
	
	/**
	 * Sets the high score.
	 * @param g Game to access score settings
	 * @param score New high score.
	 */
	public static void setHighScore(Game g, int score) {
		g.getIO().setPreference(HIGH_SCORE, score);
	}
}
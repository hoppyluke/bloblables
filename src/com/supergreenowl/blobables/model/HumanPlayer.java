package com.supergreenowl.blobables.model;


public class HumanPlayer extends Player {
	
	private int score;
	
	public HumanPlayer(byte colour) {
		super(colour);
		score = 0;
	}

	/**
	 * Adds the specified number of points to the current score.
	 * @param points Number of points to add.
	 */
	public void addPoints(int points) {
		score += points;
	}
	
	/**
	 * Gets the current score.
	 * @return
	 */
	public int getScore() {
		return score;
	}

	@Override
	public void reset() {
		super.reset();
		score = 0; // reset score
	}
	
	
}

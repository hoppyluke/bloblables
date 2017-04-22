package com.supergreenowl.blobables.model.ai;

/**
 * Very simple AI player that just randomly plonks all its cells on
 * the board.
 * @author luke
 *
 */
public class RandomPlayer extends DroidPlayer {
	
	public RandomPlayer(byte colour) {
		super(colour);
	}
	
	/**
	 * Makes this random player play the game.
	 * The strategy is to place all available cells on the
	 * board at random locations.
	 */
	@Override
	public void play(long delta) {
		
		// Keeps placing random cells until there is no stock left or the board is full
		while(getStock() > 0) {
			if(putRandomCell() == -1) return;
		}
	}
}

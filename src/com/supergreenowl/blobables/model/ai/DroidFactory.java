package com.supergreenowl.blobables.model.ai;

/**
 * Instantiates AI player types of varying difficulties.
 * @author luke
 *
 */
public class DroidFactory {

	private DroidFactory() {
		// Cannot instantiate me!
	}
	
	/**
	 * Creates a new easy AI player that uses the specified colour.
	 * @param colour Blob colour for player.
	 * @return Easy AI player.
	 */
	public static DroidPlayer easyPlayer(byte colour) {
		return new  RandomPlayer(colour);
	}
	
	/**
	 * Creates a new medium AI player that uses the specified colour.
	 * @param colour Blob colour for player.
	 * @return Medium AI player.
	 */
	public static DroidPlayer mediumPlayer(byte colour) {
		return new AttackBuildPlayer(colour, 500, 20);
	}
	
	/**
	 * Creates a new hard AI player that uses the specified colour.
	 * @param colour Blob colour for player.
	 * @return Hard AI player.
	 */
	public static DroidPlayer hardPlayer(byte colour) {
		return new GreedyPlayer(colour, 500, 10);
	}
}

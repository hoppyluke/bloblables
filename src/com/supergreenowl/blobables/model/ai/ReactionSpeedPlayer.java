package com.supergreenowl.blobables.model.ai;

/**
 * Base class for AI players that have a defined reaction speed.
 * @author luke
 *
 */
public abstract class ReactionSpeedPlayer extends DroidPlayer {

	private int reactionSpeed;
	private long elapsed;
	
	public ReactionSpeedPlayer(byte colour, int reactionSpeed) {
		super(colour);
		elapsed = 0;
		this.reactionSpeed = reactionSpeed;
	}

	@Override
	public void play(long delta) {
		elapsed += delta;
		
		while(elapsed > reactionSpeed) {
			elapsed -= reactionSpeed;
			react();
		}
	}

	@Override
	public void reset() {
		super.reset();
		elapsed = 0;
	}
	
	/**
	 * Reacts to changes in the game state, as fast as allowed by the reaction speed.
	 */
	protected abstract void react();
}

package com.supergreenowl.blobables.game.presentation;

/**
 * An animation that runs indefinitely until asked to end.
 * Resetting the animation re-starts it.
 * @author luke
 *
 */
public abstract class InfiniteAnimation extends Animation {

	private boolean isComplete;
	
	public InfiniteAnimation(int fps) {
		super(fps);
		isComplete = false;
	}
	
	/**
	 * Tells this animation that it is complete.
	 */
	public void finish() {
		isComplete = true;
	}
	
	@Override
	public boolean isComplete() {
		return isComplete;
	}

	@Override
	public void reset() {
		super.reset();
		isComplete = false;
	}
}

package com.supergreenowl.blobables.game.presentation;

import com.supergreenowl.blobables.framework.Graphics;

/**
 * An animation that runs for a finite period of time, and then ends.
 * @author luke
 *
 */
public abstract class FiniteAnimation extends Animation {

	private static final float F_SECOND = 1000f;
	
	/**
	 * Animation duration in milliseconds.
	 */
	protected int duration;
	
	/**
	 * Total number of frames in the animation.
	 */
	protected int totalFrames;
	
	/**
	 * Number of frames drawn already.
	 */
	protected int framesDrawn;
	
	public FiniteAnimation(int fps, int duration) {
		super(fps);
		
		this.duration = duration;	
		totalFrames = (int)((duration / F_SECOND) * fps);
		framesDrawn = 0;
	}

	/**
	 * Increments the number of frames drawn. Sub-classes should include a <code>super.drawFrame</code>
	 * call in their implementation.
	 */
	@Override
	protected void drawFrame(Graphics g) {
		framesDrawn++;
	}

	@Override
	public void reset() {
		super.reset();
		framesDrawn = 0;
	}

	@Override
	public boolean isComplete() {
		return framesDrawn >= totalFrames;
	}

}

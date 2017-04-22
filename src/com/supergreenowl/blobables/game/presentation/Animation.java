package com.supergreenowl.blobables.game.presentation;

import android.graphics.Rect;

import com.supergreenowl.blobables.framework.Graphics;

/**
 * Provides a short animation with a set duration and FPS.
 * @author luke
 *
 */
public abstract class Animation {

	private static final int ONE_SECOND = 1000;
	
	/**
	 * Frames per second.
	 */
	protected int fps;
	
	private int frameDuration;
	private long elapsed;
	
	protected Rect fillRect;
	protected int fillColour;
	
	public Animation(int fps) {
		this.fps = fps;
		
		frameDuration = ONE_SECOND / fps;
		elapsed = 0;
	}
	
	/**
	 * Draws a single frame of the animation.
	 * @param g Graphics to draw to.
	 */
	protected abstract void drawFrame(Graphics g);
	
	/**
	 * Draws the completed state of the animation.
	 * @param g Graphics to draw to.
	 */
	protected abstract void drawComplete(Graphics g);
	
	/**
	 * Resets the animation.
	 */
	public void reset() {
		elapsed = 0;
	}
	
	/**
	 * Draws the animation for the specified amount of time elapsed since
	 * the last draw.
	 * @param g Graphics to draw to.
	 * @param elapsedMilliseconds
	 */
	public void animate(Graphics g, long elapsedMilliseconds) {
		elapsed += elapsedMilliseconds;
		
		while(elapsed > frameDuration) {
			elapsed -= frameDuration;
			
			if(!isComplete()) drawFrame(g);
			else drawComplete(g);
		}
	}
	
	/**
	 * Checks if the animation has completed yet.
	 * @return True if the animation is complete and false otherwise.
	 */
	public abstract boolean isComplete();
	
	/**
	 * Sets the fill area and colour that is drawn before each frame of the animation.
	 * @param colour
	 * @param left
	 * @param top
	 * @param right
	 * @param bottom
	 */
	public void setFill(int colour, int left, int top, int right, int bottom) {
		fillColour = colour;
		fillRect = new Rect(left, top, right, bottom);
	}
	
	/**
	 * Removes the fill that is drawn before each frame.
	 */
	public void clearFill() {
		fillRect = null;
	}
}

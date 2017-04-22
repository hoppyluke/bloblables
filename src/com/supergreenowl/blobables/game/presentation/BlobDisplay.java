package com.supergreenowl.blobables.game.presentation;

import com.supergreenowl.blobables.framework.Graphics;

import android.graphics.Bitmap;

public class BlobDisplay extends FiniteAnimation {
	
	private static final int ALPHA_MAX = 255;
	private static final int ALPHA_MIN = 0;
	
	protected Bitmap image;
	private Bitmap underlay;
	private int imageAlpha;
	private int underlayAlpha;
	private int alphaIncrement;
	
	private int x, y;
	private int w, h;
	
	public BlobDisplay(int fps, int duration) {
		super(fps, duration);
		alphaIncrement = (int)Math.ceil(ALPHA_MAX / (float)this.totalFrames);
	}
	
	public void setImage(Bitmap image) {
		this.underlay = this.image;
		this.image = image;
		
		if(image != null) {
			this.w = image.getWidth();
			this.h = image.getHeight();
		}
		
		underlayAlpha = ALPHA_MAX;
		imageAlpha = ALPHA_MIN;
		
		// Must be super.reset() as this.reset() wipes out images!
		super.reset();
	}
	
	/**
	 * Sets the location at which image(s) for the blob should be drawn.
	 * @param x
	 * @param y
	 */
	public  void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	protected void drawFrame(Graphics g) {
		
		super.drawFrame(g);
		
		// if there are no images to display then nothing to be done
		if(image == null && underlay == null) return;
		
		// update image alpha values to fade them in/out
		if(imageAlpha <  ALPHA_MAX) imageAlpha += alphaIncrement;
		if(underlayAlpha > ALPHA_MIN) underlayAlpha -= alphaIncrement;
		
		if(imageAlpha > ALPHA_MAX) imageAlpha = ALPHA_MAX;
		if(underlayAlpha < ALPHA_MIN) underlayAlpha = ALPHA_MIN;
		
		// Draw the images
		if(underlay != null) {
			g.drawBitmap(underlay, x, y, w, h, underlayAlpha);
		}
		
		if(image != null) {
			g.drawBitmap(image, x, y, w, h, imageAlpha);
		}
	}

	@Override
	protected void drawComplete(Graphics g) {
		
		if(image != null)
			g.drawBitmap(image, x, y, w, h);
	}
	
	/**
	 * Draws the blob without any animation.
	 * @param g
	 * @param x
	 * @param y
	 */
	public void drawNoAnimation(Graphics g, int x, int y) {
		if(image != null) g.drawBitmap(image, x, y, w, h);
	}
	
	@Override
	public void reset() {
		super.reset();
		image = underlay = null;
	}
}
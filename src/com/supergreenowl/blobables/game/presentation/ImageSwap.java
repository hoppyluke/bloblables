package com.supergreenowl.blobables.game.presentation;

import com.supergreenowl.blobables.framework.Graphics;

import android.graphics.Bitmap;

public class ImageSwap extends FiniteAnimation {

	private Bitmap b1, b2;
	private int x1, x2;
	private int y1, y2;
	private int d1, d2;
	private float v1, v2;
	private boolean isRightToLeft;
	private boolean isInitialised;
	
	/**
	 * An animation that swaps one bitmap for another.
	 * @param fps Frames per second for the animation.
	 * @param duration Duration in milliseconds.
	 * @param oldImage Image to be replace.
	 * @param newImage Replacement image.
	 * @param isFromRight If true, the new image moves in from the right. Otherwise it moves from the left.
	 */
	public ImageSwap(int fps, int duration, Bitmap oldImage, Bitmap newImage, boolean isFromRight) {
		super(fps, duration);
		this.b1 = oldImage;
		this.b2 = newImage;
		this.isRightToLeft = isFromRight;
		this.isInitialised = false;
	}
	
	@Override
	protected void drawFrame(Graphics g) {
		super.drawFrame(g);
		
		if(!isInitialised) init(g);
		
		if(isRightToLeft) {
			x1 -= v1;
			x2 -= v2;
		}
		else {
			x1 += v1;
			x2 += v2;
		}
		
		g.drawBitmap(b1, x1, y1);
		g.drawBitmap(b2, x2, y2);
		
	}

	@Override
	protected void drawComplete(Graphics g) {
		g.drawBitmap(b2, d2, y2);
	}
	
	private void init(Graphics g) {
		int w = g.getWidth();
		int h = g.getHeight();
		
		if(isRightToLeft) {
			x1 = (w - b1.getWidth()) / 2;
			d1 = 0 - b1.getWidth();
			x2 = w;
			d2 = (w - b2.getWidth()) / 2;
			
			y1 = (h - b1.getHeight()) / 2;
			y2 = (h - b2.getHeight()) / 2;
			
			v1 = (x1 - d1) / totalFrames;
			v2 = (x2 - d2) / totalFrames;
		}
		else {
			x1 = 0 - b1.getWidth();
			d1 = (w - b1.getWidth()) / 2;
			x2 = (w - b2.getWidth()) / 2;
			d2 = w;
			
			y1 = (h - b1.getHeight()) / 2;
			y2 = (h - b2.getHeight()) / 2;
			
			v1 = (d1 - x1) / (float)totalFrames;
			v2 = (d2 - x2) / (float)totalFrames;
		}
		isInitialised = true;
	}

}

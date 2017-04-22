package com.supergreenowl.blobables.game.presentation;

import com.supergreenowl.blobables.framework.Graphics;

import android.graphics.Bitmap;

/**
 * Utility for drawing portions of horizontal image atlases.
 * Atlas must contain horizontally arranged components with a fixed width.
 * @author luke
 *
 */
public class ImageAtlas {

	private Graphics g;
	private Bitmap atlas;
	private int width;
	private int height;
	
	/**
	 * Creates a new image atlas.
	 * @param g Graphics module to draw with.
	 * @param atlasFileName File name for the atlas image file.
	 * @param width Width of atlas portions.
	 * @param height Height of atlas portions.
	 */
	public ImageAtlas(Graphics g, String atlasFileName, int width, int height) {
		this.g = g;
		this.width = width;
		this.height = height;
		atlas = g.loadBitmap(atlasFileName, Bitmap.Config.ARGB_8888);
	}
	
	/**
	 * Draws the portion of the atlas at the specified offset.
	 * @param offset Offset of portion to draw.
	 * @param x Location to draw the portion.
	 * @param y Location to draw the portion.
	 */
	public void draw(int offset, int x, int y) {
		g.drawBitmap(atlas, x, y, offset * width, 0, width, height);
	}
	
	/**
	 * Disposes of resources used by this image atlas.
	 */
	public void dispose() {
		atlas.recycle();
	}
}

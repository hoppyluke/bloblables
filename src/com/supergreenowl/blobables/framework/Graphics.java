package com.supergreenowl.blobables.framework;

/*
 * NB: I am deviating from "Beginning Android Games" by not
 * putting a wrapper around Bitmap. For now.
 */
import android.graphics.Bitmap;
import android.graphics.Rect;

public interface Graphics {
	
	/**
	 * Draws a single pixel.
	 * @param x
	 * @param y
	 * @param colour
	 */
	public void drawPixel(int x, int y, int colour);
	
	/**
	 * Draws a line.
	 * @param beginX
	 * @param beginY
	 * @param endX
	 * @param endY
	 * @param colour
	 */
	public void drawLine(int beginX, int beginY, int endX, int endY, int colour);
	
	/**
	 * Draws a rectangle.
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param colour
	 */
	public void drawRectangle(int x, int y, int width, int height, int colour);
	
	/**
	 * Draws a rectangle.
	 * @param rectangle Rectangle to draw.
	 * @param colour Colour to draw rectangle in.
	 */
	public void drawRectangle(Rect rectangle, int colour);
	
	/**
	 * Draws an entire bitmap.
	 * @param b
	 * @param x
	 * @param y
	 */
	public void drawBitmap(Bitmap b, int x, int y);
	
	/**
	 * Draws a portion of a bitmap.
	 * @param b
	 * @param x
	 * @param y
	 * @param srcX x-coordinate within bitmap of portion to draw.
	 * @param srcY y-coordinate within bitmap of portion to draw.
	 * @param w Width of portion to draw.
	 * @param h Height of portion to draw.
	 */
	public void drawBitmap(Bitmap b, int x, int y, int srcX, int srcY, int w, int h);
	
	/**
	 * Draws a bitmap. Draws a portion of the bitmap taken from the top-left corner
	 * that has the specified width and height. The rendered version has the same width
	 * and height as the portion of the bitmap selected to draw.
	 * @param b Bitmap to draw.
	 * @param x Location to draw bitmap (left).
	 * @param y Location to draw bitmap (top).
	 * @param w Width for drawn bitmap.
	 * @param h Height for drawn bitmap.
	 */
	public void drawBitmap(Bitmap b, int x, int y, int w, int h);

	/**
	 * Draws an entire bitmap with specified size and transparency.
	 * @param b Bitmap to draw.
	 * @param x Location to draw bitmap (left).
	 * @param y Location to draw bitmap (top).
	 * @param w Width for drawn bitmap.
	 * @param h Height for drawn bitmap.
	 * @param alpha Alpha value 0-255.
	 */
	public void drawBitmap(Bitmap b, int x, int y, int w, int h, int alpha);
	
	/**
	 * Draws an entire bitmap, scaled to the specified size.
	 * @param b Bitmap to draw.
	 * @param x x-coordinate to draw bitmap (left).
	 * @param y y-coordinate to draw bitmap (top).
	 * @param srcW Width of bitmap to draw.
	 * @param srcH Height of bitmap to draw.
	 * @param w Width for scaled version.
	 * @param h Height for scaled version.
	 */
	public void drawScaledBitmap(Bitmap b, int x, int y, int srcW, int srcH, int w, int h);

	/**
	 * Fills the entire drawable region.
	 * @param colour
	 */
	public void fill(int colour);
	
	/**
	 * Gets the width of the area being drawn on.
	 * @return
	 */
	public int getWidth();
	
	/**
	 * Gets the height of the area being drawn on.
	 * @return
	 */
	public int getHeight();
	
	/**
	 * Loads a bitmap file.
	 * @param filename Filename of asset to load.
	 * @param formatHint Suggested colour format.
	 * @return
	 */
	public Bitmap loadBitmap(String filename, Bitmap.Config formatHint);

	public TextDrawer getTextDrawer();
	
}

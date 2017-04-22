package com.supergreenowl.blobables.game.presentation;

import android.graphics.Bitmap;

import com.supergreenowl.blobables.framework.Graphics;

/**
 * Manages access to main menu image resources.
 * @author luke
 *
 */
public class MenuImageManager {

	private static final String BMP_DIFFICULTY = "difficulty.png";
	private static final String BMP_BANNER = "blobs_banner_text.png";
	private static final String BMP_BIG_CROSS = "x_large.png";
	private static final String BMP_YOU = "you.png";
	private static final String BMP_SETUP = "setup.png";
	private static final String BMP_BETA = "beta.png";
	
	public static final int DIFFICULTY_WIDTH = 96;
	public static final int DIFFICULTY_HEIGHT = 165;
	
	public static final int CROSS_WIDTH = 96;
	public static final int CROSS_HEIGHT = 96;
	
	public static final int YOU_WIDTH = 96;
	public static final int YOU_HEIGHT = 65;
	
	public static final int SETUP_WIDTH = 375;
	public static final int SETUP_HEIGHT = 70;
	
	public static final int BETA_WIDTH = 105;
	public static final int BETA_HEIGHT = 70;
	
	public Bitmap banner, bigCross, you, setup, beta;
	public ImageAtlas difficulty;
	
	public MenuImageManager(Graphics g) {
		Bitmap.Config format = Bitmap.Config.ARGB_8888;
		banner = g.loadBitmap(BMP_BANNER, format);
		bigCross = g.loadBitmap(BMP_BIG_CROSS, format);
		you = g.loadBitmap(BMP_YOU, format);
		setup = g.loadBitmap(BMP_SETUP, format);
		beta = g.loadBitmap(BMP_BETA, format);
		difficulty = new ImageAtlas(g, BMP_DIFFICULTY, DIFFICULTY_WIDTH, DIFFICULTY_HEIGHT);
	}
	
	/**
	 * Frees image resources.
	 */
	public void dispose() {
		banner.recycle();
		bigCross.recycle();
		you.recycle();
		setup.recycle();
		beta.recycle();
		difficulty.dispose();
	}
}

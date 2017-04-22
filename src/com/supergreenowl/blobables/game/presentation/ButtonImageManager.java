package com.supergreenowl.blobables.game.presentation;

import com.supergreenowl.blobables.framework.Graphics;

import android.graphics.Bitmap;

/**
 * Provides access to button image resources.
 * @author luke
 *
 */
public class ButtonImageManager {

	public static final int BUTTON_WIDTH = 96;
	public static final int BUTTON_HEIGHT = 96;
	
	private static final String BMP_BACK = "back.png";
	private static final String BMP_FORWARD = "forward.png";
	private static final String BMP_REPLAY = "replay.png";
	private static final String BMP_PLAY = "resume.png";
	private static final String BMP_HELP = "help.png";
	private static final String BMP_HOME = "home.png";
	
	public Bitmap back, forward, replay, play, help, home;
	
	public ButtonImageManager(Graphics g) {
		Bitmap.Config format = Bitmap.Config.ARGB_8888;
		back = g.loadBitmap(BMP_BACK, format);
		forward = g.loadBitmap(BMP_FORWARD, format);
		replay = g.loadBitmap(BMP_REPLAY, format);
		play = g.loadBitmap(BMP_PLAY, format);
		help = g.loadBitmap(BMP_HELP, format);
		home = g.loadBitmap(BMP_HOME, format);
	}
	
	/**
	 * Frees image resources.
	 */
	public void dispose() {
		back.recycle();
		forward.recycle();
		replay.recycle();
		play.recycle();
		help.recycle();
		home.recycle();
	}
	
}

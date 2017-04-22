package com.supergreenowl.blobables.game.presentation;

import com.supergreenowl.blobables.framework.Graphics;
import com.supergreenowl.blobables.model.BlobState;

import android.graphics.Bitmap;

/**
 * Provides access to game image resources and utility methods for rendering game images. 
 * @author luke
 *
 */
public class GameImageManager {
	
	public static final int NUMBER_WIDTH = 20;
	public static final int NUMBER_HEIGHT = 40;
	
	public static final int STAR_WIDTH = 48;
	public static final int STAR_HEIGHT = 48;
	
	public static final int TEXTURE_WIDTH = 100;
	public static final int TEXTURE_HEIGHT = 100;
	
	public static final int HIGHSCORE_WIDTH = 280;
	public static final int HIGHSCORE_HEIGHT = 60;
	
	private static final String BMP_X = "x.png";
	private static final String BMP_RED = "blob_r_1.png";
	private static final String BMP_BLUE = "blob_b_1.png";
	private static final String BMP_GREEN = "blob_g_1.png";
	private static final String BMP_YELLOW = "blob_y_1.png";
	private static final String BMP_UNCOLOURED = "blob_x_1.png";
	private static final String BMP_TOMBSTONE = "tombstone.png";
	private static final String BMP_NUMBERS = "numberatlas_white.png";
	private static final String BMP_STAR = "star.png";
	private static final String BMP_HIGHSCORE = "highscore.png";
	
	private static final String BMP_TEXTURE_SOIL = "texture_soil.png";
	
	private static final String BMP_PAUSED = "paused.png";
	private static final String BMP_GAME_OVER = "gameover.png";
	private static final String BMP_VICTORY = "victory.png";
	
	public Bitmap red;
	public Bitmap green;
	public Bitmap blue;
	public Bitmap yellow;
	public Bitmap uncoloured;
	
	public Bitmap highscore;
	
	public Bitmap cross;
	public Bitmap tombstone;
	public Bitmap star;
	
	public Bitmap paused, gameOver, victory;
	
	public Bitmap texture;
	
	public ImageAtlas numbers;
	
	public GameImageManager(Graphics g) {
		
		Bitmap.Config format =  Bitmap.Config.ARGB_8888;
		red = g.loadBitmap(BMP_RED, format);
		green = g.loadBitmap(BMP_GREEN, format);
		blue = g.loadBitmap(BMP_BLUE, format);
		yellow = g.loadBitmap(BMP_YELLOW, format);
		uncoloured = g.loadBitmap(BMP_UNCOLOURED, format);
		cross = g.loadBitmap(BMP_X, format);
		tombstone = g.loadBitmap(BMP_TOMBSTONE, format);
		star = g.loadBitmap(BMP_STAR, format);
		
		paused = g.loadBitmap(BMP_PAUSED, format);
		gameOver = g.loadBitmap(BMP_GAME_OVER, format);
		victory = g.loadBitmap(BMP_VICTORY, format);
		
		highscore = g.loadBitmap(BMP_HIGHSCORE, format);
		
		numbers = new ImageAtlas(g, BMP_NUMBERS, NUMBER_WIDTH, NUMBER_HEIGHT);
		
		/*int i = (new java.util.Random()).nextInt(1);
		switch(i) {
			case 0:
			default:*/ texture = g.loadBitmap(BMP_TEXTURE_SOIL, format); /*break;
		}*/
	}
	
	/**
	 * Gets a bitmap to render for a blob of the specified colour.
	 * @param colour Colour of the blob.
	 * @return Bitmap image to represent the blob. Null if the specified
	 * colour is BlobState.DEAD.
	 */
	public Bitmap getBitmap(byte colour) {
		switch(colour) {
		case BlobState.C1: return red;
		case BlobState.C2: return blue;
		case BlobState.C3: return green;
		case BlobState.C4: return yellow;
		case BlobState.UNCOLOURED: return uncoloured;
		case BlobState.DEAD:
		default: return null;
		}
	}
	
	/**
	 * Draws a number on screen.
	 * @param number Number to draw.
	 * @param x Location to draw at.
	 * @param y Location to draw at.
	 */
	public void drawNumber(int number, int x, int y) {
		
		String numberString = Integer.toString(number);
		int len = numberString.length();
		for(int i = 0; i < len; i++) {
			int digit = Integer.parseInt(Character.toString(numberString.charAt(i)));
			numbers.draw(digit, x, y);
			x += NUMBER_WIDTH;
		}
	}
	
	/**
	 * Frees image resources used.
	 */
	public void dispose() {
		red.recycle();
		green.recycle();
		blue.recycle();
		yellow.recycle();
		uncoloured.recycle();
		cross.recycle();
		tombstone.recycle();
		star.recycle();
		
		texture.recycle();
		
		paused.recycle();
		gameOver.recycle();
		victory.recycle();
		highscore.recycle();
		
		numbers.dispose();
	}
}
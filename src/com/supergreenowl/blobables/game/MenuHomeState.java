package com.supergreenowl.blobables.game;

import java.util.List;
import java.util.Random;

import com.supergreenowl.blobables.framework.Graphics;
import com.supergreenowl.blobables.framework.ScreenState;
import com.supergreenowl.blobables.framework.StateScreen;
import com.supergreenowl.blobables.framework.TouchEvent;
import com.supergreenowl.blobables.game.presentation.ButtonImageManager;
import com.supergreenowl.blobables.game.presentation.GameImageManager;
import com.supergreenowl.blobables.game.presentation.ImageProvider;
import com.supergreenowl.blobables.game.presentation.MenuImageManager;
import com.supergreenowl.blobables.model.BlobState;

import android.graphics.Bitmap;

public class MenuHomeState extends ScreenState {

	private static final int PADDING = 24;
	private static final int BLOB_WIDTH = 48;
	private static final int BLOB_HEIGHT = 48;
	private static final int BACKGROUND_COLOUR = 0;

	private static final int BLOBS_DURATION = 1000;
	
	private int playX, helpX, buttonY;
	
	private int blobsTop;
	private int blobsBottom;
	private int blobsRight;
	private long blobsElapsed;
	private Random gen;
	
	private boolean isRendered = false;
	private MenuEventsListener menuEvents;
	
	public MenuHomeState(StateScreen screen, MenuEventsListener menuEvents) {
		super(screen);
		this.menuEvents = menuEvents;
		gen = new Random();
	}
	
	@Override
	public void pause() {
		// Nothing to pause.
	}

	@Override
	public void resume() {
		// Nothing to resume.
	}

	@Override
	public void update(long deltaTime) {
		List<TouchEvent> touches = screen.game.getInput().getTouchEvents();
		int len = touches.size();
		for(int i = 0; i < len; i++) {
			TouchEvent t = touches.get(i);
			if(t.type == TouchEvent.TOUCH_UP) {
				if(isButtonTouched(playX, buttonY, t)) {
					isRendered = false;
					menuEvents.onQuickPlay();
				}
				else if(isButtonTouched(helpX, buttonY, t)) {
					isRendered = false;
					menuEvents.onHelp();
				}
			}
		}

	}

	@Override
	public void present(long deltaTime) {
		
		Graphics g = screen.game.getGraphics();
		int w = g.getWidth();
		int h = g.getHeight();
		
		// Render static images once only
		if(!isRendered) {
			g.fill(BACKGROUND_COLOUR);
			
			// Draw logo banner
			MenuImageManager menuImages = ImageProvider.getMenuImages(g); 
			Bitmap banner = menuImages.banner;
			int bannerW = banner.getWidth();
			int bannerH = banner.getHeight();
			int bannerX =  (w - bannerW) / 2;
			g.drawBitmap(banner, bannerX, PADDING, bannerW, bannerH);
			
			/*
			// Beta version only: draw beta image
			int betaH = menuImages.beta.getHeight();
			int betaY = PADDING + ((bannerH - betaH) / 2);
			g.drawBitmap(menuImages.beta, bannerX + bannerW + PADDING, betaY, menuImages.beta.getWidth(), betaH);*/
			
			// Draw buttons
			ButtonImageManager buttons = ImageProvider.getButtons(g);
			buttonY = h - PADDING - ButtonImageManager.BUTTON_HEIGHT;
			
			playX = w - PADDING - ButtonImageManager.BUTTON_WIDTH;
			helpX = PADDING;
			g.drawBitmap(buttons.play, playX, buttonY, ButtonImageManager.BUTTON_WIDTH, ButtonImageManager.BUTTON_HEIGHT);
			g.drawBitmap(buttons.help, helpX, buttonY, ButtonImageManager.BUTTON_WIDTH, ButtonImageManager.BUTTON_HEIGHT);
			
			// Calculate limits for coordinates where blobs can be placed
			blobsTop = PADDING + banner.getHeight() + PADDING;
			blobsBottom = buttonY - PADDING - BLOB_HEIGHT;
			blobsRight = w - BLOB_WIDTH;
			
			// Draw high score
			int score = ScoreManager.getHighScore(screen.game);
			if(score > 0) {
				GameImageManager images = ImageProvider.getGameImages(g);
				int len = Integer.toString(score).length();
				int x = (g.getWidth() - GameImageManager.STAR_WIDTH - (GameImageManager.NUMBER_WIDTH * len)) / 2;
				int y = buttonY + ((ButtonImageManager.BUTTON_HEIGHT - GameImageManager.STAR_HEIGHT) / 2);
				g.drawBitmap(images.star, x, y, GameImageManager.STAR_WIDTH, GameImageManager.STAR_HEIGHT);
				y = buttonY + ((ButtonImageManager.BUTTON_HEIGHT - GameImageManager.NUMBER_HEIGHT) / 2);
				x += GameImageManager.STAR_WIDTH;
				images.drawNumber(score, x, y);
			}
			
			isRendered = true;
		}

		// Draw blobs - only if there is room on screen for them
		// if screen is small (height <= 394px with current image sizes) then there is no room
		// and Random.nextInt will error as bottom - top will be -ve
		if(blobsBottom > blobsTop) {
			blobsElapsed += deltaTime;
			while(blobsElapsed >= BLOBS_DURATION) {
				blobsElapsed -= BLOBS_DURATION;
				int x = gen.nextInt(blobsRight);
				int y = gen.nextInt(blobsBottom - blobsTop) + blobsTop;
				g.drawBitmap(getBlobImage(g), x, y);
			}
		}
	}

	@Override
	public void dispose() {
		// Nothing to dispose.
	}
	
	/**
	 * Gets a random blob image to draw.
	 * @param g Graphics for the game (necessary to get bitmap to draw).
	 * @return Blob image.
	 */
	private Bitmap getBlobImage(Graphics g) {
		int n = gen.nextInt(BlobState.NUM_COLOURS + 1);
		byte c = (byte)(BlobState.UNCOLOURED + n);
		return ImageProvider.getGameImages(g).getBitmap(c);
	}
	
	/**
	 * Determines if the specified bitmap, drawn at the specified location was touched by a touch event.
	 * @param b Bitmap to check.
	 * @param x x-coordinate of where bitmap was drawn on screen.
	 * @param y y-coordinate of where bitmap was drawn on screen.
	 * @param t Touch event to check.
	 * @return True if the touch falls within the bitmap, false otherwise.
	 */
	private boolean isButtonTouched(int x, int y, TouchEvent t) {
		return t.x >= x && t.x < (x + ButtonImageManager.BUTTON_WIDTH)
				&& t.y >= y && t.y < (y + ButtonImageManager.BUTTON_HEIGHT);
	}

}

package com.supergreenowl.blobables.game;

import android.graphics.Bitmap;

import com.supergreenowl.blobables.framework.Game;
import com.supergreenowl.blobables.framework.Graphics;
import com.supergreenowl.blobables.framework.Screen;
import com.supergreenowl.blobables.game.presentation.ImageProvider;

public class SplashScreen extends Screen {
	
	private static final byte STATE_INITIAL = 0;
	private static final byte STATE_RENDERED = 1;
	private static final byte STATE_LOADED = 2;
	
	private static final String BMP_LOGO = "sgo_logo_white.png";
	
	private static final int BACKGROUND_COLOUR = 0xffffffff;
	private static final int DURATION = 2000;
	
	private byte state;
	private long elapsed;
	private Bitmap logo;
	
	public SplashScreen(Game game) {
		super(game);
		state = STATE_INITIAL;
		elapsed = 0;
	}

	@Override
	public void update(long deltaTime) {
		
		elapsed += deltaTime;
		
		if(state == STATE_RENDERED) {
			
			// Get image provider to load assets
			Graphics g = game.getGraphics();
			ImageProvider.getGameImages(g);
			ImageProvider.getMenuImages(g);
			ImageProvider.getButtons(g);
			
			state = STATE_LOADED;
		}
		else if(state == STATE_LOADED && elapsed >= DURATION) {
			// Once assets are loaded and minimum duration has passed, move to main menu
			game.setScreen(new MenuScreen(game));
		}

	}

	@Override
	public void present(long deltaTime) {
		// Only need to render once
		if(state == STATE_INITIAL) {
			
			Graphics g = game.getGraphics();
			g.fill(BACKGROUND_COLOUR);
			
			// Draw logo
			logo = g.loadBitmap(BMP_LOGO, Bitmap.Config.ARGB_8888);
			int w = logo.getWidth();
			int h = logo.getHeight();
			int x = (g.getWidth() - w) / 2;
			int y = (g.getHeight() - h) / 2;
			g.drawBitmap(logo, x, y, w, h);
			
			state = STATE_RENDERED;
		}
	}

	@Override
	public void pause() {
		// Nothing to pause
	}

	@Override
	public void resume() {
		// Nothing to resume.
	}

	@Override
	public void dispose() {
		
		if(logo != null) logo.recycle();
		
		// Unload any loaded assets
		ImageProvider.disposeAll();
	}

}

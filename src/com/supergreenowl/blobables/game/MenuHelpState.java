package com.supergreenowl.blobables.game;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.supergreenowl.blobables.framework.Graphics;
import com.supergreenowl.blobables.framework.KeyEvent;
import com.supergreenowl.blobables.framework.ScreenState;
import com.supergreenowl.blobables.framework.StateScreen;
import com.supergreenowl.blobables.framework.TouchEvent;
import com.supergreenowl.blobables.game.presentation.Animation;
import com.supergreenowl.blobables.game.presentation.ButtonImageManager;
import com.supergreenowl.blobables.game.presentation.ImageProvider;
import com.supergreenowl.blobables.game.presentation.ImageSwap;

import android.graphics.Bitmap;

public class MenuHelpState extends ScreenState {

	private static final String BMP_HELP_10 = "help_10.png";
	private static final String BMP_HELP_20 = "help_20.png";
	private static final String BMP_HELP_21 = "help_21.png";
	private static final String BMP_HELP_22 = "help_22.png";
	private static final String BMP_HELP_30 = "help_30.png";
	private static final String BMP_HELP_31 = "help_31.png";
	private static final String BMP_HELP_40 = "help_40.png";
	
	private static final int PADDING = 24;
	private static final int BACKGROUND_COLOUR = 0;
	
	private static final int FPS = 60;
	private static final int SWIPE_DURATION = 1000;
	
	private Queue<Animation> swipesQueue;
	
	private boolean isRendered = false;
	private boolean isImageRendered = false;
	
	private int backX, backY, forwardX, forwardY, quitX, quitY;
	
	private Bitmap[] images;
	private int currentImage;
	
	private MenuEventsListener menuEvents;
	
	public MenuHelpState(StateScreen s, MenuEventsListener menuEvents) {
		super(s);
		this.menuEvents = menuEvents;
		Graphics g = s.game.getGraphics();
		Bitmap.Config formatHint = Bitmap.Config.ARGB_8888;
		
		images = new Bitmap[] {
				g.loadBitmap(BMP_HELP_10, formatHint),
				g.loadBitmap(BMP_HELP_20, formatHint),
				g.loadBitmap(BMP_HELP_21, formatHint),
				g.loadBitmap(BMP_HELP_22, formatHint),
				g.loadBitmap(BMP_HELP_30, formatHint),
				g.loadBitmap(BMP_HELP_31, formatHint),
				g.loadBitmap(BMP_HELP_40, formatHint)
		};
		
		swipesQueue = new LinkedList<Animation>();
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
		List<KeyEvent> keyEvents = screen.game.getInput().getKeyEvents();
		int len = keyEvents.size();
		for(int i = 0; i < len; i++) {
			KeyEvent k = keyEvents.get(i);
			
			if(k.type == KeyEvent.KEY_UP && k.keyCode == android.view.KeyEvent.KEYCODE_BACK) {
				isRendered = false;
				isImageRendered = false;
				menuEvents.onHome();
			}
		}
		
		List<TouchEvent> touches = screen.game.getInput().getTouchEvents();
		len = touches.size();
		for(int i = 0; i < len; i++) {
			TouchEvent t = touches.get(i);
			if(t.type == TouchEvent.TOUCH_UP) {
				if(isButtonTouched(backX, backY, t)) {
					int prevImg = currentImage;
					if(--currentImage < 0) currentImage = images.length - 1;
					swipesQueue.add(new ImageSwap(FPS, SWIPE_DURATION, images[currentImage], images[prevImg], false));
				} else if(isButtonTouched(forwardX, forwardY, t)) {
					int prevImg = currentImage;
					if(++currentImage >= images.length) currentImage = 0;
					swipesQueue.add(new ImageSwap(FPS, SWIPE_DURATION, images[prevImg], images[currentImage], true));
				} else if(isButtonTouched(quitX, quitY, t)) {
					isRendered = false;
					isImageRendered = false;
					menuEvents.onHome();
				}
			}
		}

	}

	@Override
	public void present(long deltaTime) {
		
		Graphics g = screen.game.getGraphics();
		int w = g.getWidth();
		int h = g.getHeight();
		
		if(!isRendered) {
			backX = PADDING;
			backY = h - ButtonImageManager.BUTTON_HEIGHT - PADDING;
			
			quitX = PADDING + ButtonImageManager.BUTTON_WIDTH + PADDING;
			quitY = h - ButtonImageManager.BUTTON_HEIGHT - PADDING;
			
			forwardX = w - ButtonImageManager.BUTTON_WIDTH - PADDING;
			forwardY = h - ButtonImageManager.BUTTON_HEIGHT - PADDING;
			
			isRendered = true;
		}
		
		ButtonImageManager buttons = ImageProvider.getButtons(g);
		if(swipesQueue.isEmpty() && !isImageRendered) {
			g.fill(BACKGROUND_COLOUR);
			g.drawBitmap(buttons.back, backX, backY, ButtonImageManager.BUTTON_WIDTH, ButtonImageManager.BUTTON_HEIGHT);
			g.drawBitmap(buttons.forward, forwardX, forwardY, ButtonImageManager.BUTTON_WIDTH, ButtonImageManager.BUTTON_HEIGHT);
			g.drawBitmap(buttons.home, quitX, quitY, ButtonImageManager.BUTTON_WIDTH, ButtonImageManager.BUTTON_HEIGHT);
			
			Bitmap img = images[currentImage];		
			g.drawBitmap(img, (w - img.getWidth()) / 2, (h - img.getHeight()) / 2);
			isImageRendered = true;
		}
		
		if(!swipesQueue.isEmpty()) {
			Animation swipe = swipesQueue.peek();
			g.fill(BACKGROUND_COLOUR);
			g.drawBitmap(buttons.back, backX, backY, ButtonImageManager.BUTTON_WIDTH, ButtonImageManager.BUTTON_HEIGHT);
			g.drawBitmap(buttons.forward, forwardX, forwardY, ButtonImageManager.BUTTON_WIDTH, ButtonImageManager.BUTTON_HEIGHT);
			g.drawBitmap(buttons.home, quitX, quitY, ButtonImageManager.BUTTON_WIDTH, ButtonImageManager.BUTTON_HEIGHT);
			swipe.animate(g, deltaTime);
			
			if(swipe.isComplete()) {
				swipesQueue.remove();
				isImageRendered = false;
			}
		}
	}

	@Override
	public void dispose() {
		if(images != null) {
			int len = images.length;
			for(int i = 0; i < len; i++)
				images[i].recycle();
			images = null;
		}
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
		return t.x >= x && t.x < (x + ButtonImageManager.BUTTON_WIDTH) && t.y >= y && t.y < (y + ButtonImageManager.BUTTON_HEIGHT);
	}

}

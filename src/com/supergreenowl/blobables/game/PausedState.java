package com.supergreenowl.blobables.game;

import java.util.List;

import com.supergreenowl.blobables.framework.Graphics;
import com.supergreenowl.blobables.framework.KeyEvent;
import com.supergreenowl.blobables.framework.ScreenState;
import com.supergreenowl.blobables.framework.StateScreen;
import com.supergreenowl.blobables.framework.TouchEvent;
import com.supergreenowl.blobables.game.presentation.ButtonImageManager;
import com.supergreenowl.blobables.game.presentation.GameImageManager;
import com.supergreenowl.blobables.game.presentation.ImageProvider;

public class PausedState extends ScreenState {
	
	private static final int BG_FADE_MASK = 0xaa666666;
	
	private static final int VERTICAL_PADDING = 24;
	
	private GameEventsListener gameEvents;
	private int backX, backY;
	private int replayX, replayY;
	private int resumeX, resumeY;
	
	private boolean isRendered;
	
	public PausedState(StateScreen s, GameEventsListener listener) {
		super(s);
		gameEvents = listener;
		isRendered = false;
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
				gameEvents.onBack();
			}
		}
		
		// Handle button touch events
		List<TouchEvent> events = screen.game.getInput().getTouchEvents();
		len = events.size();
		for(int i = 0; i < len; i++) {
			TouchEvent event = events.get(i);
			
			if(event.type == TouchEvent.TOUCH_UP) {
				int x = event.x;
				int y = event.y;
				
				int bw = ButtonImageManager.BUTTON_WIDTH;
				int bh = ButtonImageManager.BUTTON_HEIGHT;
				
				if(x >= backX && x <= (backX + bw) && y >= backY && y < (backY + bh)) {
					isRendered = false;
					gameEvents.onBack();
				}
				else if (x >= replayX && x <= (replayX + bw) && y >= replayY && y < (replayY + bh)) {
					isRendered = false;
					gameEvents.onReplay();
				}
				else if (x >= resumeX && x <= (resumeX + bw) && y >= resumeY && y < (resumeY + bh)) {
					isRendered = false;
					gameEvents.onResume();
				}
			}
		}

	}

	@Override
	public void present(long deltaTime) {
		if(!isRendered) {
			// Render game over/victory message
			Graphics g = screen.game.getGraphics();
			GameImageManager images = ImageProvider.getGameImages(g);
			ButtonImageManager  buttons = ImageProvider.getButtons(g);
			
			int pausedHeight = images.paused.getHeight();
			int pausedWidth = images.paused.getWidth();
			
			g.drawRectangle(0, 0, g.getWidth(), g.getHeight(), BG_FADE_MASK);
			
			int x = (g.getWidth() - pausedWidth) / 2;
			int y = (g.getHeight() - (pausedHeight + ButtonImageManager.BUTTON_HEIGHT + VERTICAL_PADDING)) / 2;
			g.drawBitmap(images.paused, x, y);
			
			// Draw buttons
			y += pausedHeight + VERTICAL_PADDING;
			g.drawBitmap(buttons.back, x, y, ButtonImageManager.BUTTON_WIDTH, ButtonImageManager.BUTTON_HEIGHT);
			backX = x;
			backY = y;
			
			x += (pausedWidth - ButtonImageManager.BUTTON_WIDTH);
			g.drawBitmap(buttons.replay, x, y, ButtonImageManager.BUTTON_WIDTH, ButtonImageManager.BUTTON_HEIGHT);
			replayX = x;
			replayY = y;
			
			x = ((g.getWidth() - pausedWidth) / 2) + ((pausedWidth - ButtonImageManager.BUTTON_WIDTH) / 2);
			g.drawBitmap(buttons.play, x, y, ButtonImageManager.BUTTON_WIDTH, ButtonImageManager.BUTTON_HEIGHT);
			resumeX = x;
			resumeY = y;
			isRendered = true;
		}

	}

	@Override
	public void dispose() {
		// Nada.
	}

}

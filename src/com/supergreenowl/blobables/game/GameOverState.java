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

import android.graphics.Bitmap;

public class GameOverState extends ScreenState {

	private static final int SCORE_BG_COLOUR = 0xaa666666;
	
	private static final int VERTICAL_PADDING = 24;
	
	private boolean isRendered;
	
	private Bitmap gameOver;
	
	private int backX, backY;
	private int replayX, replayY;
	private int score;
	private boolean isHighScore;
	
	private GameEventsListener gameEvents;
	
	public GameOverState(StateScreen s, GameEventsListener gameEvents) {
		super(s);
		isRendered = false;
		isHighScore = false;
		score = 0;
		this.gameEvents = gameEvents;
	}
	
	public void setPlayerWon(boolean playerWon, int score, int gameWidth) {
		isHighScore = false;
		
		Graphics g = screen.game.getGraphics();
		gameOver = playerWon ? ImageProvider.getGameImages(g).victory : ImageProvider.getGameImages(g).gameOver;
		this.score = score;
		
		// Check high score
		if(playerWon && score > ScoreManager.getHighScore(screen.game)) {
			isHighScore = true;
			ScoreManager.setHighScore(screen.game, score);
		}
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
		
		List<TouchEvent> events = screen.game.getInput().getTouchEvents();
		len = events.size();
		for(int i = 0; i < len; i++) {
			TouchEvent event = events.get(i);
			
			if(event.type == TouchEvent.TOUCH_UP) {
				int x = event.x;
				int y = event.y;
				
				if(x >= backX && x <= (backX + ButtonImageManager.BUTTON_WIDTH) && y >= backY && y < (backY + ButtonImageManager.BUTTON_HEIGHT)) {
					isRendered = false;
					gameEvents.onBack();
				}
				else if (x >= replayX && x <= (replayX + ButtonImageManager.BUTTON_WIDTH) && y >= replayY && y < (replayY + ButtonImageManager.BUTTON_HEIGHT)) {
					gameEvents.onReplay();
					isRendered = false;
				}
			}
		}
	}

	@Override
	public void present(long deltaTime) {
		// Remember not to clear canvas so game remains visible underneath.
		
		// Only draw once!
		if(!isRendered) {
			// Must call setPlayerWon() first to choose image to draw
			if(gameOver == null) throw new IllegalStateException();
			
			// Render game over/victory message
			Graphics g = screen.game.getGraphics();
			g.drawRectangle(0, 0, g.getWidth(), g.getHeight(), SCORE_BG_COLOUR);
			int x = (g.getWidth() - gameOver.getWidth()) / 2;
			int y = (g.getHeight() - (gameOver.getHeight() + ButtonImageManager.BUTTON_HEIGHT + VERTICAL_PADDING)) / 2;
			g.drawBitmap(gameOver, x, y);
			
			int top = y;
			
			// Draw buttons
			ButtonImageManager buttons = ImageProvider.getButtons(g);
			y += gameOver.getHeight() + VERTICAL_PADDING;
			g.drawBitmap(buttons.back, x, y, ButtonImageManager.BUTTON_WIDTH, ButtonImageManager.BUTTON_HEIGHT);
			backX = x;
			backY = y;
			
			x += (gameOver.getWidth() - ButtonImageManager.BUTTON_WIDTH);
			g.drawBitmap(buttons.replay, x, y, ButtonImageManager.BUTTON_WIDTH, ButtonImageManager.BUTTON_HEIGHT);
			replayX = x;
			replayY = y;
			
			// Draw score
			GameImageManager gameImages = ImageProvider.getGameImages(g);
			int scoreWidth = Integer.toString(score).length() * GameImageManager.NUMBER_WIDTH;
			x = (g.getWidth() - scoreWidth - GameImageManager.STAR_WIDTH) / 2;
			y += (ButtonImageManager.BUTTON_HEIGHT - GameImageManager.STAR_HEIGHT) / 2;
			g.drawBitmap(gameImages.star, x, y, GameImageManager.STAR_WIDTH, GameImageManager.STAR_HEIGHT);
			x += GameImageManager.STAR_WIDTH;
			y += (GameImageManager.STAR_HEIGHT - GameImageManager.NUMBER_HEIGHT) / 2;
			gameImages.drawNumber(score, x, y);
			
			// Render a new high score icon.
			if(isHighScore) {
				x = (g.getWidth() - GameImageManager.HIGHSCORE_WIDTH) / 2;
				y = top + gameOver.getHeight() + VERTICAL_PADDING + ButtonImageManager.BUTTON_HEIGHT;
				g.drawBitmap(gameImages.highscore, x, y, GameImageManager.HIGHSCORE_WIDTH, GameImageManager.HIGHSCORE_HEIGHT);
			}
			
			isRendered = true;
		}
	}

	@Override
	public void dispose() {
		gameOver = null;
	}

}

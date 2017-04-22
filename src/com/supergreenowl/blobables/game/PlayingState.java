package com.supergreenowl.blobables.game;

import java.util.List;

import com.supergreenowl.blobables.framework.Graphics;
import com.supergreenowl.blobables.framework.KeyEvent;
import com.supergreenowl.blobables.framework.ScreenState;
import com.supergreenowl.blobables.framework.StateScreen;
import com.supergreenowl.blobables.framework.TouchEvent;
import com.supergreenowl.blobables.game.presentation.BoardDisplay;
import com.supergreenowl.blobables.game.presentation.GameImageManager;
import com.supergreenowl.blobables.game.presentation.ImageProvider;
import com.supergreenowl.blobables.model.BlobState;
import com.supergreenowl.blobables.model.GameEngine;
import com.supergreenowl.blobables.model.HumanPlayer;
import com.supergreenowl.blobables.model.ai.DroidPlayer;

import android.graphics.Bitmap;


/**
 * State of the game screen when the game is being played.
 * @author luke
 *
 */
public class PlayingState extends ScreenState {

	private static final int CELL_WIDTH = 48;
	private static final int CELL_HEIGHT = 48;
	
	private static final int STATS_COLS = 3;
	private static final int STATS_PADDING = 4;
	
	private static final short FADE_FPS = 60;
	private static final int CELL_FADE_DURATION = 500;
	
	private static final int BACKGROUND_COLOUR = 0xff000000;
	private static final int STATS_COLOUR = 0xff333333;

	private DroidPlayer[] ai;
	private HumanPlayer human;
	private int rows;
	private int cols;
	private int gameWidth;
	
	private byte[] prevBoard;
	private BoardDisplay display;
	
	private GameEngine engine;
	private GameEventsListener gameEvents;
	
	/**
	 * Creates a new playing state for the game.
	 * @param s Game screen that this is a state of.
	 * @param human Human player. 
	 * @param ai Array of players for the game.
	 * @param gameEvents Game events listener for handling game state changes.
	 */
	public PlayingState(StateScreen s, HumanPlayer human, DroidPlayer[] ai, GameEventsListener gameEvents) {
		super(s);
		
		this.gameEvents = gameEvents;
		
		Graphics g = s.game.getGraphics();
		cols = g.getWidth() / CELL_WIDTH;
		cols -= STATS_COLS;
		rows = g.getHeight() / CELL_HEIGHT;
		int size = rows * cols;
		engine = new GameEngine(cols, rows);
		
		this.ai = ai;
		this.human = human;
		engine.registerPlayers(human, ai);
		
		// Setup the cell displays
		display = new BoardDisplay(FADE_FPS, CELL_FADE_DURATION, cols, rows);
		display.setFill(BACKGROUND_COLOUR, 0, 0, CELL_WIDTH * cols, CELL_HEIGHT * rows);
		prevBoard = new byte[size];
	}

	@Override
	public void update(long deltaTime) {
		
		// Check for end of game - doing this here so if previous update ended game
		// present() gets called once more to render final turn
		if(engine.isGameOver) {
			boolean isVictory = human != null && !human.isDead() && !engine.isBoardFull;
			gameEvents.onGameOver(isVictory, human.getScore(), gameWidth);
			return;
		}
		
		// Process user input
		
		// Process key events - menu key = pause
		List<KeyEvent> keys = screen.game.getInput().getKeyEvents();
		int len = keys.size();
		for(int i = 0; i < len; i++) {
			KeyEvent k = keys.get(i);
			if((k.keyCode == android.view.KeyEvent.KEYCODE_MENU || k.keyCode == android.view.KeyEvent.KEYCODE_BACK)
				&& k.type == KeyEvent.KEY_UP) {
				gameEvents.onPause(gameWidth);
				return;
			}
		}
		
		// Process touch events which are probably user placing blobs
		List<TouchEvent> touches = screen.game.getInput().getTouchEvents();
		
		len = touches.size();
		for(int i = 0; i < len; i++) {
			TouchEvent t = touches.get(i);
			if(t.type == TouchEvent.TOUCH_UP) {
				// if user clicked on a square
				// place cell if allowed
				
				int x = t.x / CELL_WIDTH;
				int y = t.y / CELL_HEIGHT;
				
				// Skip touches outside of board
				if(x >= cols || y >= rows || x < 0 || y < 0) break;
				
				if(human != null && !human.isDead() && human.getStock() > 0 && engine.board.getColour(x, y) == BlobState.DEAD) {
					engine.board.putBlob(x, y, human.colour);
					human.setCellsCount(engine.board.getBlobCount(human.colour));
					human.removeStock();
				}
			}
		}
		
		// Update the state of the game board
		engine.update(deltaTime);
		
		// Update display of cells now board has changed
		for(int y = 0; y < rows; y++) {
			for(int x = 0; x < cols; x++) {
				byte colour = engine.board.getColour(x, y);
				int idx = (y*cols)+x;
				byte oldColour = prevBoard[idx];
				
				if(colour != oldColour) {
					display.setColour(x, y, colour);
					prevBoard[idx] = colour;
				}
			}
		}
	}

	@Override
	public void present(long deltaTime) {
		Graphics g = screen.game.getGraphics();
		GameImageManager images = ImageProvider.getGameImages(g);
		
		// Draw the blobs
		// When the game is over, draws full images with no fade
		if(engine.isGameOver) display.finish();
		display.animate(g, deltaTime);
		
		// Draw game stats
		int statsWidth = g.getWidth() - (CELL_WIDTH * cols);
		gameWidth = g.getWidth() - statsWidth;
		g.drawRectangle(gameWidth, 0, statsWidth, g.getHeight(), STATS_COLOUR);
		
		int y = STATS_PADDING;
		int left = g.getWidth() - statsWidth + STATS_PADDING;
		
		if(human != null) {
			Bitmap blob = images.getBitmap(human.colour);
			if(blob != null) {
				g.drawBitmap(blob, left, y);
				if(human.isDead()) g.drawBitmap(images.cross, left, y);
				int x = left + CELL_WIDTH + STATS_PADDING;
				int numberY = y + ((CELL_HEIGHT - GameImageManager.NUMBER_HEIGHT) / 2);
				images.drawNumber(human.getCellsCount(), x, numberY);
				y += CELL_HEIGHT + STATS_PADDING;
			}
		}
		
		if(ai != null) {
			for(int i = 0; i < ai.length; i++) {
				DroidPlayer p = ai[i];
				Bitmap blob = images.getBitmap(p.colour);
				if(blob == null) continue;
				
				g.drawBitmap(blob, left, y);
				if(p.isDead()) g.drawBitmap(images.cross, left, y);
				int x = left + CELL_WIDTH + STATS_PADDING;
				int numberY = y + ((CELL_HEIGHT - GameImageManager.NUMBER_HEIGHT) / 2);
				images.drawNumber(p.getCellsCount(), x, numberY);
				y += CELL_HEIGHT + STATS_PADDING;
			}
		}
		
		// Draw is death permanent indicator
		g.drawBitmap(images.tombstone, left, y);
		if(!engine.isDeathPermanent) g.drawBitmap(images.cross, left, y);
		
		// Render score
		if(human != null) {
			y += CELL_HEIGHT + STATS_PADDING;
			g.drawBitmap(images.star, left, y, GameImageManager.STAR_HEIGHT, GameImageManager.STAR_HEIGHT);
			y += (GameImageManager.STAR_HEIGHT - GameImageManager.NUMBER_HEIGHT) / 2;
			images.drawNumber(human.getScore(), left + STATS_PADDING + GameImageManager.STAR_WIDTH, y);
		}
		
		// Draw human player stock
		if(human != null) {
			Bitmap humanBlob = images.getBitmap(human.colour);
			int stock = human.getStock();
			if(stock > 0) {
				int stockW = CELL_WIDTH / 2;
				int stockH = CELL_HEIGHT / 2;
				int stockCols = statsWidth / stockW;
				y = g.getHeight() - stockH;
				int stockX = 0;
				for(int i = 0; i < stock; i++) {
					if(stockX == stockCols) {
						stockX = 0;
						y -= stockH;
					}
					g.drawScaledBitmap(humanBlob, left + (stockX * stockW), y, CELL_WIDTH, CELL_HEIGHT, stockW, stockH);
					stockX++;
				}
			}
		}

	}
	
	@Override
	public void pause() {
		gameEvents.onPause(gameWidth);
	}

	@Override
	public void resume() {
		// Nothing to resume.

	}
	
	@Override
	public void dispose() {
		// Nothing to dispose.
	}
	
	/**
	 * Resets the game state so it can begin again.
	 */
	public void reset() {
		engine.reset();
		display.reset();
	}
	
	/**
	 * Signals to the display that it should completely re-draw itself on the next frame.
	 */
	public void invalidateDisplay() {
		display.invalidateDisplay();
	}

}

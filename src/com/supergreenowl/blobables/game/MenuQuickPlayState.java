package com.supergreenowl.blobables.game;

import java.util.ArrayList;
import java.util.List;

import com.supergreenowl.blobables.framework.Graphics;
import com.supergreenowl.blobables.framework.KeyEvent;
import com.supergreenowl.blobables.framework.ScreenState;
import com.supergreenowl.blobables.framework.StateScreen;
import com.supergreenowl.blobables.framework.TouchEvent;
import com.supergreenowl.blobables.framework.input.Hotspot;
import com.supergreenowl.blobables.framework.input.RectangleHotspot;
import com.supergreenowl.blobables.game.presentation.ButtonImageManager;
import com.supergreenowl.blobables.game.presentation.ImageProvider;
import com.supergreenowl.blobables.game.presentation.MenuImageManager;
import com.supergreenowl.blobables.model.BlobState;
import com.supergreenowl.blobables.model.HumanPlayer;
import com.supergreenowl.blobables.model.ai.DroidFactory;
import com.supergreenowl.blobables.model.ai.DroidPlayer;

public class MenuQuickPlayState extends ScreenState {

	private static final byte PLAYER_OFF = 0;
	private static final byte PLAYER_EASY = 1;
	private static final byte PLAYER_MEDIUM = 2;
	private static final byte PLAYER_HARD = 3;
	
	private static final int PADDING = 24;
	private static final int BACKGROUND_COLOUR = 0;
	private static final int PLAYERS_COLOUR = 0xff999999;
	
	private byte[] players;
	private byte[] colours;
	
	private List<Hotspot> hotspots;
	private MenuEventsListener menuEvents;
	private boolean isLayoutValid;
	
	public MenuQuickPlayState(StateScreen s, MenuEventsListener menuEvents) {
		super(s);
		this.menuEvents = menuEvents;
		
		players = new byte[BlobState.NUM_COLOURS];
		colours = new byte[BlobState.NUM_COLOURS];
		
		for(int i = 0; i < BlobState.NUM_COLOURS; i++) {
			players[i] = i == 0 ? PLAYER_MEDIUM : PLAYER_EASY;
			colours[i] = (byte)(BlobState.C1 + i);
		}
		
		hotspots = new ArrayList<Hotspot>();
		
		isLayoutValid = false;
	}
	
	@Override
	public void pause() {
		// Nothing to pause.

	}

	@Override
	public void resume() {
		// Nothing to pause.
	}

	@Override
	public void update(long deltaTime) {
		
		List<KeyEvent> pressed = screen.game.getInput().getKeyEvents();
		int len = pressed.size();
		for(int i = 0; i < len; i++) {
			KeyEvent k = pressed.get(i);
			if(k.type == KeyEvent.KEY_UP && k.keyCode == android.view.KeyEvent.KEYCODE_BACK) {
				isLayoutValid = false;
				menuEvents.onHome();
			}
		}
		
		// Dispatch touch events to screen hotspots
		List<TouchEvent> touches = screen.game.getInput().getTouchEvents();
		len = touches.size();
		int hotspotLen = hotspots.size();
		for(int i = 0; i < len; i++) {
			TouchEvent t = touches.get(i);
			if(t.type == TouchEvent.TOUCH_UP) {
				for(int j = 0; j < hotspotLen; j++) {
					Hotspot h = hotspots.get(j);
					if(h.handle(t)) break;
				}
			}
		}

	}

	@Override
	public void present(long deltaTime) {
		if(!isLayoutValid) {
			
			hotspots.clear(); // Remove current hotspots
			
			Graphics g = screen.game.getGraphics();
			g.fill(BACKGROUND_COLOUR);
			int w = g.getWidth();
			int h = g.getHeight();
			
			MenuImageManager images = ImageProvider.getMenuImages(g);
			
			int widthPerPlayer = w / BlobState.NUM_COLOURS;
			int offset = (widthPerPlayer - MenuImageManager.DIFFICULTY_WIDTH) / 2;
			int y = (h - MenuImageManager.DIFFICULTY_HEIGHT) / 2;
			g.drawRectangle(0, y, w, MenuImageManager.DIFFICULTY_HEIGHT, PLAYERS_COLOUR);
			
			// draw setup title
			int setupY = (y - MenuImageManager.SETUP_HEIGHT) / 2;
			g.drawBitmap(images.setup, (w - MenuImageManager.SETUP_WIDTH) / 2, setupY, MenuImageManager.SETUP_WIDTH, MenuImageManager.SETUP_HEIGHT);
			
			// Draw the "you" label
			int youX = (widthPerPlayer - MenuImageManager.YOU_WIDTH) / 2;
			g.drawBitmap(images.you, youX, y, MenuImageManager.YOU_WIDTH, MenuImageManager.YOU_HEIGHT);
			
			// Draw player blob icons
			for(int i = 0; i < BlobState.NUM_COLOURS; i++) {
				int x = (widthPerPlayer * i) + offset;
				
				int colourOffset = 0;
				byte colour = colours[i];
				switch(colour) {
					case BlobState.C1: colourOffset = 0; break;
					case BlobState.C2: colourOffset = 1; break;
					case BlobState.C3: colourOffset = 2; break;
					case BlobState.C4: colourOffset = 3; break;
				}
				
				int difficultyOffset = players[i] == PLAYER_OFF ? 1 : players[i] - 1;
				int atlasOffset = (colourOffset * 3) + difficultyOffset;
				images.difficulty.draw(atlasOffset, x, y);
				
				if(players[i] == PLAYER_OFF) {
					int crossY = (MenuImageManager.DIFFICULTY_HEIGHT - MenuImageManager.CROSS_HEIGHT) + y;
					g.drawBitmap(images.bigCross, x, crossY);
				}
				
				// Register a screen hotspot to handle touch events
				final int playerIdx = i;
				hotspots.add(new RectangleHotspot(x, y, MenuImageManager.DIFFICULTY_WIDTH, MenuImageManager.DIFFICULTY_HEIGHT) {
					@Override
					protected void onTouch(TouchEvent event) {
						playerIconTouched(playerIdx);
					}
				});
			}
			
			// Render buttons
			ButtonImageManager buttons = ImageProvider.getButtons(g);
			
			// Draw play button
			int buttonX = w - ButtonImageManager.BUTTON_WIDTH - PADDING;
			int buttonY = h - ButtonImageManager.BUTTON_HEIGHT - PADDING;
			g.drawBitmap(buttons.play, buttonX, buttonY, ButtonImageManager.BUTTON_WIDTH, ButtonImageManager.BUTTON_HEIGHT);
			hotspots.add(new RectangleHotspot(buttonX, buttonY, ButtonImageManager.BUTTON_WIDTH, ButtonImageManager.BUTTON_HEIGHT) {
				@Override
				protected void onTouch(TouchEvent event) {
					startGame();
				}
			});
			
			// Draw back button
			buttonX = PADDING;
			g.drawBitmap(buttons.back, buttonX, buttonY, ButtonImageManager.BUTTON_WIDTH, ButtonImageManager.BUTTON_HEIGHT);
			hotspots.add(new RectangleHotspot(buttonX, buttonY, ButtonImageManager.BUTTON_WIDTH, ButtonImageManager.BUTTON_HEIGHT) {
				@Override
				protected void onTouch(TouchEvent event) {
					isLayoutValid = false;
					menuEvents.onHome();
				}
			});
			
			isLayoutValid = true;
		}

	}

	@Override
	public void dispose() {
		// Nothing to dispose.
	}
	
	/**
	 * Toggles player colours and difficulties.
	 * @param player Index of the player for which the icon was pressed.
	 */
	private void playerIconTouched(int player) {
		if(player == 0) {
			// special case for human - change colour
			byte colourNow = colours[0];
			// cycle human colour
			byte nextColour = colourNow == BlobState.C4 ? BlobState.C1 : (byte)(colourNow + 1);
			
			// swap colour of human and whichever AI player currently uses next colour
			int indexToSwap = 0;
			for(int i = 1; i < BlobState.NUM_COLOURS; i++) {
				if(colours[i] == nextColour) {
					indexToSwap = i;
					break;
				}
			}
			
			colours[0] = nextColour;
			colours[indexToSwap] = colourNow;
			isLayoutValid = false;
		}
		else {
			// For an AI player icon touched, cycle difficulty and on/off for that player
			byte state = players[player];
			byte nextState = state == PLAYER_HARD ? PLAYER_OFF : (byte)(state + 1);
			players[player] = nextState;
			isLayoutValid = false;
		}
	}
	
	/**
	 * Starts the game.
	 */
	private void startGame() {
		
		// Count number of players turned on
		int numAiPlayers = 0;
		for(int i = 1; i < BlobState.NUM_COLOURS; i++) {
			if(players[i] != PLAYER_OFF) numAiPlayers++;
		}
		
		HumanPlayer human  = new HumanPlayer(colours[0]);
		
		
		DroidPlayer[] ai = null;
		
		if(numAiPlayers > 0) {
			ai = new DroidPlayer[numAiPlayers];
			int nextPlayer = 0;
			for(int i = 1; i < BlobState.NUM_COLOURS; i++) {
				if(players[i] != PLAYER_OFF) {
					
					// Create AI player of requested difficulty
					DroidPlayer droid = null;
					if(players[i] == PLAYER_EASY) droid = DroidFactory.easyPlayer(colours[i]);
					else if(players[i] == PLAYER_MEDIUM) droid = DroidFactory.mediumPlayer(colours[i]);
					else if(players[i] == PLAYER_HARD) droid = DroidFactory.hardPlayer(colours[i]);
					
					if(droid != null) ai[nextPlayer++] = droid;
				}
			}
		}
		
		isLayoutValid = false; // invalidate the layout so if we return to this screen, it will be re-drawn
		
		// Start the game!
		screen.game.setScreen(new GameScreen(screen.game, human, ai));
	}

}

package com.supergreenowl.blobables.game;

import com.supergreenowl.blobables.framework.Game;
import com.supergreenowl.blobables.framework.StateScreen;
import com.supergreenowl.blobables.game.presentation.ImageProvider;
import com.supergreenowl.blobables.model.HumanPlayer;
import com.supergreenowl.blobables.model.ai.DroidPlayer;

public class GameScreen extends StateScreen implements GameEventsListener {

	private PlayingState playingState;
	private GameOverState gameOverState;
	private PausedState pauseState;
	
	public GameScreen(Game g, HumanPlayer human, DroidPlayer[] players) {
		super(g);
		
		// Initialise the states of the screen
		gameOverState = new GameOverState(this, this);
		playingState = new PlayingState(this, human, players, this);
		pauseState = new PausedState(this, this);
		
		// Set the starting state for the game
		game.getInput().setSwallowsBackKey(true);
		setState(playingState);
	}
	
	@Override
	public void onGameOver(boolean playerWon, int score, int gameWidth) {
		gameOverState.setPlayerWon(playerWon, score, gameWidth);
		setState(gameOverState);
	}

	
	@Override
	public void onReplay() {
		playingState.reset();
		setState(playingState);
	}
	

	@Override
	public void onPause(int gameWidth) {
		setState(pauseState);
	}

	@Override
	public void onResume() {
		playingState.invalidateDisplay();
		setState(playingState);
	}
	
	@Override
	public void onBack() {
		game.getInput().setSwallowsBackKey(false);
		game.setScreen(new MenuScreen(game));
	}

	/**
	 * Free up resources from all the states of the screen.
	 */
	@Override
	public void dispose() {
		playingState.dispose();
		pauseState.dispose();
		gameOverState.dispose();
		ImageProvider.disposeAll();
	}

}

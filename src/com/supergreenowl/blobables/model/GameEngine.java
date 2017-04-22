package com.supergreenowl.blobables.model;

import com.supergreenowl.blobables.model.ai.DroidPlayer;


/**
 * Runs the game of blobs.
 * @author luke
 *
 */
public class GameEngine {

	private static final int TICK_DURATION = 2000;
	
	private static final int STARTING_CELLS = 5;
	private static final int DEATH_TIME = 5;
	
	public boolean isDeathPermanent = false;
	
	public final Board board;
	public boolean isGameOver = false;
	public boolean isBoardFull = false;
	public boolean isHumanDead = false;
	
	private HumanPlayer human;
	private DroidPlayer[] ai;
	private long elapsedTick = 0;
	private int boardSize;
	
	/**
	 * Creates a new game engine to run a game with the specified
	 * board size.
	 * @param w
	 * @param h
	 */
	public GameEngine(int w, int h) {
		boardSize = w * h;
		board = new BlobablesBoard(w, h);
	}
	
	/**
	 * Sets the players for the game.
	 * @param human Human player (possibly null).
	 * @param ai AI players (possibly null).
	 */
	public void registerPlayers(HumanPlayer human, DroidPlayer[] ai) {
		if(human != null) {
			human.setStock(STARTING_CELLS);
			human.setBoard(board);
		}
		this.human = human;
		
		if(ai != null) {
			int len = ai.length;
			for(int i = 0; i < len; i++) {
				ai[i].setStock(STARTING_CELLS);
				ai[i].setBoard(board);
			}
		}
		this.ai = ai;
	}
	
	/**
	 * Updates the game board, player stats and lets AI players
	 * play.
	 * @param delta Elapsed milliseconds since last update.
	 */
	public void update(long delta) {
		
		elapsedTick += delta;
		
		long aiDelta = delta;
		
		while(elapsedTick > TICK_DURATION) {
			elapsedTick -= TICK_DURATION;	
			board.transition();
			
			boolean checkVictory = false;
			
			if(board.getTime() >= DEATH_TIME) {
				checkVictory = isDeathPermanent = true;
			}
			
			// Game might be over by full board or elimination
			isGameOver(checkVictory);
			
			// Kill off dead players, update cell counts after transition and add stock to live players
			if(human != null) {
				int cells = board.getBlobCount(human.colour);
				if(cells == 0 && isDeathPermanent) human.kill();
				
				// must update cells count before play() as AI players might want to know it
				human.setCellsCount(cells);
				human.addPoints(cells); // update human score
				
				if(!isGameOver) human.addStock();
			}
			
			if(ai != null) {
				int len = ai.length;
				for(int i = 0; i < len; i++) {
					DroidPlayer p = ai[i];
					int cells = board.getBlobCount(p.colour);
					if(cells == 0 && isDeathPermanent) p.kill();
					
					// must update cells count before play() as AI players might want to know it
					p.setCellsCount(cells);
					
					if(!isGameOver) p.addStock();
				}
			}
		}
		
		/*
		 * In theory, if delta > TICK_DURATION then the AI may not be asked to play
		 * until after multiple board updates e.g. if 5s elapsed then the board will update
		 * twice and then the AI will play for 5s on the final board when really they should
		 * play on the original board, after 1 update and then on the final board.
		 * 
		 * In practice, this requires delta > 2s to happen so I'm just going to update the AI
		 * after the board rather than trying to portion the elapsed time between board updates.
		 */
		if(ai != null) aiPlay(aiDelta);
	}
	
	/**
	 * Instructs all the games players to play. This will make AI players
	 * take their moves.
	 * @param delta Milliseconds elapsed since the last time AI players were asked to play.
	 */
	private void aiPlay(long delta) {
		
		for(int i = 0; i < ai.length; i++) {
			DroidPlayer p = ai[i];
			
			if(!isGameOver) p.play(delta);
			
			// game might be over here due to full board
			isGameOver(false);
		}
	}
	
	/**
	 * Resets the game and all players so it can be restarted.
	 */
	public void reset() {
		isDeathPermanent = isGameOver = isBoardFull = isHumanDead = false;
		board.reset();
		elapsedTick = 0;
		
		if(human != null) {
			human.reset();
			human.setStock(STARTING_CELLS);
		}
		
		if(ai != null) {
			int p = ai.length;
			for(int i = 00; i < p; i++) {
				ai[i].reset();
				ai[i].setStock(STARTING_CELLS);
			}
		}
	}
	
	/**
	 * Determines if the game has ended. That is the board is full or only 1 player remains
	 * (if there was more than 1 player to begin with).
	 * @param checkForVictory If true, checks for only one
	 * player being left. Otherwise just checks for full board.
	 * @return
	 */
	private boolean isGameOver(boolean checkForVictory) {
	
		// Once game is over, it always is
		if(isGameOver) return true;
		
		// Check for end of game caused by a full board
		if(board.getBlobCount() == boardSize) {
			return isGameOver = isBoardFull = true;
		}
		
		// Check for end of game caused by only one player remaining
		// (unless this was always a 1 player game)
		if(checkForVictory) {
			int remainingPlayers = 0;
			boolean isOnePlayer = human != null && ai == null;
			if(human != null) {
				
				if(board.getBlobCount(human.colour) > 0)
					remainingPlayers++;
				
				// End of game as human is out
				else return isGameOver = isHumanDead = true;
			}
			
			if(ai != null) {
				int len = ai.length;
				for(int i = 0; i < len; i++) {
					if(board.getBlobCount(ai[i].colour) > 0)
						remainingPlayers++;
				}
			}
			
			if(remainingPlayers <= 1 && !isOnePlayer) {
				return isGameOver = true;
			}
		}
		
		return false;
	}
	
}

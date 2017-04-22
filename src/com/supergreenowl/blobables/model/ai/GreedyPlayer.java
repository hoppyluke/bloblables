package com.supergreenowl.blobables.model.ai;

import java.util.Arrays;

import com.supergreenowl.blobables.model.BlobState;
import com.supergreenowl.blobables.model.Board;

/**
 * AI player that employs a greedy-based algorithm to select the best
 * place to play each time it reacts.
 * @author luke
 *
 */
public class GreedyPlayer extends ReactionSpeedPlayer {

	private static final byte SCORE_SURVIVE = 1;
	private static final byte SCORE_SURVIVE_DEFENSIVE = 10;
	private static final byte SCORE_GROW = 1;
	private static final byte SCORE_KILL = 10; // 1.0.3 changed to 10 from 2
	private static final byte SCORE_UNDEFINED = -1;
	
	private static final int LOCATION_UNDEFINED = -1;
	
	private int defenseThreshold;
	
	private byte[] colours;
	private byte[] neighbours;
	private byte[] hostileNeighbours;
	private byte[] scores;
	private byte[] calculatedScores;
	
	private int w, h, size;
	
	/**
	 * Creates a new Greedy AI player.
	 * @param colour Blob colour for this player.
	 * @param reactionSpeed Reaction speed in milliseconds.
	 * @param defenseThreshold Number of blobs the player must have on the board before it stops playing defensively.
	 */
	public GreedyPlayer(byte colour, int reactionSpeed, int defenseThreshold) {
		super(colour, reactionSpeed);
		this.defenseThreshold = defenseThreshold;
	}
	
	@Override
	public void setBoard(Board b) {
		super.setBoard(b);
		w = b.getWidth();
		h = b.getHeight();
		size = b.getSize();
		
		// initialise calculation arrays for scoring cells
		colours = new byte[size];
		neighbours = new byte[size];
		hostileNeighbours = new byte[size];
		scores = new byte[size];
		calculatedScores = new byte[size];
	}

	@Override
	protected void react() {
		if(getStock() <= 0) return; // Will only play when possible
		
		int cells = getCellsCount();
		
		// Choose a random start cell
		if(cells == 0) {
			putRandomCell();
			return;
		}
		
		Board b = getBoard();
		
		// Loop through board and get info on blobs available
		for(int y = 0; y < h; y++) {
			for(int x = 0; x < w; x++) {
				int i = (y * w) + x;
				byte c = b.getColour(x, y);
				colours[i] = c;
				
				// Casts are safe since these methods return r s.t. 0 <= r <= 8
				neighbours[i] = (byte)b.neighbours(x, y);
				
				// for an empty cell hostile neighbours holds # that would be hostile to my own blob
				// for a non-empty cell, hostile neighbours holds # hostile to blob at (x, y)
				hostileNeighbours[i] = c == BlobState.DEAD ? (byte)b.hostileNeighbours(x, y, colour) : (byte)b.hostileNeighbours(x, y);	
			}
		}
		
		// Loop through board again and assign scores to empty cells where I could play
		Arrays.fill(scores, (byte)0);
		Arrays.fill(calculatedScores, SCORE_UNDEFINED);
		int kill = b.getKillLevel();
		int grow = b.getGrowLevel();
		boolean isDefensive = cells < defenseThreshold;
		for(int y = 0; y < h; y++) {
			for(int x = 0; x < w; x++) {
				int i = (y * w) + x;
				byte c = colours[i];
				if(c != BlobState.DEAD) continue;
				
				// Score for blob placed here surviving next transition
				if(hostileNeighbours[i] < kill && neighbours[i] >= grow)
					scores[i] += isDefensive ? SCORE_SURVIVE_DEFENSIVE : SCORE_SURVIVE;
				
				// Score for blob placed here killing hostile blobs
				boolean cellsUp = y > 0;
				boolean cellsDown = y < (h-1);
				
				if(x > 0) {
					scores[i] += scoreCell(i-1, kill, grow);
					if(cellsUp) scores[i] +=  scoreCell(i-w-1, kill, grow); // (x-1, y-1)
					if(cellsDown) scores[i] +=  scoreCell(i+w-1, kill, grow);; // (x-1, y+1)
				}
				
				if(x < (w - 1)) {
					scores[i] +=  scoreCell(i+1, kill, grow);; // (x+1, y)
					if(cellsUp) scores[i] +=  scoreCell(i-w+1, kill, grow); // (x+1, y-1)
					if(cellsDown) scores[i] +=  scoreCell(i+w+1, kill, grow); // (x+1, y+1)
				}
				
				if(cellsUp) scores[i] +=  scoreCell(i-w, kill, grow); // (x, y-1)
				if(cellsDown) scores[i] +=  scoreCell(i+w, kill, grow); // (x, y+1)
			}
		}
		
		// Look for cell with maximum score
		int playAt = LOCATION_UNDEFINED;
		int maxScore = 0;
		for(int i = 0; i < size; i++) {
			if(scores[i] > maxScore) {
				playAt = i;
				maxScore = scores[i];
			}
		}
		
		// If there is a cell with a non-zero score then play there
		if(playAt != LOCATION_UNDEFINED) {
			int x = playAt % w;
			int y = playAt / w;
			putCell(x, y);
		}
	}

	/**
	 * Calculates a score based on a neighbouring cell.
	 * @param i
	 * @param kill
	 * @param grow
	 * @return
	 */
	private byte scoreCell(int i, int kill, int grow) {
		
		// Only need to score a cell once per turn, return cached result if known
		if(calculatedScores[i] != SCORE_UNDEFINED)
			return calculatedScores[i];
		
		byte score = 0;
		byte c = colours[i];
		if(c == colour) {
			// score for keeping my blobs alive
			int n = neighbours[i];
			if(n < grow)
				score += (n * SCORE_SURVIVE);
		}
		else if(c != BlobState.DEAD && c != BlobState.UNCOLOURED) {
			// score for killing hostile blobs
			int h = hostileNeighbours[i];
			if(h < kill)
				score += (h * SCORE_KILL);
		} else if(c == BlobState.DEAD && neighbours[i] == (grow - 1)) {
			score += SCORE_GROW;
		}
		
		// cache result for next time
		return calculatedScores[i] = score;
	}

}

package com.supergreenowl.blobables.model.ai;

import com.supergreenowl.blobables.model.BlobState;
import com.supergreenowl.blobables.model.Board;

/**
 * AI player class that has 2 behaviours to play: attack (try to kill a hostile blob) and build (try to
 * put a blob down next its own blobs). The decision to attack or build is based on having a certain number of
 * blobs on the board. If the player has this threshold number, it will attack. Otherwise it builds. This is a
 * reaction speed based player, it will only make an attack/build decision every so often. 
 * 
 * @author luke
 *
 */
public class AttackBuildPlayer extends ReactionSpeedPlayer {
	
	private int attackThreshold;
	private int size;
	
	private int lastIndex;
	
	/**
	 * Creates a new AttackBuildPlayer.
	 * @param colour Blob colour for this player.
	 * @param board Board player is playing on.
	 * @param reactionSpeed Reaction speed in milliseconds.
	 * @param attackThreshold Number of blobs this player should have on the board before it starts
	 * attack behaviour.
	 */
	public AttackBuildPlayer(byte colour, int reactionSpeed, int attackThreshold) {
		super(colour, reactionSpeed);
		this.attackThreshold = attackThreshold;
		lastIndex = -1;
	}

	@Override
	public void setBoard(Board b) {
		super.setBoard(b);
		size = b.getWidth() * b.getHeight();
	}
	
	@Override
	protected void react() {
		// Can't play with no cells to place
		if(getStock() <= 0) return;
		
		if(getCellsCount() >= attackThreshold) attack();
		else build();

	}
	
	@Override
	public void reset() {
		super.reset();
		lastIndex = -1;
	}

	/**
	 * Looks for a place to put a blob that will kill a hostile blob
	 * on the board. Note that no blob is placed if no such place exists.
	 */
	private void attack() {
		
		Board b = getBoard();
		int w = b.getWidth();
		int kill = b.getKillLevel() - 1;
		byte myColour = colour;
		
		int start = gen.nextInt(size);
		int i = start;
		
		// Loop through the whole board looking for a blob that can be killed
		do {
			int x = i % w;
			int y = i / w;
			
			byte c = b.getColour(x, y);
			
			// Only want to attack hostile cells
			if(c != BlobState.DEAD && c != BlobState.UNCOLOURED && c != myColour) {
			
				int totalNeighbours = b.neighbours(x, y);
				int friendlyNeighbours = b.neighbours(x, y, c);
				
				// if blob could be killed then attack it
				if(totalNeighbours - friendlyNeighbours == kill) {
					
					// if possible place a blob near target to kill it
					if(tryPutNeighbour(x, y)) {
						return;
					}
				}
			}
			
			i++;
			if(i >= size) i = 0; // return to beginning once we reach end
		} while(i != start);
			
	}
	
	/**
	 * Places a blob on the board close to other blobs of this player.
	 */
	private void build() {
		
		Board b = getBoard();
		int w = b.getWidth();
		int h = b.getHeight();
		
		if(lastIndex != -1) {
			// If we know where to build then try and add a cell near there
			int x = lastIndex % w;
			int y = lastIndex / w;
			
			int iterations = Math.max(Math.max(x, w-1-x), Math.max(y, h-1-y));
			
			// Checks cells in a spiral from the last cell placed 
			for(int i = 0; i <= iterations; i++) {
				for(int ny = (y-i); ny <= (y+i); ny++) {
					for(int nx = (x-i); nx <= (x+i); nx++) {
						if(tryPutNeighbour(nx, ny)) {
							lastIndex = (ny * w) + nx;
							return;
						}
					}
				}
			}
			
		} else {
			// Do not know where to build so just place cell at random
			lastIndex = putRandomCell();
		}
	}
}

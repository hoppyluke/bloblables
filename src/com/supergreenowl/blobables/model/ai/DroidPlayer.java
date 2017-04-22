package com.supergreenowl.blobables.model.ai;

import java.util.Random;

import com.supergreenowl.blobables.model.BlobState;
import com.supergreenowl.blobables.model.Board;
import com.supergreenowl.blobables.model.Player;


/**
 * Base type for all AI players. Provides access to game board
 * and defines abstract method that AI players should override to
 * take action in the game.
 * @author luke
 *
 */
public abstract class DroidPlayer extends Player {
	
	protected Random gen;
	
	/**
	 * Creates a new DroidPlayer playing on the specified game board.
	 * @param colour Player colour.
	 * @param board Board to play on.
	 */
	public DroidPlayer(byte colour) {
		super(colour);
		gen = new Random();
	}
	

	/**
	 * Triggers the playing logic for this DroidPlayer.
	 * @param delta Milliseconds elapsed since last game update.
	 */
	public abstract void play(long delta);
	
	/**
	 * Attempts to put a blob as a neighbour of the specified blob.
	 * @param x x-coordinate of blob to add neighbour to.
	 * @param y y-coordinate of blob to add neighbour to.
	 * @return True if a neighbour was added, false otherwise.
	 */
	protected boolean tryPutNeighbour(int x, int y) {
		if(tryPutCell(x-1, y-1)) return true;
		if(tryPutCell(x, y-1)) return true;
		if(tryPutCell(x+1, y-1)) return true;
		
		if(tryPutCell(x-1, y)) return true;
		if(tryPutCell(x+1, y)) return true;
		
		if(tryPutCell(x-1, y+1)) return true;
		if(tryPutCell(x, y+1)) return true;
		if(tryPutCell(x+1, y+1)) return true;
		
		return false;
	}
	
	/**
	 * Attempts to put a blob at the specified location.
	 * @param x x-coordinate of location to place blob.
	 * @param y y-coordinate of location to place blob.
	 * @return True if a blob was placed at the location; false if blob cannot be placed there.
	 */
	protected boolean tryPutCell(int x, int y) {
		Board b = getBoard();
		if(x < 0 || x >= b.getWidth() || y < 0 || y >= b.getHeight()) return false;
		
		if(b.getColour(x, y) == BlobState.DEAD) {
			putCell(x, y);
			return true;
		}
		
		return false;
	}
	
	/**
	 * Places a random cell on the board.
	 * @return Index of the location cell was placed. Co-ordinates may be
	 * obtained from index as (x = i % width) and (y = index / width).
	 * Returns -1 if the board is full and no cell was placed.
	 */
	protected int putRandomCell() {
		Board b = getBoard();
		
		int w = b.getWidth();
		int size = b.getSize();
		
		/* Random location is determined by linear search from a random start point.
		 * This means performance will not degrade significantly on a full board as it would
		 * if random coordinates were picked until an empty cell is found (many duplicates could be tried
		 * in this approach). In addition, this will not infinite loop on a full board as it only goes
		 * through the board at most once.
		 */
		
		int i, x, y;
		int start = i = gen.nextInt(size);
		boolean placed = false;
		
		do {
			x = i % w;
			y = i / w;
			
			placed = tryPutCell(x, y);
			
			i++;
			if(i >= size) i = 0;
		} while(!placed && i != start);
		
		return placed ? i : -1;
	}
}

package com.supergreenowl.blobables.model;

import java.util.Arrays;

public class BlobablesBoard implements Board {

	private static final byte LIFETIME = 5;
	private static final int KILL = 3;
	private static final int GROW = 3;
	
	/**
	 * Number of transitions before blobables start being killed.
	 * Allows players to put down blobables at game start without them dying from loneliness.
	 */
	private static final int TRANSITIONS_BEFORE_DEATH = 5;
	
	private byte[] b;
	private byte[] nb;
	private byte[] ttl;
	
	private int w, h, s, time;
	private int lastRow, lastCol;
	
	private ColourCounter blobCounts, colourCounts;
	
	public BlobablesBoard(int columns, int rows) {
		
		// Setup board size for the specified dimensions
		this.w = columns;
		this.h = rows;
		this.s = rows * columns;
		this.lastCol = w - 1;
		this.lastRow = h - 1;
		
		// Setup colour and time-to-live arrays
		b = new byte[s];
		nb = new byte[s];
		ttl = new byte[s];
		
		// Setup blob counters
		blobCounts = new ColourCounter();
		colourCounts = new ColourCounter();
	}
	
	@Override
	public int getWidth() {
		return w;
	}

	@Override
	public int getHeight() {
		return h;
	}

	@Override
	public int getSize() {
		return s;
	}

	@Override
	public void putBlob(int x, int y, byte colour) {
		int idx = (y*w) + x;
		
		if(b[idx] != BlobState.DEAD) throw new IllegalArgumentException();
		
		b[idx] = colour;
		ttl[idx] = LIFETIME;
		blobCounts.count(colour);
	}

	@Override
	public boolean tryPutBlob(int x, int y, byte colour) {
		int idx = (y*w) + x;
		
		if(b[idx] == BlobState.DEAD) {
			b[idx] = colour;
			ttl[idx] = LIFETIME;
			blobCounts.count(colour);
			return true;
		}
		else return false;
	}

	@Override
	public byte getColour(int x, int y) {
		return b[(y*w)+x];
	}

	@Override
	public byte getTimeToLive(int x, int y) {
		return ttl[(y*w)+x];
	}

	@Override
	public void transition() {
	
		blobCounts.reset();
		
		for(int i = 0; i < s; i++) {
			int x = i % w;
			
			byte colour = b[i];
			nb[i] = colour; // default behaviour is for colour to persist
			
			if(colour == BlobState.DEAD) { 
				// Check to see if a new blob can grow
				int n = neighboursInternal(i, x);
				if(n == GROW) {
					byte c = calculateBlobColour(i, x);
					nb[i] = c;
					ttl[i] = LIFETIME;
					blobCounts.count(c);
				}
			}
			else {
				if(ttl[i]-- > 0) {
					int n = neighboursInternal(i, x);
					if(n < GROW && time > TRANSITIONS_BEFORE_DEATH) {
						// Death by loneliness - only implemented after set number of transitions
						// this gives (human) players a chance to get their bloables established
						nb[i] = BlobState.DEAD;
						ttl[i] = 0;
					}
					else if(colour == BlobState.UNCOLOURED) {
						// Uncoloured blob may get a colour
						byte c = calculateBlobColour(i, x);
						nb[i] = c;
						blobCounts.count(c);
					}
					else {
						// Coloured blob can either die by hostile neighbours or survive
						
						int h = hostileNeighboursInternal(i, x);
						if(h >= KILL) {
							// Blob killed by hostile neighbours
							nb[i] = BlobState.DEAD;
							ttl[i] = 0;
						}
						else {
							// Survival
							blobCounts.count(colour);
						}
					}
				}
				else {
					// Death by old age
					nb[i] = BlobState.DEAD;
				}
			}
		}
		
		// Copy new board values into the active board
		System.arraycopy(nb, 0, b, 0, s);
		
		// Increases board time
		time++;
	}

	@Override
	public int getTime() {
		return time;
	}

	@Override
	public int getBlobCount() {
		return blobCounts.getTotal();
	}

	@Override
	public int getBlobCount(byte colour) {
		if(colour == BlobState.DEAD) return s - blobCounts.getTotal();
		else return blobCounts.getCount(colour);
	}

	@Override
	public int neighbours(int x, int y) {
		return neighboursInternal((y*w)+x, x);
	}

	@Override
	public int neighbours(int x, int y, byte colour) {
		int i = (y*w)+x;
		int count = 0;
		
		boolean cellsUp = y > 0;
		boolean cellsDown = y < lastRow;
		
		if(x > 0) {
			if(b[i-1] == colour) count ++; // (x-1, y)
			if(cellsUp && b[i-w-1] == colour) count++; // (x-1, y-1)
			if(cellsDown && b[i+w-1] == colour) count++; // (x-1, y+1)
		}
		
		if(x < lastCol) {
			if(b[i+1] == colour) count++; // (x+1, y)
			if(cellsUp && b[i-w+1] == colour) count++; // (x+1, y-1)
			if(cellsDown && b[i+w+1] == colour) count++; // (x+1, y+1)
		}
		
		if(cellsUp && b[i-w] == colour) count++; // (x, y-1)
		if(cellsDown && b[i+w] == colour) count++; // (x, y+1)
		
		
		return count;
	}

	@Override
	public int hostileNeighbours(int x, int y) {
		return hostileNeighboursInternal((y*w) + x, x);
	}

	@Override
	public int hostileNeighbours(int x, int y, byte colour) {
		return hostileNeighboursInternal((y*w)+x, x, colour);
	}

	@Override
	public int getKillLevel() {
		return KILL;
	}

	@Override
	public int getGrowLevel() {
		return GROW;
	}
	
	@Override
	public void reset() {
		time = 0;
		blobCounts.reset();
		Arrays.fill(b, BlobState.DEAD);
		Arrays.fill(ttl, (byte)0);
	}

	private int neighboursInternal(int i, int x) {
		int count = 0;
		
		boolean cellsUp = i >= w;
		boolean cellsDown = i < (s - w);
		
		if(x > 0) {
			count += b[i-1]; // (x-1, y)
			if(cellsUp) count += b[i-w-1]; // (x-1, y-1)
			if(cellsDown) count += b[i+w-1]; // (x-1, y+1)
		}
		
		if(x < lastCol) {
			count += b[i+1]; // (x+1, y)
			if(cellsUp) count += b[i-w+1]; // (x+1, y-1)
			if(cellsDown) count += b[i+w+1]; // (x+1, y+1)
		}
		
		if(cellsUp) count += b[i-w]; // (x, y-1)
		if(cellsDown) count += b[i+w]; // (x, y+1)
		
		return count / BlobState.UNCOLOURED;
	}
	
	/**
	 * Calculates number of hostile neighbours for a blob.
	 * @param i Index into blobs array.
	 * @param x Column of blob on board.
	 * @return Number of hostile neighbouring blobs.
	 */
	private int hostileNeighboursInternal(int i, int x) {
		return hostileNeighboursInternal(i, x, b[i]);
	}
	
	/**
	 * Calculates number of hostile neighbours for a blob.
	 * @param i Index into blobs array.
	 * @param x Column of blob on board.
	 * @param colour Colour of blob to look for hostile neighbours of.
	 * @return Number of hostile neighbouring blobs.
	 */
	private int hostileNeighboursInternal(int i, int x, byte colour) {
		byte c = BlobState.DEAD;
		int count = 0;
		
		boolean cellsUp = i >= w;
		boolean cellsDown = i < (s - w);
		
		if(x > 0) {
			c = b[i-1]; // (x-1, y)
			if(c != BlobState.DEAD && c != BlobState.UNCOLOURED && c != colour) count++;
			
			if(cellsUp) {
				c = b[i-w-1]; // (x-1, y-1)
				if(c != BlobState.DEAD && c != BlobState.UNCOLOURED && c != colour) count++;
			}
			
			if(cellsDown) {
				c = b[i+w-1]; // (x-1, y+1)
				if(c != BlobState.DEAD && c != BlobState.UNCOLOURED && c != colour) count++;
			}
		}
		
		if(x < lastCol) {
			c = b[i+1]; // (x+1, y)
			if(c != BlobState.DEAD && c != BlobState.UNCOLOURED && c != colour) count++;
			
			if(cellsUp) {
				c = b[i-w+1]; // (x+1, y-1)
				if(c != BlobState.DEAD && c != BlobState.UNCOLOURED && c != colour) count++;
			}
			
			if(cellsDown) {
				c = b[i+w+1]; // (x+1, y+1)
				if(c != BlobState.DEAD && c != BlobState.UNCOLOURED && c != colour) count++;
			}
		}
		
		if(cellsUp) {
			c = b[i-w]; // (x, y-1)
			if(c != BlobState.DEAD && c != BlobState.UNCOLOURED && c != colour) count++;
		}
		
		if(cellsDown) {
			c = b[i+w]; // (x, y+1)
			if(c != BlobState.DEAD && c != BlobState.UNCOLOURED && c != colour) count++;
		}
		
		return count;
	}
	
	/**
	 * Calculates the colour for a new blob/change of colour.
	 * @param i Index into internal blobs array.
	 * @param x Column of blob.
	 * @return Colour.
	 */
	private byte calculateBlobColour(int i, int x) {
		boolean cellsUp = i >= w;
		boolean cellsDown = i < (s - w);
		
		byte c = BlobState.DEAD;
		colourCounts.reset();
		
		// Count all of the colours of neighbouring cells
		if(x > 0) {
			c = b[i-1]; // (x-1, y)
			if(c != BlobState.DEAD && c != BlobState.UNCOLOURED) colourCounts.count(c);
			
			if(cellsUp) {
				c = b[i-w-1]; // (x-1, y-1)
				if(c != BlobState.DEAD && c != BlobState.UNCOLOURED) colourCounts.count(c);
			}
			
			if(cellsDown) {
				c = b[i+w-1]; // (x-1, y+1)
				if(c != BlobState.DEAD && c != BlobState.UNCOLOURED) colourCounts.count(c);
			}
		}
		
		if(x < lastCol) {
			c = b[i+1]; // (x+1, y)
			if(c != BlobState.DEAD && c != BlobState.UNCOLOURED) colourCounts.count(c);
			
			if(cellsUp) {
				c = b[i-w+1]; // (x+1, y-1)
				if(c != BlobState.DEAD && c != BlobState.UNCOLOURED) colourCounts.count(c);
			}
			
			if(cellsDown) {
				c = b[i+w+1]; // (x+1, y+1)
				if(c != BlobState.DEAD && c != BlobState.UNCOLOURED) colourCounts.count(c);
			}
		}
		
		if(cellsUp) {
			c = b[i-w]; // (x, y-1)
			if(c != BlobState.DEAD && c != BlobState.UNCOLOURED) colourCounts.count(c);
		}
		
		if(cellsDown) {
			c = b[i+w]; // (x, y+1)
			if(c != BlobState.DEAD && c != BlobState.UNCOLOURED) colourCounts.count(c);
		}
		
		return colourCounts.getModalColour(BlobState.UNCOLOURED);
	}
}

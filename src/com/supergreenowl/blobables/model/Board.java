package com.supergreenowl.blobables.model;

/**
 * Represents the game board.
 * @author luke
 *
 */
public interface Board {

	/**
	 * Gets the number of columns on the board.
	 * @return Number of columns.
	 */
	public int getWidth();
	
	/**
	 * Gets the number of rows on the board.
	 * @return Number of rows.
	 */
	public int getHeight();
	
	/**
	 * Gets the size of the board.
	 * @return Board size.
	 */
	public int getSize();
	
	/**
	 * Places a blob on the board.
	 * @param x Column in which to put blob.
	 * @param y Row in which to put blob.
	 * @param colour Colour of blob to put.
	 */
	public void putBlob(int x, int y, byte colour);
	
	/**
	 * Places a blob on the board, if it is possible.
	 * @param x Column in which to put blob.
	 * @param y Row in which to put blob.
	 * @param colour Colour of blob to put.
	 * @return True if placing a blob at the specified location succeeded; false
	 * if the location already has a blob in it.
	 */
	public boolean tryPutBlob(int x, int y, byte colour);
	
	/**
	 * Gets the colour of the blob at the specified location.
	 * @param x Column of blob to look at.
	 * @param y Row of blob to look at.
	 * @return Colour/state value of blob at the specified location.
	 */
	public byte getColour(int x,  int y);
	
	/**
	 * Gets the number of transitions which the blob at the specified location can survive.
	 * @param x Column of blob to look at.
	 * @param y Row of blob to look at.
	 * @return Number of transitions blob can survive; 0 if it is already dead.
	 */
	public byte getTimeToLive(int x, int y);
	
	/**
	 * Transitions the board to the next time instant.
	 */
	public void transition();
	
	/**
	 * Gets the number of transitions that have occurred.
	 * @return
	 */
	public int getTime();
	
	/**
	 * Calculates the number of living blobs on the board. 
	 * @return Total number of living blobs.
	 */
	public int getBlobCount();
	
	/**
	 * Calculates the number of living blobs on the board of the specified colour.
	 * @param colour Colour of blobs to count.
	 * @return Number of blobs of that colour.
	 */
	public int getBlobCount(byte colour);
	
	/**
	 * Calculates the number of living blobs that surround the specified location.
	 * @param x Column.
	 * @param y Row.
	 * @return Number of living blobs that neighbour that location.
	 */
	public int neighbours(int x, int y);
	
	/**
	 * Calculates the number of living blobs of the specified colour that
	 * surround the specified location.
	 * @param x Column.
	 * @param y Row.
	 * @param colour Colour of blobs to count.
	 * @return Number of blobs of the specified colour.
	 */
	public int neighbours(int x, int y, byte colour);
	
	/**
	 * Calculates the number of neighbouring blobs that are hostile to the blob at the specified
	 * location.
	 * @param x Column.
	 * @param y Row.
	 * @return Number of hostile blobs around; 0 if the blob at the location specified is dead.
	 */
	public int hostileNeighbours(int x, int y);
	
	/**
	 * Calculates the number of cells neighbouring the specified location that contain blobs hostile to
	 * the specified colour.
	 * @param x
	 * @param y
	 * @param colour
	 * @return Number of hostile neighbouring blobs.
	 */
	public int hostileNeighbours(int x, int y, byte colour);
	
	/**
	 * Gets the number of neighbouring hostile blobs required to kill a blob.
	 * @return Number of blobs required to kill.
	 */
	public int getKillLevel();
	
	/**
	 * Gets the number of neighbouring blobs required to cause a blob to grow.
	 * @return Number of blobs required for a blob to grow.
	 */
	public int getGrowLevel();
	
	/**
	 * Resets the board to its initial configuration to allow play to begin again.
	 */
	public void reset();
}

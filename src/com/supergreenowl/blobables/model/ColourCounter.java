package com.supergreenowl.blobables.model;

/**
 * Counts the number of blobs of different colours on the board.
 * @author luke
 *
 */
public class ColourCounter {

	private static final int LENGTH = BlobState.NUM_COLOURS + 1;
	
	private int[] counts;
	
	/**
	 * Creates a new colour counter.
	 */
	public ColourCounter() {
		counts = new int[LENGTH];
	}
	
	/**
	 * Resets the counts for all colours.
	 */
	public void reset() {
		for(int i = 0; i < LENGTH; i++) {
			counts[i] = 0;
		}
	}
	
	/**
	 * Sets the count for the specified blob colour.
	 * @param colour
	 * @param count
	 */
	public void setCount(byte colour, int count) {
		counts[index(colour)] = count;
	}
	
	/**
	 * Increments the count for the specified blob colour.
	 * @param colour
	 */
	public void count(byte colour) {
		++counts[index(colour)];
	}
	
	/**
	 * Gets the count for the specified blob colour.
	 * @param colour
	 * @return
	 */
	public int getCount(byte colour) {
		return counts[index(colour)];
	}
	
	/**
	 * Gets the total count of blobs for all colours.
	 * @return
	 */
	public int getTotal() {
		int sum = 0;
		for(int i = 0; i < LENGTH; i++) {
			sum += counts[i];
		}
		return sum;
	}
	
	/**
	 * Gets the colour associated with the highest count.
	 * @param defaultColour Value to return when the highest count is a draw.
	 * @return Modal colour (or default).
	 */
	public byte getModalColour(byte defaultColour) {
		
		int mode = 0;
		byte modalColour = defaultColour;
		
		for(int i = 0; i < LENGTH; i++) {
			int count = counts[i];
			
			if(count == mode) {
				// there is a draw for mode so return default
				modalColour = defaultColour;
			}
			else if(count > mode) {
				// this colour is outright mode
				mode = count;
				modalColour = colour(i);
			}
		}
		
		return modalColour;
	}
	
	/**
	 * Converts a byte colour value into an array index. No error checking is performed.
	 * @param colour
	 * @return
	 */
	private int index(byte colour) {
		return colour - BlobState.UNCOLOURED;
	}
	
	/**
	 * Converts an array index into a byte colour.
	 * @param index
	 * @return
	 */
	private byte colour(int index) {
		return (byte)(index + BlobState.UNCOLOURED);
	}
}

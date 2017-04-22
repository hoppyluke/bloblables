package com.supergreenowl.blobables.model;

/**
 * Provides constants specifying the different states a cell can take.
 * @author luke
 *
 */
public final class BlobState {
	
	public static final byte DEAD = 0;
	public static final byte UNCOLOURED = 100;
	public static final byte C1 = 101;
	public static final byte C2 = 102;
	public static final byte C3 = 103;
	public static final byte C4 = 104;
	
	public static final int NUM_COLOURS = 4;
	
	// Cannot initialise class
	private BlobState() { }
}

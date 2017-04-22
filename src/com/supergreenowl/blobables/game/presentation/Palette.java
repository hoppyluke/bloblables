package com.supergreenowl.blobables.game.presentation;

import com.supergreenowl.blobables.model.BlobState;

public class Palette {

	public int background;
	public int dead;
	public int uncoloured;
	public int c1, c2, c3, c4;
	public int text;
	
	public Palette(int bg, int dead, int uncoloured, int c1, int c2, int c3, int c4, int text) {
		this.background = bg;
		this.dead = dead;
		this.c1 = c1;
		this.c2 = c2;
		this.c3 = c3;
		this.c4 = c4;
		this.uncoloured = uncoloured;
		this.text = text;
	}
	
	/**
	 * Gets the colour of the specified cell state.
	 * @param cellState
	 * @return
	 */
	public int colourOf(byte cellState) {
		switch(cellState) {
			case BlobState.DEAD: return dead;
			case BlobState.UNCOLOURED: return uncoloured;
			case BlobState.C1: return c1;
			case BlobState.C2: return c2;
			case BlobState.C3: return c3;
			case BlobState.C4: return c4;
			default: return background;
		}
	}
	
	public static final Palette DEFAULT = new Palette(0xff000000,
			0xff000000, 0xff666666,
			0xffff0000, 0xff0000ff, 0xff00ff00, 0xffffff00,
			0xffffffff);
}

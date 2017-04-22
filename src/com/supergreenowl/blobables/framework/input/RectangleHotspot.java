package com.supergreenowl.blobables.framework.input;

/**
 * Screen hotspot that is rectangular.
 * @author luke
 *
 */
public abstract class RectangleHotspot extends Hotspot {

	private int x, y;
	private int mx, my;
	
	public RectangleHotspot(int x, int y, int w, int h) {
		super();
		this.x = x;
		this.y = y;
		this.mx = x + w;
		this.my = y + h;
	}
	
	@Override
	protected boolean isInBounds(int x, int y) {
		// Check if the event falls between rectangle bounds
		return (x >= this.x && x <= this.mx && y >= this.y && y <= this.my);
	}

}

package com.supergreenowl.blobables.framework.input;

import com.supergreenowl.blobables.framework.TouchEvent;

/**
 * Hotspot on a screen that responds to touch events within
 * its own area.
 * @author luke
 *
 */
public abstract class Hotspot {

	private boolean consume;
	
	/**
	 * Creates a new Hotspot that will consume any events it handles.
	 */
	public Hotspot() {
		consume = true;
	}
	
	/**
	 * Creates a new Hotspot.
	 * @param consumeEvents If true, any handled events are consumed and not passed to other hotspots.
	 */
	public Hotspot(boolean consumeEvents) {
		consume = consumeEvents;
	}
	
	/**
	 * Handles a touch if it occurred within Hotspot area.
	 * @param event TouchEvent to handle
	 * @return True if the event was processed by this Hotspot; false if further processing is required
	 */
	public boolean handle(TouchEvent event) {
		if(isInBounds(event.x, event.y)) {
			onTouch(event);
			return consume;
		}
		else return false;
	}
	
	/**
	 * Determines if a touch at the specified co-ordinates is within the Hotspot.
	 * @param x
	 * @param y
	 * @return True if the location is within the hotspot.
	 */
	protected abstract boolean isInBounds(int x, int y);
	
	/**
	 * Handles a touch event that is within the Hotspot.
	 * @param event
	 */
	protected abstract void onTouch(TouchEvent event);
}

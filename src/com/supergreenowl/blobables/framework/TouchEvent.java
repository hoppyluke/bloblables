package com.supergreenowl.blobables.framework;

/**
 * User input event for a touch on the screen.
 * @author luke
 *
 */
public class TouchEvent {
	
	public static final byte TOUCH_DOWN = 0;
	public static final byte TOUCH_UP = 1;
	public static final byte TOUCH_DRAG = 2;
	
	public int x, y, pointer;
	public byte type;
	
	public TouchEvent() { }
	
	public TouchEvent(byte type, int x, int y) {
		this.type = type;
		this.x = x;
		this.y = y;
		this.pointer = 0;
	}
	
	public TouchEvent(byte type, int x, int y, int pointer) {
		this.type = type;
		this.x = x;
		this.y = y;
		this.pointer = pointer;
	}
}
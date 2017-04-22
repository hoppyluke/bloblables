package com.supergreenowl.blobables.framework;

/**
 * User input event for a key being pressed.
 * @author luke
 *
 */
public class KeyEvent {
	
	public static final byte KEY_UP = 0;
	public static final byte KEY_DOWN = 1;
	
	// Providing public members for performance over getter/setter methods
	public int keyCode;
	public char keyChar;
	public byte type;
	
	public KeyEvent() { }
	
	public KeyEvent(byte type, int keyCode, char keyChar) {
		this.type = type;
		this.keyCode = keyCode;
		this.keyChar = keyChar;
	}
}
package com.supergreenowl.blobables.framework.input;

import com.supergreenowl.blobables.framework.KeyEvent;
import com.supergreenowl.blobables.framework.input.EventPool.EventFactory;

import android.view.View;
import android.view.View.OnKeyListener;

public class KeyboardHandler extends EventHandler<KeyEvent> implements OnKeyListener {

	// maximum key code for which we track state
	private static final int MAX_CODE = 128;
	
	private boolean[] pressed = new boolean[MAX_CODE];
	
	private boolean swallowBack = false;
	
	/**
	 * Creates a new keyboard events handler for the specified view.
	 * @param v
	 */
	public KeyboardHandler(View v) {
		super(new EventFactory<KeyEvent>() {
			public KeyEvent create() {
				return new KeyEvent();
			}
		});
		
		v.setOnKeyListener(this);
		
		// Ensure view receives key events
		v.setFocusableInTouchMode(true);
		v.requestFocus();
	}

	/**
	 * Logs the fact that a key has been pressed/released.
	 * @param v View that captured key event
	 * @param keyCode Key that was pressed
	 * @param e Generated event for the key press
	 * @return True if the event was completely handled; false otherwise.
	 */
	@Override
	public boolean onKey(View v, int keyCode, android.view.KeyEvent e) {
		
		// Not handling multiple key presses
		if(e.getAction() == android.view.KeyEvent.ACTION_MULTIPLE)
			return false;
		
		boolean swallowKey = false;
		
		// synchronisation as UI thread will log events and game thread consume them
		synchronized (this) {
			KeyEvent key = pool.retrieve();
			key.keyCode = keyCode;
			key.keyChar = (char)e.getUnicodeChar();
			
			if(keyCode == android.view.KeyEvent.KEYCODE_BACK && swallowBack)
				swallowKey = true;
			
			if(e.getAction() == android.view.KeyEvent.ACTION_DOWN) {
				key.type = KeyEvent.KEY_DOWN;
				
				if(keyCode >= 0 && keyCode < MAX_CODE) // book has > 0 and < MAX -1... I disagree!
					pressed[keyCode] = true;
			}
			else if(e.getAction() == android.view.KeyEvent.ACTION_UP) {
				key.type = KeyEvent.KEY_UP;
				
				if(keyCode > 0 && keyCode < MAX_CODE - 1)
					pressed[keyCode] = false;
			}
			buffer.add(key);
		}
		
		return swallowKey;
	}
	
	/**
	 * Determines if the specified key is currently pressed.
	 * @param keyCode
	 * @return
	 */
	public boolean isKeyPressed(int keyCode) {
		if(keyCode < 0 || keyCode >= MAX_CODE) return false;
		
		return pressed[keyCode];
	}
	
	/**
	 * Sets a flag indicating if this keyboard handler should swallow the back key.
	 * @param swallow
	 */
	public void setSwallowsBack(boolean swallow) {
		// synchronised as game thread will set this value but
		// UI thread will read it when key is logged
		synchronized (this) {
			swallowBack = swallow;
		}
	}
	
}

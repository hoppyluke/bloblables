package com.supergreenowl.blobables.framework;

import java.util.List;

import com.supergreenowl.blobables.framework.input.KeyboardHandler;
import com.supergreenowl.blobables.framework.input.MultiTouchHandler;
import com.supergreenowl.blobables.framework.input.SingleTouchHandler;
import com.supergreenowl.blobables.framework.input.TouchHandler;

import android.os.Build.VERSION;
import android.view.View;

/**
 * Provides user input management for the game.
 * @author luke
 *
 */
public class UserInputManager implements UserInput {

	private KeyboardHandler keyboard;
	private TouchHandler touch;
	
	/**
	 * Creates a new input manager for the specified view.
	 * @param v View to process input from.
	 * @param scaleX x-axis scale factor for touch events.
	 * @param scaleY y-axis scale factor for touch events.
	 */
	public UserInputManager(View v, float scaleX, float scaleY) {
		this(v, scaleX, scaleY, true, true);
	}
	
	/**
	 * Creates a new input manager for the specified view.
	 * @param v View to process input from.
	 * @param scaleX x-axis scale factor for touch events.
	 * @param scaleY y-axis scale factor for touch events.
	 * @param handleKeys Handles key events only if true.
	 * @param handleTouch Handles touch events only if true.
	 */
	public UserInputManager(View v, float scaleX, float scaleY, boolean handleKeys, boolean handleTouch) {
		
		keyboard = handleKeys ? new KeyboardHandler(v) : null;
		touch = handleTouch ?
				(Integer.parseInt(VERSION.SDK) < 5 ? new SingleTouchHandler(v, scaleX, scaleY) : new MultiTouchHandler(v, scaleX, scaleY))
				: null;
		
	}
	
	@Override
	public boolean isKeyPressed(int keyCode) {
		return keyboard == null ? false : keyboard.isKeyPressed(keyCode);
	}

	@Override
	public boolean isTouchDown(int pointer) {
		return touch == null ? false : touch.isTouchDown(pointer);
	}

	@Override
	public int getTouchX(int pointer) {
		return touch == null ? 0 : touch.getTouchX(pointer);
	}

	@Override
	public int getTouchY(int pointer) {
		return touch == null ? 0 : touch.getTouchY(pointer);
	}

	@Override
	public List<KeyEvent> getKeyEvents() {
		return keyboard == null ? null : keyboard.getEvents();
	}

	@Override
	public List<TouchEvent> getTouchEvents() {
		return touch == null ? null : touch.getEvents();
	}

	@Override
	public void setSwallowsBackKey(boolean swallow) {
		keyboard.setSwallowsBack(swallow);
	}
}
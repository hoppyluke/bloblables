package com.supergreenowl.blobables.framework.input;

import com.supergreenowl.blobables.framework.TouchEvent;

import android.view.MotionEvent;
import android.view.View;

public class MultiTouchHandler extends TouchHandler {

	/**
	 * Max number of simultaneous touches we can handle.
	 */
	private static final byte POINTERS = 20;
	
	private boolean[] isTouched;
	private int[] x, y;
	
	public MultiTouchHandler(View v, float scaleX, float scaleY) {
		super(v, scaleX, scaleY);
		
		isTouched = new boolean[POINTERS];
		x = new int[POINTERS];
		y = new int[POINTERS];
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		
		synchronized (this) {
			int action = event.getAction() & MotionEvent.ACTION_MASK;
			int pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_ID_MASK) >> MotionEvent.ACTION_POINTER_ID_SHIFT;
			int pointer = event.getPointerId(pointerIndex);
			
			TouchEvent touch;
			
			switch(action) {
			case MotionEvent.ACTION_POINTER_DOWN:
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_POINTER_UP:
				touch = pool.retrieve();
				touch.type =  action == MotionEvent.ACTION_POINTER_DOWN ? TouchEvent.TOUCH_DOWN : TouchEvent.TOUCH_UP;
				touch.pointer = pointer;
				touch.x = x[pointer] = (int)(event.getX(pointerIndex) * scaleX);
				touch.y = y[pointer] = (int)(event.getY(pointerIndex) * scaleY);
				isTouched[pointer] = action == MotionEvent.ACTION_POINTER_DOWN;
				buffer.add(touch);
				break;
			case MotionEvent.ACTION_MOVE:
				int pointerCount = event.getPointerCount();
				for(int i = 0; i < pointerCount; i++) {
					pointerIndex = i;
					pointer = event.getPointerId(pointerIndex);
					touch = pool.retrieve();
					touch.type = TouchEvent.TOUCH_DRAG;
					touch.pointer = pointer;
					touch.x = x[pointer] = (int)(event.getX(pointerIndex) * scaleX);
					touch.y = y[pointer] = (int)(event.getY(pointerIndex) * scaleY);
					isTouched[pointer] = true;
					buffer.add(touch);
				}
				break;
			}
			
			return true;
		}
	}

	@Override
	public boolean isTouchDown(int pointer) {
		synchronized (this) {
			return pointer < 0 || pointer >= POINTERS ? false : isTouched[pointer];
		}
	}

	@Override
	public int getTouchX(int pointer) {
		synchronized (this) {
			return pointer < 0 || pointer >= POINTERS ? 0 : x[pointer];
		}
	}

	@Override
	public int getTouchY(int pointer) {
		synchronized (this) {
			return pointer < 0 || pointer >= POINTERS ? 0 : y[pointer];
		}
	}
}

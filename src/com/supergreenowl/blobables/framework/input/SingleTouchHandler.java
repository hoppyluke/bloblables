package com.supergreenowl.blobables.framework.input;

import com.supergreenowl.blobables.framework.TouchEvent;

import android.view.MotionEvent;
import android.view.View;

/**
 * TouchHandler for devices that only have single touch screens.
 * @author luke
 *
 */
public class SingleTouchHandler extends TouchHandler {

	private boolean isTouched;
	private int x, y;
	
	public SingleTouchHandler(View v, float scaleX, float scaleY) {
		super(v, scaleX, scaleY);
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		synchronized (this) {
			TouchEvent touch = pool.retrieve();
			
			switch(event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				touch.type = TouchEvent.TOUCH_DOWN;
				isTouched = true;
				break;
			case MotionEvent.ACTION_MOVE:
				touch.type = TouchEvent.TOUCH_DRAG;
				isTouched = true;
				break;
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
				touch.type = TouchEvent.TOUCH_UP;
				isTouched = false;
			}
			
			touch.x = this.x = (int)(event.getX() * scaleX);
			touch.y = this.y = (int)(event.getY() * scaleY);
			buffer.add(touch);
			
			return true;
		}
	}

	@Override
	public boolean isTouchDown(int pointer) {
		synchronized(this) {
			return pointer == 0 ? isTouched: false;
		}
	}

	@Override
	public int getTouchX(int pointer) {
		synchronized(this) {
			return pointer == 0 ? x : 0;
		}
	}

	@Override
	public int getTouchY(int pointer) {
		synchronized(this) {
			return pointer == 0 ? y : 0;
		}
	}

}
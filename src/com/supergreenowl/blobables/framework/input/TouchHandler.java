package com.supergreenowl.blobables.framework.input;

import com.supergreenowl.blobables.framework.TouchEvent;
import com.supergreenowl.blobables.framework.input.EventPool.EventFactory;

import android.view.View;
import android.view.View.OnTouchListener;

public abstract class TouchHandler extends EventHandler<TouchEvent> implements OnTouchListener {

	protected float scaleX, scaleY;
	
	public TouchHandler(View v, float scaleX, float scaleY) {
		super(new EventFactory<TouchEvent>() {
			@Override
			public TouchEvent create() {
				return new TouchEvent();
			}
		});
		
		this.scaleX = scaleX;
		this.scaleY = scaleY;
		
		v.setOnTouchListener(this);
	}
	
	public abstract boolean isTouchDown(int pointer);
	
	public abstract int getTouchX(int pointer);
	
	public abstract int getTouchY(int pointer);
}

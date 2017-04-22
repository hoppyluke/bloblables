package com.supergreenowl.blobables.framework;

import java.util.List;

public interface UserInput {
	
	public boolean isKeyPressed(int keyCode);
	
	public boolean isTouchDown(int pointer);
	
	public int getTouchX(int pointer);
	
	public int getTouchY(int pointer);
	
	public List<KeyEvent> getKeyEvents();
	
	public List<TouchEvent> getTouchEvents();
	
	public void setSwallowsBackKey(boolean swallow);
}

package com.supergreenowl.blobables.framework;

import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

public interface TextDrawer {

	public void setTextAlign(Paint.Align align);
	
	public void setTextSize(int size);
	
	public void setTextColour(int colour);
	
	public void setFont(Typeface font);
	
	public void drawText(String text, int x, int y);
	
	public void getBounds(String text, int start, int end, Rect bounds);
	
	public void getBounds(String text, Rect bounds);
}

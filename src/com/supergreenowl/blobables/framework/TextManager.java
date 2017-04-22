package com.supergreenowl.blobables.framework;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

public class TextManager implements TextDrawer {

	private Paint p;
	private Canvas c;
	
	public TextManager(Canvas c, Paint p) {
		this.c = c;
		this.p = p;
	}
	
	/**
	 * Sets the text alignment.
	 */
	@Override
	public void setTextAlign(Paint.Align align) {
		p.setTextAlign(align);
	}

	@Override
	public void setTextSize(int size) {
		p.setTextSize(size);
	}

	@Override
	public void setTextColour(int colour) {
		p.setColor(colour);
	}

	@Override
	public void setFont(Typeface font) {
		p.setTypeface(font);
	}

	@Override
	public void drawText(String text, int x, int y) {
		c.drawText(text, x, y, p);
	}

	@Override
	public void getBounds(String text, int start, int end, Rect bounds) {
		p.getTextBounds(text, start, end, bounds);
	}

	@Override
	public void getBounds(String text, Rect bounds) {
		p.getTextBounds(text, 0, text.length(), bounds);
	}
}

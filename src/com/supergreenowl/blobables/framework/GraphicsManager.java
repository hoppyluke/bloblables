package com.supergreenowl.blobables.framework;

import java.io.InputStream;
import java.io.IOException;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class GraphicsManager implements Graphics {

	private AssetManager assets;
	private Bitmap framebuffer;
	private Canvas canvas;
	private Paint paint;
	private Rect src, dst;
	private TextDrawer text;
	
	public GraphicsManager(AssetManager assets, Bitmap framebuffer) {
		this.assets = assets;
		this.framebuffer = framebuffer;
		canvas = new Canvas(framebuffer);
		paint = new Paint();
		src = new Rect();
		dst = new Rect();
		text = new TextManager(canvas, paint);
	}
	
	@Override
	public void drawPixel(int x, int y, int colour) {
		paint.setColor(colour);
		canvas.drawPoint(x, y, paint);
	}

	@Override
	public void drawLine(int beginX, int beginY, int endX, int endY, int colour) {
		paint.setColor(colour);
		canvas.drawLine(beginX, beginY, endX, endY, paint);
	}

	@Override
	public void drawRectangle(int x, int y, int width, int height, int colour) {
		paint.setColor(colour);
		paint.setStyle(Paint.Style.FILL);
		canvas.drawRect(x, y, x + width - 1, y + height - 1, paint);
	}

	@Override
	public void drawRectangle(Rect rectangle, int colour) {
		paint.setColor(colour);
		paint.setStyle(Paint.Style.FILL);
		canvas.drawRect(rectangle, paint);
	}

	@Override
	public void drawBitmap(Bitmap b, int x, int y) {
		canvas.drawBitmap(b, x, y, null);
	}

	@Override
	public void drawBitmap(Bitmap b, int x, int y, int w, int h) {
		
		// draw entire source bitmap
		src.left = 0;
		src.top = 0;
		src.right = w - 1;
		src.bottom = h - 1;
		
		dst.left = x;
		dst.top = y;
		dst.right = x + w - 1;
		dst.bottom = y + h - 1;
		
		canvas.drawBitmap(b, src, dst, null);
		
	}
	
	@Override
	public void drawBitmap(Bitmap b, int x, int y, int srcX, int srcY, int w,
			int h) {
		
		src.left = srcX;
		src.top = srcY;
		src.right = srcX + w - 1;
		src.bottom = srcY + h - 1;
		
		dst.left = x;
		dst.top = y;
		dst.right = x + w - 1;
		dst.bottom = y + h - 1;
		
		canvas.drawBitmap(b, src, dst, null);
	}

	@Override
	public void drawBitmap(Bitmap b, int x, int y, int w, int h, int alpha) {
		paint.setAlpha(alpha);
		
		// draw entire source bitmap
		src.left = 0;
		src.top = 0;
		src.right = w - 1;
		src.bottom = h - 1;
		
		dst.left = x;
		dst.top = y;
		dst.right = x + w - 1;
		dst.bottom = y + h - 1;
		
		canvas.drawBitmap(b, src, dst, paint);
		
		paint.setAlpha(255);
	}

	@Override
	public void drawScaledBitmap(Bitmap b, int x, int y, int srcW, int srcH,
			int w, int h) {
		
		
		// draw entire source bitmap
		src.left = 0;
		src.top = 0;
		src.right = srcW - 1;
		src.bottom = srcH - 1;
		
		dst.left = x;
		dst.top = y;
		dst.right = x + w - 1;
		dst.bottom = y + h - 1;
		
		canvas.drawBitmap(b, src, dst, null);
		
	}

	@Override
	public void fill(int colour) {
		canvas.drawRGB((colour & 0xff0000) >> 16, // extract R
				(colour & 0xff00) >> 8, // extract G
				(colour & 0xff)); // extract B
	}

	@Override
	public int getWidth() {
		return framebuffer.getWidth();
	}

	@Override
	public int getHeight() {
		return framebuffer.getHeight();
	}

	@Override
	public Bitmap loadBitmap(String filename, Bitmap.Config formatHint) {
		
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = formatHint;
		
		InputStream in = null;
		Bitmap b = null;
		
		try {
			in = assets.open(filename);
			b = BitmapFactory.decodeStream(in, null, options);
			if(b == null) throw throwHelper(filename);
		}
		catch(IOException e) {
			throw throwHelper(filename);
		}
		finally {
			if(in != null) {
				try { in.close(); }
				catch(IOException e) { }
			}
		}
		
		return b;
	}
	
	/**
	 * Builds a runtime exception to throw when loading a bitmap fails.
	 * @param filename
	 * @return
	 */
	private RuntimeException throwHelper(String filename) {
		return new RuntimeException("Failed to load bitmap at '" + filename + "'");
	}

	@Override
	public TextDrawer getTextDrawer() {
		return text;
	}

}

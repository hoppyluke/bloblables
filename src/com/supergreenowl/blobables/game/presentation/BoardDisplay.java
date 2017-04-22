package com.supergreenowl.blobables.game.presentation;

import java.util.Arrays;

import android.graphics.Bitmap;

import com.supergreenowl.blobables.framework.Graphics;
import com.supergreenowl.blobables.framework.GraphicsManager;
import com.supergreenowl.blobables.model.BlobState;

public class BoardDisplay extends InfiniteAnimation {

	private static final short ALPHA_MIN = 0;
	private static final short ALPHA_MAX = 255;
	
	private static final int BLOB_WIDTH = 48;
	private static final int BLOB_HEIGHT = 48;
	
	private int w;
	private int size;
	private byte[] overlay;
	private byte[] underlay;
	private short[] alpha;
	
	private boolean drawnOnce;

	private int alphaIncrement;
	
	private Bitmap bg;
	private int bgW, bgH;
	
	public BoardDisplay(int fps, int fadeDuration, int w, int h) {
		super(fps);
		
		this.w = w;;
		this.size = w * h;
		
		drawnOnce = false;
		
		overlay = new byte[size];
		underlay = new byte[size];
		alpha = new short[size];
		
		int totalFrames = (int)((fadeDuration / 1000f) * fps);
		
		alphaIncrement = (int)Math.ceil(ALPHA_MAX / (float)totalFrames);
		
		// Setup BG dimensions
		bgW = w * BLOB_WIDTH;
		bgH = h * BLOB_HEIGHT;
		
	}
	
	@Override
	protected void drawFrame(Graphics g) {
		int len = size;

		// If no change since last draw, just do nothing
		boolean hasWorkToDraw = !drawnOnce;
		if(!hasWorkToDraw) {
			for(int i = 0; i < len; i++) {
				boolean hasBlob = overlay[i] != BlobState.DEAD || underlay[i] != BlobState.DEAD;
				boolean isFadeOver = alpha[i] >= ALPHA_MAX;
				
				if(hasBlob && !isFadeOver) {
					hasWorkToDraw = true;
					break;
				}
			}
		}
		
		if(!hasWorkToDraw) return;
		
		GameImageManager images = ImageProvider.getGameImages(g);
		
		// Draw BG texture
		if(fillRect != null) {
			if(!drawnOnce) g.drawRectangle(fillRect, fillColour);
			
			if(bg == null) {
				bg = Bitmap.createBitmap(bgW, bgH, Bitmap.Config.RGB_565);
				int x, y;
				x = y = 0;
				while(y < bgH) {
					while(x < bgW) {
						Graphics bgGraphics = new GraphicsManager(null, bg);
						bgGraphics.drawBitmap(images.texture, x, y, GameImageManager.TEXTURE_WIDTH, GameImageManager.TEXTURE_HEIGHT);
						x += GameImageManager.TEXTURE_WIDTH - 1;
					}
					x = 0;
					y += GameImageManager.TEXTURE_HEIGHT - 1;
				}
			}
			g.drawBitmap(bg, 0, 0, bgW, bgH);
		}
		
		for(int i = 0; i < len; i++) {
			
			byte overColour = overlay[i];
			byte underColour = underlay[i];
			
			// Nothing to draw for this blob
			if(overColour == BlobState.DEAD && underColour == BlobState.DEAD) continue;
			
			short a = alpha[i];
			
			if(a >= ALPHA_MAX) {
				// Fade is already over - just draw overlay image
				if(overColour != BlobState.DEAD) {
					Bitmap img = images.getBitmap(overColour);
					g.drawBitmap(img, (i % w) * BLOB_WIDTH, (i / w) *  BLOB_HEIGHT, BLOB_WIDTH, BLOB_HEIGHT);
				}
				
			}
			else {
				// continue fade
				a = alpha[i] += alphaIncrement;
				int overlayAlpha = a > ALPHA_MAX ? ALPHA_MAX : a;
				int underlayAlpha = ALPHA_MAX - a;
				if(underlayAlpha < ALPHA_MIN) {
					underlayAlpha = ALPHA_MIN;
					underColour = underlay[i] = BlobState.DEAD;
				}
				
				// Calculate location to draw images at
				int x = (i % w) * BLOB_WIDTH;
				int y = (i / w) *  BLOB_HEIGHT;
				
				// Draw fading out underlay
				if(underColour != BlobState.DEAD) {
					Bitmap under = images.getBitmap(underColour);
					g.drawBitmap(under, x, y, BLOB_WIDTH, BLOB_HEIGHT, underlayAlpha);
				}
				
				// Draw fading in overlay
				if(overColour != BlobState.DEAD) {
					Bitmap over = images.getBitmap(overColour);
					g.drawBitmap(over, x, y, BLOB_WIDTH, BLOB_HEIGHT, overlayAlpha);
				}
			}
		}
		drawnOnce = true;
	}

	@Override
	protected void drawComplete(Graphics g) {
		GameImageManager images = ImageProvider.getGameImages(g);
		
		// Draw background texture
		if(fillRect != null) {
			g.drawBitmap(bg, 0, 0, bgW, bgH);
		}
		
		int len = size;
		for(int i = 0; i < len; i++) {
			byte overColour = overlay[i];
			
			// No fading, just draw the images for any blobs that are not dead
			if(overColour != BlobState.DEAD) {
				Bitmap img = images.getBitmap(overColour);
				g.drawBitmap(img, (i % w) * BLOB_WIDTH, (i / w) *  BLOB_HEIGHT, BLOB_WIDTH, BLOB_HEIGHT);
			}	
		}
	}

	/**
	 * Sets the colour of the byte to display at the specified location.
	 * @param x
	 * @param y
	 * @param colour
	 */
	public void setColour(int x, int y, byte colour) {
		int idx = (y * w) + x;
		underlay[idx] = overlay[idx];
		overlay[idx] = colour;
		alpha[idx] = ALPHA_MIN;
	}

	@Override
	public void reset() {
		super.reset();
		Arrays.fill(overlay, BlobState.DEAD);
		Arrays.fill(underlay, BlobState.DEAD);
		Arrays.fill(alpha, ALPHA_MIN);
		drawnOnce = false;
	}
	
	/**
	 * Tells the animation that it needs to completely re-render itself next frame.
	 */
	public void invalidateDisplay() {
		drawnOnce = false;
	}
}

package com.supergreenowl.blobables.framework;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class RenderView extends SurfaceView implements Runnable {
	
	private GameActivity game;
	private Bitmap framebuffer;
	private Thread gameThread;
	private SurfaceHolder holder;
	private volatile boolean running = false;

	public RenderView(GameActivity game, Bitmap framebuffer) {
		super(game);
		this.game = game;
		this.framebuffer = framebuffer;
		this.holder = getHolder();
	}
	
	public void resume() {
		running = true;
		gameThread = new Thread(this);
		gameThread.start();
	}
	
	public void pause() {
		running = false;
		
		while(true) {
			try {
				gameThread.join();
				break;
			}
			catch(InterruptedException e)
			{
				// Retry joining thread
			}
		}
	}
	
	@Override
	public void run() {
		
		Rect dst = new Rect();
		long start = System.currentTimeMillis();
		
		while(running) {
			if(!holder.getSurface().isValid()) continue;
			
			long delta = System.currentTimeMillis() - start;
			start = System.currentTimeMillis();
			
			game.getScreen().update(delta);
			game.getScreen().present(delta);
			
			Canvas canvas = holder.lockCanvas();
			canvas.getClipBounds(dst);
			canvas.drawBitmap(framebuffer, null, dst, null);
			holder.unlockCanvasAndPost(canvas);
		}
		

	}

}
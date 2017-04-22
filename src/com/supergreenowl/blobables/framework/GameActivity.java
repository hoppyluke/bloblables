package com.supergreenowl.blobables.framework;

import android.app.Activity;
import android.content.Context;
//import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.Window;
import android.view.WindowManager;

public abstract class GameActivity extends Activity implements Game {

	private Graphics graphics;
	private UserInput input;
	private FileIO io;
	private Screen screen;
	private RenderView renderView;
	private WakeLock wakeLock;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Want a full-screen window with no title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		/*boolean isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;*/
		
		int fbWidth = getWindowManager().getDefaultDisplay().getWidth();
		int fbHeight = getWindowManager().getDefaultDisplay().getHeight();
		
		Bitmap fb = Bitmap.createBitmap(fbWidth, fbHeight, Bitmap.Config.RGB_565);
		
		float scaleX = (float)fbWidth / getWindowManager().getDefaultDisplay().getWidth();
		float scaleY = (float)fbHeight / getWindowManager().getDefaultDisplay().getHeight();
		
		renderView = new RenderView(this, fb);
		graphics = new GraphicsManager(getAssets(), fb);
		io = new FileManager(this, getAssets());
		input = new UserInputManager(renderView, scaleX, scaleY, true, true);
		
		screen = getStartupScreen();
		setContentView(renderView);
		
		// Acquire a wake lock while the game is running
		PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
		wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, this.getLocalClassName());
	}

	@Override
	protected void onPause() {
		super.onPause();
		
		wakeLock.release();
		renderView.pause();
		screen.pause();
		
		if(isFinishing()) screen.dispose();
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		wakeLock.acquire();
		screen.resume();
		renderView.resume();
	}
	
	@Override
	public UserInput getInput() {
		return input;
	}

	@Override
	public FileIO getIO() {
		return io;
	}

	@Override
	public Graphics getGraphics() {
		return graphics;
	}

	@Override
	public Screen getScreen() {
		return screen;
	}

	@Override
	public void setScreen(Screen s) {
		if(s == null) throw new IllegalArgumentException();
		
		screen.pause();
		screen.dispose();
		s.resume();
		s.update(0);
		screen = s;
	}

}

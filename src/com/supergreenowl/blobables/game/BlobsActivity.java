package com.supergreenowl.blobables.game;

import com.supergreenowl.blobables.framework.GameActivity;
import com.supergreenowl.blobables.framework.Screen;

public class BlobsActivity extends GameActivity {
	
	@Override
	public Screen getStartupScreen() {
		return new SplashScreen(this);
	}
}

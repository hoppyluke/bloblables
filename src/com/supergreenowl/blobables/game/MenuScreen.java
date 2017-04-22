package com.supergreenowl.blobables.game;

import com.supergreenowl.blobables.framework.Game;
import com.supergreenowl.blobables.framework.StateScreen;
import com.supergreenowl.blobables.game.presentation.ImageProvider;

public class MenuScreen extends StateScreen implements MenuEventsListener {

	private MenuHomeState homeState;
	private MenuHelpState helpState;
	private MenuQuickPlayState quickPlayState;
	
	public MenuScreen(Game game) {
		super(game);
		homeState = new MenuHomeState(this, this);
		helpState = new MenuHelpState(this, this);
		quickPlayState = new MenuQuickPlayState(this, this);
		setState(homeState);
	}
	
	@Override
	public void dispose() {
		homeState.dispose();
		helpState.dispose();
		quickPlayState.dispose();
		ImageProvider.disposeAll();
	}

	@Override
	public void onHelp() {
		game.getInput().setSwallowsBackKey(true);
		setState(helpState);
	}

	@Override
	public void onHome() {
		game.getInput().setSwallowsBackKey(false);
		setState(homeState);
	}

	@Override
	public void onQuickPlay() {
		game.getInput().setSwallowsBackKey(true);
		setState(quickPlayState);
	}
}

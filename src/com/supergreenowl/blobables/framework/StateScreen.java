package com.supergreenowl.blobables.framework;

/**
 * Screen that can have different states and delegates
 * most processing to its current state.
 * @author luke
 *
 */
public abstract class StateScreen extends Screen {

	/**
	 * Current state of the screen.
	 */
	protected ScreenState state;
	
	public StateScreen(Game g) {
		super(g);
	}
	
	/**
	 * Sets the current state of the screen.
	 * @param s
	 */
	public void setState(ScreenState s) {
		this.state = s;
	}
	
	/**
	 * Delegates updating to the current state.
	 */
	@Override
	public void update(long deltaTime) {
		state.update(deltaTime);
		
	}

	/**
	 * Delegates presenting to the current state.
	 */
	@Override
	public void present(long deltaTime) {
		state.present(deltaTime);
		
	}

	/**
	 * Delegates pausing to the current state.
	 */
	@Override
	public void pause() {
		state.pause();
	}

	/**
	 * Delegates resuming to the current state.
	 */
	@Override
	public void resume() {
		state.resume();
	}

}
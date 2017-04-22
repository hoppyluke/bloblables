package com.supergreenowl.blobables.game.presentation;

import com.supergreenowl.blobables.framework.Graphics;

/**
 * Provides centralised access to bitmap assets.
 * Uses the singleton pattern to ensure all objects share assets
 * through a single provider instance.
 * @author luke
 */
public class ImageProvider {

	private static ImageProvider instance;

	private GameImageManager gameImages;
	private ButtonImageManager buttonImages;
	private MenuImageManager menuImages;
	
	/**
	 * Private default constructor for singleton pattern.
	 */
	private ImageProvider() { }
	
	/**
	 * Gets a manager for in-game images.
	 * @param g Graphics module used for loading image assets.
	 * @return Image manager for in-game images.
	 */
	public static GameImageManager getGameImages(Graphics g) {
		if(instance == null) {
			instance = new ImageProvider();
		}
		
		if(instance.gameImages == null) {
			instance.gameImages = new GameImageManager(g);
		}
		
		return instance.gameImages;
	}
	
	/**
	 * Gets a manager for button images.
	 * @param g Graphics module to load image assets.
	 * @return Image manager for button images.
	 */
	public static ButtonImageManager getButtons(Graphics g) {
		if(instance == null) {
			instance = new ImageProvider();
		}
		
		if(instance.buttonImages == null) {
			instance.buttonImages = new ButtonImageManager(g);
		}
		
		return instance.buttonImages;
	}
	
	/**
	 * Gets a manager for main menu images.
	 * @param g
	 * @return
	 */
	public static MenuImageManager getMenuImages(Graphics g) {
		if(instance == null) {
			instance = new ImageProvider();
		}
		
		if(instance.menuImages == null) {
			instance.menuImages = new MenuImageManager(g);
		}
		
		return instance.menuImages;
	}
	
	/**
	 * Frees up all resources (if any) held by the provider.
	 */
	public static void disposeAll() {
		if(instance != null) {
			if(instance.gameImages != null) {
				instance.gameImages.dispose();
			}
			
			if(instance.buttonImages != null) {
				instance.buttonImages.dispose();
			}
			
			if(instance.menuImages != null) {
				instance.menuImages.dispose();
			}
			
			instance = null;
		}
	}
}

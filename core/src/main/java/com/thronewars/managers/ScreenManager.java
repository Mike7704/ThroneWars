package com.thronewars.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.thronewars.GdxGame;

/**
 * Manages the switching of screens in the game.
 */
public class ScreenManager {
	
	public static final float GAME_WIDTH = 1920f;
	public static final float GAME_HEIGHT = 1080f;
	
	private final GdxGame game;
	private Screen currentScreen;

	public ScreenManager(GdxGame game) {
		this.game = game;
	}

	/**
	 * Shows the specified screen.
	 * @param nextScreen The screen to show.
	 */
	public void showScreen(Screen nextScreen) {
		if (nextScreen == null) {
			throw new IllegalArgumentException("The next screen cannot be null.");
		}
		// Switch screens on the main rendering thread
		Gdx.app.postRunnable(() -> switchScreen(nextScreen));
	}

	/**
	 * Switches the current screen to the next screen and disposes of the previous screen.
	 * @param nextScreen The screen to switch to.
	 */
	private void switchScreen(Screen nextScreen) {
		if (nextScreen == currentScreen) {
			return;
		}

		Screen previousScreen = currentScreen;
		
		game.setScreen(nextScreen);
		currentScreen = nextScreen;

		if (previousScreen != null) {
			previousScreen.dispose();
		}
	}

	public Screen getCurrentScreen() {
		return currentScreen;
	}
}

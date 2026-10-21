package com.thronewars.managers;

import java.util.EnumMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.thronewars.GdxGame;
import com.thronewars.screens.GameScreen;
import com.thronewars.screens.MainMenuScreen;
import com.thronewars.screens.SettingsMenuScreen;

/**
 * Manages the switching of screens in the game.
 */
public class ScreenManager {
	
	public static final float VIEW_WIDTH = 1920f;
	public static final float VIEW_HEIGHT = 1080f;
	
	private final GdxGame game;
	private final Map<ScreenType, Screen> screens = new EnumMap<>(ScreenType.class);
	private ScreenType currentScreenType;

	public ScreenManager(GdxGame game) {
		this.game = game;
		
		// Initialise screens
		screens.put(ScreenType.MAIN_MENU, new MainMenuScreen(game));
		screens.put(ScreenType.SETTINGS_MENU, new SettingsMenuScreen(game));
		screens.put(ScreenType.GAME, new GameScreen(game));
	}

	/**
	 * Shows the specified screen.
	 * 
	 * @param type The type of screen to show.
	 */
	public void showScreen(ScreenType type) {
		if (type == null) {
			throw new IllegalArgumentException("The screen type cannot be null.");
		}

		Gdx.app.postRunnable(() -> {
			if (type == currentScreenType) {
				return;
			}

			Screen nextScreen = screens.get(type);

			if (nextScreen == null) {
				throw new IllegalStateException("No screen registered for type: " + type);
			}

		    game.setScreen(nextScreen);
			currentScreenType = type;
		});
	}
	
	/**
	 * Returns the current screen being displayed.
	 * 
	 * @return The current screen.
	 */
	public Screen getCurrentScreen() {
		if (currentScreenType == null) {
			throw new IllegalStateException("Current screen type is not set.");
		}
		
		return screens.get(currentScreenType);
	}
	
	/**
	 * Disposes all cached screens when the manager is shut down.
	 */
	public void dispose() {
		for (Screen screen : screens.values()) {
			screen.dispose();
		}

		screens.clear();
		currentScreenType = null;
	}
}

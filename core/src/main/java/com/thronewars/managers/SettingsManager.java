package com.thronewars.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * Stores the game's user-configurable settings.
 */
public class SettingsManager {

	private static final String PREFERENCES_NAME = "throne-wars-settings";

	private static final String FULLSCREEN_KEY = "fullscreenEnabled";
	private static final String ENTITY_SHADOWS_KEY = "entityShadowsEnabled";

	private final Preferences preferences;
	
	private boolean fullscreenEnabled = false;
	private boolean entityShadowsEnabled = true;

	public SettingsManager() {
		preferences = Gdx.app.getPreferences(PREFERENCES_NAME);
		
		// Load saved settings, using defaults for settings not yet saved.
		fullscreenEnabled = preferences.getBoolean(FULLSCREEN_KEY, false);
		entityShadowsEnabled = preferences.getBoolean(ENTITY_SHADOWS_KEY, true);
		
		if (isFullscreenEnabled()) {
		    Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
		}
	}
	
	public boolean isFullscreenEnabled() {
		return fullscreenEnabled;
	}

	public void setFullscreenEnabled(boolean enabled) {
		fullscreenEnabled = enabled;
		preferences.putBoolean(FULLSCREEN_KEY, enabled);
		preferences.flush();
	}
	
	public boolean areEntityShadowsEnabled() {
		return entityShadowsEnabled;
	}

	public void setEntityShadowsEnabled(boolean enabled) {
		entityShadowsEnabled = enabled;
		preferences.putBoolean(ENTITY_SHADOWS_KEY, enabled);
		preferences.flush();
	}
}

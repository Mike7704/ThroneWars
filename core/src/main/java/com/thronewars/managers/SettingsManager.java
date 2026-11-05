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
	private static final String MUSIC_VOLUME_KEY = "musicVolume";
	private static final String SOUND_EFFECTS_VOLUME_KEY = "soundEffectsVolume";

	private final Preferences preferences;
	
	private boolean fullscreenEnabled = false;
	private boolean entityShadowsEnabled = true;
	private float musicVolume = 0.33f;
	private float soundEffectsVolume = 0.5f;

	public SettingsManager() {
		preferences = Gdx.app.getPreferences(PREFERENCES_NAME);
		
		// Load saved settings, using defaults for settings not yet saved.
		fullscreenEnabled = preferences.getBoolean(FULLSCREEN_KEY, false);
		entityShadowsEnabled = preferences.getBoolean(ENTITY_SHADOWS_KEY, true);
		musicVolume = preferences.getFloat(MUSIC_VOLUME_KEY, 0.33f);
		soundEffectsVolume = preferences.getFloat(SOUND_EFFECTS_VOLUME_KEY, 0.5f);
		
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
	
	public float getMusicVolume() {
		return musicVolume;
	}
	
	public void setMusicVolume(float volume) {
	    musicVolume = Math.max(0f, Math.min(volume, 1f));
	}

	public void saveMusicVolume() {
	    preferences.putFloat(MUSIC_VOLUME_KEY, musicVolume);
	    preferences.flush();
	}
	
	public float getSoundEffectsVolume() {
		return soundEffectsVolume;
	}
	
	public void setSoundEffectsVolume(float volume) {
	    soundEffectsVolume = Math.max(0f, Math.min(volume, 1f));
	}

	public void saveSoundEffectsVolume() {
	    preferences.putFloat(SOUND_EFFECTS_VOLUME_KEY, soundEffectsVolume);
	    preferences.flush();
	}
	
}

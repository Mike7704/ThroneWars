package com.thronewars.managers;

/**
 * Stores the game's user-configurable settings.
 */
public class SettingsManager {

	private boolean fullscreenEnabled = false;
	private boolean entityShadowsEnabled = true;

	public boolean isFullscreenEnabled() {
		return fullscreenEnabled;
	}

	public void setFullscreenEnabled(boolean enabled) {
		fullscreenEnabled = enabled;
	}
	
	public boolean areEntityShadowsEnabled() {
		return entityShadowsEnabled;
	}

	public void setEntityShadowsEnabled(boolean enabled) {
		entityShadowsEnabled = enabled;
	}
}

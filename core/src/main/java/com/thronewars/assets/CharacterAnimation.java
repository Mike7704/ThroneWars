package com.thronewars.assets;

/**
 * Identifies the animation sprite sheets available for each character.
 */
public enum CharacterAnimation {
    IDLE,
    WALK,
    HURT,
    SLASH,
    THRUST,
    BACKSLASH,
    SHOOT;

	/**
	 * Returns the animation name in lowercase for use in asset paths.
	 *
	 * @return the corresponding sprite sheet file name, without extension
	 */
	public String getFileName() {
		return name().toLowerCase();
	}
}

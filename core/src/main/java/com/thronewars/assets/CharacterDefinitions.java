package com.thronewars.assets;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Defines the available characters in the game and their associated animations.
 */
public final class CharacterDefinitions {

	public static final CharacterDefinition JON_SNOW =
		new CharacterDefinition(
	        "jon-snow",
	        CharacterAnimation.IDLE,
	        CharacterAnimation.WALK
	        //CharacterAnimation.HURT,
	        //CharacterAnimation.SLASH,
	        //CharacterAnimation.THRUST,
	        //CharacterAnimation.BACKSLASH
		);

	public static final List<CharacterDefinition> ALL = Collections.unmodifiableList(Arrays.asList(JON_SNOW));
}

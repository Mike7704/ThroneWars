package com.thronewars.assets;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Defines the available unit characters in the game and their associated animations.
 */
public final class UnitDefinitions {

	public static final UnitDefinition JON_SNOW =
		new UnitDefinition(
	        "jon-snow",
	        UnitAnimation.IDLE,
	        UnitAnimation.WALK,
	        //UnitAnimation.HURT,
	        UnitAnimation.SLASH,
	        UnitAnimation.THRUST
	        //UnitAnimation.BACKSLASH
		);
	
	public static final UnitDefinition GREYBEARD =
		new UnitDefinition(
	        "greybeard",
	        UnitAnimation.IDLE,
	        UnitAnimation.WALK,
	        //UnitAnimation.HURT,
	        UnitAnimation.SLASH,
	        UnitAnimation.THRUST
	        //UnitAnimation.BACKSLASH
		);

	public static final List<UnitDefinition> ALL =
		Collections.unmodifiableList(Arrays.asList(
			JON_SNOW,
			GREYBEARD
		));
}

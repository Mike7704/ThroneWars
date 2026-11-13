package com.thronewars.assets;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

/**
 * Defines the asset configuration for a unit character.
 */
public class UnitDefinition {
	
	private final String id;
	private final Set<UnitAnimation> animations;

	public UnitDefinition(String id, UnitAnimation... animations) {
		if (id == null) {
			throw new IllegalArgumentException("Unit ID must not be null.");
		}

		if (animations == null || animations.length == 0) {
			throw new IllegalArgumentException("A unit must have at least one animation.");
		}
		    
		this.id = id;
		this.animations = EnumSet.noneOf(UnitAnimation.class);

		for (UnitAnimation animation : animations) {
			this.animations.add(animation);
		}
	}

	public String getId() {
		return id;
	}

	public Set<UnitAnimation> getAnimations() {
		return Collections.unmodifiableSet(animations);
	}

	public boolean hasAnimation(UnitAnimation animation) {
		return animations.contains(animation);
	}
}

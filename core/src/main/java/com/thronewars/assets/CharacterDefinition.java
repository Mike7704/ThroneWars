package com.thronewars.assets;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

/**
 * Defines the asset configuration for a character.
 */
public class CharacterDefinition {
	
	private final String id;
	private final Set<CharacterAnimation> animations;

	public CharacterDefinition(String id, CharacterAnimation... animations) {
		if (id == null) {
			throw new IllegalArgumentException("Character ID must not be null.");
		}

		if (animations == null || animations.length == 0) {
			throw new IllegalArgumentException("A character must have at least one animation.");
		}
		    
		this.id = id;
		this.animations = EnumSet.noneOf(CharacterAnimation.class);

		for (CharacterAnimation animation : animations) {
			this.animations.add(animation);
		}
	}

	public String getId() {
		return id;
	}

	public Set<CharacterAnimation> getAnimations() {
		return Collections.unmodifiableSet(animations);
	}

	public boolean hasAnimation(CharacterAnimation animation) {
		return animations.contains(animation);
	}
}

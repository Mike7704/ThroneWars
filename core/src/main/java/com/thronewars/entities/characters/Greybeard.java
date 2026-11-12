package com.thronewars.entities.characters;

import com.thronewars.assets.CharacterAssets;
import com.thronewars.entities.CharacterType;
import com.thronewars.entities.EntityCharacter;
import com.thronewars.entities.EntityType;
import com.thronewars.managers.AudioManager;

/**
 * Represents Greybeard entity in the game.
 */
public class Greybeard extends EntityCharacter {

	private static final String NAME = "Greybeard";
	private static final float SPEED = 200f;
	private static final float MAX_HEALTH = 100f;
    private static final int COST = 50;

	public Greybeard(CharacterAssets characterAssets, float x, float y, AudioManager audioManager) {
		super(EntityType.HERO, CharacterType.GREYBEARD, NAME, x, y, audioManager, characterAssets);

		speed = SPEED;
		maxHealth = MAX_HEALTH;
		health = maxHealth;
		cost = COST;
	}
}

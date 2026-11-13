package com.thronewars.entities.units;

import com.thronewars.assets.UnitAssets;
import com.thronewars.entities.UnitType;
import com.thronewars.entities.EntityUnit;
import com.thronewars.entities.EntityType;
import com.thronewars.managers.AudioManager;

/**
 * Represents Greybeard entity in the game.
 */
public class Greybeard extends EntityUnit {

	private static final String NAME = "Greybeard";
	private static final float SPEED = 200f;
	private static final float MAX_HEALTH = 100f;
    private static final int COST = 50;

	public Greybeard(UnitAssets unitAssets, float x, float y, AudioManager audioManager) {
		super(EntityType.HERO, UnitType.GREYBEARD, NAME, x, y, audioManager, unitAssets);

		speed = SPEED;
		maxHealth = MAX_HEALTH;
		health = maxHealth;
		cost = COST;
	}
}

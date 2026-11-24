package com.thronewars.entities.units;

import com.thronewars.assets.UnitAssets;
import com.thronewars.entities.UnitType;
import com.thronewars.entities.EntityUnit;
import com.thronewars.entities.UnitFaction;
import com.thronewars.entities.EntityType;
import com.thronewars.managers.AudioManager;

/**
 * Represents Lannister Knight entity in the game.
 */
public class LannisterKnight extends EntityUnit {

	private static final String NAME = "Lannister Knight";
	private static final float SPEED = 200f;
	private static final float MAX_HEALTH = 100f;
	private static final float ATTACK_DAMAGE = 25f;
    private static final int COST = 50;

	public LannisterKnight(UnitAssets unitAssets, float x, float y, AudioManager audioManager) {
		super(EntityType.SOLDIER, UnitType.LANNISTER_KNIGHT, UnitFaction.LANNISTER, NAME, x, y, audioManager, unitAssets);

		speed = SPEED;
		maxHealth = MAX_HEALTH;
		health = maxHealth;
		attackDamage = ATTACK_DAMAGE;
		cost = COST;
	}
}

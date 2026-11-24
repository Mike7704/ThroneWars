package com.thronewars.entities.units;

import com.thronewars.assets.UnitAssets;
import com.thronewars.entities.UnitType;
import com.thronewars.entities.EntityUnit;
import com.thronewars.entities.UnitFaction;
import com.thronewars.entities.EntityType;
import com.thronewars.managers.AudioManager;

/**
 * Represents Jon Snow entity in the game.
 */
public class JonSnow extends EntityUnit {

    private static final String NAME = "Jon Snow";
    private static final float SPEED = 200f;
    private static final float MAX_HEALTH = 125f;
	private static final float ATTACK_DAMAGE = 50f;
    private static final int COST = 100;

    public JonSnow(UnitAssets unitAssets, float x, float y, AudioManager audioManager) {
        super(EntityType.HERO, UnitType.JON_SNOW, UnitFaction.STARK, NAME, x, y, audioManager, unitAssets);

        speed = SPEED;
        maxHealth = MAX_HEALTH;
        health = maxHealth;
		attackDamage = ATTACK_DAMAGE;
        cost = COST;
    }
}

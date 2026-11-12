package com.thronewars.entities.characters;

import com.thronewars.assets.CharacterAssets;
import com.thronewars.entities.CharacterType;
import com.thronewars.entities.EntityCharacter;
import com.thronewars.entities.EntityType;
import com.thronewars.managers.AudioManager;

/**
 * Represents Jon Snow entity in the game.
 */
public class JonSnow extends EntityCharacter {

    private static final String NAME = "Jon Snow";
    private static final float SPEED = 200f;
    private static final float MAX_HEALTH = 100f;
    private static final int COST = 100;

    public JonSnow(CharacterAssets characterAssets, float x, float y, AudioManager audioManager) {
        super(EntityType.HERO, CharacterType.JON_SNOW, NAME, x, y, audioManager, characterAssets);

        speed = SPEED;
        maxHealth = MAX_HEALTH;
        health = maxHealth;
        cost = COST;
    }
}

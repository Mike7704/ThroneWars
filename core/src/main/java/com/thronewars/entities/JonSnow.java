package com.thronewars.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.thronewars.assets.CharacterAssets;

/**
 * Represents Jon Snow entity in the game.
 */
public class JonSnow extends Entity {

    private static final String NAME = "Jon Snow";
    private static final float SPEED = 2f;
    private static final float MAX_HEALTH = 100f;

    private final CharacterAssets characterAssets;

    public JonSnow(CharacterAssets characterAssets, float x, float y) {
        super(EntityType.HERO, NAME, x, y);

        this.characterAssets = characterAssets;

        speed = SPEED;
        maxHealth = MAX_HEALTH;
        health = maxHealth;
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(characterAssets.getIdleFrame(2), x, y, width, height);
    }
}

package com.thronewars.entities.characters;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.thronewars.assets.CharacterAnimation;
import com.thronewars.assets.CharacterAssets;
import com.thronewars.entities.Entity;
import com.thronewars.entities.EntityType;

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
		TextureRegion frame = characterAssets.getFrame(CharacterAnimation.IDLE, direction, stateTime);
    	
        batch.draw(frame, x, y, width, height);
    }
}

package com.thronewars.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.thronewars.assets.UnitAnimation;
import com.thronewars.assets.UnitAssets;
import com.thronewars.managers.AudioManager;

/**
 * Represents an animated character unit with shared animation rendering behaviour.
 */
public abstract class EntityUnit extends Entity {

	private static final float ATTACK_FRAME_SIZE = 384f;

	private final UnitType unitType;
	
	protected final UnitAssets unitAssets;

	protected EntityUnit(EntityType type, UnitType unitType, String name, float x, float y, AudioManager audioManager, UnitAssets unitAssets) {
		super(type, name, x, y, audioManager);
		this.unitType = unitType;
		this.unitAssets = unitAssets;
	}

	@Override
	public void render(SpriteBatch batch) {
		UnitAnimation animation;

		switch (state) {
			case IDLE: 		animation = UnitAnimation.IDLE; break;
			case WALKING: 	animation = UnitAnimation.WALK; break;
			case SLASHING:	animation = UnitAnimation.SLASH; break;
			case THRUSTING:	animation = UnitAnimation.THRUST; break;
			default: 		animation = UnitAnimation.IDLE;
		}

		TextureRegion frame = unitAssets.getFrame(animation, direction, stateTime);

		if (animation == UnitAnimation.SLASH || animation == UnitAnimation.THRUST) {
			renderAttack(batch, frame);
		}
		else {
			batch.draw(frame, x, y, width, height);
		}
	}

	protected void renderAttack(SpriteBatch batch, TextureRegion frame) {
		float offsetX = (ATTACK_FRAME_SIZE - width) / 2f;
		float offsetY = (ATTACK_FRAME_SIZE - height) / 2f;

		batch.draw(frame, x - offsetX, y - offsetY, ATTACK_FRAME_SIZE, ATTACK_FRAME_SIZE);
	}
	
	public UnitType getUnitType() {
	    return unitType;
	}
	
	public UnitAssets getUnitAssets() {
	    return unitAssets;
	}
}
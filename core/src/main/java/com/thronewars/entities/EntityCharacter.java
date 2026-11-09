package com.thronewars.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.thronewars.assets.CharacterAnimation;
import com.thronewars.assets.CharacterAssets;
import com.thronewars.managers.AudioManager;

/**
 * Represents an animated character with shared animation rendering behaviour.
 */
public abstract class EntityCharacter extends Entity {

	private static final float ATTACK_FRAME_SIZE = 384f;

	protected final CharacterAssets characterAssets;

	protected EntityCharacter(EntityType type, String name, float x, float y, AudioManager audioManager, CharacterAssets characterAssets) {
		super(type, name, x, y, audioManager);
		this.characterAssets = characterAssets;
	}

	@Override
	public void render(SpriteBatch batch) {
		CharacterAnimation animation;

		switch (state) {
			case IDLE: 		animation = CharacterAnimation.IDLE; break;
			case WALKING: 	animation = CharacterAnimation.WALK; break;
			case SLASHING:	animation = CharacterAnimation.SLASH; break;
			case THRUSTING:	animation = CharacterAnimation.THRUST; break;
			default: 		animation = CharacterAnimation.IDLE;
		}

		TextureRegion frame = characterAssets.getFrame(animation, direction, stateTime);

		if (animation == CharacterAnimation.SLASH || animation == CharacterAnimation.THRUST) {
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
}
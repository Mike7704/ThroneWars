package com.thronewars.assets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Converts a sprite sheet into individual frames.
 */
public class CharacterAssets {

	private final TextureRegion[] idleFrames;

	public CharacterAssets(Texture idleTexture) {
		TextureRegion[][] frames = TextureRegion.split(idleTexture, 64, 64);

		// Sprite direction order: 0 = Up, 1 = Left, 2 = Down, 3 = Right
		idleFrames = new TextureRegion[] { frames[0][0], frames[1][0], frames[2][0], frames[3][0] };
	}

	public TextureRegion getIdleFrame(int direction) {
		return idleFrames[direction];
	}
}
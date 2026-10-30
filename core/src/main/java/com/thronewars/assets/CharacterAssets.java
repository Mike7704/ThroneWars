
package com.thronewars.assets;

import java.util.EnumMap;
import java.util.Map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.thronewars.entities.EntityDirection;

/**
 * Splits the character's sprite sheets into individual frames and organises them by direction.
 */
public class CharacterAssets {

    private static final int FRAME_WIDTH_64 = 64;
    private static final int FRAME_HEIGHT_64 = 64;
    private static final int FRAME_WIDTH_192 = 192;
    private static final int FRAME_HEIGHT_192 = 192;
    
    private static final int DIRECTION_COUNT = 4;
    
    private static final int WALK_FRAME_COUNT = 8;
    private static final float WALK_FRAME_DURATION = 0.1f; // Seconds
    
    private static final int SLASH_FRAME_COUNT = 6;
    private static final float SLASH_FRAME_DURATION = 0.1f; // Seconds

    private final Map<CharacterAnimation, Animation<TextureRegion>[]> animations =  new EnumMap<>(CharacterAnimation.class);

    /**
     * Creates the character's animations from the supplied sprite sheets.
     *
     * @param idleTexture the texture containing one idle frame per direction
     * @param walkTexture the texture containing the walking animation frames
     */
	public CharacterAssets(CharacterDefinition character, Map<CharacterAnimation, Texture> textures) {

		if (character == null) {
			throw new IllegalArgumentException("Character definition cannot be null.");
		}

		if (textures == null) {
			throw new IllegalArgumentException("Character textures cannot be null.");
		}

		for (CharacterAnimation animation : character.getAnimations()) {
			Texture texture = textures.get(animation);

			if (texture == null) {
				throw new IllegalArgumentException("Missing texture for animation: " + animation);
			}

			switch (animation) {
				case IDLE: animations.put(animation, loadIdleAnimations(texture)); break;
				case WALK: animations.put(animation, loadWalkAnimations(texture)); break;
				case SLASH: animations.put(animation, loadSlashAnimations(texture)); break;
				
				default: throw new UnsupportedOperationException("Animation layout not implemented: " + animation);
			}
		}
	}

    /**
     * Extracts one idle frame for each direction from the idle sprite sheet.
     *
     * @param texture the idle sprite sheet
     * @return an array of idle frames ordered up, left, down, right
     */
	@SuppressWarnings("unchecked")
	private Animation<TextureRegion>[] loadIdleAnimations(Texture texture) {
		TextureRegion[][] frames = TextureRegion.split(texture, FRAME_WIDTH_64, FRAME_HEIGHT_64);

		Animation<TextureRegion>[] directionalAnimations = new Animation[DIRECTION_COUNT];

		for (int direction = 0; direction < DIRECTION_COUNT; direction++) {
			directionalAnimations[direction] = new Animation<>(1f, frames[direction][0]);
		}

		return directionalAnimations;
    }

    /**
     * Extracts the walking frames and creates a looping animation for each direction.
     *
     * @param texture the walking sprite sheet
     * @return an array of walking animations ordered up, left, down, right
     */
    @SuppressWarnings("unchecked")
	private Animation<TextureRegion>[] loadWalkAnimations(Texture texture) {
		TextureRegion[][] frames = TextureRegion.split(texture, FRAME_WIDTH_64, FRAME_HEIGHT_64);

		Animation<TextureRegion>[] directionalAnimations = new Animation[DIRECTION_COUNT];

		for (int direction = 0; direction < DIRECTION_COUNT; direction++) {
			TextureRegion[] directionFrames = new TextureRegion[WALK_FRAME_COUNT];

			for (int frame = 0; frame < WALK_FRAME_COUNT; frame++) {
				directionFrames[frame] = frames[direction][frame];
			}

			Animation<TextureRegion> animation = new Animation<>(WALK_FRAME_DURATION, directionFrames);

			animation.setPlayMode(PlayMode.LOOP);
			directionalAnimations[direction] = animation;
		}

		return directionalAnimations;
	}

    /**
     * Extracts the slash frames and creates an animation for each direction.
     *
     * @param texture the slash sprite sheet
     * @return an array of slash animations ordered up, left, down, right
     */
    @SuppressWarnings("unchecked")
	private Animation<TextureRegion>[] loadSlashAnimations(Texture texture) {
		TextureRegion[][] frames = TextureRegion.split(texture, FRAME_WIDTH_192, FRAME_HEIGHT_192);

		Animation<TextureRegion>[] directionalAnimations = new Animation[DIRECTION_COUNT];

		for (int direction = 0; direction < DIRECTION_COUNT; direction++) {
			TextureRegion[] directionFrames = new TextureRegion[SLASH_FRAME_COUNT];

			for (int frame = 0; frame < SLASH_FRAME_COUNT; frame++) {
				directionFrames[frame] = frames[direction][frame];
			}

			Animation<TextureRegion> animation = new Animation<>(SLASH_FRAME_DURATION, directionFrames);

			animation.setPlayMode(PlayMode.NORMAL);
			directionalAnimations[direction] = animation;
		}

		return directionalAnimations;
	}
    
    /**
     * Returns the frame for the specified animation, direction, and elapsed time.
     *
     * @param animation the animation type to retrieve
     * @param direction the direction index: 0 = up, 1 = left, 2 = down, 3 = right
     * @param stateTime the elapsed time in seconds since the animation began
     * @return the frame to render at the specified time
     */
	public TextureRegion getFrame(CharacterAnimation animation, EntityDirection direction, float stateTime) {

		if (animation == null) {
			throw new IllegalArgumentException("Animation cannot be null.");
		}

		Animation<TextureRegion>[] directionalAnimations = animations.get(animation);

		if (directionalAnimations == null) {
			throw new IllegalArgumentException("Animation not loaded: " + animation);
		}

		if (direction == null) {
		    throw new IllegalArgumentException("Direction cannot be null.");
		}
		
		int directionIndex = direction.ordinal();

		return directionalAnimations[directionIndex].getKeyFrame(stateTime);
	}
		
	public static float getSlashDuration() {
	    return SLASH_FRAME_COUNT * SLASH_FRAME_DURATION;
	}
}

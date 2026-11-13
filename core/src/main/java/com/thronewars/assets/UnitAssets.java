
package com.thronewars.assets;

import java.util.EnumMap;
import java.util.Map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.thronewars.entities.EntityDirection;

/**
 * Splits the unit character's sprite sheets into individual frames and organises them by direction.
 */
public class UnitAssets {

    private static final int FRAME_SIZE_64 = 64;
    private static final int FRAME_SIZE_192 = 192;
    
    private static final int DIRECTION_COUNT = 4;
    
    private static final int WALK_FRAME_COUNT = 8;
    private static final float WALK_FRAME_DURATION = 0.1f; // Seconds
    
    private static final int SLASH_FRAME_COUNT = 6;
    private static final float SLASH_FRAME_DURATION = 0.1f; // Seconds
    
    private static final int THRUST_FRAME_COUNT = 8;
    private static final float THRUST_FRAME_DURATION = 0.1f; // Seconds

    private final Map<UnitAnimation, Animation<TextureRegion>[]> animations =  new EnumMap<>(UnitAnimation.class);

    /**
     * Creates the unit character's animations from the supplied sprite sheets.
     *
     * @param idleTexture the texture containing one idle frame per direction
     * @param walkTexture the texture containing the walking animation frames
     */
	public UnitAssets(UnitDefinition unit, Map<UnitAnimation, Texture> textures) {

		if (unit == null) {
			throw new IllegalArgumentException("Unit definition cannot be null.");
		}

		if (textures == null) {
			throw new IllegalArgumentException("Unit textures cannot be null.");
		}

		for (UnitAnimation animation : unit.getAnimations()) {
			Texture texture = textures.get(animation);

			if (texture == null) {
				throw new IllegalArgumentException("Missing texture for animation: " + animation);
			}

			switch (animation) {
				case IDLE:
					animations.put(animation, loadIdleAnimations(texture));
					break;
	
				case WALK:
					animations.put(animation, loadAnimations(texture, FRAME_SIZE_64, FRAME_SIZE_64,
							WALK_FRAME_COUNT, WALK_FRAME_DURATION, PlayMode.LOOP));
					break;
	
				case SLASH:
					animations.put(animation, loadAnimations(texture, FRAME_SIZE_192, FRAME_SIZE_192,
							SLASH_FRAME_COUNT, SLASH_FRAME_DURATION, PlayMode.NORMAL));
					break;
	
				case THRUST:
					animations.put(animation, loadAnimations(texture, FRAME_SIZE_192, FRAME_SIZE_192,
							THRUST_FRAME_COUNT, THRUST_FRAME_DURATION, PlayMode.NORMAL));
					break;
				
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
		TextureRegion[][] frames = TextureRegion.split(texture, FRAME_SIZE_64, FRAME_SIZE_64);

		Animation<TextureRegion>[] directionalAnimations = new Animation[DIRECTION_COUNT];

		for (int direction = 0; direction < DIRECTION_COUNT; direction++) {
			directionalAnimations[direction] = new Animation<>(1f, frames[direction][0]);
		}

		return directionalAnimations;
    }
    
    /**
	 * Splits the sprite sheet into frames and creates an animation for each direction.
	 *
	 * @param texture the sprite sheet containing frames for all directions
	 * @param frameWidth the width of each frame in pixels
	 * @param frameHeight the height of each frame in pixels
	 * @param frameCount the number of frames per direction
	 * @param frameDuration the duration of each frame in seconds
	 * @param playMode the play mode for the animation (e.g., LOOP, NORMAL)
	 * @return an array of animations ordered up, left, down, right
	 */
    @SuppressWarnings("unchecked")
	private Animation<TextureRegion>[] loadAnimations(Texture texture, int frameWidth, int frameHeight, int frameCount,
			float frameDuration, PlayMode playMode) {

        TextureRegion[][] frames = TextureRegion.split(texture, frameWidth, frameHeight);

        Animation<TextureRegion>[] directionalAnimations = new Animation[DIRECTION_COUNT];

        for (int direction = 0; direction < DIRECTION_COUNT; direction++) {
            TextureRegion[] directionFrames = new TextureRegion[frameCount];

            for (int frame = 0; frame < frameCount; frame++) {
                directionFrames[frame] = frames[direction][frame];
            }

            Animation<TextureRegion> animation =
                    new Animation<>(frameDuration, directionFrames);

            animation.setPlayMode(playMode);
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
	public TextureRegion getFrame(UnitAnimation animation, EntityDirection direction, float stateTime) {

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
	
	public static float getThrustDuration() {
	    return THRUST_FRAME_COUNT * THRUST_FRAME_DURATION;
	}
}

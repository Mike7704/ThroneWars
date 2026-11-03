package com.thronewars.utils;

import java.util.Random;

/**
 * Utility class for generating random numbers.
 */
public final class RandomUtils {

    private static final Random RANDOM = new Random();

    private RandomUtils() {
    }

    /**
	 * Generates a random integer between the specified minimum and maximum values (inclusive).
	 *
	 * @param min the minimum value (inclusive)
	 * @param max the maximum value (inclusive)
	 * @return a random integer between min and max
	 */
    public static int randomInt(int min, int max) {
        return RANDOM.nextInt(max - min + 1) + min;
    }

    /**
	 * Generates a random float between the specified minimum and maximum values (inclusive).
	 *
	 * @param min the minimum value (inclusive)
	 * @param max the maximum value (inclusive)
	 * @return a random integer between min and max
	 */
    public static float randomFloat(float min, float max) {
        return min + RANDOM.nextFloat() * (max - min);
    }
}
package com.thronewars.world;

import com.thronewars.entities.EntityUnit;

/**
 * Represents a waypoint in the game world that can be occupied by an EntityUnit.
 */
public class Waypoint {

    private final float x;
    private final float y;

    private EntityUnit occupant;

    public Waypoint(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public boolean isAvailable() {
        return occupant == null;
    }

    public boolean assign(EntityUnit unit) {
        if (!isAvailable()) {
            return false;
        }

        occupant = unit;
        return true;
    }

    public void release() {
        occupant = null;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public EntityUnit getOccupant() {
        return occupant;
    }
}
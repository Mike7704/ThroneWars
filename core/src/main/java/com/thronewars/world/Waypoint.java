package com.thronewars.world;

import com.thronewars.entities.EntityUnit;

/**
 * Represents a waypoint in the game world that can be occupied by an EntityUnit.
 */
public class Waypoint {

    private float x;
    private float y;
    private final int column;

    private EntityUnit occupant;

    public Waypoint(float x, float y, int column) {
        this.x = x;
        this.y = y;
        this.column = column;
    }

    public boolean isAvailable() {
        return occupant == null;
    }

    public boolean assign(EntityUnit unit) {
        if (unit == null || !isAvailable()) {
            return false;
        }

        occupant = unit;
        return true;
    }

    public void release() {
        occupant = null;
    }

    public void setX(float x) {
        this.x = x;
    }
    
    public float getX() {
        return x;
    }
    
    public void setY(float y) {
        this.y = y;
    }

    public float getY() {
        return y;
    }
    
    public int getColumn() {
        return column;
    }

    public EntityUnit getOccupant() {
        return occupant;
    }
}
package com.thronewars.entities;

import com.badlogic.gdx.utils.Array;

/**
 * Defines the unit characters available to the player.
 */
public class PlayerUnitRoster {

    private final Array<EntityUnit> units = new Array<>();

    public PlayerUnitRoster(EntityFactory entityFactory) {
    	units.add(entityFactory.createGreybeard(0f, 0f));
        units.add(entityFactory.createJonSnow(0f, 0f));
    }

    public Array<EntityUnit> getUnits() {
        return units;
    }
}
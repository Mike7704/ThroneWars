package com.thronewars.entities;

import com.badlogic.gdx.utils.Array;

/**
 * Defines the unit characters available to the player and opponent.
 */
public class UnitRoster {

    private final Array<EntityUnit> units = new Array<>();

    public UnitRoster(EntityFactory entityFactory, UnitFaction faction) {
    	switch (faction) {
	    	case STARK:
	    		units.add(entityFactory.createGreybeard(0f, 0f));
	            units.add(entityFactory.createJonSnow(0f, 0f));
	    		break;
	    		
	    	case LANNISTER:
	    		units.add(entityFactory.createLannisterKnight(0f, 0f));
	    		break;
	    		
	    	default:
	    	    throw new IllegalArgumentException("Unsupported faction: " + faction);
	    }
    }

    public Array<EntityUnit> getUnits() {
        return units;
    }
}
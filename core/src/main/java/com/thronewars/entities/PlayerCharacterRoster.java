package com.thronewars.entities;

import com.badlogic.gdx.utils.Array;

/**
 * Defines the characters available to the player.
 */
public class PlayerCharacterRoster {

    private final Array<EntityCharacter> characters = new Array<>();

    public PlayerCharacterRoster(EntityFactory entityFactory) {
    	characters.add(entityFactory.createGreybeard(0f, 0f));
        characters.add(entityFactory.createJonSnow(0f, 0f));
    }

    public Array<EntityCharacter> getCharacters() {
        return characters;
    }
}
package com.thronewars.entities;

import com.thronewars.GdxGame;
import com.thronewars.assets.CharacterAssets;
import com.thronewars.assets.CharacterDefinitions;
import com.thronewars.entities.characters.JonSnow;

/**
 * Creates game entities and assigns assets.
 */
public class EntityFactory {

    private final GdxGame game;

    public EntityFactory(GdxGame game) {
        this.game = game;
    }

    public Entity createJonSnow(float x, float y) {
		CharacterAssets assets = game.getGameAssets().getCharacterAssets(CharacterDefinitions.JON_SNOW);

        return new JonSnow(assets, x, y);
    }
}

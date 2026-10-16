package com.thronewars.entities;

import com.thronewars.GdxGame;
import com.thronewars.assets.CharacterAssets;

/**
 * Creates game entities and assigns assets.
 */
public class EntityFactory {

    private final GdxGame game;

    public EntityFactory(GdxGame game) {
        this.game = game;
    }

    public Entity createJonSnow(float x, float y) {
        CharacterAssets assets = new CharacterAssets(
            game.getGameAssets().getJonSnowIdle()
        );

        return new JonSnow(assets, x, y);
    }
}

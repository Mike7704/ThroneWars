package com.thronewars.entities;

import com.thronewars.GdxGame;
import com.thronewars.assets.CharacterAssets;
import com.thronewars.assets.CharacterDefinitions;
import com.thronewars.entities.characters.Greybeard;
import com.thronewars.entities.characters.JonSnow;

/**
 * Creates game entities and assigns assets.
 */
public class EntityFactory {

	private final GdxGame game;

	public EntityFactory(GdxGame game) {
		this.game = game;
	}

	/**
	 * Creates a character entity based on the provided character definition and position.
	 *
	 * @param character The character definition to create.
	 * @param x The x-coordinate for the character's initial position.
	 * @param y The y-coordinate for the character's initial position.
	 * @return A new instance of the specified character entity.
	 */
	public EntityCharacter createCharacter(EntityCharacter character, float x, float y) {
		switch (character.getCharacterType()) {
			case JON_SNOW: return createJonSnow(x, y);
			case GREYBEARD: return createGreybeard(x, y);
	
			default: throw new IllegalArgumentException("Unsupported character type: " + character.getCharacterType());
		}
	}

	public JonSnow createJonSnow(float x, float y) {
		CharacterAssets assets = game.getGameAssets().getCharacterAssets(CharacterDefinitions.JON_SNOW);

		return new JonSnow(assets, x, y, game.getAudioManager());
	}

	public Greybeard createGreybeard(float x, float y) {
		CharacterAssets assets = game.getGameAssets().getCharacterAssets(CharacterDefinitions.GREYBEARD);

		return new Greybeard(assets, x, y, game.getAudioManager());
	}
}

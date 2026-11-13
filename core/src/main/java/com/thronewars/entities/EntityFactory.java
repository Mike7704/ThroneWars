package com.thronewars.entities;

import com.thronewars.GdxGame;
import com.thronewars.assets.UnitAssets;
import com.thronewars.assets.UnitDefinitions;
import com.thronewars.entities.units.Greybeard;
import com.thronewars.entities.units.JonSnow;

/**
 * Creates game entities and assigns assets.
 */
public class EntityFactory {

	private final GdxGame game;

	public EntityFactory(GdxGame game) {
		this.game = game;
	}

	/**
	 * Creates a unit entity based on the provided unit character definition and position.
	 *
	 * @param unit The unit character definition to create.
	 * @param x The x-coordinate for the unit's initial position.
	 * @param y The y-coordinate for the unit's initial position.
	 * @return A new instance of the specified unit character entity.
	 */
	public EntityUnit createUnit(EntityUnit unit, float x, float y) {
		switch (unit.getUnitType()) {
			case JON_SNOW: return createJonSnow(x, y);
			case GREYBEARD: return createGreybeard(x, y);
	
			default: throw new IllegalArgumentException("Unsupported unit type: " + unit.getUnitType());
		}
	}

	public JonSnow createJonSnow(float x, float y) {
		UnitAssets assets = game.getGameAssets().getUnitAssets(UnitDefinitions.JON_SNOW);

		return new JonSnow(assets, x, y, game.getAudioManager());
	}

	public Greybeard createGreybeard(float x, float y) {
		UnitAssets assets = game.getGameAssets().getUnitAssets(UnitDefinitions.GREYBEARD);

		return new Greybeard(assets, x, y, game.getAudioManager());
	}
}

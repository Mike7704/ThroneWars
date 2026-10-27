package com.thronewars.entities.world;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.thronewars.entities.Entity;
import com.thronewars.entities.EntityType;

/**
 * Represents a mineable ore deposit in the world.
 */
public class Ore extends Entity {

	private static final String NAME = "Ore";
    private static final float SPEED = 0f;
    private static final float MAX_HEALTH = 50f;
	
	private final TextureRegion textureRegion;

	public Ore(TiledMapTileMapObject object, float mapScale) {
		super(EntityType.ORE, NAME, object.getX() * mapScale, object.getY() * mapScale);

		textureRegion = object.getTextureRegion();

		width = textureRegion.getRegionWidth();
		height = textureRegion.getRegionHeight();

		speed = SPEED;
        maxHealth = MAX_HEALTH;
        health = maxHealth;
	}

	@Override
	public void render(SpriteBatch batch) {
		if (textureRegion == null) {
			return;
		}

		batch.draw(textureRegion, x, y, width, height);
	}
	
	@Override
	public void renderShadow(SpriteBatch batch, Texture shadowTexture) {
	    float shadowWidth = width;
	    float shadowHeight = height;
	    float shadowYOffset  = 2f;
	    
	    float shadowX = x + (width - shadowWidth) / 2f;
	    float shadowY = y - (shadowHeight / 2f) + shadowYOffset ;

		batch.draw(shadowTexture, shadowX, shadowY, shadowWidth, shadowHeight);
	}
}

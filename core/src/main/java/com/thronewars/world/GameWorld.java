package com.thronewars.world;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Array;
import com.thronewars.entities.world.Ore;

/**
 * Represents the game world, including the map and its objects.
 */
public class GameWorld {

	private static final float MAP_SCALE = 2f;
	
    private final TiledMap tiledMap;
    private final OrthogonalTiledMapRenderer mapRenderer;

    // World entity objects
    private final Array<Ore> ores = new Array<>();
    
    public GameWorld() {
        tiledMap = new TmxMapLoader().load("maps/the_north/the_north.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap, MAP_SCALE);
        
        loadOres();
    }

    /**
     * Creates an Ore entity for every tile object in the Tiled map's ores layer.
     */
	private void loadOres() {
		MapLayer layer = tiledMap.getLayers().get("ores");

		if (layer == null) {
			return;
		}

		for (MapObject object : layer.getObjects()) {
			if (object instanceof TiledMapTileMapObject) {
				TiledMapTileMapObject tileObject = (TiledMapTileMapObject) object;

				if (tileObject.getTextureRegion() != null) {
					ores.add(new Ore(tileObject, MAP_SCALE));
				}
			}
		}
	}
    
	/**
	 * Returns the ore entities loaded from the map.
	 */
	public Array<Ore> getOres() {
		return ores;
	}

    /**
     * Renders map layers.
     */
    public void render(OrthographicCamera camera, SpriteBatch batch) {
        mapRenderer.setView(camera);

		for (int i = 0; i < tiledMap.getLayers().getCount(); i++) {
			MapLayer layer = tiledMap.getLayers().get(i);

			if (!layer.isVisible()) {
				continue;
			}

			switch (layer.getName()) {
				case "ores": continue; // Entity object
				case "trees-big": renderObjectLayer(layer, batch, camera); break;
				default: mapRenderer.render(new int[] { i });
			}
		}
    }

    /**
     * Renders tile objects from an object layer.
     * @param layer
     * @param batch
     * @param camera
     */
	private void renderObjectLayer(MapLayer layer, SpriteBatch batch, OrthographicCamera camera) {
		batch.setProjectionMatrix(camera.combined);
		batch.begin();

		for (MapObject object : layer.getObjects()) {
			if (!(object instanceof TiledMapTileMapObject)) {
				continue;
			}

			TiledMapTileMapObject tileObject = (TiledMapTileMapObject) object;

            TextureRegion region = tileObject.getTextureRegion();

            if (region == null) {
                continue;
            }

            batch.draw(
                region,
                tileObject.getX() * MAP_SCALE,
                tileObject.getY() * MAP_SCALE,
                tileObject.getOriginX() * MAP_SCALE,
                tileObject.getOriginY() * MAP_SCALE,
                region.getRegionWidth() * MAP_SCALE,
                region.getRegionHeight() * MAP_SCALE,
                tileObject.getScaleX(),
                tileObject.getScaleY(),
                tileObject.getRotation()
            );
        }

        batch.end();
    }
    
    public void dispose() {
        mapRenderer.dispose();
        tiledMap.dispose();
    }
}

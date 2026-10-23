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

/**
 * Represents the game world, including the map and its objects.
 */
public class GameWorld {

	private static final float MAP_SCALE = 2f;
	
    private final TiledMap tiledMap;
    private final OrthogonalTiledMapRenderer mapRenderer;

    public GameWorld() {
        tiledMap = new TmxMapLoader().load("maps/the_north/the_north.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap, MAP_SCALE);
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

			if (layer.getName().equals("trees-big") || layer.getName().equals("ores")) {
				renderObjectLayer(layer, batch, camera);
			} else {
				mapRenderer.render(new int[] { i });
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

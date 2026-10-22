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

public class GameWorld {

	private static final float MAP_SCALE = 2f;
	
    private final TiledMap tiledMap;
    private final OrthogonalTiledMapRenderer mapRenderer;

    public GameWorld() {
        tiledMap = new TmxMapLoader().load("maps/the_north/the_north.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap, MAP_SCALE);
    }

    public void renderMap(OrthographicCamera camera) {
        mapRenderer.setView(camera);
        mapRenderer.render();
    }
    
    public void renderTreeObjects(SpriteBatch batch, OrthographicCamera camera) {
        MapLayer treesLayer = tiledMap.getLayers().get("trees");

        if (treesLayer == null) {
            return;
        }

        for (MapObject object : treesLayer.getObjects()) {
            if (!(object instanceof TiledMapTileMapObject)) {
                continue;
            }

            TiledMapTileMapObject tree = (TiledMapTileMapObject) object;
            TextureRegion region = tree.getTextureRegion();

            if (region == null) {
                continue;
            }

            batch.draw(
                region,
                tree.getX() * MAP_SCALE,
                tree.getY() * MAP_SCALE,
                tree.getOriginX() * MAP_SCALE,
                tree.getOriginY() * MAP_SCALE,
                region.getRegionWidth() * MAP_SCALE,
                region.getRegionHeight() * MAP_SCALE,
                tree.getScaleX(),
                tree.getScaleY(),
                tree.getRotation()
            );
        }
    }
    
    public void dispose() {
        mapRenderer.dispose();
        tiledMap.dispose();
    }
}

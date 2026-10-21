package com.thronewars.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.thronewars.GdxGame;
import com.thronewars.entities.Entity;
import com.thronewars.entities.EntityFactory;
import com.thronewars.managers.ScreenManager;
import com.thronewars.managers.ScreenType;

/**
 * Screen to display the main game view.
 */
public class GameScreen extends ScreenAdapter {

	private static final float WORLD_WIDTH = 5760f;
	private static final float WORLD_HEIGHT = ScreenManager.VIEW_HEIGHT;
	
	private final GdxGame game;
	private final SpriteBatch batch;
	private final FitViewport gameViewport;

	private final Texture backgroundTexture;
	
	private final Array<Entity> entities = new Array<>();
	
	public GameScreen(GdxGame game) {
		this.game = game;
		this.batch = game.getBatch();

		gameViewport = new FitViewport(ScreenManager.VIEW_WIDTH, ScreenManager.VIEW_HEIGHT);

		backgroundTexture = game.getGameAssets().getGameBackground();
		
		addEntities();
	}
	
	/**
	 * Adds game entities.
	 */
	private void addEntities() {
		EntityFactory entityFactory = new EntityFactory(game);

	    entities.add(entityFactory.createJonSnow(200f, 100f));
	}

	@Override
	public void render(float delta) {
		input();
		update(delta);
		draw();
	}

	private void input() {
		
		if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
			entities.get(0).moveUp();
		}
		else if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
			entities.get(0).moveLeft();
		}
		else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
			entities.get(0).moveDown();
		}
		else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
			entities.get(0).moveRight();
		}
		else {
			entities.get(0).setIdle();
		}
				
		if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			game.getScreenManager().showScreen(ScreenType.MAIN_MENU);
		}
	}

	private void update(float delta) {
		// Entities
		for (Entity entity : entities) {
		    entity.update(delta, WORLD_WIDTH, WORLD_HEIGHT);
		}
	}

	private void draw() {
		ScreenUtils.clear(Color.BLACK);

		gameViewport.apply();

		batch.setProjectionMatrix(gameViewport.getCamera().combined);

		batch.begin();

		drawBackground();

		// Entities
		for (Entity entity : entities) {
		    entity.render(batch);
		}
		
		batch.end();
	}

	/**
	 * Draws the background texture, scaling it to fit the viewport while maintaining its aspect ratio.
	 */
	private void drawBackground() {
		float worldWidth = gameViewport.getWorldWidth();
		float worldHeight = gameViewport.getWorldHeight();

		float u2 = worldWidth / ScreenManager.VIEW_WIDTH;

		batch.draw(backgroundTexture, 0f, 0f, worldWidth, worldHeight, 0f, 1f, u2, 0f);
	}

	@Override
	public void resize(int width, int height) {
		gameViewport.update(width, height, true);
	}
}

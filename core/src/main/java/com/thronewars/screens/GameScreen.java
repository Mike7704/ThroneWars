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
	private static final float CAMERA_SPEED = 960f;
	
	private final GdxGame game;
	private final SpriteBatch batch;
	private final FitViewport gameViewport;

	private final Texture backgroundTexture;
	
	private final Array<Entity> entities = new Array<>();
	
	public GameScreen(GdxGame game) {
		this.game = game;
		this.batch = game.getBatch();

		// Set up the game viewport with a fixed aspect ratio
		gameViewport = new FitViewport(ScreenManager.VIEW_WIDTH, ScreenManager.VIEW_HEIGHT);
		gameViewport.getCamera().position.set(ScreenManager.VIEW_WIDTH / 2f, ScreenManager.VIEW_HEIGHT / 2f, 0f);
		gameViewport.getCamera().update();
		
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
		input(delta);
		update(delta);
		draw();
	}

	private void input(float delta) {
		// Player controls
		controlCharacter();
		
		// Camera controls
		moveCamera(delta);
		
		// Menu controls
		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
			game.getScreenManager().showScreen(ScreenType.MAIN_MENU);
		}
	}
	
	/**
	 * Control the selected character
	 */
	private void controlCharacter() {
		Entity player = entities.get(0);
		 
		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			player.moveUp();
		}
		else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			player.moveLeft();
		}
		else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
			player.moveDown();
		}
		else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
			player.moveRight();
		}
		else {
			player.setIdle();
		}
	}
	
	/**
	 * Move the camera left or right within the world boundaries.
	 * @param delta
	 */
	private void moveCamera(float delta) {
		float movement = CAMERA_SPEED * delta;

		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			gameViewport.getCamera().position.x -= movement;
		}

		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			gameViewport.getCamera().position.x += movement;
		}

		// Keep the camera inside the world.
		float minX = ScreenManager.VIEW_WIDTH / 2f;
		float maxX = WORLD_WIDTH - ScreenManager.VIEW_WIDTH / 2f;

		gameViewport.getCamera().position.x = Math.max(minX, Math.min(gameViewport.getCamera().position.x, maxX));

		gameViewport.getCamera().update();
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
		float cameraX = gameViewport.getCamera().position.x - ScreenManager.VIEW_WIDTH / 2f;
		 
		batch.draw(backgroundTexture, cameraX, 0f, ScreenManager.VIEW_WIDTH, ScreenManager.VIEW_HEIGHT);
	}
	
	@Override
	public void resize(int width, int height) {
		gameViewport.update(width, height, false);
	}
}

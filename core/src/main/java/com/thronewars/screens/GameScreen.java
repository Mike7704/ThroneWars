package com.thronewars.screens;

import java.util.Comparator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.thronewars.GdxGame;
import com.thronewars.entities.Entity;
import com.thronewars.entities.EntityUnit;
import com.thronewars.entities.EntityFactory;
import com.thronewars.entities.PlayerUnitRoster;
import com.thronewars.managers.ScreenManager;
import com.thronewars.managers.ScreenType;
import com.thronewars.ui.GameHUD;
import com.thronewars.world.GameWorld;

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
	
	private final GameWorld world;
	private final GameHUD gameHUD;
	
	private final Texture shadowTexture;
	
	private final EntityFactory entityFactory;
	private final PlayerUnitRoster playerUnitRoster;
	private final Array<Entity> entities = new Array<>();
	private Entity player;
	
	private int playerHealth = 1000;
	private int playerGold = 500;
	private int playerUnitCount = 0;
	private int opponentHealth = 1000;
	private int opponentGold = 500;
	private int opponentUnitCount = 0;
	
	public GameScreen(GdxGame game) {
		this.game = game;
		this.batch = game.getBatch();

		// Set up the game viewport with a fixed aspect ratio
		gameViewport = new FitViewport(ScreenManager.VIEW_WIDTH, ScreenManager.VIEW_HEIGHT);
		gameViewport.getCamera().position.set(ScreenManager.VIEW_WIDTH / 2f, ScreenManager.VIEW_HEIGHT / 2f, 0f);
		gameViewport.getCamera().update();
		
		world = new GameWorld(game);

		entityFactory = new EntityFactory(game);
		
		playerUnitRoster = new PlayerUnitRoster(entityFactory);
		
		gameHUD = new GameHUD(game.getUIManager(), this, playerUnitRoster);

		shadowTexture = game.getGameAssets().getShadowTexture();
		
		createEntities();
	}
	
	/**
	 * Adds entities 
	 */
	private void createEntities() {
		player = entityFactory.createJonSnow(100f, 300f);
		entities.add(player);
		playerUnitCount++;
		
		// World entity objects
	    entities.addAll(world.getOres());
	}

	
	public void addCharacterUnit(EntityUnit unit) {
		int cost = unit.getCost();

		// Check whether the player can afford the unit
	    if (playerGold < cost) {
	        return;
	    }
	    
	    // Temp spawn values
	    float spawnX = player.getX();
	    float spawnY = player.getY();
	    
	    EntityUnit newUnit = entityFactory.createUnit(unit, spawnX, spawnY);
	    entities.add(newUnit);

	    playerGold -= cost;
	    playerUnitCount++;
	}
	
	@Override
	public void render(float delta) {
		input(delta);
		update(delta);
		draw(delta);
	}

	private void input(float delta) {
		// Player controls
		controlCharacterUnit();
		
		// Camera controls
		moveCamera(delta);
		
		// Menu controls
		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
			game.getScreenManager().showScreen(ScreenType.MAIN_MENU);
		}
	}
	
	/**
	 * Control the selected character unit
	 */
	private void controlCharacterUnit() {
		if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			player.attack();
		}
		else if (Gdx.input.isKeyPressed(Input.Keys.W)) {
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

		// Keep the camera inside the world
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
		
	    gameHUD.updateValues();
	}

	private void draw(float delta) {
	    ScreenUtils.clear(Color.BLACK);

	    gameViewport.apply();

		OrthographicCamera camera = (OrthographicCamera) gameViewport.getCamera();

		// Render world layers and objects
	    world.render(camera, batch);

	    // Sort entities from back to front
		entities.sort(Comparator.comparingDouble(Entity::getY).reversed());
	    
	    batch.setProjectionMatrix(camera.combined);
	    batch.begin();

		// Entities
		for (Entity entity : entities) {
			if (game.getSettingsManager().areEntityShadowsEnabled()) {
				entity.renderShadow(batch, shadowTexture);	
			}
		    entity.render(batch);
		}
		
		batch.end();
		
		// HUD
		gameHUD.render(delta);
	}
	
	@Override
	public void resize(int width, int height) {
		gameViewport.update(width, height, false);
		gameHUD.resize(width, height);
	}
	
	@Override
	public void show() {
	    Gdx.input.setInputProcessor(gameHUD.getStage());
	}
	
	@Override
	public void dispose() {
	    gameHUD.dispose();
	}
	
	public int getPlayerHealth() {
	    return playerHealth;
	}

	public int getPlayerGold() {
	    return playerGold;
	}

	public int getPlayerUnitCount() {
	    return playerUnitCount;
	}

	public int getOpponentHealth() {
	    return opponentHealth;
	}
	
	public int getOpponentGold() {
	    return opponentGold;
	}

	public int getOpponentUnitCount() {
	    return opponentUnitCount;
	}
}

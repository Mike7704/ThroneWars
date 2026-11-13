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
import com.thronewars.entities.UnitCommand;
import com.thronewars.managers.ScreenManager;
import com.thronewars.managers.ScreenType;
import com.thronewars.ui.GameHUD;
import com.thronewars.utils.RandomUtils;
import com.thronewars.world.GameWorld;
import com.thronewars.world.Waypoint;
import com.thronewars.world.WaypointFormation;

/**
 * Screen to display the main game view.
 */
public class GameScreen extends ScreenAdapter {

	private static final float WORLD_WIDTH = 5760f;
	private static final float WORLD_HEIGHT = ScreenManager.VIEW_HEIGHT;
	private static final float WORLD_MIN_Y = 260f;
	private static final float WORLD_MAX_Y = 616f;
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
	private final Array<EntityUnit> playerUnits = new Array<>();
	private final Array<EntityUnit> opponentUnits = new Array<>();
	private Entity player;
	
	private UnitCommand currentUnitCommand = UnitCommand.DEFEND;
	
	private WaypointFormation playerDefenceFormation;
	private WaypointFormation opponentDefenceFormation;
	
	private static final int MAX_UNIT_COUNT = 25;
	private int playerHealth = 1000;
	private int playerGold = 5000;
	private int opponentHealth = 1000;
	private int opponentGold = 500;
	
	public GameScreen(GdxGame game) {
		this.game = game;
		this.batch = game.getBatch();

		// Set up the game viewport with a fixed aspect ratio
		gameViewport = new FitViewport(ScreenManager.VIEW_WIDTH, ScreenManager.VIEW_HEIGHT);
		gameViewport.getCamera().position.set(ScreenManager.VIEW_WIDTH / 2f, ScreenManager.VIEW_HEIGHT / 2f, 0f);
		gameViewport.getCamera().update();
		
		world = new GameWorld(game);

		generateWaypoints();
		
		entityFactory = new EntityFactory(game);
		
		playerUnitRoster = new PlayerUnitRoster(entityFactory);
		
		gameHUD = new GameHUD(game.getUIManager(), this, playerUnitRoster);

		shadowTexture = game.getGameAssets().getShadowTexture();
		
		createEntities();
	}
	
	/**
	 * Generates the waypoint formations for both the player and opponent.
	 */
	private void generateWaypoints() {
		int rows = 5;
		int columns = 5;
		float startingX = 530f;
		float horzSpacing = 90f;
		float vertSpacing = 55f;

		playerDefenceFormation = new WaypointFormation(rows, columns, startingX, WORLD_MIN_Y,
				horzSpacing, vertSpacing);

		opponentDefenceFormation = new WaypointFormation(rows, columns, WORLD_WIDTH - startingX, WORLD_MIN_Y,
				-horzSpacing, vertSpacing);
    }
	
	/**
	 * Adds entities 
	 */
	private void createEntities() {
		player = entityFactory.createJonSnow(100f, 300f);
		entities.add(player);
		playerUnits.add((EntityUnit) player);
		
		// World entity objects
	    entities.addAll(world.getOres());
	}

	/**
	 * Adds a new character unit to the game world if the player has enough gold.
	 * @param unit The character unit to be added.
	 */
	public void recruitUnit(EntityUnit unit) {
		
		if (playerUnits.size >= MAX_UNIT_COUNT) {
			return;
		}
		
		int cost = unit.getCost();
	    if (playerGold < cost) {
	        return;
	    }
	    
	    // Spawn the unit off screen
	    EntityUnit newUnit = entityFactory.createUnit(unit, -100f, RandomUtils.randomFloat(WORLD_MIN_Y, WORLD_MAX_Y));
	    entities.add(newUnit);
		playerUnits.add(newUnit);
		
	    playerGold -= cost;
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
		updatePlayerUnits();

	    for (Entity entity : entities) {
	        entity.update(delta, WORLD_WIDTH, WORLD_MIN_Y, WORLD_MAX_Y);
	    }

	    gameHUD.updateValues();
	}

	/**
	 * Updates the player units to move to their target.
	 */
	private void updatePlayerUnits() {
		if (currentUnitCommand != UnitCommand.DEFEND) {
			return;
		}

		for (EntityUnit unit : playerUnits) {
			if (unit == player || unit.getAssignedWaypoint() != null) {
				continue;
			}

			Waypoint waypoint = playerDefenceFormation.getAvailableWaypoint();

			if (waypoint == null) {
				break;
			}

			waypoint.assign(unit);
			unit.setAssignedWaypoint(waypoint);

			unit.moveTo(waypoint.getX(), waypoint.getY());
		}
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
	
	public Array<EntityUnit> getPlayerUnits() {
	    return playerUnits;
	}
	
	public Array<EntityUnit> getOpponentUnits() {
	    return opponentUnits;
	}
	
	public int getPlayerHealth() {
	    return playerHealth;
	}

	public int getPlayerGold() {
	    return playerGold;
	}

	public int getOpponentHealth() {
	    return opponentHealth;
	}
	
	public int getOpponentGold() {
	    return opponentGold;
	}
}

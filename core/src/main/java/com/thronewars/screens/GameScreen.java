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
import com.thronewars.entities.EntityDirection;
import com.thronewars.entities.EntityUnit;
import com.thronewars.entities.EntityFactory;
import com.thronewars.entities.UnitRoster;
import com.thronewars.entities.UnitCommand;
import com.thronewars.entities.UnitFaction;
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
	private final UnitRoster playerUnitRoster;
	private final UnitRoster opponentUnitRoster;
	private final Array<Entity> entities = new Array<>();
	private final Array<EntityUnit> playerUnits = new Array<>();
	private final Array<EntityUnit> opponentUnits = new Array<>();
	private Entity player;
	
	private UnitCommand currentPlayerUnitCommand = UnitCommand.DEFEND;
	private UnitCommand currentOpponentUnitCommand = UnitCommand.DEFEND;
	
	private WaypointFormation playerRetreatFormation;
	private WaypointFormation playerDefenceFormation;
	private WaypointFormation opponentRetreatFormation;
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
		
		playerUnitRoster = new UnitRoster(entityFactory, UnitFaction.STARK);
		opponentUnitRoster = new UnitRoster(entityFactory, UnitFaction.LANNISTER);
		
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
		float startingRetreatX = -600f;
		float startingDefenceX = 530f;
		float horzSpacing = 90f;
		float vertSpacing = 55f;

		playerRetreatFormation = new WaypointFormation(rows, columns, startingRetreatX, WORLD_MIN_Y,
				horzSpacing, vertSpacing);
		
		playerDefenceFormation = new WaypointFormation(rows, columns, startingDefenceX, WORLD_MIN_Y,
				horzSpacing, vertSpacing);

		opponentRetreatFormation = new WaypointFormation(rows, columns, WORLD_WIDTH + Math.abs(startingRetreatX), WORLD_MIN_Y,
				horzSpacing, vertSpacing);
		
		opponentDefenceFormation = new WaypointFormation(rows, columns, WORLD_WIDTH - startingDefenceX, WORLD_MIN_Y,
				-horzSpacing, vertSpacing);
    }
	
	/**
	 * Adds entities 
	 */
	private void createEntities() {
		player = entityFactory.createJonSnow(100f, 300f);
		entities.add(player);
		playerUnits.add((EntityUnit) player);
		
		// Opponent Units
		EntityUnit lannisterKnight = opponentUnitRoster.getUnits().first();
		for (int i = 0; i < 5; i++) {
		    recruitOpponentUnit(lannisterKnight);
		}
		
		// World entity objects
	    entities.addAll(world.getOres());
	}

	/**
	 * Adds a new character unit to the game world if the player has enough gold.
	 * @param unit The character unit to be added.
	 */
	public void recruitPlayerUnit(EntityUnit unit) {
		
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
	
	/**
	 * Adds a new character unit to the game world if the opponent has enough gold.
	 * @param unit The character unit to be added.
	 */
	public void recruitOpponentUnit(EntityUnit unit) {
		
		if (opponentUnits.size >= MAX_UNIT_COUNT) {
			return;
		}
		
		int cost = unit.getCost();
	    if (opponentGold < cost) {
	        return;
	    }
	    
	    // Spawn the unit off screen
	    EntityUnit newUnit = entityFactory.createUnit(unit, WORLD_WIDTH + 100f, RandomUtils.randomFloat(WORLD_MIN_Y, WORLD_MAX_Y));
	    entities.add(newUnit);
	    opponentUnits.add(newUnit);
		
	    opponentGold -= cost;
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
		updateUnits();

	    for (Entity entity : entities) {
	        entity.update(delta, WORLD_WIDTH, WORLD_MIN_Y, WORLD_MAX_Y, entities);
	    }

	    gameHUD.updateValues();
	}

	/**
	 * Updates the player and opponent units to move to their target.
	 */
	private void updateUnits() {
		for (EntityUnit unit : playerUnits) {
			// Unit is controlled by the player.
			if (unit == player) {
				continue;
			}

			// Only issue new orders when the command changes.
			if (unit.getAssignedCommand() == currentPlayerUnitCommand) {
				continue;
			}

			releaseAssignedWaypoint(unit);

			switch (currentPlayerUnitCommand) {
				case RETREAT:
					assignWaypoint(unit, playerRetreatFormation, EntityDirection.RIGHT, currentPlayerUnitCommand);
					break;
					
	            case DEFEND:
	                assignWaypoint(unit, playerDefenceFormation, EntityDirection.RIGHT, currentPlayerUnitCommand);
	                break;	
	
	            case ATTACK:
	                unit.moveTo(WORLD_WIDTH - 300f, unit.getY(), EntityDirection.RIGHT);
	                unit.setAssignedCommand(UnitCommand.ATTACK);
	                break;
	        }
		}
		
		for (EntityUnit unit : opponentUnits) {
			// Only issue new orders when the command changes.
			if (unit.getAssignedCommand() == currentOpponentUnitCommand) {
				continue;
			}

			releaseAssignedWaypoint(unit);

			switch (currentOpponentUnitCommand) {
				case RETREAT:
					assignWaypoint(unit, opponentRetreatFormation, EntityDirection.LEFT, currentOpponentUnitCommand);
					break;
					
	            case DEFEND:
	                assignWaypoint(unit, opponentDefenceFormation, EntityDirection.LEFT, currentOpponentUnitCommand);
	                break;	
	
	            case ATTACK:
	                unit.moveTo(300f, unit.getY(), EntityDirection.LEFT);
	                unit.setAssignedCommand(UnitCommand.ATTACK);
	                break;
	        }
		}
	}
	
	/**
	 * Assigns a waypoint to the given unit based on the specified formation.
	 * @param unit The unit to assign a waypoint to.
	 * @param formation The waypoint formation to use for assignment.
	 */
	private void assignWaypoint(EntityUnit unit, WaypointFormation formation, EntityDirection targetDirection, UnitCommand unitCommand) {
	    Waypoint waypoint = formation.getAvailableWaypoint();

	    if (waypoint == null) {
	        return;
	    }

	    waypoint.assign(unit);
	    unit.setAssignedWaypoint(waypoint);
	    unit.moveTo(waypoint.getX(), waypoint.getY(), targetDirection);

	    unit.setAssignedCommand(unitCommand);
	}
	
	/**
	 * Releases the assigned waypoint for the given unit.
	 * @param unit The unit whose assigned waypoint should be released.
	 */
	private void releaseAssignedWaypoint(EntityUnit unit) {
	    Waypoint waypoint = unit.getAssignedWaypoint();

	    if (waypoint != null) {
	        waypoint.release();
	        unit.setAssignedWaypoint(null);
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
	
	public void setPlayerUnitCommand(UnitCommand command) {
		currentPlayerUnitCommand = command;
	}
	
	public void setOpponentUnitCommand(UnitCommand command) {
		currentOpponentUnitCommand = command;
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

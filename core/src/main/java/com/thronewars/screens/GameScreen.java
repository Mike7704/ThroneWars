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
import com.thronewars.entities.EntityFactory;
import com.thronewars.entities.EntityUnit;
import com.thronewars.entities.UnitCommand;
import com.thronewars.entities.UnitFaction;
import com.thronewars.entities.UnitRoster;
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
	private WaypointFormation playerAttackFormation;
	private WaypointFormation opponentRetreatFormation;
	private WaypointFormation opponentDefenceFormation;
	private WaypointFormation opponentAttackFormation;
	
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
		float startingAttackX = 1000f;
		float horzSpacing = 90f;
		float vertSpacing = 55f;

		playerRetreatFormation = new WaypointFormation(rows, columns, startingRetreatX, WORLD_MIN_Y,
				horzSpacing, vertSpacing);
		
		playerDefenceFormation = new WaypointFormation(rows, columns, startingDefenceX, WORLD_MIN_Y,
				horzSpacing, vertSpacing);

		playerAttackFormation = new WaypointFormation(rows, columns, WORLD_WIDTH - startingAttackX, WORLD_MIN_Y,
				horzSpacing, vertSpacing);
		
		opponentRetreatFormation = new WaypointFormation(rows, columns, WORLD_WIDTH + Math.abs(startingRetreatX), WORLD_MIN_Y,
				-horzSpacing, vertSpacing);
		
		opponentDefenceFormation = new WaypointFormation(rows, columns, WORLD_WIDTH - startingDefenceX, WORLD_MIN_Y,
				-horzSpacing, vertSpacing);
		
		opponentAttackFormation = new WaypointFormation(rows, columns, startingAttackX, WORLD_MIN_Y,
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
	        entity.update(delta, WORLD_WIDTH, WORLD_MIN_Y, WORLD_MAX_Y);
	    }

	    gameHUD.updateValues();
	}

	/**
	 * Updates the orders and combat state of both player and opponent units.
	 */
	private void updateUnits() {
		updateUnitOrders(playerUnits, currentPlayerUnitCommand, playerRetreatFormation, playerDefenceFormation,
				playerAttackFormation, EntityDirection.RIGHT);
		updateUnitOrders(opponentUnits, currentOpponentUnitCommand, opponentRetreatFormation, opponentDefenceFormation,
				opponentAttackFormation, EntityDirection.LEFT);
		
		updateUnitCombat(playerUnits, opponentUnits, currentPlayerUnitCommand);
		updateUnitCombat(opponentUnits, playerUnits, currentOpponentUnitCommand);
	}
	
	/**
	 * Updates unit movement orders when their assigned command changes.
	 *
	 * @param units The units receiving orders.
	 * @param command The current command.
	 * @param retreatFormation The retreat formation.
	 * @param defenceFormation The defensive formation.
	 * @param attackFormation The attack formation.
	 * @param direction The direction units should face when reaching their waypoints.
	 */
	private void updateUnitOrders(Array<EntityUnit> units, UnitCommand command, WaypointFormation retreatFormation,
			WaypointFormation defenceFormation, WaypointFormation attackFormation, EntityDirection direction) {

		for (EntityUnit unit : units) {
			// Unit is controlled by the player or dead.
			if (unit == player || !unit.isAlive()) {
				continue;
			}

			// Only issue new orders when the command changes.
			if (unit.getAssignedCommand() == command) {
				continue;
			}

			WaypointFormation formation;

			switch (command) {
				case RETREAT: formation = retreatFormation; break;
				case DEFEND: formation = defenceFormation; break;
				case ATTACK: formation = attackFormation; break;
				default: continue;
			}

			assignWaypoint(unit, formation, direction, command);
		}
	}
	
	/**
	 * Assigns a waypoint to the given unit based on the specified formation.
	 * @param unit The unit to assign a waypoint to.
	 * @param formation The waypoint formation to use for assignment.
	 * @param targetDirection The direction the unit should face when moving to the waypoint.
	 * @param unitCommand The command associated with the unit's movement.
	 * @return true if a waypoint was successfully assigned; false if no available waypoints were found.
	 */
	private boolean assignWaypoint(EntityUnit unit, WaypointFormation formation, EntityDirection targetDirection, UnitCommand unitCommand) {
		Waypoint waypoint = formation.getAvailableWaypoint();

		if (waypoint == null) {
			return false;
		}

		releaseAssignedWaypoint(unit);
		
		waypoint.assign(unit);
		unit.setAssignedWaypoint(waypoint);
		unit.setAssignedCommand(unitCommand);
		unit.moveTo(waypoint.getX(), waypoint.getY(), targetDirection);

		return true;
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
	
	/**
	 * Updates the combat state of the given units based on their proximity to enemies.
	 * @param units The units to update.
	 * @param enemies The potential enemy units.
	 * @param command The current command for the units.
	 */
	private void updateUnitCombat(Array<EntityUnit> units, Array<EntityUnit> enemies, UnitCommand command) {

		for (EntityUnit unit : units) {
			if (unit == player || !unit.isAlive()) {
				continue;
			}

			// Retreating units avoid attacking.
			if (command == UnitCommand.RETREAT) {
				unit.setEnemyTarget(null);
				unit.resumeMovement();
				continue;
			}
			
			// Defending units return to formation before attacking.
			if (command == UnitCommand.DEFEND && !unit.hasReachedTarget()) {
	            unit.setEnemyTarget(null);
	            unit.resumeMovement();
	            continue;
	        }

			EntityUnit enemy = findNearestEnemy(unit, enemies);

			if (enemy != null && isWithinAttackRange(unit, enemy)) {
				unit.setEnemyTarget(enemy);
				unit.pauseMovement();
			} else {
				unit.setEnemyTarget(null);
				unit.resumeMovement();
			}
		}
	}
	
	/**
	 * Finds the nearest enemy unit to the given unit from the list of enemies.
	 * @param unit The unit for which to find the nearest enemy.
	 * @param enemies The list of potential enemy units.
	 * @return The nearest enemy unit, or null if no enemies are found.
	 */
	private EntityUnit findNearestEnemy(EntityUnit unit, Array<EntityUnit> enemies) {
		EntityUnit nearestEnemy = null;
		float nearestDistance = Float.MAX_VALUE;

		for (EntityUnit enemy : enemies) {
			if (!enemy.isAlive()) {
				continue;
			}

			float dx = enemy.getX() - unit.getX();
			float dy = enemy.getY() - unit.getY();

			float distance = dx * dx + dy * dy;

			if (distance < nearestDistance) {
				nearestDistance = distance;
				nearestEnemy = enemy;
			}
		}

		return nearestEnemy;
	}
	
	/**
	 * Checks if the given unit is within attack range of the specified enemy unit.
	 * @param unit The unit to check.
	 * @param enemy The enemy unit to check against.
	 * @return true if the unit is within attack range of the enemy; false otherwise.
	 */
	private boolean isWithinAttackRange(EntityUnit unit, EntityUnit enemy) {
		float dx = enemy.getX() - unit.getX();
		float dy = enemy.getY() - unit.getY();

		float distanceSquared = dx * dx + dy * dy;

		return distanceSquared <= unit.getAttackRange() * unit.getAttackRange();
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

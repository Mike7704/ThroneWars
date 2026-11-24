package com.thronewars.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.thronewars.assets.UnitAnimation;
import com.thronewars.assets.UnitAssets;
import com.thronewars.managers.AudioManager;
import com.thronewars.world.Waypoint;

/**
 * Represents an animated character unit with shared animation rendering behaviour.
 */
public abstract class EntityUnit extends Entity {

	private static final float ATTACK_FRAME_SIZE = 384f;

	private final UnitType unitType;
	private final UnitFaction faction;
	
	protected final UnitAssets assets;
	
	private UnitCommand assignedCommand; 
	private Waypoint assignedWaypoint;
	
	private float attackRange = 100f;
    private EntityUnit enemyTarget;

	protected EntityUnit(EntityType type, UnitType unitType, UnitFaction faction, String name, float x, float y, AudioManager audioManager, UnitAssets unitAssets) {
		super(type, name, x, y, audioManager);
		this.unitType = unitType;
		this.faction = faction;
		this.assets = unitAssets;
	}

	@Override
	public void render(SpriteBatch batch) {
		UnitAnimation animation;

		switch (state) {
			case IDLE: 		animation = UnitAnimation.IDLE; break;
			case WALKING: 	animation = UnitAnimation.WALK; break;
			case DEAD:      animation = UnitAnimation.DEATH; break;
			case SLASHING:	animation = UnitAnimation.SLASH; break;
			case THRUSTING:	animation = UnitAnimation.THRUST; break;
			default: 		animation = UnitAnimation.IDLE;
		}

		TextureRegion frame = assets.getFrame(animation, direction, stateTime);

		if (animation == UnitAnimation.SLASH || animation == UnitAnimation.THRUST) {
			renderAttack(batch, frame);
		}
		else {
			batch.draw(frame, x, y, width, height);
		}
	}

	protected void renderAttack(SpriteBatch batch, TextureRegion frame) {
		float offsetX = (ATTACK_FRAME_SIZE - width) / 2f;
		float offsetY = (ATTACK_FRAME_SIZE - height) / 2f;

		batch.draw(frame, x - offsetX, y - offsetY, ATTACK_FRAME_SIZE, ATTACK_FRAME_SIZE);
	}
	
	public UnitCommand getAssignedCommand() {
	    return assignedCommand;
	}

	public void setAssignedCommand(UnitCommand command) {
	    this.assignedCommand = command;
	}
	
	public void setAssignedWaypoint(Waypoint waypoint) {
	    this.assignedWaypoint = waypoint;
	}

	public Waypoint getAssignedWaypoint() {
	    return assignedWaypoint;
	}
	
	public float getAttackRange() {
	    return attackRange;
	}

	public EntityUnit getEnemyTarget() {
	    return enemyTarget;
	}

	public void setEnemyTarget(EntityUnit enemyTarget) {
	    this.enemyTarget = enemyTarget;
	}
	
	public UnitType getUnitType() {
	    return unitType;
	}
	
	public UnitFaction getFaction() {
	    return faction;
	}
	
	public UnitAssets getUnitAssets() {
	    return assets;
	}
}
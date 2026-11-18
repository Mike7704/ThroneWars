package com.thronewars.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.thronewars.assets.UnitAssets;
import com.thronewars.managers.AudioManager;
import com.thronewars.utils.RandomUtils;

/**
 * Represents a generic entity with common properties and behaviours.
 */
public abstract class Entity {

	protected final AudioManager audioManager;
	
	protected final EntityType type;
	protected final String name;
    protected float speed = 200f;
    protected float health = 10f;
    protected float maxHealth = 10f;
    protected int cost = 0;
	
	protected float x;
    protected float y;
    protected float width = 128f;
    protected float height = 128f;
	
    private float targetX;
    private float targetY;
    private EntityDirection targetDirection;
    private boolean movingToTarget = false;
    
    protected EntityState state = EntityState.IDLE;
    protected EntityDirection direction = EntityDirection.DOWN;
    protected float stateTime = 0f;
    
    protected boolean hasCollision = true;
    protected float collisionRadius = 20f;
    
    protected Entity(EntityType type, String name, float x, float y, AudioManager audioManager) {
        this.type = type;
        this.name = name;
        this.x = x;
        this.y = y;
        this.audioManager = audioManager;
    }

    public void update(float delta, float worldWidth, float worldMinY, float worldMaxY, Array<Entity> entities) {
    	if (state == EntityState.SLASHING) {
            stateTime += delta;
            if (stateTime >= UnitAssets.getSlashDuration()) {
            	finishAttack();
            }
        }
    	else if (state == EntityState.THRUSTING) {
            stateTime += delta;
            if (stateTime >= UnitAssets.getThrustDuration()) {
            	finishAttack();
            }
        }
    	else if (state == EntityState.WALKING || movingToTarget) {
    	    stateTime += delta;

    	    if (movingToTarget) {
    	        moveTowardsTarget(delta, worldWidth, worldMinY, worldMaxY, entities);
    	    } else if (state == EntityState.WALKING) {
    	        move(delta, worldWidth, worldMinY, worldMaxY, entities);
    	    }
    	}
    }

	public abstract void render(SpriteBatch batch);
	
	public void renderShadow(SpriteBatch batch, Texture shadowTexture) {
	    float shadowWidth = width * 0.5f;
	    float shadowHeight = height * 0.5f;
	    float shadowYOffset  = 7f;
	    
	    float shadowX = x + (width - shadowWidth) / 2f;
	    float shadowY = y - (shadowHeight / 2f) + shadowYOffset ;

		batch.draw(shadowTexture, shadowX, shadowY, shadowWidth, shadowHeight);
	}
	
	public void attack() {
		if (state == EntityState.IDLE || state == EntityState.WALKING) {
			if (RandomUtils.randomInt(0, 1) == 1) {
				setState(EntityState.SLASHING);	
			}
			else {
				setState(EntityState.THRUSTING);
			}
			audioManager.playSwordAttackSound();
	    }
	}
	
	protected void finishAttack() {
	    setState(EntityState.IDLE);
	}
	
	public void takeDamage(float damage) {
		if (damage <= 0) {
			return;
		}

		health = Math.max(0, health - damage);
	}
	
	public void moveTo(float targetX, float targetY, EntityDirection targetDirection) {
	    this.targetX = targetX;
	    this.targetY = targetY;
	    this.targetDirection = targetDirection;
	    
	    // Calculate the distance to the target position
	    float dx = targetX - x;
	    float dy = targetY - y;

	    // If the distance is less than 1 unit, stop moving and set the entity to idle
	    if (dx * dx + dy * dy < 1f) {
	        movingToTarget = false;
	        setDirection(targetDirection);
	        setIdle();
	        return;
	    }

	    movingToTarget = true;
	    setState(EntityState.WALKING);
	}
	
	private void moveTowardsTarget(float delta, float worldWidth, float worldMinY, float worldMaxY, Array<Entity> entities) {
		float dx = targetX - x;
		float dy = targetY - y;

		// Calculate the distance to the target position
		float distance = (float) Math.sqrt(dx * dx + dy * dy);
		float movement = speed * delta;

		// If the distance to the target is less than or equal to the movement distance, snap to the target position
		if (distance <= movement || distance == 0f) {
			if (canMoveTo(targetX, targetY, entities)) {
				x = targetX;
				y = targetY;

				keepInsideWorldHeight(worldMinY, worldMaxY);

				movingToTarget = false;
				setDirection(targetDirection);
				setIdle();
			}
			
			return;
		}

		float directionX = dx / distance;
		float directionY = dy / distance;

		float nextX = x + directionX * movement;
		float nextY = y + directionY * movement;

		if (canMoveTo(nextX, nextY, entities)) {
		    x = nextX;
		    y = nextY;

		    if (state != EntityState.WALKING) {
		        setState(EntityState.WALKING);
		    }
		} else {
		    setIdle();
		}
		
		// Face whichever direction has the greater movement component.
		if (Math.abs(dx) > Math.abs(dy)) {
		    direction = dx > 0 ? EntityDirection.RIGHT : EntityDirection.LEFT;
		} else {
		    direction = dy > 0 ? EntityDirection.UP : EntityDirection.DOWN;
		}

		keepInsideWorldHeight(worldMinY, worldMaxY);
	}

	private void move(float delta, float worldWidth, float worldMinY, float worldMaxY, Array<Entity> entities) {

		float nextX = x;
		float nextY = y;
		float distance = speed * delta;

		switch (direction) {
			case UP:
				nextY += distance;
				break;
			case DOWN:
				nextY -= distance;
				break;
			case LEFT:
				nextX -= distance;
				break;
			case RIGHT:
				nextX += distance;
				break;
		}

		if (canMoveTo(nextX, nextY, entities)) {
			x = nextX;
			y = nextY;
		} else {
			setIdle();
		}

		keepInsideWorld(worldWidth, worldMinY, worldMaxY);
	}
	
	/**
	 * Checks if the entity can move to the specified position without colliding with other entities.
	 *
	 * @param nextX The x-coordinate of the next position.
	 * @param nextY The y-coordinate of the next position.
	 * @param entities The list of all entities in the game world.
	 * @return true if the entity can move to the specified position, false otherwise.
	 */
	private boolean canMoveTo(float nextX, float nextY, Array<Entity> entities) {
		float centerX = nextX + width / 2f;
		float centerY = nextY + height / 2f;

		for (int i = 0; i < entities.size; i++) {
			Entity other = entities.get(i);
			
			// Don't check against yourself or dead entities.
			if (other == this || !other.isAlive() || !other.hasCollision()) {
				continue;
			}

			float otherCenterX = other.x + other.width / 2f;
			float otherCenterY = other.y + other.height / 2f;

			float dx = centerX - otherCenterX;
			float dy = centerY - otherCenterY;

			float minimumDistance = collisionRadius + other.collisionRadius;
			float minimumDistanceSquared = minimumDistance * minimumDistance;

			// Distance at the current position.
			float currentDX = x + width / 2f - otherCenterX;
			float currentDY = y + height / 2f - otherCenterY;

			float currentDistanceSquared = currentDX * currentDX + currentDY * currentDY;

			// Distance at the proposed position.
			float nextDistanceSquared = dx * dx + dy * dy;

			if (nextDistanceSquared < minimumDistanceSquared && nextDistanceSquared <= currentDistanceSquared) {
				return false;
			}
		}

		return true;
	}
	
	protected void keepInsideWorld(float worldWidth, float worldMinY, float worldMaxY) {		
	    x = Math.max(0f, Math.min(x, worldWidth - width));
	    y = Math.max(worldMinY, Math.min(y, worldMaxY - height));
	}
	
	protected void keepInsideWorldHeight(float worldMinY, float worldMaxY) {		
	    y = Math.max(worldMinY, Math.min(y, worldMaxY - height));
	}
		
	public void moveUp() {
		if (state == EntityState.IDLE || state == EntityState.WALKING) {
			movingToTarget = false;
			setState(EntityState.WALKING);
			direction = EntityDirection.UP;
		}
	}

	public void moveLeft() {
		if (state == EntityState.IDLE || state == EntityState.WALKING) {
			movingToTarget = false;
			setState(EntityState.WALKING);
			direction = EntityDirection.LEFT;
		}
	}

	public void moveDown() {
		if (state == EntityState.IDLE || state == EntityState.WALKING) {
			movingToTarget = false;
			setState(EntityState.WALKING);
			direction = EntityDirection.DOWN;
		}
	}

	public void moveRight() {
		if (state == EntityState.IDLE || state == EntityState.WALKING) {
			movingToTarget = false;
		    setState(EntityState.WALKING);
		    direction = EntityDirection.RIGHT;
		}
	}

	public void setIdle() {
		if (state == EntityState.WALKING) {
			setState(EntityState.IDLE);
		}
	}
	
	public boolean isAlive() {
		return health > 0;
	}
	
	public EntityType getType() {
		return type;
	}
	
	public EntityState getState() {
		return state;
	}
	
	public void setState(EntityState newState) {
	    if (state != newState) {
	        state = newState;
	        stateTime = 0f;
	    }
	}
	
	public EntityDirection getDirection() {
		return direction;
	}
	
	public void setDirection(EntityDirection direction) {
		this.direction = direction;
	}

	public String getName() {
		return name;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public float getHealth() {
		return health;
	}

	public void setHealth(float health) {
		this.health = Math.max(0, Math.min(health, maxHealth));
	}

	public float getMaxHealth() {
		return maxHealth;
	}

	public void setMaxHealth(float maxHealth) {
		this.maxHealth = Math.max(0, maxHealth);
	}

	public int getCost() {
		return cost;
	}
	
	public void setCost(int cost) {
		this.cost = cost;
	}
	
	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
	
	public boolean hasCollision() {
	    return hasCollision;
	}
	
	public float getCollisionRadius() {
	    return collisionRadius;
	}
}

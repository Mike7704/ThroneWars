package com.thronewars.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
    private boolean movingToTarget = false;
    
    protected EntityState state = EntityState.IDLE;
    protected EntityDirection direction = EntityDirection.DOWN;
    protected float stateTime = 0f;
    
    protected Entity(EntityType type, String name, float x, float y, AudioManager audioManager) {
        this.type = type;
        this.name = name;
        this.x = x;
        this.y = y;
        this.audioManager = audioManager;
    }

    public void update(float delta, float worldWidth, float worldMinY, float worldMaxY) {
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
    	else if (state == EntityState.WALKING) {
    	    stateTime += delta;

    	    if (movingToTarget) {
    	        moveTowardsTarget(delta, worldWidth, worldMinY, worldMaxY);
    	    } else {
    	        move(delta, worldWidth, worldMinY, worldMaxY);
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
	
	public void moveTo(float targetX, float targetY) {
	    this.targetX = targetX;
	    this.targetY = targetY;

	    float dx = targetX - x;
	    float dy = targetY - y;

	    if (dx * dx + dy * dy < 1f) {
	        movingToTarget = false;
	        setIdle();
	        return;
	    }

	    movingToTarget = true;
	    setState(EntityState.WALKING);
	}
	
	private void moveTowardsTarget(float delta, float worldWidth, float worldMinY, float worldMaxY) {
		float dx = targetX - x;
		float dy = targetY - y;

		float distance = (float) Math.sqrt(dx * dx + dy * dy);
		float movement = speed * delta;

		if (distance <= movement || distance == 0f) {
		    x = targetX;
		    y = targetY;

		    keepInsideWorld(worldWidth, worldMinY, worldMaxY);

		    movingToTarget = false;
		    setIdle();
		    return;
		}

		float directionX = dx / distance;
		float directionY = dy / distance;

		x += directionX * movement;
		y += directionY * movement;

		// Face whichever direction has the greater movement component.
		if (Math.abs(dx) > Math.abs(dy)) {
			direction = dx > 0 ? EntityDirection.RIGHT : EntityDirection.LEFT;
		} else {
			direction = dy > 0 ? EntityDirection.UP : EntityDirection.DOWN;
		}

		keepInsideWorld(worldWidth, worldMinY, worldMaxY);
	}

	private void move(float delta, float worldWidth, float worldMinY, float worldMaxY) {
	    float distance = speed * delta;

	    switch (direction) {
	        case UP:
	            y += distance;
	            break;
	        case DOWN:
	            y -= distance;
	            break;
	        case LEFT:
	            x -= distance;
	            break;
	        case RIGHT:
	            x += distance;
	            break;
	    }
	    
	    keepInsideWorld(worldWidth, worldMinY, worldMaxY);
	}
	
	protected void keepInsideWorld(float worldWidth, float worldMinY, float worldMaxY) {
	    x = Math.max(0f, Math.min(x, worldWidth - width));
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
}

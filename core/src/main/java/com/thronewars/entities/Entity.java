package com.thronewars.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Represents a generic entity with common properties and behaviours.
 */
public abstract class Entity {
	
	protected final EntityType type;
	protected final String name;
    protected float speed = 1f;
    protected float health = 10f;
    protected float maxHealth = 10f;
	
	protected float x;
    protected float y;
    protected float width = 128f;
    protected float height = 128f;
	
    protected Entity(EntityType type, String name, float x, float y) {
        this.type = type;
        this.name = name;
        this.x = x;
        this.y = y;
    }

	public void update(float delta) {
		
	}

	public abstract void render(SpriteBatch batch);

	public void takeDamage(float damage) {
		if (damage <= 0) {
			return;
		}

		health = Math.max(0, health - damage);
	}

	public boolean isAlive() {
		return health > 0;
	}
	
	public EntityType getType() {
		return type;
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

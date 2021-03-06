package com.ezardlabs.lostsector.objects;

import com.ezardlabs.dethsquare.Vector2;
import com.ezardlabs.dethsquare.networking.NetworkScript;
import com.ezardlabs.lostsector.Game.DamageType;

public abstract class Entity extends NetworkScript {
	protected final float maxHealth;
	protected float health;
	private DamageListener damageListener;

	public Entity(float maxHealth) {
		this.maxHealth = maxHealth;
		health = maxHealth;
	}

	public float getHealth() {
		return health;
	}

	public float getMaxHealth() {
		return maxHealth;
	}

	public boolean addHealth(float health) {
		if (this.health == maxHealth) return false;
		this.health += health;
		if (this.health > maxHealth) {
			this.health = maxHealth;
		}
		return true;
	}

	public void applyDamage(float damage, DamageType damageType, Vector2 attackOrigin) {
		if (damage == 0 || isDead()) return;
		health -= damage;
		if (damageListener != null) {
			damageListener.onDamageReceived(damage, damageType, attackOrigin);
		}
		if (health <= 0) {
			die(damageType, attackOrigin);
		}
	}

	public boolean isDead() {
		return health <= 0;
	}

	protected abstract void die(DamageType damageType, Vector2 attackOrigin);

	public void setDamageListener(DamageListener damageListener) {
		this.damageListener = damageListener;
	}

	protected interface DamageListener {
		void onDamageReceived(float damage, DamageType damageType, Vector2 attackOrigin);
	}
}

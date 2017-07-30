package com.ezardlabs.lostsector.objects;

import com.ezardlabs.dethsquare.Script;
import com.ezardlabs.dethsquare.Vector2;
import com.ezardlabs.lostsector.Game.DamageType;

public abstract class Entity extends Script {
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

	public void addHealth(float health) {
		this.health += health;
		if (this.health > maxHealth) {
			this.health = maxHealth;
		}
	}

	public void applyDamage(float damage, DamageType damageType, Vector2 attackOrigin) {
		if (damage == 0) return;
		health -= damage;
		if (damageListener != null) {
			damageListener.onDamageReceived(damage, damageType, attackOrigin);
		}
		if (health <= 0) {
			die(damageType, attackOrigin);
		}
	}

	protected abstract void die(DamageType damageType, Vector2 attackOrigin);

	public void setDamageListener(DamageListener damageListener) {
		this.damageListener = damageListener;
	}

	protected interface DamageListener {
		void onDamageReceived(float damage, DamageType damageType, Vector2 attackOrigin);
	}
}

package com.ezardlabs.lostsector.objects;

import com.ezardlabs.dethsquare.Script;
import com.ezardlabs.dethsquare.Vector2;
import com.ezardlabs.lostsector.Game.DamageType;

public abstract class Entity extends Script {
	protected final int maxHealth;
	protected int health;
	private DamageListener damageListener;

	public Entity(int maxHealth) {
		this.maxHealth = maxHealth;
		health = maxHealth;
	}

	public int getHealth() {
		return health;
	}

	public void addHealth(int health) {
		this.health += health;
		if (this.health > maxHealth) {
			this.health = maxHealth;
		}
	}

	public void applyDamage(int damage, DamageType damageType, Vector2
			attackOrigin) {
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
		void onDamageReceived(int damage, DamageType damageType,
				Vector2 attackOrigin);
	}
}

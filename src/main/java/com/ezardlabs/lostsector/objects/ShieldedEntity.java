package com.ezardlabs.lostsector.objects;

import com.ezardlabs.dethsquare.Vector2;
import com.ezardlabs.lostsector.Game.DamageType;

public abstract class ShieldedEntity extends Entity {
	protected final int maxShield;
	protected int shield;
	private long shieldRegenTime;
	private long nextShieldRegen;

	public ShieldedEntity(int maxHealth, int maxShield, long shieldRegenTime) {
		super(maxHealth);
		this.maxShield = maxShield;
		shield = maxShield;
		this.shieldRegenTime = shieldRegenTime;
	}

	public int getShield() {
		return shield;
	}

	public int getMaxShield() {
		return maxShield;
	}

	public void addShield(int shield) {
		this.shield += shield;
		if (this.shield > maxShield) {
			this.shield = maxShield;
		}
	}

	@Override
	public void update() {
		if (shield < maxShield && System.currentTimeMillis() > nextShieldRegen) {
			shield++;
			nextShieldRegen = System.currentTimeMillis() + shieldRegenTime;
		}
	}

	@Override
	public void applyDamage(float damage, DamageType damageType, Vector2 attackOrigin) {
		if (damage == 0) return;
		if (shield > 0) {
			shield--;
			super.applyDamage(0, damageType, attackOrigin);
		} else {
			super.applyDamage(damage, damageType, attackOrigin);
		}
		nextShieldRegen = System.currentTimeMillis() + shieldRegenTime;
	}
}

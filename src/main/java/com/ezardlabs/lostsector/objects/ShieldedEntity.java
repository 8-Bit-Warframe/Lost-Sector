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

	public void addShield(int shield) {
		this.shield += shield;
		if (this.shield > maxShield) {
			this.shield = maxShield;
		}
	}

	@Override
	public final void update() {
		if (shield < maxShield && System.currentTimeMillis() > nextShieldRegen) {
			shield++;
			nextShieldRegen += shieldRegenTime;
		}
	}

	@Override
	public void applyDamage(int damage, DamageType damageType,
			Vector2 attackOrigin) {
		if (shield > 0) {
			shield--;
			super.applyDamage(0, damageType, attackOrigin);
		} else {
			super.applyDamage(damage, damageType, attackOrigin);
		}
	}
}

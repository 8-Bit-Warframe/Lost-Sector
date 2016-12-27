package com.ezardlabs.lostsector.objects;

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

	@Override
	public final void update() {
		if (shield < maxShield && System.currentTimeMillis() > nextShieldRegen) {
			shield++;
			nextShieldRegen += shieldRegenTime;
		}
	}
}

package com.ezardlabs.lostsector.ai;

import com.ezardlabs.dethsquare.Transform;
import com.ezardlabs.lostsector.Game.DamageType;
import com.ezardlabs.lostsector.objects.Entity;

public class MeleeBehaviour extends Behaviour {
	private final float meleeRange;
	private final int meleeDamage;

	MeleeBehaviour(float moveSpeed, boolean willPatrol, float visionRange, float meleeRange, int meleeDamage) {
		super(moveSpeed, willPatrol, visionRange);
		this.meleeRange = meleeRange;
		this.meleeDamage = meleeDamage;
	}

	@Override
	protected CombatState onEnemySighted(Transform self, Transform enemy) {
		if (Math.abs(self.position.x - enemy.position.x) <= meleeRange) {
			return CombatState.ATTACKING;
		} else {
			return CombatState.TRACKING;
		}
	}

	@Override
	protected CombatState attack(Transform self, Transform target) {
		if (Math.abs(target.position.y - self.position.y) <= 100 &&
				Math.abs(target.position.x - self.position.x) <= 150) {
			Entity e = target.gameObject.getComponentOfType(Entity.class);
			if (e != null) {
				e.applyDamage(1, DamageType.NORMAL, self.position);
			}
		}
		return null;
	}

	public static class Builder extends Behaviour.Builder<Builder> {
		private float meleeRange;
		private int meleeDamage;

		public Builder setMeleeRange(float meleeRange) {
			this.meleeRange = meleeRange;
			return this;
		}

		public Builder setMeleeDamage(int meleeDamage) {
			this.meleeDamage = meleeDamage;
			return this;
		}

		@Override
		public MeleeBehaviour create() {
			return new MeleeBehaviour(moveSpeed, willPatrol, visionRange, meleeRange, meleeDamage);
		}
	}
}

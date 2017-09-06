package com.ezardlabs.lostsector.ai;

import com.ezardlabs.dethsquare.Transform;
import com.ezardlabs.lostsector.Game.DamageType;
import com.ezardlabs.lostsector.objects.Entity;

public class MeleeBehaviour extends Behaviour {
	private final float meleeRange;
	private final int meleeDamage;
	private final int damageFrame;

	private boolean damageApplied = false;

	MeleeBehaviour(float moveSpeed, boolean willPatrol, float visionRange, float meleeRange, int meleeDamage,
			int damageFrame) {
		super(moveSpeed, willPatrol, visionRange);
		this.meleeRange = meleeRange;
		this.meleeDamage = meleeDamage;
		this.damageFrame = damageFrame;
	}

	@Override
	boolean inRange(Transform self, Transform enemy) {
		return Math.abs(self.position.x - enemy.position.x) <= meleeRange &&
				Math.abs(self.position.y - enemy.position.y) < 200;
	}

	@Override
	protected void attack(Transform self, Transform target) {
		if (Math.abs(target.position.y - self.position.y) <= 100 &&
				Math.abs(target.position.x - self.position.x) <= 150) {
			if (self.gameObject.animator.getCurrentAnimationFrame() == damageFrame) {
				if (!damageApplied) {
					Entity e = target.gameObject.getComponentOfType(Entity.class);
					if (e != null) {
						e.applyDamage(meleeDamage, DamageType.NORMAL, self.position);
						damageApplied = true;
					}
				}
			} else {
				damageApplied = false;
			}
		}
	}

	public static class Builder extends Behaviour.Builder<Builder> {
		private float meleeRange;
		private int meleeDamage;
		private int damageFrame;

		public Builder setMeleeRange(float meleeRange) {
			this.meleeRange = meleeRange;
			return this;
		}

		public Builder setMeleeDamage(int meleeDamage) {
			this.meleeDamage = meleeDamage;
			return this;
		}

		public Builder setDamageFrame(int damageFrame) {
			this.damageFrame = damageFrame;
			return this;
		}

		@Override
		public MeleeBehaviour create() {
			return new MeleeBehaviour(moveSpeed, willPatrol, visionRange, meleeRange, meleeDamage, damageFrame);
		}
	}
}

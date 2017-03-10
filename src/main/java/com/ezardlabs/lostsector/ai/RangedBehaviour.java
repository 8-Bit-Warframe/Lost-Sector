package com.ezardlabs.lostsector.ai;

import com.ezardlabs.dethsquare.Transform;

public class RangedBehaviour extends Behaviour {
	private final float range;

	RangedBehaviour(float moveSpeed, boolean willPatrol, float visionRange, float range) {
		super(moveSpeed, willPatrol, visionRange);
		this.range = range;
	}

	@Override
	protected CombatState onEnemySighted(Transform self, Transform enemy) {
		if (Math.abs(self.position.x - enemy.position.x) <= range) {
			return CombatState.ATTACKING;
		} else {
			return CombatState.TRACKING;
		}
	}

	@Override
	protected CombatState attack(Transform target) {
		return null;
	}

	public static class Builder extends Behaviour.Builder<Builder> {
		private float range = 1500;

		public Builder setRange(float range) {
			this.range = range;
			return this;
		}

		@Override
		public RangedBehaviour create() {
			return new RangedBehaviour(moveSpeed, willPatrol, visionRange, range);
		}
	}
}

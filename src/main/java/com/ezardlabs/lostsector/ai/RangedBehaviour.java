package com.ezardlabs.lostsector.ai;

import com.ezardlabs.dethsquare.Transform;
import com.ezardlabs.lostsector.ai.RangedBehaviour.Builder.ShootAction;

public class RangedBehaviour extends Behaviour {
	private final float range;
	private final ShootAction shootAction;

	RangedBehaviour(float moveSpeed, boolean willPatrol, float visionRange, float range, ShootAction shootAction) {
		super(moveSpeed, willPatrol, visionRange);
		this.range = range;
		this.shootAction = shootAction;
	}

	@Override
	boolean inRange(Transform self, Transform enemy) {
		return Math.abs(self.position.x - enemy.position.x) <= range &&
				Math.abs(self.position.y - enemy.position.y) < 200;
	}

	@Override
	protected void attack(Transform self, Transform target) {
		shootAction.onShoot(self, target);
	}

	public static class Builder extends Behaviour.Builder<Builder> {
		private float range = 1500;
		private ShootAction shootAction;

		public Builder setRange(float range) {
			this.range = range;
			return this;
		}

		public Builder setShootAction(ShootAction shootAction) {
			this.shootAction = shootAction;
			return this;
		}

		@Override
		public RangedBehaviour create() {
			return new RangedBehaviour(moveSpeed, willPatrol, visionRange, range, shootAction);
		}

		public interface ShootAction {

			void onShoot(Transform self, Transform target);
		}
	}
}

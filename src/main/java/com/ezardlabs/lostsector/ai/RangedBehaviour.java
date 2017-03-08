package com.ezardlabs.lostsector.ai;

public class RangedBehaviour extends Behaviour {
	private final float range;

	RangedBehaviour(float moveSpeed, boolean willPatrol, float visionRange, float range) {
		super(moveSpeed, willPatrol, visionRange);
		this.range = range;
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

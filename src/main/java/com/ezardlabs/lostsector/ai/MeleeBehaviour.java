package com.ezardlabs.lostsector.ai;

public class MeleeBehaviour extends Behaviour {
	private final float meleeRange;

	MeleeBehaviour(float moveSpeed, boolean willPatrol, float visionRange, float meleeRange) {
		super(moveSpeed, willPatrol, visionRange);
		this.meleeRange = meleeRange;
	}

	protected abstract static class Builder extends Behaviour.Builder<Builder> {
		private float meleeRange;

		public Builder setMeleeRange(float meleeRange) {
			this.meleeRange = meleeRange;
			return this;
		}

		@Override
		public MeleeBehaviour create() {
			return new MeleeBehaviour(moveSpeed, willPatrol, visionRange, meleeRange);
		}
	}
}

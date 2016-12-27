package com.ezardlabs.lostsector.ai;

public class MeleeBehaviour extends Behaviour {
	private final float meleeRange;

	MeleeBehaviour(float moveSpeed, boolean willPatrol, float visionRange,
			float meleeRange) {
		super(moveSpeed, willPatrol, visionRange);
		this.meleeRange = meleeRange;
	}
}

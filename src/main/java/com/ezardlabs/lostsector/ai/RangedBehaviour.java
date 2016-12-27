package com.ezardlabs.lostsector.ai;

public class RangedBehaviour extends Behaviour {
	private final float range;

	RangedBehaviour(float moveSpeed, boolean willPatrol, float visionRange,
			float range) {
		super(moveSpeed, willPatrol, visionRange);
		this.range = range;
	}
}

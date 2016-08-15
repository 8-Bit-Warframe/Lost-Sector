package com.ezardlabs.lostsector.ai;

public class RangedBehaviour extends Behaviour {
	private final int range;

	public RangedBehaviour(float moveSpeed, int range) {
		super(moveSpeed);
		this.range = range;
	}

	@Override
	public void update() {
		super.update();

	}
}

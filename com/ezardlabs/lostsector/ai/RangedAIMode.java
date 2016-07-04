package com.ezardlabs.lostsector.ai;

public class RangedAIMode extends AIMode {
	private final int range;

	public RangedAIMode(float moveSpeed, int range) {
		super(moveSpeed);
		this.range = range;
	}

	@Override
	public void update() {
		super.update();

	}
}

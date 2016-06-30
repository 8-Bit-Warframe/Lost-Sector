package com.ezardlabs.lostsector.ai;

public class MeleeAI extends BaseAI {

	public MeleeAI(float moveSpeed) {
		super(moveSpeed);
	}

	@Override
	public void start() {
		super.start();
	}

	@Override
	public void update() {
		super.update();
		gameObject.animator.play("idle");
		if (target != null) {
			if (transform.position.y == target.position.y && Math.abs(transform.position.x - target.position.x) <= (gameObject.renderer.hFlipped ? 100 : 200)) {
//				gameObject.animator.play("attack");
				if (target.position.x < transform.position.x) {
					gameObject.renderer.hFlipped = true;
				} else if (target.position.x > transform.position.x) {
					gameObject.renderer.hFlipped = false;
				}
			} else {
				move();
			}
		}
	}
}

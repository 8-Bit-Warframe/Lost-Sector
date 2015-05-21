package com.ezardlabs.lostsector.objects;

import com.ezardlabs.dethsquare.Animator;
import com.ezardlabs.dethsquare.Script;

public class Door extends Script {
	private Animator animator;

	@Override
	public void start() {
		animator = getComponent(Animator.class);
	}

	@Override
	public void update() {
		if (Player.player.collider.bounds.bottom > gameObject.collider.bounds.top &&
				Player.player.collider.bounds.top < gameObject.collider.bounds.bottom &&
				Math.abs(transform.position.x + 100 - Player.player.transform.position.x) < 600) {
			animator.play("open");
		} else {
			animator.play("close");
		}
	}
}

package com.ezardlabs.lostsector.objects.environment;

import com.ezardlabs.dethsquare.Script;
import com.ezardlabs.lostsector.objects.Player;

public class Door extends Script {

	@Override
	public void start() {
	}

	@Override
	public void update() {
		if (Player.player.collider.bounds.bottom > gameObject.collider.bounds.top &&
				Player.player.collider.bounds.top < gameObject.collider.bounds.bottom &&
				Math.abs(transform.position.x + 100 - Player.player.transform.position.x) < 600) {
			gameObject.animator.play("open");
		} else {
			gameObject.animator.play("close");
		}
	}
}

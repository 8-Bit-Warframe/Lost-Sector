package com.ezardlabs.lostsector.objects.enemies.corpus;

import com.ezardlabs.dethsquare.Animator;
import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.Vector2;
import com.ezardlabs.lostsector.Game;
import com.ezardlabs.lostsector.objects.Avatar;

public class ShockwaveMoa extends Avatar {
	private Animator animator;

	public ShockwaveMoa() {
		super(3);
	}

	@Override
	public void start() {
		animator = gameObject.getComponent(Animator.class);
	}

	@Override
	public void update() {
		for (GameObject player : Game.players) {
			if (Vector2.distance(transform.position, player.transform.position) < 300) {
				animator.play("stomp");
			}
		}
	}
}

package com.ezardlabs.lostsector.objects.enemies.corpus;

import com.ezardlabs.dethsquare.Vector2;
import com.ezardlabs.lostsector.Game;
import com.ezardlabs.lostsector.NavMesh;
import com.ezardlabs.lostsector.objects.enemies.Enemy;

public class Crewman extends Enemy {
	Vector2 target;

	public Crewman(String name) {
		super("corpus/crewmen/" + name, 3);
	}

	@Override
	public void update() {
		int x = 0;
		if (gameObject.rigidbody.gravity == 0) {
			if (target == null || transform.position.x == target.x) {
				NavMesh.NavPoint[] path = NavMesh.getPath(transform, Game.players[0].transform);
				if (path != null && path.length > 0) {
					int pathIndex = 0;
					if (path.length > 1 && path[0] != null && transform.position.x == path[0].position.x)
						pathIndex = 1;
					target = path[pathIndex].position;
				}
			}
			if (target != null) {
				if (target.x < transform.position.x) {
					x = -10;
					gameObject.renderer.hFlipped = true;
				} else if (target.x > transform.position.x) {
					x = 10;
					gameObject.renderer.hFlipped = false;
				}
			}
			transform.translate(x, 0);
			if (x == 0) {
				gameObject.animator.play("idle");
			} else {
				gameObject.animator.play("run");
			}
		}
	}
}

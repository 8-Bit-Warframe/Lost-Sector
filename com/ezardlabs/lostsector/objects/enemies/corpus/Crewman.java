package com.ezardlabs.lostsector.objects.enemies.corpus;

import com.ezardlabs.lostsector.objects.enemies.Enemy;

public class Crewman extends Enemy {

	public Crewman(String name) {
		super("corpus/crewmen/" + name, 3);
	}

	@Override
	public void update() {
		super.update();
//		int x = 0;
//		if (gameObject.rigidbody.gravity == 0) {
//			NavMesh.NavPoint[] path = NavMesh.getPath(transform, Game.players[0].transform);
//			if (path != null && path.length > 0) {
//				int pathIndex = 0;
//				if (path.length > 1 && path[0] != null && transform.position.x == path[0].position.x) pathIndex = 1;
//				if (path[pathIndex].position.x < transform.position.x) {
//					x = -10;
//					gameObject.renderer.hFlipped = true;
//				} else if (path[pathIndex].position.x > transform.position.x) {
//					x = 10;
//					gameObject.renderer.hFlipped = false;
//				}
//			}
//		}
//		transform.translate(x, 0);
//		if (x == 0) {
//			animator.play("idle");
//		} else {
//			animator.play("run");
//		}
	}
}

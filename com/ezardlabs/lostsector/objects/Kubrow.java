package com.ezardlabs.lostsector.objects;

import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.Vector2;
import com.ezardlabs.lostsector.Game;
import com.ezardlabs.lostsector.NavMesh;
import com.ezardlabs.lostsector.objects.enemies.Enemy;

public class Kubrow extends Avatar {
	private float speed = 10;
	public boolean attacking = false;
	TargetType targetType = TargetType.PLAYER;

	enum TargetType {
		PLAYER,
		ENEMY
	}

	public Kubrow() {
		super(3);
	}

	@Override
	public void update() {
		if (attacking) return;

		float x = 0;

		double minDistance = 1000;
		GameObject best = null;
		double temp;
		for (GameObject go : GameObject.findAllWithTag("enemy")) {
			if ((temp = Vector2.distance(transform.position, go.transform.position)) < minDistance) {
				minDistance = temp;
				best = go;
			}
		}
		if (best == null) {
			targetType = TargetType.PLAYER;
		} else {
			targetType = TargetType.ENEMY;
		}
		switch (targetType) {
			case PLAYER:
				break;
			case ENEMY:
				break;
		}
		if (best == null) {
			if (gameObject.rigidbody.gravity == 0) {
				NavMesh.NavPoint[] path = NavMesh.getPath(transform, Game.players[0].transform);
				if (path != null && path.length > 1) {
					int pathIndex = 0;
					if (path.length > 1 && path[0] != null && transform.position.x == path[0].position.x) pathIndex = 1;
					if (path[pathIndex].position.x < transform.position.x) {
						x = -speed;
						gameObject.renderer.hFlipped = true;
					} else if (path[pathIndex].position.x > transform.position.x) {
						x = speed;
						gameObject.renderer.hFlipped = false;
					}
				}
			}
		} else {
			if (gameObject.rigidbody.gravity == 0) {
				if (best.transform.position.x - transform.position.x <= 106.25f) { // 125
					transform.position.x = best.transform.position.x - 106.25f;
					gameObject.animator.play("attack");
					attacking = true;
					//noinspection ConstantConditions
					((Enemy) best.getComponentOfType(Enemy.class)).kubrowAttack(transform.position);
					return;
				} else {
					NavMesh.NavPoint[] path = NavMesh.getPath(transform, best.transform);
					if (path != null && path.length > 0) {
						int pathIndex = 0;
						if (path.length > 1 && path[0] != null && transform.position.x == path[0].position.x) pathIndex = 1;
						if (path[pathIndex].position.x < transform.position.x) {
							x = -speed;
							gameObject.renderer.hFlipped = true;
						} else if (path[pathIndex].position.x > transform.position.x) {
							x = speed;
							gameObject.renderer.hFlipped = false;
						}
					}
				}
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
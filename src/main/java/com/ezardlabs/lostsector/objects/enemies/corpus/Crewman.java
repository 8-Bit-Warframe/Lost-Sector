package com.ezardlabs.lostsector.objects.enemies.corpus;

import com.ezardlabs.dethsquare.Collider;
import com.ezardlabs.dethsquare.Collider.Collision;
import com.ezardlabs.dethsquare.Vector2;
import com.ezardlabs.lostsector.Game;
import com.ezardlabs.lostsector.NavMesh;
import com.ezardlabs.lostsector.objects.enemies.Enemy;

public abstract class Crewman extends Enemy {
	Vector2 target;
	boolean shooting = false;

	public Crewman() {
		super(3);
	}

	@Override
	public void update() {
		if (dead || frozen) return;
		int x = 0;
		if (!landing && gameObject.rigidbody.velocity.y == 0) {
			if (Game.players.length > 0 && transform.position.y == Game.players[0].transform.position.y && Math.abs(transform.position.x - Game.players[0].transform.position.x) < 1500) {
				shooting = true;
				gameObject.animator.play("shoot");
				if (Game.players[0].transform.position.x < transform.position.x) {
					gameObject.renderer.hFlipped = true;
				} else if (Game.players[0].transform.position.x > transform.position.x) {
					gameObject.renderer.hFlipped = false;
				}
				return;
			} else {
				if ((target == null || transform.position.x == target.x) && Game.players.length > 0) {
					NavMesh.NavPoint[] path = NavMesh.getPath(transform, Game.players[0].transform);
					if (path != null && path.length > 0) {
						int pathIndex = 0;
						if (path.length > 1 && path[0] != null && transform.position.x == path[0].position.x) pathIndex = 1;
						target = path[pathIndex].position;
					}
				}
				if (target != null) {
					if (target.x < transform.position.x) {
						gameObject.renderer.hFlipped = true;
					} else if (target.x > transform.position.x) {
						gameObject.renderer.hFlipped = false;
					}
					if (target.y - 100 < transform.position.y) {
						if (gameObject.rigidbody.velocity.y >= 0) {
							gameObject.rigidbody.velocity.y = -30f;
						}
					} else {
						if (target.x < transform.position.x) {
							x = -10;
						} else if (target.x > transform.position.x) {
							x = 10;
						}
					}
				}
			}
		}
		transform.translate(x, 0);
		if (landing) {
			gameObject.animator.play("land");
		} else if (gameObject.rigidbody.velocity.y > 0) {
			gameObject.animator.play("fall");
		} else if (x != 0) {
			gameObject.animator.play("run");
		} else {
			gameObject.animator.play("idle");
		}
	}

	@Override
	public void onCollision(Collision collision) {
		if (collision.speed > 37 && collision.location == Collider.CollisionLocation.BOTTOM) {
			gameObject.animator.play("land");
			landing = true;
		}
	}
}
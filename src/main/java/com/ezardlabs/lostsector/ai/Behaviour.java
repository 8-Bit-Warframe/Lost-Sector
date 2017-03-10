package com.ezardlabs.lostsector.ai;

import com.ezardlabs.dethsquare.Collider;
import com.ezardlabs.dethsquare.Collider.Collision;
import com.ezardlabs.dethsquare.Physics;
import com.ezardlabs.dethsquare.Physics.RaycastHit;
import com.ezardlabs.dethsquare.Transform;
import com.ezardlabs.dethsquare.Vector2;
import com.ezardlabs.lostsector.NavMesh;
import com.ezardlabs.lostsector.NavMesh.NavPoint;

public abstract class Behaviour {
	private float moveSpeed;
	private final boolean willPatrol;
	private final float visionRange;
	private State state = State.IDLE;
	private CombatState combatState = CombatState.IDLE;
	private Transform target = null;
	private NavPoint[] path = null;
	private int pathIndex = 0;

	public enum State {
		IDLE,
		MOVING,
		JUMPING,
		FALLING,
		LANDING,
		FROZEN
	}

	protected enum CombatState {
		IDLE,
		PATROLLING,
		TRACKING,
		SEARCHING,
		ATTACKING
	}

	Behaviour(float moveSpeed, boolean willPatrol, float visionRange) {
		this.moveSpeed = moveSpeed;
		this.willPatrol = willPatrol;
		this.visionRange = visionRange;
	}

	public final void update(Transform transform) {
		Transform sightedEnemy = visionCheck(transform);
		if (sightedEnemy != null) {
			combatState = onEnemySighted(transform, sightedEnemy);
			switch (combatState) {
				case IDLE:
				case PATROLLING:
					target = null;
					break;
				case TRACKING:
				case SEARCHING:
				case ATTACKING:
					target = sightedEnemy;
					break;
			}
		}
		switch (combatState) {
			case IDLE:
				break;
			case PATROLLING:
				// TODO implement patrolling
				break;
			case TRACKING:
				// TODO only update path if target has moved to a new NavPoint
				path = NavMesh.getPath(transform, target);
				pathIndex = 0;
				break;
			case SEARCHING:
				break;
			case ATTACKING:
				break;
		}
		move(transform);
	}

	private void move(Transform transform) {
		if (path != null && path.length > 0 && pathIndex < path.length) {
			System.out.println(transform.position.x + ", " + NavMesh.getNavPoint(transform.position).position.x);
			if (path[pathIndex].position.x == transform.position.x) {
				pathIndex++;
			}
			if (path.length > pathIndex + 2) {
				if (path[pathIndex + 1].position.x < path[pathIndex].position.x &&
						transform.position.x < path[pathIndex].position.x) {
					pathIndex++;
				}
				if (path[pathIndex + 1].position.x > path[pathIndex].position.x &&
						transform.position.x > path[pathIndex].position.x) {
					pathIndex++;
				}
			}
			if (pathIndex >= path.length) {
				return;
			}
			if (path[pathIndex].position.x < transform.position.x) {
				transform.scale.x = -1;
			} else {
				transform.scale.x = 1;
			}
			if (path[pathIndex].position.y < transform.position.y && transform.gameObject.rigidbody.velocity.y >= 0) {
				jump(transform);
			}
			if (path[pathIndex].position.x > transform.position.x) {
				transform.translate(moveSpeed, 0);
				state = State.MOVING;
				if (path[pathIndex].position.x <= transform.position.x) {
					pathIndex++;
				}
			} else if (path[pathIndex].position.x < transform.position.x) {
				transform.translate(-moveSpeed, 0);
				state = State.MOVING;
				if (path[pathIndex].position.x >= transform.position.x) {
					pathIndex++;
				}
			}
		} else {
			state = State.IDLE;
		}
	}

	private void jump(Transform transform) {
		transform.gameObject.rigidbody.velocity.y = -30;
		state = State.JUMPING;
	}

	public State getState() {
		return state;
	}

	public void onCollision(Collision collision) {
		if (collision.speed > 37 && collision.location == Collider.CollisionLocation.BOTTOM) {
			state = State.LANDING;
		}
	}

	protected abstract CombatState onEnemySighted(Transform self, Transform enemy);

	protected abstract CombatState attack(Transform self, Transform target);

	private Transform visionCheck(Transform transform) {
		RaycastHit hit = Physics.raycast(transform.position.offset(transform.scale.x > 0 ? 200 : 0, 100),
				new Vector2(transform.scale.x, 0), visionRange, "player");
		if (hit == null) {
			return null;
		} else {
			return hit.transform;
		}
	}

	@SuppressWarnings("unchecked")
	protected abstract static class Builder<T extends Builder> {
		float moveSpeed = 10;
		float visionRange = 2000;
		boolean willPatrol = false;

		public T setMoveSpeed(float moveSpeed) {
			this.moveSpeed = moveSpeed;
			return (T) this;
		}

		public T setWillPatrol(boolean willPatrol) {
			this.willPatrol = willPatrol;
			return (T) this;
		}

		public T setVisionRange(float visionRange) {
			this.visionRange = visionRange;
			return (T) this;
		}

		public abstract Behaviour create();
	}
}

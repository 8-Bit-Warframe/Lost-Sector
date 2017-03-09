package com.ezardlabs.lostsector.ai;

import com.ezardlabs.dethsquare.Collider;
import com.ezardlabs.dethsquare.Collider.Collision;
import com.ezardlabs.dethsquare.Physics;
import com.ezardlabs.dethsquare.Physics.RaycastHit;
import com.ezardlabs.dethsquare.Transform;
import com.ezardlabs.dethsquare.Vector2;

public abstract class Behaviour {
	private float moveSpeed;
	private final boolean willPatrol;
	private final float visionRange;
	private State state = State.IDLE;
	private CombatState combatState = CombatState.IDLE;

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
			onEnemySighted(transform, sightedEnemy);
		}
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

	protected Transform visionCheck(Transform transform) {
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

/*	private final float moveSpeed;
	private NavPoint lastTargetNavPoint;
	private ArrayList<NavPoint> path;
	protected Transform target;
	private GameObject demo;

	public Behaviour(float moveSpeed) {
		this.moveSpeed = moveSpeed;
		demo = new GameObject("Demo", new Renderer("images/pink.png", 10, 10));
	}

	@Override
	public void start() {
		GameObject.instantiate(demo, new Vector2());
	}

	public void update() {
		NavPoint temp;
		if (target != null && (temp = NavMesh.getNavPoint(target.position)) != lastTargetNavPoint) {
			if (lastTargetNavPoint != null) {
				if (path != null) {
					path.addAll(Arrays.asList(NavMesh.getPath(lastTargetNavPoint.position, temp.position)));
				} else {
					path = new ArrayList<>(Arrays.asList(NavMesh.getPath(lastTargetNavPoint.position, temp.position)));
				}
			}
			lastTargetNavPoint = temp;
		}
	}

	public Transform getTarget() {
		return target;
	}

	public final void setTarget(Transform target) {
		this.target = target;
		setPath(NavMesh.getPath(transform, target));
	}

	public final void setTarget(Vector2 target) {
		setPath(NavMesh.getPath(transform.position, target));
	}

	private void setPath(NavPoint[] path) {
		this.path = new ArrayList<>(Arrays.asList(path));
	}

	protected final void move() {
		if (path.size() > 0) {
			Vector2 target = path.get(0).position;
			if (target.x == transform.position.x) {
				path.remove(0);
				if (path.size() == 0) {
					return;
				}
				target = path.get(0).position;
			}
			demo.transform.position = target;

			if (target.x < transform.position.x) {
				gameObject.renderer.hFlipped = true;
			} else if (target.x > transform.position.x) {
				gameObject.renderer.hFlipped = false;
			}

			if (target.y < transform.position.y && gameObject.rigidbody.velocity.y >= 0) {
				gameObject.rigidbody.velocity.y = -30f;
			}

//			System.out.println("Before: " + transform.position + " -> " + target);

			if (target.x > transform.position.x) {
				transform.translate(moveSpeed, 0);
				if (target.x <= transform.position.x) {
					path.remove(0);
				}
			} else {
				transform.translate(-moveSpeed, 0);
				if (target.x >= transform.position.x) {
					path.remove(0);
				}
			}

//			System.out.println("After: " + transform.position + " -> " + target);
		}
	}*/
}

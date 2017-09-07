package com.ezardlabs.lostsector.ai;

import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.Level;
import com.ezardlabs.dethsquare.LevelManager;
import com.ezardlabs.dethsquare.Mathf;
import com.ezardlabs.dethsquare.Physics;
import com.ezardlabs.dethsquare.Physics.RaycastHit;
import com.ezardlabs.dethsquare.Transform;
import com.ezardlabs.dethsquare.Vector2;
import com.ezardlabs.lostsector.Game.DamageType;
import com.ezardlabs.lostsector.NavMesh;
import com.ezardlabs.lostsector.NavMesh.NavPoint;
import com.ezardlabs.lostsector.ai.StateMachine.Transition;
import com.ezardlabs.lostsector.levels.MissionLevel;

public abstract class Behaviour {
	private float moveSpeed;
	private final boolean willPatrol;
	private final float visionRange;
	private NavPoint[] path = null;
	private int pathIndex = 0;
	private NavPoint currentNavPoint = null;
	private NavPoint currentTargetNavPoint = null;
	private long freezeTime = 2000;
	private long freezeStart;
	private float[] freezeTint = {
			0,
			0.3f,
			0.6f
	};
	private long thawTime = 1000;
	private long thawStart = 0;
	private int directionToLook = 0;

	private StateMachine<State> stateMachine = new StateMachine<>();
	private Transform transform;
	private Transform target;
	private GameObject[] objectives;
	private AnimationState animationState = AnimationState.IDLE;

	public enum State {
		IDLE,
		TRACKING,
		ATTACKING,
		FROZEN,
		THAWING,
		SHATTER,
		DEAD
	}

	public enum AnimationState {
		IDLE,
		MOVING,
		JUMPING,
		FALLING,
		ATTACKING
	}

	Behaviour(float moveSpeed, boolean willPatrol, float visionRange) {
		this.moveSpeed = moveSpeed;
		this.willPatrol = willPatrol;
		this.visionRange = visionRange;

		stateMachine.addState(State.IDLE,
				new Transition<>(State.TRACKING, () -> canSeeEnemy() && !inRange(transform, target)),
				new Transition<>(State.ATTACKING, () -> canSeeEnemy() && inRange(transform, target)));

		stateMachine.addState(State.TRACKING,
				new Transition<>(State.ATTACKING, () -> inRange(transform, target)));

		stateMachine.addState(State.ATTACKING,
				new Transition<>(State.TRACKING, () -> !inRange(transform, target)));

		stateMachine.addState(State.FROZEN,
				new Transition<>(State.THAWING, () -> System.currentTimeMillis() - freezeStart > freezeTime, () -> {
					freezeStart = 0;
					thawStart = System.currentTimeMillis();
				}));

		stateMachine.addState(State.THAWING,
				new Transition<>(State.IDLE, () -> System.currentTimeMillis() - thawStart > thawTime,
						() -> {
							transform.gameObject.renderer.setTint(0, 0, 0);
							transform.gameObject.animator.shouldUpdate = true;
						}));

		stateMachine.addTransitionFromAnyState(new Transition<>(State.FROZEN, () -> freezeStart > 0, () -> {
			transform.gameObject.renderer.setTint(freezeTint[0], freezeTint[1], freezeTint[2]);
			transform.gameObject.animator.shouldUpdate = false;
		}));

		stateMachine.init(State.IDLE);
	}

	public void init(Transform transform) {
		this.transform = transform;
		Level level = LevelManager.getCurrentLevel();
		if (level instanceof MissionLevel) {
			objectives = ((MissionLevel) level).getObjectiveTargets();
		}
	}

	public final void update() {
		stateMachine.update();
		if (stateMachine.getState() == State.IDLE) {
			visionCheck();
			if (target == null && objectives != null) {
				target = getObjectiveTarget();
			}
		}
		switch (stateMachine.getState()) {
			case IDLE:
				animationState = AnimationState.IDLE;
				break;
			case TRACKING:
				animationState = move();
				break;
			case ATTACKING:
				attack(transform, target);
				animationState = AnimationState.ATTACKING;
				break;
			case FROZEN:
				break;
			case THAWING:
				float ratio = (float) (System.currentTimeMillis() - thawStart) / (float) thawTime;
				transform.gameObject.renderer.setTint(freezeTint[0] - ratio * freezeTint[0],
						freezeTint[1] - ratio * freezeTint[1], freezeTint[2] - ratio * freezeTint[2]);
				break;
			case SHATTER:
				break;
			case DEAD:
				break;
			default:
				break;
		}
		if (stateMachine.getState() != State.FROZEN && stateMachine.getState() != State.THAWING &&
				directionToLook != 0) {
			transform.scale.x = directionToLook;
			directionToLook = 0;
		}
	}

	private NavPoint[] getPath() {
		NavPoint self = NavMesh.getNavPoint(transform.position.x, transform.position.y);
		NavPoint targetNavPoint = NavMesh.getNavPoint(target.position);
		if ((!self.equals(currentNavPoint) || !targetNavPoint.equals(currentTargetNavPoint)) && !self.links.isEmpty()) {
			currentNavPoint = self;
			currentTargetNavPoint = targetNavPoint;
			path = NavMesh.getPath(currentNavPoint, currentTargetNavPoint);
			pathIndex = 0;
		}
		return path;
	}

	private AnimationState move() {
		path = getPath();
		if (path != null && path.length > 0) {
			if (pathIndex < path.length - 1) {
				float direction = Mathf.clamp(path[pathIndex + 1].position.x - path[pathIndex].position.x, -1, 1);
				transform.translate(moveSpeed * direction, 0);
				transform.scale.x = direction;

				if (transform.gameObject.rigidbody.velocity.y > transform.gameObject.rigidbody.gravity) {
					return AnimationState.FALLING;
				} else if (path[pathIndex + 1].position.y < path[pathIndex].position.y) {
					if (animationState != AnimationState.JUMPING) {
						jump(Math.abs(path[pathIndex + 1].position.y - path[pathIndex].position.y));
					}
					return AnimationState.JUMPING;
				} else if (direction == 0) {
					return AnimationState.IDLE;
				} else {
					return AnimationState.MOVING;
				}
			}
		}
		return AnimationState.IDLE;
	}

	private void jump(float distance) {
		transform.gameObject.rigidbody.velocity.y = (float) -Math.sqrt(2.5 * distance) - 2;
	}

	public State getState() {
		return stateMachine.getState();
	}

	public AnimationState getAnimationState() {
		return animationState;
	}

	private boolean canSeeEnemy() {
		return target != null;
	}

	abstract boolean inRange(Transform self, Transform enemy);

	protected abstract void attack(Transform self, Transform target);

	private void visionCheck() {
		RaycastHit hit = Physics.raycast(transform.position.offset(transform.scale.x > 0 ? 200 : 0, 100),
				new Vector2(transform.scale.x, 0), visionRange, "player", "cryopod");
		if (hit == null) {
			target = null;
		} else {
			target = hit.transform;
		}
	}

	private Transform getObjectiveTarget() {
		int nonNullObjectives = 0;
		for (GameObject objective : objectives) {
			if (objective != null) {
				nonNullObjectives++;
			}
		}
		if (nonNullObjectives > 0) {
			int rand = (int) (Math.random() * nonNullObjectives);
			for (GameObject objective : objectives) {
				if (objective != null) {
					if (rand == 0) {
						return objective.transform;
					}
					rand--;
				}
			}
		}
		return null;
	}

	public void onDamageReceived(DamageType damageType, Transform self, Vector2 attackOrigin) {
		switch (damageType) {
			case NORMAL:
				break;
			case SLASH:
				break;
			case COLD:
				freezeStart = System.currentTimeMillis();
				break;
			case KUBROW:
				break;
			default:
				break;
		}
		if (attackOrigin.x > self.position.x) {
			directionToLook = 1;
		} else if (attackOrigin.x < self.position.x) {
			directionToLook = -1;
		}
	}

	public void die() {
		System.out.println("died");
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

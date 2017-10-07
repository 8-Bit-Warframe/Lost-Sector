package com.ezardlabs.lostsector.objects;

import com.ezardlabs.dethsquare.Camera;
import com.ezardlabs.dethsquare.Collider.Collision;
import com.ezardlabs.dethsquare.Collider.CollisionLocation;
import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.Input;
import com.ezardlabs.dethsquare.Input.KeyCode;
import com.ezardlabs.dethsquare.Layers;
import com.ezardlabs.dethsquare.Script;
import com.ezardlabs.dethsquare.StateMachine;
import com.ezardlabs.dethsquare.StateMachine.Transition;
import com.ezardlabs.dethsquare.Time;
import com.ezardlabs.dethsquare.Vector2;
import com.ezardlabs.dethsquare.animation.Animator;
import com.ezardlabs.dethsquare.graphics.Renderer;
import com.ezardlabs.lostsector.camera.SmartCamera;
import com.ezardlabs.lostsector.effects.Dust;
import com.ezardlabs.lostsector.objects.warframes.Warframe;
import com.ezardlabs.lostsector.objects.weapons.primary.Gorgon;
import com.ezardlabs.lostsector.objects.weapons.primary.Lanka;
import com.ezardlabs.lostsector.objects.weapons.primary.Supra;

public class Player extends Script {
	public int jumpCount = 0;
	private float speed = 12.5f;
	private Warframe warframe;

	private StateMachine<State> stateMachine = new StateMachine<>();

	public enum State {
		IDLE,
		RUNNING,
		JUMPING,
		DOUBLE_JUMPING,
		FALLING,
		LANDING,
		MELEE,
		MELEE_WAITING,
		MELEE_STOW,
		SHOOTING,
		CASTING
	}

	public Player() {
		Transition<State> idle = new Transition<>(State.IDLE, this::idleCheck);
		Transition<State> running = new Transition<>(State.RUNNING, this::runCheck);
		Transition<State> jumping = new Transition<>(State.JUMPING, this::jumpCheck, this::jump);
		Transition<State> doubleJumping = new Transition<>(State.DOUBLE_JUMPING, this::doubleJumpCheck, this::jump);
		Transition<State> falling = new Transition<>(State.FALLING, this::fallCheck);
		Transition<State> melee = new Transition<>(State.MELEE, this::meleeCheck);
		Transition<State> meleeWaiting = new Transition<>(State.MELEE_WAITING,
				() -> warframe.getMeleeWeapon().isWaiting());
		Transition<State> meleeStow = new Transition<>(State.MELEE_STOW, () -> warframe.getMeleeWeapon().shouldStow());
		Transition<State> shooting = new Transition<>(State.SHOOTING, this::shootCheck);

		Transition<State> meleeJumping = new Transition<>(State.JUMPING, this::jumpCheck, () -> {
			jump();
			warframe.meleeWeapon.reset();
			gameObject.rigidbody.velocity.x = 0;
		});
		Transition<State> meleeFalling = new Transition<>(State.FALLING, this::fallCheck, () -> {
			warframe.meleeWeapon.reset();
			gameObject.rigidbody.velocity.x = 0;
		});

		stateMachine.addState(State.IDLE, jumping, falling, melee, shooting, running);
		stateMachine.addState(State.RUNNING, jumping, falling, melee, shooting, idle);
		stateMachine.addState(State.JUMPING, doubleJumping, falling);
		stateMachine.addState(State.DOUBLE_JUMPING, falling);
		stateMachine.addState(State.FALLING, jumping, doubleJumping);
		stateMachine.addState(State.LANDING, new Transition<>(State.IDLE, () -> gameObject.animator.isFinished()));
		stateMachine.addState(State.MELEE, meleeFalling, meleeJumping, meleeWaiting);
		stateMachine.addState(State.MELEE_WAITING, meleeFalling, meleeJumping, melee, meleeStow);
		stateMachine.addState(State.MELEE_STOW,
				new Transition<>(State.IDLE, () -> warframe.getMeleeWeapon().isStowed()), jumping, melee, shooting);
		stateMachine.addState(State.SHOOTING,
				new Transition<>(State.IDLE, () -> !warframe.getPrimaryWeapon().isShooting()));
		stateMachine.init(State.IDLE);
	}

	@Override
	public void start() {
		warframe = gameObject.getComponentOfType(Warframe.class);
		gameObject.setTag("player");
	}

	@Override
	public void update() {
		if (warframe.getHealth() <= 0) {
			return;
		}

		int x = getMovement();

		stateMachine.update();

		if (stateMachine.getState() != State.SHOOTING) {
			if (x < 0) {
				gameObject.transform.scale.x = -1;
			} else if (x > 0) {
				gameObject.transform.scale.x = 1;
			}

			warframe.stopShooting();
		}

		switch (stateMachine.getState()) {
			case IDLE:
				gameObject.animator.play("idle");
				castCheck();
				break;
			case RUNNING:
				gameObject.animator.play("run");
				transform.translate(x * speed * Time.fpsScaling60, 0);
				castCheck();
				break;
			case JUMPING:
				gameObject.animator.play("jump");
				transform.translate(x * speed * Time.fpsScaling60, 0);
				ability1Check();
				break;
			case DOUBLE_JUMPING:
				gameObject.animator.play("double-jump");
				transform.translate(x * speed * Time.fpsScaling60, 0);
				ability1Check();
				break;
			case FALLING:
				gameObject.animator.play("fall");
				transform.translate(x * speed * Time.fpsScaling60, 0);
				ability1Check();
				break;
			case LANDING:
				gameObject.animator.play("land");
				break;
			case MELEE:
				gameObject.animator.play(warframe.meleeWeapon.getNextAnimation(x), true);
				break;
			case MELEE_WAITING:
				break;
			case MELEE_STOW:
				gameObject.animator.play("stow");
				break;
			case SHOOTING:
				warframe.shoot();
				break;
			case CASTING:
				gameObject.animator.play("cast");
				break;
			default:
				break;
		}
		if (transform.position.x < 0) transform.translate(-transform.position.x, 0);
		if (transform.position.y < 0) transform.translate(0, -transform.position.y);

		if (stateMachine.getState() != State.SHOOTING) {
			if (Input.getKeyDown(KeyCode.F1)) {
				warframe.setPrimaryWeapon(new Lanka());
			} else if (Input.getKeyDown(KeyCode.F2)) {
				warframe.setPrimaryWeapon(new Supra());
			} else if (Input.getKeyDown(KeyCode.F3)) {
				warframe.setPrimaryWeapon(new Gorgon());
			}
		}
	}

	private int getMovement() {
		return (Input.getKey(KeyCode.A) ? -1 : 0) + (Input.getKey(KeyCode.D) ? 1 : 0);
	}

	private void jump() {
		jumpCount++;
		gameObject.rigidbody.velocity.y = -25f;
	}

	private boolean idleCheck() {
		return !runCheck();
	}

	private boolean runCheck() {
		return Input.getKey(KeyCode.A) || Input.getKey(KeyCode.D);
	}

	private boolean jumpCheck() {
		return jumpCount == 0 && Input.getKeyDown(KeyCode.SPACE);
	}

	private boolean doubleJumpCheck() {
		return jumpCount == 1 && Input.getKeyDown(KeyCode.SPACE);
	}

	private boolean fallCheck() {
		return gameObject.rigidbody.velocity.y > 0;
	}

	private boolean meleeCheck() {
		return Input.getKey(KeyCode.MOUSE_LEFT);
	}

	private boolean shootCheck() {
		return Input.getKeyDown(KeyCode.MOUSE_RIGHT);
	}

	private void ability1Check() {
		if (Input.getKeyDown(KeyCode.ALPHA_1) && warframe.hasEnergy(5)) {
			warframe.removeEnergy(5);
			warframe.ability1();
		}
	}

	private void ability2Check() {
		if (Input.getKeyDown(KeyCode.ALPHA_2) && warframe.hasEnergy(10)) {
			warframe.removeEnergy(10);
			warframe.ability2();
		}
	}

	private void ability3Check() {
		if (Input.getKeyDown(KeyCode.ALPHA_3) && warframe.hasEnergy(25)) {
			warframe.removeEnergy(25);
			warframe.ability3();
		}
	}

	private void ability4Check() {
		/*if ((state == State.IDLE || state == State.RUNNING) && Input.getKeyDown(KeyCode.ALPHA_4) &&
				warframe.hasEnergy(50)) {
			warframe.removeEnergy(50);
			warframe.ability4();
			state = State.CASTING;
		}*/
	}

	private boolean castCheck() {
		ability1Check();
		ability2Check();
		ability3Check();
		ability4Check();
		return false;
	}

	@Override
	public void onCollision(Collision collision) {
		if (collision.location == CollisionLocation.BOTTOM &&
				(stateMachine.getState() == State.FALLING || stateMachine.getState() == State.JUMPING ||
						stateMachine.getState() == State.DOUBLE_JUMPING)) {
			jumpCount = 0;
			if (collision.speed > 37) {
				stateMachine.setState(State.LANDING);
				//noinspection ConstantConditions
				Camera.main.gameObject.getComponent(SmartCamera.class).startQuake(100, 0.3f);
				GameObject.destroy(
						GameObject.instantiate(new GameObject("Dust", new Renderer(), new Animator(), new Dust()),
								new Vector2(transform.position.x - 262, transform.position.y + 150)), 300);
			} else {
				stateMachine.setState(State.IDLE);
			}
		}
	}
}

package com.ezardlabs.lostsector.objects;

import com.ezardlabs.dethsquare.Animation;
import com.ezardlabs.dethsquare.AnimationType;
import com.ezardlabs.dethsquare.Animator;
import com.ezardlabs.dethsquare.Camera;
import com.ezardlabs.dethsquare.Collider;
import com.ezardlabs.dethsquare.Collider.Collision;
import com.ezardlabs.dethsquare.Collider.CollisionLocation;
import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.Input;
import com.ezardlabs.dethsquare.Input.KeyCode;
import com.ezardlabs.dethsquare.Renderer;
import com.ezardlabs.dethsquare.Screen;
import com.ezardlabs.dethsquare.Script;
import com.ezardlabs.dethsquare.TextureAtlas;
import com.ezardlabs.dethsquare.TextureAtlas.Sprite;
import com.ezardlabs.dethsquare.Touch;
import com.ezardlabs.dethsquare.Touch.TouchPhase;
import com.ezardlabs.dethsquare.Vector2;
import com.ezardlabs.dethsquare.util.Utils;
import com.ezardlabs.lostsector.objects.hud.HUD;
import com.ezardlabs.lostsector.objects.hud.WeaponControl.WeaponType;
import com.ezardlabs.lostsector.objects.warframes.Warframe;

import java.util.Timer;
import java.util.TimerTask;

public class Player extends Script {
	public static GameObject player;
	public int jumpCount = 0;
	private int x = 0;
	private float speed = 12.5f;
	private Warframe warframe;

	public State state = State.IDLE;
	public boolean dead = false;

	public enum State {
		IDLE,
		RUNNING,
		JUMPING,
		DOUBLE_JUMPING,
		FALLING,
		LANDING,
		MELEE,
		MELEE_WAITING,
		SHOOTING,
		CASTING
	}

	@Override
	public void start() {
		player = gameObject;
		warframe = gameObject.getComponentOfType(Warframe.class);
		gameObject.setTag("player");
	}

	@Override
	public void update() {
		HUD.update();

		if (dead) return;

		x = getMovement();

		if (x < 0) {
			gameObject.renderer.hFlipped = true;
		} else if (x > 0) {
			gameObject.renderer.hFlipped = false;
		}

		switchWeaponCheck();

		switch (state) {
			case IDLE:
				if (jumpCheck()) break;
				if (fallCheck()) break;
				if (meleeCheck()) break;
				if (shootCheck()) break;
				if (castCheck()) break;
				if (x != 0) state = State.RUNNING;
				break;
			case RUNNING:
				if (jumpCheck()) break;
				if (fallCheck()) break;
				if (meleeCheck()) break;
				if (shootCheck()) break;
				if (castCheck()) break;
				if (x == 0) state = State.IDLE;
				break;
			case JUMPING:
				if (jumpCheck()) break;
				if (fallCheck()) break;
				break;
			case DOUBLE_JUMPING:
				if (fallCheck()) break;
				break;
			case FALLING:
				if (jumpCheck()) break;
				break;
			case LANDING:
				break;
			case MELEE:
				if (fallCheck()) {
					warframe.meleeWeapon.reset();
					gameObject.rigidbody.velocity.x = 0;
					gameObject.renderer.setSize(200, 200);
					gameObject.renderer.setOffsets(0, 0);
					break;
				}
				break;
			case MELEE_WAITING:
				break;
			case SHOOTING:
				break;
			case CASTING:
				break;
		}

		switch (state) {
			case IDLE:
				gameObject.animator.play("idle");
				break;
			case RUNNING:
				gameObject.animator.play("run");
				transform.translate(x * speed, 0);
				if (Input.getKeyDown(KeyCode.ALPHA_1)) warframe.ability1();
				if (Input.getKeyDown(KeyCode.ALPHA_2)) warframe.ability2();
				if (Input.getKeyDown(KeyCode.ALPHA_3)) warframe.ability3();
				if (Input.getKeyDown(KeyCode.ALPHA_4)) warframe.ability4();
				break;
			case JUMPING:
				gameObject.animator.play("jump");
				transform.translate(x * speed, 0);
				if (Input.getKeyDown(KeyCode.ALPHA_1)) warframe.ability1();
				break;
			case DOUBLE_JUMPING:
				gameObject.animator.play("doublejump");
				transform.translate(x * speed, 0);
				if (Input.getKeyDown(KeyCode.ALPHA_1)) warframe.ability1();
				break;
			case FALLING:
				gameObject.animator.play("fall");
				transform.translate(x * speed, 0);
				if (Input.getKeyDown(KeyCode.ALPHA_1)) warframe.ability1();
				break;
			case LANDING:
				gameObject.animator.play("land");
				break;
			case MELEE:
				gameObject.animator.play(warframe.meleeWeapon.getNextAnimation(x));
				break;
			case MELEE_WAITING:
				meleeCheck();
				break;
			case SHOOTING:
				gameObject.animator.play("shoot");
				break;
			case CASTING:
				gameObject.animator.play("cast");
				break;
		}
	}

	private int getMovement() {
		switch (Utils.PLATFORM) {
			case ANDROID:
				int movement = 0;
				if (Input.touches.length == 0) return movement;
				for (Touch t : Input.touches) {
					if (t.position.x < Screen.width / 6f) {
						movement--;
						break;
					}
				}
				for (Touch t : Input.touches) {
					if (t.position.x > Screen.width / 6f && t.position.x < Screen.width / 2f) {
						movement++;
						break;
					}
				}
				return movement;
			case DESKTOP:
				return (Input.getKey(KeyCode.A) ? -1 : 0) + (Input.getKey(KeyCode.D) ? 1 : 0);
			default:
				return 0;
		}
	}

	private boolean jumpCheck() {
		boolean touchJump = false;
		for (Touch t : Input.touches) {
			if (!HUD.switchButtonHitTest(t) && t.phase == TouchPhase.BEGAN && t.position.x > Screen.width / 2f && t.startPosition.x > Screen.width / 2f && Vector2.distance(t.position, t.startPosition) < 150 && !HUD.isAttackButtonPressed(t.position)) {
				touchJump = true;
			}
		}
		if ((Input.getKeyDown(KeyCode.SPACE) || Input.getKeyDown(KeyCode.J) || touchJump) && jumpCount++ < 2) {
			state = jumpCount == 1 ? State.JUMPING : State.DOUBLE_JUMPING;
			gameObject.rigidbody.velocity.y = -25f;
			return true;
		}
		return false;
	}

	private boolean fallCheck() {
		if (gameObject.rigidbody.velocity.y > 0) {
			state = State.FALLING;
			return true;
		}
		return false;
	}

	private boolean meleeCheck() {
		if (HUD.getCurrentWeaponType() != WeaponType.MELEE) return false;
		boolean touchMelee = false;
		if (Utils.PLATFORM == Utils.Platform.ANDROID && HUD.getCurrentWeaponType() == WeaponType.MELEE) {
			for (Touch t : Input.touches) {
				if (t.phase == Touch.TouchPhase.BEGAN && HUD.isAttackButtonPressed(t.position)) {
					touchMelee = true;
					break;
				}
			}
		}
		if (Input.getKeyDown(KeyCode.RETURN) || Input.getKeyDown(KeyCode.K) || touchMelee) {
			state = State.MELEE;
			return true;
		}
		return false;
	}

	private boolean shootCheck() {
		if (HUD.getCurrentWeaponType() != WeaponType.RANGED) return false;
		boolean touchRanged = false;
		if (Utils.PLATFORM == Utils.Platform.ANDROID && HUD.getCurrentWeaponType() == WeaponType.RANGED) {
			for (Touch t : Input.touches) {
				if (t.phase == Touch.TouchPhase.BEGAN && HUD.isAttackButtonPressed(t.position)) {
					touchRanged = true;
					break;
				}
			}
		}
		if (Input.getKeyDown(KeyCode.RETURN) || Input.getKeyDown(KeyCode.K) || touchRanged) {
			state = State.SHOOTING;
			return true;
		}
		return false;
	}

	private void switchWeaponCheck() {
		boolean touchSwitch = false;
		if (Utils.PLATFORM == Utils.Platform.ANDROID) {
			touchSwitch = HUD.isSwitchButtonPressed();
		}
		if (Input.getKeyDown(KeyCode.L) || touchSwitch) {
			HUD.switchWeapons();
		}
	}

	private boolean castCheck() {
		boolean ability1 = false;
		boolean ability2 = false;
		boolean ability3 = false;
		boolean ability4 = false;
		for (Touch t : Input.touches) {
			if (t.phase == Touch.TouchPhase.ENDED) {
				float x = t.position.x - t.startPosition.x;
				float y = t.position.y - t.startPosition.y;
				if (x < -150 * Screen.scale) { // left
					ability3 = true;
				} else if (x > 150 * Screen.scale) { // right
					ability1 = true;
				} else if (y < -150 * Screen.scale) { // up
					ability4 = true;
				} else if (y > 150 * Screen.scale) { // down
					ability2 = true;
				}
			}
		}
		if (ability1 || Input.getKeyDown(KeyCode.ALPHA_1)) {
			if (warframe.hasEnergy(5)) {
				warframe.removeEnergy(5);
				warframe.ability1();
			}
		}
		if (ability2 || Input.getKeyDown(KeyCode.ALPHA_2)) {
			if (warframe.hasEnergy(10)) {
				warframe.removeEnergy(10);
				warframe.ability2();
			}
		}
		if (ability3 || Input.getKeyDown(KeyCode.ALPHA_3)) {
			if (warframe.hasEnergy(25)) {
				warframe.removeEnergy(25);
				warframe.ability3();
			}
		}
		if ((state == State.IDLE || state == State.RUNNING) && (ability4 || Input.getKeyDown(KeyCode.ALPHA_4))) {
			if (warframe.hasEnergy(50)) {
				warframe.removeEnergy(50);
				warframe.ability4();
				state = State.CASTING;
				return true;
			}
		}
		return false;
	}

	@Override
	public void onCollision(Collider other, Collision collision) {
		if (collision.location == CollisionLocation.BOTTOM) {
			jumpCount = 0;
			if (collision.speed > 37) {
				state = State.LANDING;
//				gameObject.renderer.setSize(200, 200);
//				gameObject.renderer.setOffsets(0, 0);
//				gameObject.animator.play("land");
				//noinspection ConstantConditions
				Camera.main.gameObject.getComponent(CameraMovement.class).startQuake(100, 0.3f);
				TextureAtlas ta = new TextureAtlas("images/effects/dust.png", "images/effects/dust.txt");
				GameObject.destroy(GameObject.instantiate(new GameObject("Dust", new Renderer(ta, ta.getSprite("dust0"), 700, 50),
								new Animator(new Animation("dust", new Sprite[]{ta.getSprite("dust0"),
										ta.getSprite("dust1"),
										ta.getSprite("dust2")}, AnimationType.ONE_SHOT, 100))),
						new Vector2(transform.position.x - 262, transform.position.y + 150)), 300);
				new Timer().schedule(new TimerTask() {
					@Override
					public void run() {
						state = State.IDLE;
					}
				}, 300);
			} else {
				state = State.IDLE;
			}
		}
	}
}

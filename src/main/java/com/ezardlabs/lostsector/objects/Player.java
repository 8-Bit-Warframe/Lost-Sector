package com.ezardlabs.lostsector.objects;

import com.ezardlabs.dethsquare.Animation;
import com.ezardlabs.dethsquare.Animation.FrameData;
import com.ezardlabs.dethsquare.AnimationType;
import com.ezardlabs.dethsquare.Animator;
import com.ezardlabs.dethsquare.Camera;
import com.ezardlabs.dethsquare.Collider.Collision;
import com.ezardlabs.dethsquare.Collider.CollisionLocation;
import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.Input;
import com.ezardlabs.dethsquare.Input.KeyCode;
import com.ezardlabs.dethsquare.LevelManager;
import com.ezardlabs.dethsquare.Renderer;
import com.ezardlabs.dethsquare.Screen;
import com.ezardlabs.dethsquare.Script;
import com.ezardlabs.dethsquare.TextureAtlas;
import com.ezardlabs.dethsquare.TextureAtlas.Sprite;
import com.ezardlabs.dethsquare.Touch;
import com.ezardlabs.dethsquare.Touch.TouchPhase;
import com.ezardlabs.dethsquare.Vector2;
import com.ezardlabs.dethsquare.util.Dethsquare;
import com.ezardlabs.lostsector.camera.SmartCamera;
import com.ezardlabs.lostsector.objects.hud.HUD;
import com.ezardlabs.lostsector.objects.hud.WeaponControl.WeaponType;
import com.ezardlabs.lostsector.objects.menus.Menu;
import com.ezardlabs.lostsector.objects.menus.Menu.MenuAction;
import com.ezardlabs.lostsector.objects.warframes.Warframe;

import java.util.Timer;
import java.util.TimerTask;

public class Player extends Script {
	public static GameObject player;
	public int jumpCount = 0;
	private int x = 0;
	private float speed = 12.5f;
	private Warframe warframe;
	private HUD hud;

	public State state = State.IDLE;

	private Menu escMenu = new Menu(new String[]{"Main Menu",
			"Close"}, new MenuAction[]{(menu, index, text) -> LevelManager.loadLevel("main_menu"),
			(menu, index, text) -> menu.close()});

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
		hud = gameObject.getComponentOfType(HUD.class);
		gameObject.setTag("player");
		gameObject.addComponent(escMenu);
	}

	@Override
	public void update() {
		if (escMenu.isOpen()) {
			if (Input.getKeyDown(KeyCode.ESCAPE)) escMenu.close();
			return;
		} else if (Input.getKeyDown(KeyCode.ESCAPE)) {
			escMenu.open();
		}

		if (warframe.getHealth() <= 0) {
			return;
		}

		x = getMovement();

		if (state != State.SHOOTING) {
			if (x < 0) {
				gameObject.transform.scale.x = -1;
			} else if (x > 0) {
				gameObject.transform.scale.x  = 1;
			}
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
				ability1Check();
				break;
			case DOUBLE_JUMPING:
				if (fallCheck()) break;
				ability1Check();
				break;
			case FALLING:
				if (jumpCheck()) break;
				ability1Check();
				break;
			case LANDING:
				break;
			case MELEE:
				if (fallCheck() || jumpCheck()) {
					warframe.meleeWeapon.reset();
					gameObject.rigidbody.velocity.x = 0;
					gameObject.renderer.setSize(200, 200);
					gameObject.renderer.setOffsets(0, 0);
					break;
				}
				break;
			case MELEE_WAITING:
				if (fallCheck() || jumpCheck()) {
					warframe.meleeWeapon.reset();
					gameObject.rigidbody.velocity.x = 0;
					gameObject.renderer.setSize(200, 200);
					gameObject.renderer.setOffsets(0, 0);
					break;
				}
				break;
			case SHOOTING:
				break;
			case CASTING:
				break;
			default:
				break;
		}

		switch (state) {
			case IDLE:
				gameObject.animator.play("idle");
				break;
			case RUNNING:
				gameObject.animator.play("run");
				transform.translate(x * speed, 0);
				break;
			case JUMPING:
				gameObject.animator.play("jump");
				transform.translate(x * speed, 0);
				break;
			case DOUBLE_JUMPING:
				gameObject.animator.play("doublejump");
				transform.translate(x * speed, 0);
				break;
			case FALLING:
				gameObject.animator.play("fall");
				transform.translate(x * speed, 0);
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
			default:
				break;
		}
		if (transform.position.x < 0) transform.translate(-transform.position.x, 0);
		if (transform.position.y < 0) transform.translate(0, -transform.position.y);
	}

	private int getMovement() {
		switch (Dethsquare.PLATFORM) {
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
			if (!hud.switchButtonHitTest(t) && !hud.attackButtonHitTest(t) && t.phase == TouchPhase
					.ENDED && t.position.x > Screen.width / 2f && t.startPosition.x > Screen.width / 2f &&
					Vector2.distance(t.position, t.startPosition) < 150 * Screen.scale) {
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
		boolean touchMelee = false;
		if (Dethsquare.PLATFORM == Dethsquare.Platform.ANDROID && hud.getCurrentWeaponType() == WeaponType.MELEE) {
			for (Touch t : Input.touches) {
				if (t.phase == Touch.TouchPhase.BEGAN && hud.isAttackButtonPressed(t.position)) {
					touchMelee = true;
					break;
				}
			}
		}
		if (Input.getKey(KeyCode.MOUSE_LEFT) || Input.getKey(KeyCode.K) || touchMelee) {
			state = State.MELEE;
			return true;
		}
		return false;
	}

	private boolean shootCheck() {
		boolean touchRanged = false;
		if (Dethsquare.PLATFORM == Dethsquare.Platform.ANDROID && hud.getCurrentWeaponType() == WeaponType.RANGED) {
			for (Touch t : Input.touches) {
				if (t.phase == Touch.TouchPhase.BEGAN && hud.isAttackButtonPressed(t.position)) {
					touchRanged = true;
					break;
				}
			}
		}
		if (Input.getKeyDown(KeyCode.MOUSE_RIGHT) || Input.getKeyDown(KeyCode.L) || touchRanged) {
			state = State.SHOOTING;
			return true;
		}
		return false;
	}

	private void switchWeaponCheck() {
		boolean touchSwitch = false;
		if (Dethsquare.PLATFORM == Dethsquare.Platform.ANDROID) {
			touchSwitch = hud.isSwitchButtonPressed();
		}
		if (Input.getKeyDown(KeyCode.L) || touchSwitch) {
			hud.switchWeapons();
		}
	}

	private void ability1Check() {
		boolean ability1 = false;
		for (Touch t : Input.touches) {
			if (t.phase == TouchPhase.ENDED && t.position.x - t.startPosition.x > 150 * Screen.scale) {
				ability1 = true;
			}
		}
		if ((ability1 || Input.getKeyDown(KeyCode.ALPHA_1)) &&
				warframe.hasEnergy(5)) {
			warframe.removeEnergy(5);
			warframe.ability1();
		}
	}

	private void ability2Check() {
		boolean ability2 = false;
		for (Touch t : Input.touches) {
			if (t.phase == TouchPhase.ENDED && t.position.y - t.startPosition.y > 150 * Screen.scale) {
				ability2 = true;
			}
		}
		if ((ability2 || Input.getKeyDown(KeyCode.ALPHA_2)) &&
				warframe.hasEnergy(10)) {
			warframe.removeEnergy(10);
			warframe.ability2();
		}
	}

	private void ability3Check() {
		boolean ability3 = false;
		for (Touch t : Input.touches) {
			if (t.phase == TouchPhase.ENDED && t.position.x - t.startPosition.x < -150 * Screen.scale) {
				ability3 = true;
			}
		}
		if ((ability3 || Input.getKeyDown(KeyCode.ALPHA_3)) &&
				warframe.hasEnergy(25)) {
			warframe.removeEnergy(25);
			warframe.ability3();
		}
	}

	private void ability4Check() {
		boolean ability4 = false;
		for (Touch t : Input.touches) {
			if (t.phase == TouchPhase.ENDED && t.position.y - t.startPosition.y < -150 * Screen.scale) { // up
				ability4 = true;
			}
		}
		if ((state == State.IDLE || state == State.RUNNING) &&
				(ability4 || Input.getKeyDown(KeyCode.ALPHA_4)) &&
				warframe.hasEnergy(50)) {
			warframe.removeEnergy(50);
			warframe.ability4();
			state = State.CASTING;
		}
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
		if (collision.location == CollisionLocation.BOTTOM && (state == State.FALLING || state == State.JUMPING || state == State.DOUBLE_JUMPING)) {
			jumpCount = 0;
			if (collision.speed > 37) {
				state = State.LANDING;
//				gameObject.renderer.setSize(200, 200);
//				gameObject.renderer.setOffsets(0, 0);
//				gameObject.animator.play("land");
				//noinspection ConstantConditions
				Camera.main.gameObject.getComponent(SmartCamera.class).startQuake(100, 0.3f);
				TextureAtlas ta = new TextureAtlas("images/effects/dust.png", "images/effects/dust.txt");
				GameObject.destroy(GameObject.instantiate(
						new GameObject("Dust", new Renderer(ta, ta.getSprite("dust0"), 700, 50), new Animator(
								new Animation("dust",
										new Sprite[]{ta.getSprite("dust0"), ta.getSprite("dust1"), ta.getSprite(
												"dust2")},
										new FrameData[]{new FrameData(700, 50, new Vector2(), 100), new FrameData(700,
												50, new Vector2(), 100), new FrameData(700, 50, new Vector2(), 100)},
										AnimationType.ONE_SHOT))),
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

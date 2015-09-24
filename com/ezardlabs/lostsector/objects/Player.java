package com.ezardlabs.lostsector.objects;

import com.ezardlabs.dethsquare.Animation;
import com.ezardlabs.dethsquare.Animator;
import com.ezardlabs.dethsquare.Camera;
import com.ezardlabs.dethsquare.Collider;
import com.ezardlabs.dethsquare.Collider.Collision;
import com.ezardlabs.dethsquare.Collider.CollisionLocation;
import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.Input;
import com.ezardlabs.dethsquare.Input.KeyCode;
import com.ezardlabs.dethsquare.Input.OnTouchListener;
import com.ezardlabs.dethsquare.Renderer;
import com.ezardlabs.dethsquare.Screen;
import com.ezardlabs.dethsquare.Script;
import com.ezardlabs.dethsquare.TextureAtlas;
import com.ezardlabs.dethsquare.TextureAtlas.Sprite;
import com.ezardlabs.dethsquare.Vector2;
import com.ezardlabs.dethsquare.animationtypes.OneShotAnimation;
import com.ezardlabs.dethsquare.util.Utils;
import com.ezardlabs.lostsector.objects.warframes.Warframe;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class Player extends Script {
	public static GameObject player;
	public int jumpCount = 0;
	private int x = 0;
	private float speed = 12.5f;
	private Warframe warframe;

	public State state = State.IDLE;

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
		switch (Utils.PLATFORM) {
			case ANDROID:
				Input.addOnTouchListener(new OnTouchListener() {
					final HashMap<Integer, Vector2> map = new HashMap<>();

					@Override
					public void onTouchDown(int id, float x, float y) {
						if (x < Screen.width / 6f) {
							Player.this.x = -1;
						} else if (x < Screen.width / 2f) {
							Player.this.x = 1;
						}
						map.put(id, new Vector2(x, y));
					}

					@Override
					public void onTouchMove(int id, float x, float y) {
						if (map.containsKey(id) && map.get(id).x > Screen.width / 2f) {
							if (x < Screen.width / 2f) {
								onTouchUp(id, Screen.width / 2f, y);
							}
						} else {
							if (x > Screen.width / 2f) {
								onTouchUp(id, Screen.width / 2f, y);
							} else if (x < Screen.width / 6f) {
								Player.this.x = -1;
							} else if (x < Screen.width / 2f) {
								Player.this.x = 1;
							}
						}
					}

					@SuppressWarnings("ConstantConditions")
					@Override
					public void onTouchUp(int id, float x, float y) {
						Vector2 v = map.get(id);
						map.remove(id);
						if (v != null) {
							if (v.x < Screen.width / 2f) {
								Player.this.x = 0;
							} else {
								v.x = x - v.x;
								v.y = y - v.y;
								Warframe w = (Warframe) gameObject.getComponentOfType(
										Warframe.class);
								if (v.x < -150 * Screen.scale) { // left
									if (w.hasEnergy(25)) {
										w.removeEnergy(25);
										w.ability3();
									}
								} else if (v.x > 150 * Screen.scale) { // right
									if (w.hasEnergy(5)) {
										w.removeEnergy(5);
										w.ability1();
									}
								} else if (v.y < -150 * Screen.scale) { // up
									if (w.hasEnergy(50)) {
										w.removeEnergy(50);
										w.ability4();
									}
								} else if (v.y > 150 * Screen.scale) { // down
									if (w.hasEnergy(10)) {
										w.removeEnergy(10);
										w.ability2();
									}
								} else {
//									jump();
								}
							}
						}
					}

					@Override
					public void onTouchCancel(int id) {
						map.remove(id);
					}

					@Override
					public void onTouchOutside(int id) {
						map.remove(id);
					}
				});
				break;
		}
		warframe = (Warframe) gameObject.getComponentOfType(Warframe.class);
	}

	@Override
	public void update() {
		x = (Input.getKey(KeyCode.A) ? -1 : 0) + (Input.getKey(KeyCode.D) ? 1 : 0);

		if (x < 0) {
			gameObject.renderer.hFlipped = true;
		} else if (x > 0) {
			gameObject.renderer.hFlipped = false;
		}

		switch (state) {
			case IDLE:
				if (jumpCheck()) break;
				if (fallCheck()) break;
				if (meleeCheck()) break;
				if (castCheck()) break;
				if (x != 0) state = State.RUNNING;
				break;
			case RUNNING:
				if (jumpCheck()) break;
				if (fallCheck()) break;
				if (meleeCheck()) break;
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
				break;
			case CASTING:
				gameObject.animator.play("cast");
				break;
		}
	}

	private boolean jumpCheck() {
		if ((Input.getKeyDown(KeyCode.SPACE) || Input.getKeyDown(KeyCode.J)) && jumpCount++ < 2) {
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
		if (Input.getKeyDown(KeyCode.RETURN) || Input.getKeyDown(KeyCode.K)) {
			state = State.MELEE;
			warframe.meleeWeapon.getNextAnimation(x);
			return true;
		}
		return false;
	}

	private boolean castCheck() {
		if (Input.getKeyDown(KeyCode.ALPHA_1)) {
			warframe.ability1();
			return false;
		}
		if (Input.getKeyDown(KeyCode.ALPHA_2)) {
			warframe.ability2();
			return false;
		}
		if (Input.getKeyDown(KeyCode.ALPHA_3)) {
			warframe.ability3();
			return false;
		}
		if ((state == State.IDLE || state == State.RUNNING) && Input.getKeyDown(KeyCode.ALPHA_4)) {
			state = State.CASTING;
			warframe.ability4();
			return true;
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
								new Animator(new Animation("dust", new Sprite[]{ta.getSprite("dust0"), ta.getSprite("dust1"), ta.getSprite("dust2")}, new OneShotAnimation(), 100))),
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

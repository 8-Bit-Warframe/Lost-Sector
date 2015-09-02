package com.ezardlabs.lostsector.objects;

import com.ezardlabs.dethsquare.Animation;
import com.ezardlabs.dethsquare.Animation.AnimationType;
import com.ezardlabs.dethsquare.Animator;
import com.ezardlabs.dethsquare.Collider;
import com.ezardlabs.dethsquare.Collider.Collision;
import com.ezardlabs.dethsquare.Collider.CollisionLocation;
import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.Input;
import com.ezardlabs.dethsquare.Input.OnKeyListener;
import com.ezardlabs.dethsquare.Input.OnTouchListener;
import com.ezardlabs.dethsquare.Renderer;
import com.ezardlabs.dethsquare.Screen;
import com.ezardlabs.dethsquare.Script;
import com.ezardlabs.dethsquare.TextureAtlas;
import com.ezardlabs.dethsquare.TextureAtlas.Sprite;
import com.ezardlabs.dethsquare.Vector2;
import com.ezardlabs.dethsquare.util.Utils;
import com.ezardlabs.lostsector.objects.warframes.Warframe;

import java.util.ArrayList;
import java.util.HashMap;

public class Player extends Script {
	public static GameObject player;
	public int jumpCount = 0;
	private int x = 0;
	public boolean landing = false;
	public boolean melee = false;
	private float speed = 12.5f;

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
								Warframe w = (Warframe) gameObject
										.getComponentOfType(Warframe.class);
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
									jump();
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
			case DESKTOP:
				Input.addOnKeyListener(new OnKeyListener() {
					private boolean leftDown = false;
					private boolean rightDown = false;
					private ArrayList<Character> down = new ArrayList<>();

					@Override
					public void onKeyTyped(char keyChar) {
					}

					@SuppressWarnings("ConstantConditions")
					@Override
					public void onKeyDown(char keyChar) {
						if (down.contains(keyChar)) return;
						else down.add(keyChar);
						Warframe w = (Warframe) gameObject.getComponentOfType(Warframe.class);
						switch (keyChar) {
							case 'a':
								leftDown = true;
								if (rightDown) x = 0;
								else x = -1;
								break;
							case 'd':
								rightDown = true;
								if (leftDown) x = 0;
								else x = 1;
								break;
							case 'w':
							case ' ':
							case 'j':
								if (!landing && !melee) jump();
								break;
							case '1':
								if (w.hasEnergy(5)) {
									w.removeEnergy(5);
									w.ability1();
								}
								break;
							case '2':
								if (w.hasEnergy(10)) {
									w.removeEnergy(10);
									w.ability2();
								}
								break;
							case '3':
								if (w.hasEnergy(25)) {
									w.removeEnergy(25);
									w.ability3();
								}
								break;
							case '4':
								if (w.hasEnergy(50)) {
									w.removeEnergy(50);
									w.ability4();
								}
								break;
							case '\n':
							case 'k':
								if (!landing) melee();
								break;
						}
					}

					@Override
					public void onKeyUp(char keyChar) {
						if (down.contains(keyChar)) down.remove((Character) keyChar);
						switch (keyChar) {
							case 'a':
								leftDown = false;
								if (rightDown) x = 1;
								else x = 0;
								break;
							case 'd':
								rightDown = false;
								if (leftDown) x = -1;
								else x = 0;
								break;
						}
					}
				});
				break;
		}
	}

	@Override
	public void update() {
		if (landing) return;

		if (x < 0) {
			gameObject.renderer.hFlipped = true;
		} else if (x > 0) {
			gameObject.renderer.hFlipped = false;
		}

		if (melee) return;

		transform.translate(x * speed, 0);

		if (jumpCount > 0 && gameObject.rigidbody.gravity > 0) {
			gameObject.animator.play("fall");
		}

		if (jumpCount == 0) {
			if (gameObject.rigidbody.gravity > 2) {
				gameObject.animator.play("fall");
			} else if (x != 0) {
				gameObject.animator.play("run");
			} else {
				gameObject.animator.play("idle");
			}
		}
	}

	public void jump() {
		if (!landing && jumpCount++ < 2) {
			gameObject.rigidbody.gravity = -25f;
			gameObject.animator.play(jumpCount == 1 ? "jump" : "doublejump");
		}
	}

	public void melee() {
		melee = true;
		//noinspection ConstantConditions
		gameObject.animator
				.play(((Warframe) gameObject.getComponentOfType(Warframe.class)).meleeWeapon
								.getNextAnimation(x));
	}

	@Override
	public void onCollision(Collider other, Collision collision) {
		if (collision.location == CollisionLocation.BOTTOM) {
			jumpCount = 0;
			if (collision.speed > 37) {
				landing = true;
				gameObject.renderer.setSize(200, 200);
				gameObject.renderer.setOffsets(0, 0);
				gameObject.animator.play("land");
				TextureAtlas ta = new TextureAtlas("images/effects/dust.png",
						"images/effects/dust.txt");
				GameObject.destroy(GameObject.instantiate(
						new GameObject("Dust", new Renderer(ta, ta.getSprite("dust0"), 700, 50),
								new Animator(new Animation("dust",
										new Sprite[]{ta.getSprite("dust0"),
												ta.getSprite("dust1"),
												ta.getSprite("dust2")}, AnimationType.ONE_SHOT,
										100))),
						new Vector2(transform.position.x - 262, transform.position.y + 150)), 300);
				new Thread() {
					@Override
					public void run() {
						try {
							Thread.sleep(300);
						} catch (InterruptedException ignored) {
						}
						landing = false;
					}
				}.start();
			}
		}
	}
}

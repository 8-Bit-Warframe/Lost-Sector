package com.ezardlabs.lostsector.objects;

import android.util.Log;

import com.ezardlabs.dethsquare.Animation;
import com.ezardlabs.dethsquare.Animation.AnimationType;
import com.ezardlabs.dethsquare.Animator;
import com.ezardlabs.dethsquare.Collider;
import com.ezardlabs.dethsquare.Collider.Collision;
import com.ezardlabs.dethsquare.Collider.CollisionLocation;
import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.Input;
import com.ezardlabs.dethsquare.Input.OnTouchListener;
import com.ezardlabs.dethsquare.Renderer;
import com.ezardlabs.dethsquare.Screen;
import com.ezardlabs.dethsquare.Script;
import com.ezardlabs.dethsquare.TextureAtlas;
import com.ezardlabs.dethsquare.TextureAtlas.Sprite;
import com.ezardlabs.dethsquare.Vector2;
import com.ezardlabs.dethsquare.util.Utils;
import com.ezardlabs.dethsquare.util.Utils.Platform;
import com.ezardlabs.lostsector.objects.warframes.Warframe;

import java.util.HashMap;

public class Player extends Script {
	public static GameObject player;
	public int jumpCount = 0;
	private int x = 0;
	private GameObject dust;
	public boolean landing = false;
	private float speed = 12.5f;

	@Override
	public void start() {
		player = gameObject;
		if (Utils.PLATFORM == Platform.ANDROID) {
			Input.addOnTouchListener(new OnTouchListener() {
				HashMap<Integer, Vector2> map = new HashMap<>();

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
//					if (map.get(id).x > Screen.width / 2f) {
//						if (Vector2.distance(map.get(id), x, y) < 50 * Screen.scale) {
//							jump();
//						}
//					} else {
//						Player.this.x = 0;
//					}
					Vector2 v = map.get(id);
					map.remove(id);
					if (v != null) {
						if (v.x < Screen.width / 2f) {
							Player.this.x = 0;
						} else {
							v.x = x - v.x;
							v.y = y - v.y;
							if (v.x < -150 * Screen.scale) { // left
								((Warframe) player.getComponentOfType(Warframe.class)).ability3();
							} else if (v.x > 150 * Screen.scale) { // right
								((Warframe) player.getComponentOfType(Warframe.class)).ability1();
							} else if (v.y < -150 * Screen.scale) { // up
								((Warframe) player.getComponentOfType(Warframe.class)).ability4();
							} else if (v.y > 150 * Screen.scale) { // down
								((Warframe) player.getComponentOfType(Warframe.class)).ability2();
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
		}
	}

	@Override
	public void update() {
		if (landing) return;


		transform.translate(x * speed, 0);

		if (!landing) {
			if (x < 0) {
				gameObject.renderer.hFlipped = true;
			} else if (x > 0) {
				gameObject.renderer.hFlipped = false;
			}

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
	}

	public void jump() {
		if (!landing && jumpCount++ < 2) {
			gameObject.rigidbody.gravity = -25f;
			gameObject.animator.play(jumpCount == 1 ? "jump" : "doublejump");
		}
	}

	@Override
	public void onCollision(Collider other, Collision collision) {
		if (collision.location == CollisionLocation.BOTTOM) {
			jumpCount = 0;
			if (collision.speed > 37) {
				landing = true;
				gameObject.animator.play("land");
				TextureAtlas ta = new TextureAtlas("images/effects/dust.png", "images/effects/dust.txt");
				GameObject.destroy(GameObject.instantiate(new GameObject("Dust", new Renderer(ta, ta.getSprite("dust0"), 700, 50), new Animator(new Animation("dust", new Sprite[]{ta.getSprite("dust0"), ta.getSprite("dust1"), ta.getSprite("dust2")}, AnimationType.ONE_SHOT, 100))), new Vector2(transform.position.x - 262, transform.position.y + 150)), 300);
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

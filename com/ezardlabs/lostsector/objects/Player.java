package com.ezardlabs.lostsector.objects;

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

import java.util.HashMap;

public class Player extends Script {
	public static GameObject player;
	private int jumpCount = 0;
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
					if (map.get(id).x > Screen.width / 2f) {
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

				@Override
				public void onTouchUp(int id, float x, float y) {
					if (map.get(id).x > Screen.width / 2f) {
						if (Vector2.distance(map.get(id), x, y) < 50 * Screen.scale) {
							jump();
						}
					} else {
						Player.this.x = 0;
					}
				}
			});
		}
		TextureAtlas ta = new TextureAtlas("images/effects/dust.png", "images/effects/dust.txt");
		dust = new GameObject(null, new Renderer(ta, ta.getSprite("dust0"), 700, 50),
				new Animator(new Animation("dust", new Sprite[]{ta.getSprite("dust0"), ta.getSprite("dust1"), ta.getSprite("dust2")}, AnimationType.ONE_SHOT, 100)));
	}

	@Override
	public void update() {
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
				GameObject.destroy(GameObject.instantiate(dust, new Vector2(transform.position.x - 262, transform.position.y + 150)), 300);
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

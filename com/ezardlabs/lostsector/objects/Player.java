package com.ezardlabs.lostsector.objects;

import android.util.SparseArray;

import com.ezardlabs.dethsquare.Animator;
import com.ezardlabs.dethsquare.Collider;
import com.ezardlabs.dethsquare.Collider.CollisionLocation;
import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.Input;
import com.ezardlabs.dethsquare.Input.OnTouchListener;
import com.ezardlabs.dethsquare.PlayerBase;
import com.ezardlabs.dethsquare.Renderer;
import com.ezardlabs.dethsquare.Screen;
import com.ezardlabs.dethsquare.Vector2;
import com.ezardlabs.dethsquare.util.Utils;
import com.ezardlabs.dethsquare.util.Utils.Platform;

public class Player extends PlayerBase {
	public static GameObject player;
	private int jumpCount = 0;
	private int x = 0;
	private Animator animator;

	@Override
	public void start() {
		player = gameObject;
		if (Utils.PLATFORM == Platform.ANDROID) {
			Input.addOnTouchListener(new OnTouchListener() {
				SparseArray<Vector2> map = new SparseArray<>();

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
		animator = getComponent(Animator.class);
	}

	@Override
	public void update() {
		gravity += 1.25f;
		if (gravity > 78.125f) gravity = 78.125f;

		transform.translate(x * 12.5f, gravity);

		if (x < 0) {
			getComponent(Renderer.class).hFlipped = true;
		} else if (x > 0) {
			getComponent(Renderer.class).hFlipped = false;
		}

		if (jumpCount > 0 && gravity > 0) {
			animator.play("fall");
		}

		if (jumpCount == 0) {
			if (gravity > 2) {
				animator.play("fall");
			} else if (x != 0) {
				animator.play("run");
			} else {
				animator.play("idle");
			}
		}
	}

	public void jump() {
		if (jumpCount++ < 2) {
			gravity = -25f;
			animator.play(jumpCount == 1 ? "jump" : "doublejump");
		}
	}

	@Override
	public void onCollision(Collider other, CollisionLocation collisionLocation) {
		switch (collisionLocation) {
			case BOTTOM:
				jumpCount = 0;
			case TOP:
				gravity = 0;
				break;
			case LEFT:
				break;
			case RIGHT:
				break;
		}
	}
}

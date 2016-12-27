package com.ezardlabs.lostsector.objects.enemies.corpus.moas;

import com.ezardlabs.dethsquare.Animation;
import com.ezardlabs.dethsquare.Animation.AnimationListener;
import com.ezardlabs.dethsquare.AnimationType;
import com.ezardlabs.dethsquare.Animator;
import com.ezardlabs.dethsquare.Collider;
import com.ezardlabs.dethsquare.Component;
import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.Renderer;
import com.ezardlabs.dethsquare.TextureAtlas.Sprite;
import com.ezardlabs.dethsquare.Vector2;
import com.ezardlabs.lostsector.Game;
import com.ezardlabs.lostsector.NavMesh;
import com.ezardlabs.lostsector.objects.enemies.corpus.Moa;

public class ShockwaveMoa extends Moa {
	private Animator animator;
	private boolean stomping = false;
	private boolean shooting = false;
	private Vector2 target;

	@Override
	protected String getAtlasPath() {
		return "corpus/moa/shockwave";
	}

	@Override
	public void start() {
		super.start();
		animator = gameObject.getComponent(Animator.class);
		animator.addAnimations(new Animation("stomp", new Sprite[]{ta.getSprite("stomp0"),
				ta.getSprite("stomp1"),
				ta.getSprite("stomp2"),
				ta.getSprite("stomp3"),
				ta.getSprite("stomp4"),
				ta.getSprite("stomp5"),
				ta.getSprite("stomp6"),
				ta.getSprite("stomp7"),
				ta.getSprite("stomp8"),
				ta.getSprite("stomp9")}, AnimationType.ONE_SHOT, 100, new AnimationListener() {
			@Override
			public void onAnimatedStarted(Animator animator) {

			}

			@Override
			public void onFrame(Animator animator, int frameNum) {
				if (frameNum == 7) {
					GameObject.instantiate(new GameObject("Shockwave", new Renderer(), new Animator(
							new Animation("shockwave", new Sprite[]{ta.getSprite("shockwave0"),
									ta.getSprite("shockwave1"),
									ta.getSprite("shockwave2"),
									ta.getSprite("shockwave3"),
									ta.getSprite("shockwave4")}, AnimationType.ONE_SHOT, 100,
									new AnimationListener() {
										@Override
										public void onAnimatedStarted(Animator animator) {

										}

										@Override
										public void onFrame(Animator animator, int frameNum) {

										}

										@Override
										public void onAnimationFinished(Animator animator) {
											GameObject.destroy(animator.gameObject);
										}
									})), new Component() {
						@Override
						public void start() {
							this.gameObject.getComponent(Animator.class).play("shockwave");
						}
					}), new Vector2(transform.position.x - 300, transform.position.y + 200));
				}
			}

			@Override
			public void onAnimationFinished(Animator animator) {
				stomping = false;
			}
		}));
		System.out.println(gameObject.renderer.textureName);
	}

	@Override
	public void update() {
		if (dead || frozen || stomping) return;
		for (GameObject player : Game.players) {
			if (Vector2.distance(transform.position, player.transform.position) < 300) {
				animator.play("stomp");
				stomping = true;
			}
		}
		int x = 0;
		if (!landing && gameObject.rigidbody.velocity.y == 0) {
			if (Game.players.length > 0 && transform.position.y == Game.players[0].transform.position.y && Math.abs(transform.position.x - Game.players[0].transform.position.x) < 1500) {
				shooting = true;
				gameObject.animator.play("shoot");
				if (Game.players[0].transform.position.x < transform.position.x) {
					gameObject.transform.scale.x = -1;
				} else if (Game.players[0].transform.position.x > transform.position.x) {
					gameObject.transform.scale.x = 1;
				}
				return;
			} else {
				if ((target == null || transform.position.x == target.x) && Game.players.length > 0) {
					NavMesh.NavPoint[] path = NavMesh.getPath(transform, Game.players[0].transform);
					if (path != null && path.length > 0) {
						int pathIndex = 0;
						if (path.length > 1 && path[0] != null && transform.position.x == path[0].position.x) pathIndex = 1;
						target = path[pathIndex].position;
					}
				}
				if (target != null) {
					if (target.x < transform.position.x) {
						gameObject.transform.scale.x = -1;
					} else if (target.x > transform.position.x) {
						gameObject.transform.scale.x = 1;
					}
					if (target.y - 100 < transform.position.y) {
						if (gameObject.rigidbody.velocity.y >= 0) {
							gameObject.rigidbody.velocity.y = -30f;
						}
					} else {
						if (target.x < transform.position.x) {
							x = -10;
						} else if (target.x > transform.position.x) {
							x = 10;
						}
					}
				}
			}
		}
		transform.translate(x, 0);
		if (landing) {
			gameObject.animator.play("land");
		} else if (gameObject.rigidbody.velocity.y > 0) {
			gameObject.animator.play("fall");
		} else if (x != 0) {
			gameObject.animator.play("run");
		} else {
			gameObject.animator.play("idle");
		}
	}

	@Override
	public void onCollision(Collider.Collision collision) {
		if (collision.speed > 37 && collision.location == Collider.CollisionLocation.BOTTOM) {
			gameObject.animator.play("land");
			landing = true;
		}
	}
}

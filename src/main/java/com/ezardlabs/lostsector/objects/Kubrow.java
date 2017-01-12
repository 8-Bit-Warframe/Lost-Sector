package com.ezardlabs.lostsector.objects;

import com.ezardlabs.dethsquare.Animation;
import com.ezardlabs.dethsquare.AnimationType;
import com.ezardlabs.dethsquare.Animator;
import com.ezardlabs.dethsquare.TextureAtlas;
import com.ezardlabs.dethsquare.Vector2;
import com.ezardlabs.lostsector.Game;
import com.ezardlabs.lostsector.Game.DamageType;
import com.ezardlabs.lostsector.NavMesh;

public class Kubrow extends Entity {
	private float speed = 10;
	public boolean attacking = false;
	TargetType targetType = TargetType.PLAYER;
	Vector2 target;

	enum TargetType {
		PLAYER,
		ENEMY
	}

	public Kubrow() {
		super(3);
	}

	@Override
	public void start() {
		TextureAtlas ta = new TextureAtlas("images/kubrows/white/atlas.png", "images/kubrows/white/atlas.txt");
		gameObject.renderer.setTextureAtlas(ta, 200, 200);
		gameObject.animator.setAnimations(new Animation("idle", new TextureAtlas.Sprite[]{ta.getSprite("idle0")}, AnimationType.ONE_SHOT, Long.MAX_VALUE), new Animation("run",
				new TextureAtlas.Sprite[]{ta.getSprite("run0"),
						ta.getSprite("run1"),
						ta.getSprite("run2"),
						ta.getSprite("run3"),
						ta.getSprite("run4"),
						ta.getSprite("run5"),
						ta.getSprite("run6")},
				AnimationType.LOOP, 100), new Animation("attack",
				new TextureAtlas.Sprite[]{ta.getSprite("attack0"),
						ta.getSprite("attack1"),
						ta.getSprite("attack2"),
						ta.getSprite("attack3"),
						ta.getSprite("attack4"),
						ta.getSprite("attack5"),
						ta.getSprite("attack6"),
						ta.getSprite("attack7"),
						ta.getSprite("attack8"),
						ta.getSprite("attack9"),
						ta.getSprite("attack10"),
						ta.getSprite("attack11")},
				AnimationType.ONE_SHOT, 100, new Animation.AnimationListener() {
			@Override
			public void onAnimatedStarted(Animator animator) {

			}

			@Override
			public void onFrame(Animator animator, int frameNum) {

			}

			@Override
			public void onAnimationFinished(Animator animator) {
				//noinspection ConstantConditions
				attacking = false;
			}
		}), new Animation("jump", new TextureAtlas.Sprite[]{ta.getSprite("jump0"),
				ta.getSprite("jump1"),
				ta.getSprite("jump2")}, AnimationType.ONE_SHOT, 100), new Animation("fall", new TextureAtlas.Sprite[]{ta.getSprite("fall0")}, AnimationType.ONE_SHOT, 100));
		gameObject.animator.play("idle");

	}

//	@Override
//	public void update() {
//		int x = 0;
//		if (gameObject.rigidbody.gravity == 0) {
//			if (target == null || transform.position.x == target.x) {
//				NavMesh.NavPoint[] path = NavMesh.getPath(transform, Game.players[0].transform);
//				if (path != null && path.length > 0) {
//					int pathIndex = 0;
//					if (path.length > 1 && path[0] != null && transform.position.x == path[0].position.x)
//						pathIndex = 1;
//					target = path[pathIndex].position;
//				}
//			}
//			if (target != null) {
//				if (target.x < transform.position.x) {
//					x = -10;
//					gameObject.renderer.hFlipped = true;
//				} else if (target.x > transform.position.x) {
//					x = 10;
//					gameObject.renderer.hFlipped = false;
//				}
//			}
//			transform.translate(x, 0);
//			if (x == 0) {
//				gameObject.animator.play("idle");
//			} else {
//				gameObject.animator.play("run");
//			}
//		}


//		if (attacking) return;
//
//		float x = 0;
//
//		double minDistance = 1000;
//		GameObject best = null;
//		double temp;
//		for (GameObject go : GameObject.findAllWithTag("enemy")) {
//			if ((temp = Vector2.distance(transform.position, go.transform.position)) < minDistance) {
//				minDistance = temp;
//				best = go;
//			}
//		}
//		if (best == null) {
//			targetType = TargetType.PLAYER;
//		} else {
//			targetType = TargetType.ENEMY;
//		}
//		if (gameObject.rigidbody.gravity == 0) {
//			NavMesh.NavPoint[] path = null;
//			switch (targetType) {
//				case PLAYER:
//					path = NavMesh.getPath(transform, Game.players[0].transform);
//					break;
//				case ENEMY:
//					path = NavMesh.getPath(transform, best.transform);
//					break;
//			}
//			if (path != null && path.length > 0) {
//				int pathIndex = 0;
//				if (path.length > 1 && path[0] != null && transform.position.x == path[0].position.x)
//					pathIndex = 1;
//				target = path[pathIndex].position;
//			}
//			if (target != null) {
//				if (target.x < transform.position.x) {
//					x = -10;
//					gameObject.renderer.hFlipped = true;
//				} else if (target.x > transform.position.x) {
//					x = 10;
//					gameObject.renderer.hFlipped = false;
//				}
//			}
//			transform.translate(x, 0);
//			if (x == 0) {
//				gameObject.animator.play("idle");
//			} else {
//				gameObject.animator.play("run");
//			}
//		}


//		if (best == null) {
//			if (gameObject.rigidbody.gravity == 0) {
//				NavMesh.NavPoint[] path = NavMesh.getPath(transform, Game.players[0].transform);
//				if (path != null && path.length > 1) {
//					int pathIndex = 0;
//					if (path.length > 1 && path[0] != null && transform.position.x == path[0].position.x)
//						pathIndex = 1;
//					if (path[pathIndex].position.x < transform.position.x) {
//						x = -speed;
//						gameObject.renderer.hFlipped = true;
//					} else if (path[pathIndex].position.x > transform.position.x) {
//						x = speed;
//						gameObject.renderer.hFlipped = false;
//					}
//				}
//			}
//		} else {
//			if (gameObject.rigidbody.gravity == 0) {
//				if (best.transform.position.x - transform.position.x <= 106.25f) { // 125
//					transform.position.x = best.transform.position.x - 106.25f;
//					gameObject.animator.play("attack");
//					attacking = true;
//					//noinspection ConstantConditions
//					((Enemy) best.getComponentOfType(Enemy.class)).kubrowAttack(transform.position);
//					return;
//				} else {
//					NavMesh.NavPoint[] path = NavMesh.getPath(transform, best.transform);
//					if (path != null && path.length > 0) {
//						int pathIndex = 0;
//						if (path.length > 1 && path[0] != null && transform.position.x == path[0].position.x)
//							pathIndex = 1;
//						if (path[pathIndex].position.x < transform.position.x) {
//							x = -speed;
//							gameObject.renderer.hFlipped = true;
//						} else if (path[pathIndex].position.x > transform.position.x) {
//							x = speed;
//							gameObject.renderer.hFlipped = false;
//						}
//					}
//				}
//			}
//		}
//
//		transform.translate(x, 0);
//		if (x == 0) {
//			gameObject.animator.play("idle");
//		} else {
//			gameObject.animator.play("run");
//		}
//	}

	@Override
	public void update() {
		int x = 0;
		if (gameObject.rigidbody.velocity.y == 0) {
			if (target == null || transform.position.x == target.x) {
				NavMesh.NavPoint[] path = NavMesh.getPath(transform, Game.players[0].transform);
				if (path != null && path.length > 0) {
					int pathIndex = 0;
					if (path.length > 1 && path[0] != null && transform.position.x == path[0].position.x)
						pathIndex = 1;
					target = path[pathIndex].position;
				}
			}
			if (target != null) {
				if (target.x < transform.position.x) {
					gameObject.transform.scale.x = -1;
				} else if (target.x > transform.position.x) {
					gameObject.transform.scale.x  = 1;
				}
				if (target.y < transform.position.y) {
					if (gameObject.rigidbody.velocity.y >= 0) {
						switch ((int) (transform.position.y - target.y)) {
							case 50:
								gameObject.rigidbody.velocity.y = -15f;
								break;
							case 100:
								gameObject.rigidbody.velocity.y = -25f;
								break;
							case 150:
								gameObject.rigidbody.velocity.y = -27f;
								break;
							case 200:
								gameObject.rigidbody.velocity.y = -30f;
								break;
							default:
								break;
						}
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
		transform.translate(x, 0);
		if (x == 0) {
			gameObject.animator.play("idle");
		} else {
			gameObject.animator.play("run");
		}
	}

	@Override
	protected void die(DamageType damageType, Vector2 attackOrigin) {
	}
}
package com.ezardlabs.lostsector.objects.enemies.corpus.crewmen;

import com.ezardlabs.dethsquare.Animation;
import com.ezardlabs.dethsquare.Animation.AnimationListener;
import com.ezardlabs.dethsquare.AnimationType;
import com.ezardlabs.dethsquare.Animator;
import com.ezardlabs.dethsquare.TextureAtlas.Sprite;
import com.ezardlabs.dethsquare.Vector2;
import com.ezardlabs.lostsector.Game;
import com.ezardlabs.lostsector.NavMesh;
import com.ezardlabs.lostsector.objects.enemies.corpus.Crewman;
import com.ezardlabs.lostsector.objects.warframes.Warframe;

public class ProvaCrewman extends Crewman {
	Vector2 target;
	private boolean attacking = false;

	@Override
	protected String getAtlasPath() {
		return "corpus/crewmen/prova";
	}

	@Override
	public void start() {
		super.start();
		gameObject.animator.addAnimations(new Animation("attack",
				new Sprite[]{ta.getSprite("attack0"),
						ta.getSprite("attack1"),
						ta.getSprite("attack2"),
						ta.getSprite("attack3"),
						ta.getSprite("attack4"),
						ta.getSprite("attack5")}, AnimationType.ONE_SHOT, 100,
				new AnimationListener() {
					@Override
					public void onAnimatedStarted(Animator animator) {
					}

					@Override
					public void onFrame(Animator animator, int frameNum) {
						if (frameNum == 2) {
							for (int i = 0; i < Game.players.length; i++) {
								if (Math.abs(transform.position.x -
										Game.players[i].transform.position.x) <= 200) {
									Warframe w = Game.players[i].getComponentOfType(Warframe.class);
									if (w != null) {
										w.removeHealth(3);
									}
								}
							}
						}
					}

					@Override
					public void onAnimationFinished(Animator animator) {
						attacking = false;
						gameObject.animator.play("idle");
					}
				}));
	}

	@Override
	public void update() {
		if (dead || frozen || attacking) return;
		int x = 0;
		if (!landing && gameObject.rigidbody.velocity.y == 0) {
			if (Game.players.length > 0 &&
					transform.position.y == Game.players[0].transform.position.y &&
					Math.abs(transform.position.x - Game.players[0].transform.position.x) <=
							(gameObject.renderer.hFlipped ? 100 : 200)) {
				attacking = true;
				gameObject.animator.play("attack");
				if (Game.players[0].transform.position.x < transform.position.x) {
					gameObject.renderer.hFlipped = true;
				} else if (Game.players[0].transform.position.x > transform.position.x) {
					gameObject.renderer.hFlipped = false;
				}
				return;
			} else {
				if ((target == null || transform.position.x == target.x) &&
						Game.players.length > 0) {
					NavMesh.NavPoint[] path = NavMesh.getPath(transform, Game.players[0].transform);
					if (path != null && path.length > 0) {
						int pathIndex = 0;
						if (path.length > 1 && path[0] != null &&
								transform.position.x == path[0].position.x) {
							pathIndex = 1;
						}
						target = path[pathIndex].position;
					}
				}
				if (target != null) {
					if (target.x < transform.position.x) {
						gameObject.renderer.hFlipped = true;
					} else if (target.x > transform.position.x) {
						gameObject.renderer.hFlipped = false;
					}
					if (target.y < transform.position.y) {
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
	protected Animation getJumpAnimation() {
		return new Animation("jump", new Sprite[]{ta.getSprite("jump0")}, AnimationType.ONE_SHOT,
				100);
	}

	@Override
	protected Animation getLandAnimation() {
		return new Animation("land", new Sprite[]{ta.getSprite("land0")}, AnimationType.ONE_SHOT,
				100, new AnimationListener() {
			@Override
			public void onAnimatedStarted(Animator animator) {
			}

			@Override
			public void onFrame(Animator animator, int frameNum) {
			}

			@Override
			public void onAnimationFinished(Animator animator) {
				landing = false;
			}
		});
	}

	@Override
	protected Animation getShootAnimation() {
		return new Animation("shoot", new Sprite[]{}, AnimationType.ONE_SHOT, 100);
	}
}

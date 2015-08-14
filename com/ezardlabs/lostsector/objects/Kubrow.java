package com.ezardlabs.lostsector.objects;

import com.ezardlabs.dethsquare.Animation;
import com.ezardlabs.dethsquare.Animator;
import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.TextureAtlas;
import com.ezardlabs.dethsquare.Vector2;
import com.ezardlabs.lostsector.Game;
import com.ezardlabs.lostsector.NavMesh;
import com.ezardlabs.lostsector.objects.enemies.Enemy;

public class Kubrow extends Avatar {
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
		gameObject.animator.setAnimations(new Animation("idle", new TextureAtlas.Sprite[]{ta.getSprite("idle0")}, Animation.AnimationType.ONE_SHOT, Long.MAX_VALUE), new Animation("run",
				new TextureAtlas.Sprite[]{ta.getSprite("run0"),
						ta.getSprite("run1"),
						ta.getSprite("run2"),
						ta.getSprite("run3"),
						ta.getSprite("run4"),
						ta.getSprite("run5"),
						ta.getSprite("run6")},
				Animation.AnimationType.LOOP, 100), new Animation("attack",
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
				Animation.AnimationType.ONE_SHOT, 100, new Animation.AnimationListener() {
			@Override
			public void onAnimatedStarted(Animator animator) {

			}

			@Override
			public void onFrame(int frameNum) {

			}

			@Override
			public void onAnimationFinished(Animator animator) {
				//noinspection ConstantConditions
				attacking = false;
			}
		}));

	}

	@Override
	public void update() {
		if (attacking) return;

		float x = 0;

		double minDistance = 1000;
		GameObject best = null;
		double temp;
		for (GameObject go : GameObject.findAllWithTag("enemy")) {
			if ((temp = Vector2.distance(transform.position, go.transform.position)) < minDistance) {
				minDistance = temp;
				best = go;
			}
		}
		if (best == null) {
			targetType = TargetType.PLAYER;
		} else {
			targetType = TargetType.ENEMY;
		}
		if (gameObject.rigidbody.gravity == 0) {
			NavMesh.NavPoint[] path;
			switch (targetType) {
				case PLAYER:
					path = NavMesh.getPath(transform, Game.players[0].transform);
					break;
				case ENEMY:
					break;
			}
		}
		if (best == null) {
			if (gameObject.rigidbody.gravity == 0) {
				NavMesh.NavPoint[] path = NavMesh.getPath(transform, Game.players[0].transform);
				if (path != null && path.length > 1) {
					int pathIndex = 0;
					if (path.length > 1 && path[0] != null && transform.position.x == path[0].position.x)
						pathIndex = 1;
					if (path[pathIndex].position.x < transform.position.x) {
						x = -speed;
						gameObject.renderer.hFlipped = true;
					} else if (path[pathIndex].position.x > transform.position.x) {
						x = speed;
						gameObject.renderer.hFlipped = false;
					}
				}
			}
		} else {
			if (gameObject.rigidbody.gravity == 0) {
				if (best.transform.position.x - transform.position.x <= 106.25f) { // 125
					transform.position.x = best.transform.position.x - 106.25f;
					gameObject.animator.play("attack");
					attacking = true;
					//noinspection ConstantConditions
					((Enemy) best.getComponentOfType(Enemy.class)).kubrowAttack(transform.position);
					return;
				} else {
					NavMesh.NavPoint[] path = NavMesh.getPath(transform, best.transform);
					if (path != null && path.length > 0) {
						int pathIndex = 0;
						if (path.length > 1 && path[0] != null && transform.position.x == path[0].position.x)
							pathIndex = 1;
						if (path[pathIndex].position.x < transform.position.x) {
							x = -speed;
							gameObject.renderer.hFlipped = true;
						} else if (path[pathIndex].position.x > transform.position.x) {
							x = speed;
							gameObject.renderer.hFlipped = false;
						}
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
}
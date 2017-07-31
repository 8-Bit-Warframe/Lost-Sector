package com.ezardlabs.lostsector.objects.enemies;

import com.ezardlabs.dethsquare.Level;
import com.ezardlabs.dethsquare.LevelManager;
import com.ezardlabs.dethsquare.TextureAtlas;
import com.ezardlabs.dethsquare.Vector2;
import com.ezardlabs.dethsquare.animation.Animations;
import com.ezardlabs.dethsquare.animation.Animations.Validator;
import com.ezardlabs.lostsector.Game.DamageType;
import com.ezardlabs.lostsector.ai.Behaviour;
import com.ezardlabs.lostsector.ai.Behaviour.State;
import com.ezardlabs.lostsector.levels.MissionLevel;
import com.ezardlabs.lostsector.levels.SurvivalLevel;
import com.ezardlabs.lostsector.objects.Entity;

public abstract class Enemy extends Entity {
	protected final TextureAtlas ta;
	protected final Behaviour behaviour;

	public Enemy(int health, Behaviour behaviour) {
		super(health);
		ta = new TextureAtlas("images/enemies/" + getAtlasPath() + "/atlas.png",
				"images/enemies/" + getAtlasPath() + "/atlas.txt");
		this.behaviour = behaviour;
	}

	@Override
	public void start() {
		gameObject.setTag("enemy");
		gameObject.renderer.setTextureAtlas(ta, 200, 200);
		gameObject.animator.setAnimations(Animations.load(getAnimationPath(), ta,
				new Validator("idle", "run", "jump", "fall", "land", "die_shoot_front", "die_shoot_back",
						"die_slash_front", "die_slash_back", "frozen", "frozen_melt", "frozen_shatter")));
		gameObject.animator.play("idle");
		gameObject.renderer.setzIndex(3);

		setDamageListener(new DamageListener() {
			@Override
			public void onDamageReceived(float damage, DamageType damageType, Vector2 attackOrigin) {
				behaviour.onDamageReceived(damageType, transform, attackOrigin);
			}
		});

		Level level = LevelManager.getCurrentLevel();
		if (level instanceof MissionLevel) {
			((MissionLevel) level).getMission().notifyEnemySpawn(this);
		}
	}

	@Override
	public void update() {
		if (behaviour != null) {
			behaviour.update(transform);

			switch (behaviour.getState()) {
				case IDLE:
					gameObject.animator.play("idle");
					break;
				case MOVING:
					gameObject.animator.play("run");
					break;
				case JUMPING:
					gameObject.animator.play("jump");
					break;
				case FALLING:
					gameObject.animator.play("fall");
					break;
				case LANDING:
					gameObject.animator.play("land");
					break;
				case FROZEN:
					gameObject.animator.play("frozen");
					break;
				case THAWING:
					gameObject.animator.play("frozen_melt");
					break;
				case ATTACKING:
					gameObject.animator.play("attack");
					break;
			}
		}
	}

	//	public void kubrowAttack(Vector2 kubrowPosition) {
//		if (kubrowPosition.x < transform.position.x) {
//			if (gameObject.renderer.hFlipped) {
//				gameObject.animator.play("die_kubrow_front");
//			} else {
//				gameObject.animator.play("die_kubrow_back");
//			}
//		} else {
//			if (gameObject.renderer.hFlipped) {
//				gameObject.animator.play("die_kubrow_back");
//			} else {
//				gameObject.animator.play("die_kubrow_front");
//			}
//		}
//		die(DamageType.KUBROW);
//	}

	public void die(DamageType damageType, Vector2 attackOrigin) {
		gameObject.setTag(null);
		if (behaviour.getState() == State.FROZEN) {
			gameObject.animator.play("frozen_shatter");
		} else {
			String direction;
			if (attackOrigin.x < transform.position.x) {
				if (gameObject.transform.scale.x < 0) {
					direction = "front";
				} else {
					direction = "back";
				}
			} else {
				if (gameObject.transform.scale.x < 0) {
					direction = "back";
				} else {
					direction = "front";
				}
			}
			String type;
			switch (damageType) {
				case NORMAL:
					type = "shoot";
					break;
				case SLASH:
					type = "slash";
					break;
				case COLD:
					type = "";
					break;
				case KUBROW:
					type = "kubrow";
					break;
				default:
					type = "";
					break;
			}
			gameObject.animator.play("die_" + type + "_" + direction);
		}
		Level level = LevelManager.getCurrentLevel();
		if (level instanceof MissionLevel) {
			((MissionLevel) level).getMission().notifyEnemyDeath(this);
		}
		if (level instanceof SurvivalLevel) {
			((SurvivalLevel) level).survivalManager.score++;
		}
	}

/*	protected Animation getIdleAnimation() {
		return new Animation("idle", new Sprite[]{ta.getSprite("idle0")}, AnimationType.ONE_SHOT, Long.MAX_VALUE);
	}

	protected Animation getRunAnimation() {
		return new Animation("run",
				new Sprite[]{ta.getSprite("run0"), ta.getSprite("run1"), ta.getSprite("run2"), ta.getSprite(
						"run3"), ta.getSprite("run4"), ta.getSprite("run5"), ta.getSprite("run6"), ta.getSprite(
						"run7")}, AnimationType.LOOP, 100);
	}

	protected Animation getJumpAnimation() {
		return new Animation("jump", new Sprite[]{ta.getSprite("jump0"), ta.getSprite("jump1")}, AnimationType.ONE_SHOT,
				100);
	}

	protected Animation getFallAnimation() {
		return new Animation("fall", new Sprite[]{ta.getSprite("fall0")}, AnimationType.ONE_SHOT, Long.MAX_VALUE);
	}

	protected Animation getLandAnimation() {
		return new Animation("land", new Sprite[]{ta.getSprite("land0"), ta.getSprite("land1")}, AnimationType.ONE_SHOT,
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

	protected Animation getShootAnimation() {
		return new Animation("shoot",
				new Sprite[]{ta.getSprite("shoot0"), ta.getSprite("shoot1"), ta.getSprite("shoot2"), ta.getSprite(
						"shoot3")}, AnimationType.LOOP, 100);
	}

	protected Animation getDieShootFrontAnimation() {
		return new Animation("die_shoot_front",
				new Sprite[]{ta.getSprite("die_shoot_front0"), ta.getSprite("die_shoot_front1"), ta.getSprite(
						"die_shoot_front2"), ta.getSprite("die_shoot_front3"), ta.getSprite(
						"die_shoot_front4"), ta.getSprite("die_shoot_front5"), ta.getSprite("die_shoot_front6")},
				AnimationType.ONE_SHOT, 100);
	}

	protected Animation getDieShootBackAnimation() {
		return new Animation("die_shoot_back",
				new Sprite[]{ta.getSprite("die_shoot_back0"), ta.getSprite("die_shoot_back1"), ta.getSprite(
						"die_shoot_back2"), ta.getSprite("die_shoot_back3"), ta.getSprite(
						"die_shoot_back4"), ta.getSprite("die_shoot_back5"), ta.getSprite("die_shoot_back6")},
				AnimationType.ONE_SHOT, 100);
	}

	protected Animation getDieSlashFrontAnimation() {
		return new Animation("die_slash_front",
				new Sprite[]{ta.getSprite("die_slash_front0"), ta.getSprite("die_slash_front1"), ta.getSprite(
						"die_slash_front2"), ta.getSprite("die_slash_front3"), ta.getSprite(
						"die_slash_front4"), ta.getSprite("die_slash_front5"), ta.getSprite("die_slash_front6")},
				AnimationType.ONE_SHOT, 100, new AnimationListener() {
			@Override
			public void onAnimatedStarted(Animator animator) {
				gameObject.renderer.setSize(400, 200);
				if (gameObject.transform.scale.x < 0) gameObject.renderer.setOffsets(0, 0);
				else gameObject.renderer.setOffsets(-400, 0);
			}

			@Override
			public void onFrame(Animator animator, int frameNum) {

			}

			@Override
			public void onAnimationFinished(Animator animator) {
			}
		});
	}

	protected Animation getDieSlashBackAnimation() {
		return new Animation("die_slash_back",
				new Sprite[]{ta.getSprite("die_slash_back0"), ta.getSprite("die_slash_back1"), ta.getSprite(
						"die_slash_back2"), ta.getSprite("die_slash_back3"), ta.getSprite(
						"die_slash_back4"), ta.getSprite("die_slash_back5"), ta.getSprite("die_slash_back6")},
				AnimationType.ONE_SHOT, 100);
	}

	protected Animation getDieKubrowFrontAnimation() {
		return new Animation("die_kubrow_front",
				new Sprite[]{ta.getSprite("die_kubrow_front0"), ta.getSprite("die_kubrow_front1"), ta.getSprite(
						"die_kubrow_front2"), ta.getSprite("die_kubrow_front3"), ta.getSprite(
						"die_kubrow_front4"), ta.getSprite("die_kubrow_front5"), ta.getSprite(
						"die_kubrow_front6"), ta.getSprite("die_kubrow_front7"), ta.getSprite(
						"die_kubrow_front8"), ta.getSprite("die_kubrow_front9"), ta.getSprite("die_kubrow_front10")},
				AnimationType.ONE_SHOT, 100, new AnimationListener() {
			@Override
			public void onAnimatedStarted(Animator animator) {
				gameObject.renderer.setSize(300, 300);
			}

			@Override
			public void onFrame(Animator animator, int frameNum) {

			}

			@Override
			public void onAnimationFinished(Animator animator) {

			}
		});
	}

	protected Animation getDieKubrowBackAnimation() {
		return new Animation("die_kubrow_back",
				new Sprite[]{ta.getSprite("die_kubrow_back0"), ta.getSprite("die_kubrow_back1"), ta.getSprite(
						"die_kubrow_back2"), ta.getSprite("die_kubrow_back3"), ta.getSprite(
						"die_kubrow_back4"), ta.getSprite("die_kubrow_back5"), ta.getSprite(
						"die_kubrow_back6"), ta.getSprite("die_kubrow_back7"), ta.getSprite(
						"die_kubrow_back8"), ta.getSprite("die_kubrow_back9"), ta.getSprite("die_kubrow_back10")},
				AnimationType.ONE_SHOT, 100, new AnimationListener() {
			@Override
			public void onAnimatedStarted(Animator animator) {
				gameObject.renderer.setSize(300, 300);
			}

			@Override
			public void onFrame(Animator animator, int frameNum) {

			}

			@Override
			public void onAnimationFinished(Animator animator) {

			}
		});
	}

	protected Animation getFrozenAnimation() {
		return new Animation("frozen", new Sprite[]{ta.getSprite("frozen0")}, AnimationType.ONE_SHOT, 5000,
				new AnimationListener() {
					@Override
					public void onAnimatedStarted(Animator animator) {
					}

					@Override
					public void onFrame(Animator animator, int frameNum) {
					}

					@Override
					public void onAnimationFinished(Animator animator) {
						if (health <= 0) {
							animator.play("frozen_shatter");
						} else {
							animator.play("frozen_melt");
						}
					}
				});
	}

	protected Animation getFrozenMeltAnimation() {
		return new Animation("frozen_melt",
				new Sprite[]{ta.getSprite("frozen_melt0"), ta.getSprite("frozen_melt1"), ta.getSprite("frozen_melt2")},
				AnimationType.ONE_SHOT, 100, new AnimationListener() {
			@Override
			public void onAnimatedStarted(Animator animator) {

			}

			@Override
			public void onFrame(Animator animator, int frameNum) {

			}

			@Override
			public void onAnimationFinished(Animator animator) {
				frozen = false;
			}
		});
	}

	protected Animation getFrozenShatterAnimation() {
		return new Animation("frozen_shatter",
				new Sprite[]{ta.getSprite("frozen_shatter0"), ta.getSprite("frozen_shatter1"), ta.getSprite(
						"frozen_shatter2"), ta.getSprite("frozen_shatter3"), ta.getSprite(
						"frozen_shatter4"), ta.getSprite("frozen_shatter5"), ta.getSprite(
						"frozen_shatter6"), ta.getSprite("frozen_shatter7"), ta.getSprite("frozen_shatter8")},
				AnimationType.ONE_SHOT, 100, new AnimationListener() {
			@Override
			public void onAnimatedStarted(Animator animator) {
				if (gameObject.transform.scale.x > 0) {
					transform.translate(-100, 0);
				}
				gameObject.renderer.setSize(300, 200);
			}

			@Override
			public void onFrame(Animator animator, int frameNum) {

			}

			@Override
			public void onAnimationFinished(Animator animator) {

			}
		});
	}*/

	protected abstract String getAtlasPath();

	protected abstract String getAnimationPath();
}

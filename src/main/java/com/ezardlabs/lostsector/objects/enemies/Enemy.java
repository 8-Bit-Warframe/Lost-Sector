package com.ezardlabs.lostsector.objects.enemies;

import com.ezardlabs.dethsquare.Collider;
import com.ezardlabs.dethsquare.Level;
import com.ezardlabs.dethsquare.LevelManager;
import com.ezardlabs.dethsquare.Rigidbody;
import com.ezardlabs.dethsquare.TextureAtlas;
import com.ezardlabs.dethsquare.Vector2;
import com.ezardlabs.dethsquare.animation.Animations;
import com.ezardlabs.dethsquare.animation.Animations.Validator;
import com.ezardlabs.dethsquare.multiplayer.Network;
import com.ezardlabs.lostsector.Game.DamageType;
import com.ezardlabs.lostsector.ai.Behaviour;
import com.ezardlabs.lostsector.ai.Behaviour.State;
import com.ezardlabs.lostsector.levels.MissionLevel;
import com.ezardlabs.lostsector.levels.SurvivalLevel;
import com.ezardlabs.lostsector.objects.DropTable;
import com.ezardlabs.lostsector.objects.Entity;

import static java.util.Arrays.asList;

public abstract class Enemy extends Entity {
	private final DropTable dropTable = new DropTable(asList("pickup_health", "pickup_energy"), asList(0.2f, 0.2f));
	private Behaviour behaviour;
	protected final TextureAtlas ta;

	public Enemy(int health, Behaviour behaviour) {
		super(health);
		ta = new TextureAtlas(getAtlasPath());
		this.behaviour = behaviour;
	}

	@Override
	public void start() {
		gameObject.setTag("enemy");
		if (((int) (Math.random() * 2)) == 0) transform.scale.x = -1;
		gameObject.renderer.setTextureAtlas(ta, 200, 200);
		gameObject.animator.setAnimations(Animations.load(getAnimationPath(), ta,
				new Validator("idle", "run", "jump", "fall", "land", "die_shoot_front", "die_shoot_back",
						"die_slash_front", "die_slash_back", "frozen_shatter")));
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
					break;
				case THAWING:
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
			gameObject.animator.shouldUpdate = true;
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
		gameObject.removeComponent(getClass());
		gameObject.removeComponent(Collider.class);
		gameObject.removeComponent(Rigidbody.class);
		behaviour = null;
		Level level = LevelManager.getCurrentLevel();
		if (level instanceof MissionLevel) {
			((MissionLevel) level).getMission().notifyEnemyDeath(this);
		}
		if (level instanceof SurvivalLevel) {
			((SurvivalLevel) level).survivalManager.score++;
		}
		spawnDrop();
	}

	private void spawnDrop() {
		String drop = dropTable.getDrop();
		if (drop != null) {
			Network.instantiate(drop, transform.position.offset(50, 0));
		}
	}

	protected abstract String getAtlasPath();

	protected abstract String getAnimationPath();
}

package com.ezardlabs.lostsector.objects.enemies.corpus.moas;

import com.ezardlabs.dethsquare.Collider;
import com.ezardlabs.dethsquare.Component;
import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.Script;
import com.ezardlabs.dethsquare.Time;
import com.ezardlabs.dethsquare.Vector2;
import com.ezardlabs.dethsquare.animation.Animation.AnimationListener;
import com.ezardlabs.dethsquare.animation.Animations;
import com.ezardlabs.dethsquare.animation.Animations.Validator;
import com.ezardlabs.dethsquare.animation.Animator;
import com.ezardlabs.dethsquare.graphics.Renderer;
import com.ezardlabs.lostsector.Game.DamageType;
import com.ezardlabs.lostsector.ai.MeleeBehaviour;
import com.ezardlabs.lostsector.objects.Entity;
import com.ezardlabs.lostsector.objects.enemies.corpus.Moa;

import java.util.ArrayList;

public class ShockwaveMoa extends Moa {

	public ShockwaveMoa() {
		super(new MeleeBehaviour.Builder().setMeleeRange(365)
										  .setMeleeDamage(1)
										  .setDamageFrame(7)
										  .setMoveSpeed(10)
										  .setVisionRange(2000)
										  .setWillPatrol(false)
										  .create());
	}

	@Override
	protected String getAtlasPath() {
		return "data/enemies/corpus/moa/shockwave";
	}

	@Override
	protected String getAnimationPath() {
		return "data/enemies/corpus/moa/shockwave";
	}

	@Override
	public void start() {
		super.start();
		//noinspection ConstantConditions
		gameObject.animator.getAnimation("attack").setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimatedStarted(Animator animator) {
			}

			@Override
			public void onFrame(Animator animator, int frameNum) {
				if (frameNum == 7) {
					GameObject.instantiate(new GameObject("Shockwave", new Renderer(), new Animator(
									Animations.load("animations/enemies/corpus/moa/shockwave/effect", ta,
											new Validator("shockwave"))), new Shockwave(transform.position.offset(100, 175))),
							transform.position.offset(-300, 100));
				}
			}

			@Override
			public void onAnimationFinished(Animator animator) {
			}
		});
	}

	private static class Shockwave extends Script {
		private final Vector2 origin;
		private GameObject left;
		private GameObject right;

		private Shockwave(Vector2 origin) {
			this.origin = origin;
		}

		@Override
		public void start() {
			left = GameObject.instantiate(new GameObject("Shockwave Left", new Renderer("images/pink.png", 50, 25),
					new Collider(50, 25, true), new ShockwaveCollision(origin)), origin.offset(-25, 0));
			right = GameObject.instantiate(new GameObject("Shockwave Right", new Renderer("images/pink.png", 50, 25),
					new Collider(50, 25, true), new ShockwaveCollision(origin)), new Vector2(origin));
			gameObject.animator.play("shockwave");
		}

		@Override
		public void update() {
			if (left.transform.gameObject != null && right.transform.gameObject != null) {
				left.transform.translate(-10 * Time.fpsScaling60, 0);
				right.transform.translate(10 * Time.fpsScaling60, 0);
			}
		}

		private static class ShockwaveCollision extends Component {
			private final ArrayList<Entity> damagedEntities = new ArrayList<>(4);
			private final Vector2 origin;

			private ShockwaveCollision(Vector2 origin) {
				this.origin = origin;
			}

			@Override
			public void onTriggerEnter(Collider other) {
				if ("player".equals(other.gameObject.getTag())) {
					Entity e = other.gameObject.getComponentOfType(Entity.class);
					if (e != null && !damagedEntities.contains(e)) {
						e.applyDamage(3, DamageType.NORMAL, origin);
						damagedEntities.add(e);
					}
				}
			}
		}
	}
}

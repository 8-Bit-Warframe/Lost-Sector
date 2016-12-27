package com.ezardlabs.lostsector.objects.enemies.corpus;

import com.ezardlabs.dethsquare.Animation;
import com.ezardlabs.dethsquare.Animation.AnimationListener;
import com.ezardlabs.dethsquare.AnimationType;
import com.ezardlabs.dethsquare.Animator;
import com.ezardlabs.dethsquare.TextureAtlas.Sprite;
import com.ezardlabs.lostsector.objects.enemies.Enemy;

public abstract class Moa extends Enemy {

	public Moa() {
		super(2);
	}

	protected Animation getRunAnimation() {
		return new Animation("run", new Sprite[]{ta.getSprite("run0"),
				ta.getSprite("run1"),
				ta.getSprite("run2"),
				ta.getSprite("run3"),
				ta.getSprite("run4"),
				ta.getSprite("run5")}, AnimationType.LOOP, 100);
	}

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

	protected Animation getShootAnimation() {
		return new Animation("shoot", new Sprite[]{ta.getSprite("shoot0"),
				ta.getSprite("shoot1")}, AnimationType.LOOP, 100);
	}

	private Animation getDieAnimation(String name) {
		return new Animation(name, new Sprite[]{ta.getSprite("die0"),
				ta.getSprite("die1"),
				ta.getSprite("die2"),
				ta.getSprite("die3"),
				ta.getSprite("die4"),
				ta.getSprite("die5"),
				ta.getSprite("die6")}, AnimationType.ONE_SHOT, 100);
	}

	protected Animation getDieShootFrontAnimation() {
		return getDieAnimation("die_shoot_front");
	}

	protected Animation getDieShootBackAnimation() {
		return getDieAnimation("die_shoot_back");
	}

	protected Animation getDieSlashFrontAnimation() {
		return getDieAnimation("die_slash_front");
	}

	protected Animation getDieSlashBackAnimation() {
		return getDieAnimation("die_slash_back");
	}

	protected Animation getDieKubrowFrontAnimation() {
		return getDieAnimation("die_kubrow_front");
	}

	protected Animation getDieKubrowBackAnimation() {
		return getDieAnimation("die_kubrow_back");
	}

	protected Animation getFrozenShatterAnimation() {
		return new Animation("frozen_shatter", new Sprite[]{ta.getSprite("frozen_shatter0"),
				ta.getSprite("frozen_shatter1"),
				ta.getSprite("frozen_shatter2"),
				ta.getSprite("frozen_shatter3"),
				ta.getSprite("frozen_shatter4"),
				ta.getSprite("frozen_shatter5"),
				ta.getSprite("frozen_shatter6"),
				ta.getSprite("frozen_shatter7"),
				ta.getSprite("frozen_shatter8"),
				ta.getSprite("frozen_shatter9")}, AnimationType.ONE_SHOT, 100,
				new AnimationListener() {
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
	}
}

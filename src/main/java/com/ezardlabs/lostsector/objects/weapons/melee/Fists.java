package com.ezardlabs.lostsector.objects.weapons.melee;

import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.TextureAtlas;
import com.ezardlabs.dethsquare.animation.Animation;
import com.ezardlabs.dethsquare.animation.Animation.AnimationListener;
import com.ezardlabs.dethsquare.animation.Animations;
import com.ezardlabs.dethsquare.animation.Animations.Validator;
import com.ezardlabs.dethsquare.animation.Animator;
import com.ezardlabs.lostsector.Game.DamageType;
import com.ezardlabs.lostsector.objects.weapons.MeleeWeapon;

public class Fists extends MeleeWeapon {
	private boolean finished = false;

	public Fists(GameObject wielder) {
		super("Fists", DamageType.NORMAL, wielder);
	}

	@Override
	public Animation[] getAnimations(TextureAtlas ta) {
		Animation[] animations = Animations.load("animations/weapons/melee/fists", ta, new Validator("punch"));
		animations[0].setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimatedStarted(Animator animator) {
				finished = false;
			}

			@Override
			public void onFrame(Animator animator, int frameNum) {
				if (frameNum == 2 || frameNum == 4) {
					damageEnemies(0.5f, animator.transform.position.x, animator.transform.position.y,
							animator.transform.position.x + animator.gameObject.renderer.width,
							animator.transform.position.y + animator.gameObject.renderer.height);
				}
			}

			@Override
			public void onAnimationFinished(Animator animator) {
				finished = true;
			}
		});
		return animations;
	}

	@Override
	public String getNextAnimation(int direction) {
		return "punch";
	}

	@Override
	public boolean isWaiting() {
		return true;
	}

	@Override
	public boolean shouldStow() {
		return finished;
	}

	@Override
	public boolean isStowed() {
		return finished;
	}
}

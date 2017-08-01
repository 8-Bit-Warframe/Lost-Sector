package com.ezardlabs.lostsector.objects.weapons.melee;

import com.ezardlabs.dethsquare.Animation;
import com.ezardlabs.dethsquare.Animation.AnimationListener;
import com.ezardlabs.dethsquare.Animator;
import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.TextureAtlas;
import com.ezardlabs.dethsquare.animation.Animations;
import com.ezardlabs.dethsquare.animation.Animations.Validator;
import com.ezardlabs.lostsector.Game.DamageType;
import com.ezardlabs.lostsector.objects.Player;
import com.ezardlabs.lostsector.objects.Player.State;
import com.ezardlabs.lostsector.objects.weapons.MeleeWeapon;

public class Fists extends MeleeWeapon {

	public Fists(GameObject wielder) {
		super("Fists", DamageType.NORMAL, wielder);
	}

	@Override
	public Animation[] getAnimations(TextureAtlas ta) {
		Animation[] animations = Animations.load("animations/weapons/melee/fists", ta, new Validator("punch"));
		animations[0].setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimatedStarted(Animator animator) {
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
				//noinspection ConstantConditions
				animator.gameObject.getComponent(Player.class).state = State.IDLE;
			}
		});
		return animations;
	}

	@Override
	public String getNextAnimation(int direction) {
		return "punch";
	}
}

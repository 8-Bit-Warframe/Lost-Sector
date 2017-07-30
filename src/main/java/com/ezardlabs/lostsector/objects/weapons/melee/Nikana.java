package com.ezardlabs.lostsector.objects.weapons.melee;

import com.ezardlabs.dethsquare.Animation;
import com.ezardlabs.dethsquare.Animation.AnimationListener;
import com.ezardlabs.dethsquare.Animator;
import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.TextureAtlas;
import com.ezardlabs.dethsquare.animation.Animations;
import com.ezardlabs.dethsquare.animation.Animations.Validator;
import com.ezardlabs.lostsector.Game;
import com.ezardlabs.lostsector.objects.Player;
import com.ezardlabs.lostsector.objects.Player.State;
import com.ezardlabs.lostsector.objects.weapons.MeleeWeapon;

import java.util.Timer;
import java.util.TimerTask;

public class Nikana extends MeleeWeapon implements AnimationListener {
	private Timer t;
	private boolean readyForNextAnimation = false;

	public Nikana(GameObject wielder) {
		super("Nikana", Game.DamageType.SLASH, wielder);
	}

	@Override
	public Animation[] getAnimations(TextureAtlas ta) {
		Animation[] animations = Animations.load("weapons/melee/nikana", ta,
				new Validator("slice1", "slice2", "slice3", "dash1", "dash2", "dash3", "stow"));
		for (Animation a : animations) {
			if (a.name.contains("slice") || a.name.contains("dash")) {
				a.setAnimationListener(this);
			}
			if ("stow".equals(a.name)) {
				a.setAnimationListener(new AnimationListener() {
					@Override
					public void onAnimatedStarted(Animator animator) {
					}

					@Override
					public void onFrame(Animator animator, int frameNum) {
					}

					@Override
					public void onAnimationFinished(Animator animator) {
						//noinspection ConstantConditions
						animator.gameObject.getComponent(Player.class).state = State.IDLE;
					}
				});
			}
		}
		return animations;
	}

	@Override
	public String getNextAnimation(int direction) {
		if (!readyForNextAnimation && currentAnimation != null) return currentAnimation;
		if (currentAnimation == null) {
			readyForNextAnimation = false;
			return currentAnimation = "slice1";
		}
		switch (currentAnimation) {
			case "slice1":
				return currentAnimation = "slice2";
			case "slice2":
				return currentAnimation = "slice3";
			case "slice3":
				if (direction == 0) return currentAnimation = "slice1";
				else return currentAnimation = "dash1";
			case "dash1":
				if (direction == 0) return currentAnimation = "slice1";
				else return currentAnimation = "dash2";
			case "dash2":
				if (direction == 0) return currentAnimation = "slice1";
				else return currentAnimation = "dash3";
			case "dash3":
				return currentAnimation = "slice1";
			default:
				return "slice1";
		}
	}

	@Override
	public void onAnimatedStarted(Animator animator) {
		if (t != null) {
			t.cancel();
		}
		int offSet = animator.gameObject.transform.scale.x < 0 ? -200 : 0;
		int damage = 0;
		switch (currentAnimation) {
			case "slice1":
				damage = 1;
				break;
			case "slice2":
				damage = 1;
				break;
			case "slice3":
				damage = 1;
				break;
			case "dash1":
				damage = 1;
				break;
			case "dash2":
				damage = 2;
				break;
			case "dash3":
				damage = 3;
				break;
			default:
				break;
		}
		damageEnemies(damage, animator.transform.position.x + offSet, animator.transform.position.y,
				animator.transform.position.x + animator.gameObject.renderer.width + offSet,
				animator.transform.position.y + animator.gameObject.renderer.height);

		readyForNextAnimation = false;
		if (currentAnimation.contains("slice1")) {
			animator.gameObject.rigidbody.velocity.x = 15 * animator.transform.scale.x;
		} else if (currentAnimation.contains("slice")) {
			animator.gameObject.rigidbody.velocity.x = 20 * animator.transform.scale.x;
		} else if (currentAnimation.contains("dash")) {
			animator.gameObject.rigidbody.velocity.x = 25 * animator.transform.scale.x;
		}
	}

	@Override
	public void onFrame(Animator animator, int frameNum) {
		if (currentAnimation.contains("slice") && frameNum == 1) {
			animator.gameObject.rigidbody.velocity.x = 0;
		}
		if (currentAnimation.contains("dash") && frameNum == 2) {
			animator.gameObject.rigidbody.velocity.x = 0;
		}
	}

	@Override
	public void onAnimationFinished(final Animator animator) {
		readyForNextAnimation = true;
		//noinspection ConstantConditions
		animator.gameObject.getComponent(Player.class).state = State.MELEE_WAITING;
		(t = new Timer()).schedule(new TimerTask() {
			@Override
			public void run() {
				animator.play("stow");
				currentAnimation = null;
			}
		}, 200);
	}
}

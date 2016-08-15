package com.ezardlabs.lostsector.objects.weapons.melee;

import com.ezardlabs.dethsquare.Animation;
import com.ezardlabs.dethsquare.AnimationType;
import com.ezardlabs.dethsquare.Animator;
import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.TextureAtlas;
import com.ezardlabs.lostsector.Game;
import com.ezardlabs.lostsector.objects.Player;
import com.ezardlabs.lostsector.objects.Player.State;
import com.ezardlabs.lostsector.objects.weapons.MeleeWeapon;

import java.util.Timer;
import java.util.TimerTask;

public class Nikana extends MeleeWeapon implements Animation.AnimationListener {
	private Timer t;
	private boolean readyForNextAnimation = false;

	public Nikana(GameObject wielder) {
		super("Nikana", Game.DamageType.SLASH, wielder);
	}

	@Override
	public Animation[] getAnimations(TextureAtlas ta) {
		return new Animation[]{new Animation("slice1", new TextureAtlas.Sprite[]{ta.getSprite("nikana_slice0"),
				ta.getSprite("nikana_slice1"),
				ta.getSprite("nikana_slice2"),
				ta.getSprite("nikana_slice3")}, AnimationType.ONE_SHOT, 100, this),
				new Animation("slice2", new TextureAtlas.Sprite[]{ta.getSprite("nikana_slice4"),
						ta.getSprite("nikana_slice5"),
						ta.getSprite("nikana_slice6"),
						ta.getSprite("nikana_slice7")}, AnimationType.ONE_SHOT, 100, this),
				new Animation("slice3", new TextureAtlas.Sprite[]{ta.getSprite("nikana_slice8"),
						ta.getSprite("nikana_slice9"),
						ta.getSprite("nikana_slice10"),
						ta.getSprite("nikana_slice11")}, AnimationType.ONE_SHOT, 100, this),
				new Animation("dash1", new TextureAtlas.Sprite[]{ta.getSprite("nikana_dash0"),
						ta.getSprite("nikana_dash1"),
						ta.getSprite("nikana_dash2"),
						ta.getSprite("nikana_dash3")}, AnimationType.ONE_SHOT, 100, this),
				new Animation("dash2", new TextureAtlas.Sprite[]{ta.getSprite("nikana_dash4"),
						ta.getSprite("nikana_dash5"),
						ta.getSprite("nikana_dash6"),
						ta.getSprite("nikana_dash7")}, AnimationType.ONE_SHOT, 100, this),
				new Animation("dash3", new TextureAtlas.Sprite[]{ta.getSprite("nikana_dash8"),
						ta.getSprite("nikana_dash9"),
						ta.getSprite("nikana_dash10"),
						ta.getSprite("nikana_dash11"),
						ta.getSprite("nikana_dash12")}, AnimationType.ONE_SHOT, 120, this),
				new Animation("stow", new TextureAtlas.Sprite[]{ta.getSprite("nikana_stow0")}, AnimationType.ONE_SHOT, 50, new Animation.AnimationListener() {
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
						animator.gameObject.renderer.setSize(200, 200);
						animator.gameObject.renderer.setOffsets(0, 0);
					}
				})};
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
		int offSet = animator.gameObject.renderer.hFlipped ? -200 : 0;
		int damage = 0;
		switch(currentAnimation) {
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
		}
		damageEnemies(damage, animator.transform.position.x + offSet, animator.transform.position.y, animator.transform.position.x + animator.gameObject.renderer.width + offSet, animator.transform.position.y + animator.gameObject.renderer.height);

		readyForNextAnimation = false;
		animator.gameObject.renderer.setSize(400, 300);
		animator.gameObject.renderer.setOffsets(-100, -100);
		if (currentAnimation.contains("slice1")) {
			animator.gameObject.rigidbody.velocity.x = 15 * (animator.gameObject.renderer.hFlipped ? -1
					: 1);
		} else if (currentAnimation.contains("slice")) {
			animator.gameObject.rigidbody.velocity.x = 20 * (animator.gameObject.renderer.hFlipped ? -1
					: 1);
		} else if (currentAnimation.contains("dash")) {
			animator.gameObject.rigidbody.velocity.x = 25 * (animator.gameObject.renderer.hFlipped ? -1
					: 1);
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

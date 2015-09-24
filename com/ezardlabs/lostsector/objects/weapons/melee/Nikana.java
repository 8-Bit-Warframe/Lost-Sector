package com.ezardlabs.lostsector.objects.weapons.melee;

import com.ezardlabs.dethsquare.Animation;
import com.ezardlabs.dethsquare.Animator;
import com.ezardlabs.dethsquare.TextureAtlas;
import com.ezardlabs.dethsquare.animationtypes.OneShotAnimation;
import com.ezardlabs.lostsector.objects.Player;
import com.ezardlabs.lostsector.objects.Player.State;
import com.ezardlabs.lostsector.objects.weapons.MeleeWeapon;

import java.util.Timer;
import java.util.TimerTask;

public class Nikana extends MeleeWeapon implements Animation.AnimationListener {
	private Timer t;
	private boolean readyForNextAnimation = false;

	public Nikana() {
		super("Nikana");
	}

	@Override
	public Animation[] getAnimations(TextureAtlas ta) {
		return new Animation[]{new Animation("slice1", new TextureAtlas.Sprite[]{ta.getSprite("nikana_slice0"),
				ta.getSprite("nikana_slice1"),
				ta.getSprite("nikana_slice2")}, new OneShotAnimation(), 100, this),
				new Animation("slice2", new TextureAtlas.Sprite[]{ta.getSprite("nikana_slice3"),
						ta.getSprite("nikana_slice4"),
						ta.getSprite("nikana_slice5")}, new OneShotAnimation(), 100, this),
				new Animation("slice3", new TextureAtlas.Sprite[]{ta.getSprite("nikana_slice6"),
						ta.getSprite("nikana_slice7"),
						ta.getSprite("nikana_slice8")}, new OneShotAnimation(), 100, this),
				new Animation("dash1", new TextureAtlas.Sprite[]{ta.getSprite("nikana_dash0"),
						ta.getSprite("nikana_dash1"),
						ta.getSprite("nikana_dash2")}, new OneShotAnimation(), 100, this),
				new Animation("dash2", new TextureAtlas.Sprite[]{ta.getSprite("nikana_dash3"),
						ta.getSprite("nikana_dash4"),
						ta.getSprite("nikana_dash5")}, new OneShotAnimation(), 100, this),
				new Animation("dash3", new TextureAtlas.Sprite[]{ta.getSprite("nikana_dash6"),
						ta.getSprite("nikana_dash7"),
						ta.getSprite("nikana_dash8")}, new OneShotAnimation(), 100, this),
				new Animation("stow", new TextureAtlas.Sprite[]{ta.getSprite("nikana_stow0")}, new OneShotAnimation(), 200, new Animation.AnimationListener() {
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
		if (currentAnimation == null) return currentAnimation = "slice1";
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
		readyForNextAnimation = false;
		animator.gameObject.renderer.setSize(400, 300);
		animator.gameObject.renderer.setOffsets(-100, -100);
        if (currentAnimation.contains("dash")) {
            animator.gameObject.rigidbody.velocity.x = 25 * (animator.gameObject.renderer.hFlipped ? -1
					: 1);
        }
	}

    @Override
    public void onFrame(Animator animator, int frameNum) {
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
		}, 300);
	}
}

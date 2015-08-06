package com.ezardlabs.lostsector.objects.warframes;

import com.ezardlabs.dethsquare.Animation;
import com.ezardlabs.dethsquare.Animation.AnimationListener;
import com.ezardlabs.dethsquare.Animation.AnimationType;
import com.ezardlabs.dethsquare.Animator;
import com.ezardlabs.dethsquare.Script;
import com.ezardlabs.dethsquare.TextureAtlas;
import com.ezardlabs.dethsquare.TextureAtlas.Sprite;

public abstract class Warframe extends Script {
	protected final TextureAtlas ta;

	public Warframe(String name) {
		ta = new TextureAtlas("images/warframes/" + name + "/atlas.png", "images/warframes/" + name + "/atlas.txt");
	}

	@Override
	public void start() {
		gameObject.renderer.setTextureAtlas(ta, 200, 200);
		gameObject.animator.setAnimations(getIdleAnimation(), getRunAnimation(), getJumpAnimation(), getDoubleJumpAnimation(), getFallAnimation(), getLandAnimation());
		gameObject.animator.play("idle");
	}

	protected Animation getIdleAnimation() {
		return new Animation("idle", new Sprite[]{ta.getSprite("idle0")}, AnimationType.ONE_SHOT, 0);
	}

	protected Animation getRunAnimation() {
		return new Animation("run", new Sprite[]{ta.getSprite("run0"), ta.getSprite("run1"), ta.getSprite("run2"), ta.getSprite("run3"), ta.getSprite("run4"), ta.getSprite("run5")},
				AnimationType.LOOP, 100);
	}

	protected Animation getJumpAnimation() {
		return new Animation("jump", new Sprite[]{ta.getSprite("jump0")}, AnimationType.ONE_SHOT, 0);
	}

	protected Animation getDoubleJumpAnimation() {
		return new Animation("doublejump",
				new Sprite[]{ta.getSprite("jump1"), ta.getSprite("jump2"), ta.getSprite("jump3"), ta.getSprite("jump4"), ta.getSprite("jump5"), ta.getSprite("jump6"), ta.getSprite("jump7")},
				AnimationType.ONE_SHOT, 50, new AnimationListener() {
			@Override
			public void onAnimatedStarted(Animator animator) {
			}

			@Override
			public void onFrame(int frameNum) {
			}

			@Override
			public void onAnimationFinished(Animator animator) {
				animator.play("fall");
			}
		});
	}

	protected Animation getFallAnimation() {
		return new Animation("fall", new Sprite[]{ta.getSprite("fall0"), ta.getSprite("fall1"), ta.getSprite("fall2")}, AnimationType.LOOP, 90);
	}

	protected Animation getLandAnimation() {
		return new Animation("land", new Sprite[]{ta.getSprite("land0"), ta.getSprite("land1"), ta.getSprite("land2")}, AnimationType.ONE_SHOT, 100);
	}

	public abstract void ability1();

	public abstract void ability2();

	public abstract void ability3();

	public abstract void ability4();
}

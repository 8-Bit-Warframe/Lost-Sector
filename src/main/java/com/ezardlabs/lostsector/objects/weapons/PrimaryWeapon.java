package com.ezardlabs.lostsector.objects.weapons;

import com.ezardlabs.dethsquare.animation.Animation;
import com.ezardlabs.dethsquare.animation.Animation.AnimationListener;
import com.ezardlabs.dethsquare.animation.AnimationType;
import com.ezardlabs.dethsquare.TextureAtlas;
import com.ezardlabs.dethsquare.animation.Animations;
import com.ezardlabs.dethsquare.animation.Animations.Validator;
import com.ezardlabs.dethsquare.animation.Animator;

public abstract class PrimaryWeapon extends Weapon2 implements AnimationListener {
	private boolean shooting = false;

	protected PrimaryWeapon(String name) {
		super(name);
	}

	@Override
	public void start() {
		TextureAtlas ta = TextureAtlas.load(getDataPath());
		Animation animation = Animations.load(getDataPath(), ta, new Validator("shoot"))[0];
		animation.setAnimationType(getAnimationType().clone());
		animation.setAnimationListener(this);
		gameObject.renderer.setTextureAtlas(ta);
		gameObject.renderer.setDepth(5);
		gameObject.animator.setAnimations(animation);
	}

	@Override
	public void update() {
		if (transform.getParent() != null) {
			transform.scale.x = transform.getParent().scale.x;
		}
	}

	public final boolean isShooting() {
		return shooting;
	}

	protected abstract String getDataPath();

	public abstract AnimationType getAnimationType();

	@Override
	public void onAnimatedStarted(Animator animator) {
		shooting = true;
	}

	@Override
	public void onFrame(Animator animator, int frameNum) {
		// unused
	}

	@Override
	public void onAnimationFinished(Animator animator) {
		shooting = false;
	}
}

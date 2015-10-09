package com.ezardlabs.lostsector.objects.weapons.primary;

import com.ezardlabs.dethsquare.Animation;
import com.ezardlabs.dethsquare.Animation.AnimationListener;
import com.ezardlabs.dethsquare.AnimationType;
import com.ezardlabs.dethsquare.Animator;
import com.ezardlabs.dethsquare.TextureAtlas;
import com.ezardlabs.dethsquare.TextureAtlas.Sprite;
import com.ezardlabs.lostsector.objects.weapons.RangedWeapon;

public class Lanka extends RangedWeapon {

	public Lanka() {
		super("Lanka");
	}

	@Override
	public Animation getAnimation(TextureAtlas ta) {
		return new Animation("fire", new Sprite[]{ta.getSprite("shoot0"), ta.getSprite("shoot1"), ta.getSprite("shoot2"), ta.getSprite("shoot3"), ta.getSprite("shoot4"), ta.getSprite("shoot5"), ta.getSprite("shoot6")},
				new AnimationType() {
					private int count = 0;

					@Override
					public int update(int currentFrame, int numFrames) {
						switch (currentFrame) {
							case 0:
							case 1:
							case 3:
							case 4:
							case 5:
							case 6:
								count = 0;
								return ++currentFrame;
							case 2:
								if (count++ == 4) return ++currentFrame;
								break;
						}
						return 0;
					}
				}, 125, new AnimationListener() {
			@Override
			public void onAnimatedStarted(Animator animator) {
//				animator.gameObject.renderer.setSize();
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

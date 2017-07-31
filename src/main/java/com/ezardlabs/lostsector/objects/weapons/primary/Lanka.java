package com.ezardlabs.lostsector.objects.weapons.primary;

import com.ezardlabs.dethsquare.Animation;
import com.ezardlabs.dethsquare.Animation.AnimationListener;
import com.ezardlabs.dethsquare.AnimationType;
import com.ezardlabs.dethsquare.Animator;
import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.TextureAtlas;
import com.ezardlabs.dethsquare.animation.Animations;
import com.ezardlabs.dethsquare.animation.Animations.Validator;
import com.ezardlabs.dethsquare.multiplayer.Network;
import com.ezardlabs.lostsector.objects.Player;
import com.ezardlabs.lostsector.objects.Player.State;
import com.ezardlabs.lostsector.objects.projectiles.LankaBeam;
import com.ezardlabs.lostsector.objects.weapons.RangedWeapon;

public class Lanka extends RangedWeapon {
	private GameObject player;

	public Lanka(GameObject player) {
		super("Lanka");
		this.player = player;
	}

	@Override
	public Animation getAnimation(TextureAtlas ta) {
		Animation[] animations = Animations.load("animations/weapons/primary/lanka", ta, new Validator("shoot"));
		Animation animation = animations[0];

		animation.setAnimationType(new AnimationType() {
			private int count = 0;
			private boolean beamCreated = false;

			@Override
			public int update(int currentFrame, int numFrames) {
				switch (currentFrame) {
					case 0:
						beamCreated = false;
					case 1:
					case 3:
					case 4:
					case 5:
						count = 0;
						return currentFrame + 1;
					case 6:
						return -1;
					case 2:
						if (count++ == 4) {
							return currentFrame + 1;
						} else {
							if (!beamCreated) {
								GameObject beam = Network.instantiate("lanka_beam", player.transform.position.offset(
										player.transform.scale.x < 0 ? -112.5f : 312.5f, 109.375f));
								beam.getComponent(LankaBeam.class).setDirection(player.transform.scale.x);
								beamCreated = true;
							}
							return 2;
						}
					default:
						break;
				}
				return 0;
			}
		});

		animation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimatedStarted(Animator animator) {
				animator.gameObject.renderer.setOffsets(animator.transform.scale.x < 0 ? -200 : 0, -100);
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

		return animation;

//		return new Animation("shoot", new Sprite[]{ta.getSprite("shoot0"),
//				ta.getSprite("shoot1"),
//				ta.getSprite("shoot2"),
//				ta.getSprite("shoot3"),
//				ta.getSprite("shoot4"),
//				ta.getSprite("shoot5"),
//				ta.getSprite("shoot6")}, new AnimationType() {
//			private int count = 0;
//			private boolean beamCreated = false;
//
//			@Override
//			public int update(int currentFrame, int numFrames) {
//				switch (currentFrame) {
//					case 0:
//						beamCreated = false;
//					case 1:
//					case 3:
//					case 4:
//					case 5:
//						count = 0;
//						return currentFrame + 1;
//					case 6:
//						return -1;
//					case 2:
//						if (count++ == 4) {
//							return currentFrame + 1;
//						} else {
//							if (!beamCreated) {
//								GameObject beam = Network.instantiate("lanka_beam", player.transform.position.offset(player.transform.scale.x < 0 ? -112.5f : 312.5f, 109.375f));
//								beam.getComponent(LankaBeam.class).setDirection(player.transform.scale.x);
//								beamCreated = true;
//							}
//							return 2;
//						}
//					default:
//						break;
//				}
//				return 0;
//			}
//		}, 125, new AnimationListener() {
//
//			@Override
//			public void onAnimatedStarted(Animator animator) {
//				animator.gameObject.renderer.setSize(400, 300);
//				animator.gameObject.renderer
//						.setOffsets(animator.transform.scale.x < 0 ? -200 : 0,
//								-100);
//			}
//
//			@Override
//			public void onFrame(Animator animator, int frameNum) {
//
//			}
//
//			@Override
//			public void onAnimationFinished(Animator animator) {
//				animator.gameObject.renderer.setSize(200, 200);
//				animator.gameObject.renderer.setOffsets(0, 0);
//				//noinspection ConstantConditions
//				animator.gameObject.getComponent(Player.class).state = State.IDLE;
//			}
//		});
	}
}

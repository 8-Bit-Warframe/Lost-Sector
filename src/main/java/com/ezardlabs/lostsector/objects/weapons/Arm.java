package com.ezardlabs.lostsector.objects.weapons;

import com.ezardlabs.dethsquare.Script;
import com.ezardlabs.dethsquare.animation.AnimationType;
import com.ezardlabs.lostsector.objects.warframes.Warframe;

public class Arm extends Script {
	private final Warframe warframe;

	public Arm(Warframe warframe) {
		this.warframe = warframe;
	}

	@Override
	public void start() {
		transform.setParent(warframe.transform);
		gameObject.renderer.setTextureAtlas(warframe.getTextureAtlas());
		gameObject.renderer.setDepth(2);
		gameObject.animator.setAnimations(warframe.gameObject.animator.getAnimation("grip_primary_arm"));
		gameObject.animator.getAnimation("grip_primary_arm").setAnimationType(new AnimationType() {
			@Override
			public int update(int currentFrame, int numFrames) {
				return currentFrame;
			}
		});
	}

	@Override
	public void update() {
		if (transform.getParent() != null) {
			transform.scale.x = transform.getParent().scale.x;
		}
	}
}

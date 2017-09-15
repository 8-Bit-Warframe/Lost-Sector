package com.ezardlabs.lostsector.objects.weapons;

import com.ezardlabs.dethsquare.AnimationType;
import com.ezardlabs.dethsquare.Script;
import com.ezardlabs.lostsector.objects.warframes.Warframe;

public class Arm extends Script {
	private final Warframe warframe;
	private String animationName;
	private AnimationType animationType;
	private boolean setAnimationType = false;

	public Arm(Warframe warframe) {
		this.warframe = warframe;
	}

	@Override
	public void start() {
		transform.setParent(warframe.transform);
	}

	@Override
	public void update() {
		if (setAnimationType) {
			System.out.println(animationName + ": " + gameObject.animator.getAnimation(animationName));
			//noinspection ConstantConditions
			gameObject.animator.getAnimation(animationName).setAnimationType(animationType);
			setAnimationType = false;
		}
	}

	public void setAnimationType(String animationName, AnimationType animationType) {
		this.animationName = animationName;
		this.animationType = animationType;
		this.setAnimationType = true;
	}
}

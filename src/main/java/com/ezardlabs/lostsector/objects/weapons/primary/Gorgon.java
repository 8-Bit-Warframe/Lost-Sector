package com.ezardlabs.lostsector.objects.weapons.primary;

import com.ezardlabs.dethsquare.Input;
import com.ezardlabs.dethsquare.Input.KeyCode;
import com.ezardlabs.dethsquare.animation.AnimationType;
import com.ezardlabs.lostsector.objects.weapons.PrimaryWeapon;

public class Gorgon extends PrimaryWeapon {

	public Gorgon() {
		super("Gorgon");
	}

	@Override
	protected String getDataPath() {
		return "data/weapons/primary/gorgon";
	}

	@Override
	public AnimationType getAnimationType() {
		return new AnimationType() {
			private boolean holstering = false;

			@Override
			public int update(int currentFrame, int numFrames) {
				switch (currentFrame) {
					case 0:
					case 1:
						holstering = !Input.getKey(KeyCode.MOUSE_RIGHT);
						if (holstering) {
							return currentFrame - 1;
						} else {
							return currentFrame + 1;
						}
					case 2:
					case 3:
					case 4:
					case 5:
						return currentFrame + 1;
					case 6:
						if (Input.getKey(KeyCode.MOUSE_RIGHT)) {
							return 2;
						} else {
							holstering = true;
							return 1;
						}
					default:
						return -1;
				}
			}
		};
	}
}

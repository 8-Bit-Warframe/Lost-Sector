package com.ezardlabs.lostsector.objects.weapons.primary;

import com.ezardlabs.dethsquare.animation.AnimationType;
import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.networking.Network;
import com.ezardlabs.lostsector.objects.projectiles.LankaBeam;
import com.ezardlabs.lostsector.objects.weapons.PrimaryWeapon;

public class Lanka extends PrimaryWeapon {

	public Lanka() {
		super("Lanka");
	}

	@Override
	protected String getDataPath() {
		return "data/weapons/primary/lanka";
	}

	@Override
	public AnimationType getAnimationType() {
		return new AnimationType() {
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
					case 6:
					case 7:
					case 8:
						count = 0;
						return currentFrame + 1;
					case 9:
						return -1;
					case 2:
						if (count++ == 4) {
							return currentFrame + 1;
						} else {
							if (!beamCreated) {
								GameObject beam = Network.instantiate("lanka_beam",
										transform.position.offset(transform.scale.x < 0 ? -112.5f : 312.5f, 109.375f));
								LankaBeam lankaBeam = beam.getComponent(LankaBeam.class);
								if (lankaBeam != null) {
									lankaBeam.setDirection(transform.scale.x);
								}
								beamCreated = true;
							}
							return 2;
						}
					default:
						break;
				}
				return 0;
			}
		};
	}
}

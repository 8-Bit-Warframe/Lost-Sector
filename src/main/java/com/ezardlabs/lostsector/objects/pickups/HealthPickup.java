package com.ezardlabs.lostsector.objects.pickups;

import com.ezardlabs.dethsquare.Collider;
import com.ezardlabs.dethsquare.networking.Network;
import com.ezardlabs.lostsector.objects.warframes.Warframe;

public class HealthPickup extends Pickup {
	private static final int HEALTH = 2;

	@Override
	protected String getAtlasPath() {
		return "data/pickups/health";
	}

	@Override
	protected String getAnimationPath() {
		return "data/pickups/health";
	}

	@Override
	public void onTriggerEnter(Collider other) {
		Warframe w = other.gameObject.getComponentOfType(Warframe.class);
		if (w != null && w.gameObject.playerId == Network.getPlayerId() && w.addHealth(HEALTH)) {
			Network.destroy(gameObject);
		}
	}
}

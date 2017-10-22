package com.ezardlabs.lostsector.objects.pickups;

import com.ezardlabs.dethsquare.Collider;
import com.ezardlabs.dethsquare.networking.Network;
import com.ezardlabs.lostsector.objects.warframes.Warframe;

public class EnergyPickup extends Pickup {
	private static final int ENERGY = 20;

	@Override
	protected String getAtlasPath() {
		return "data/pickups/energy";
	}

	@Override
	protected String getAnimationPath() {
		return "data/pickups/energy";
	}

	@Override
	public void onTriggerEnter(Collider other) {
		Warframe w = other.gameObject.getComponentOfType(Warframe.class);
		if (w != null && w.gameObject.playerId == Network.getPlayerId() && w.addEnergy(ENERGY)) {
			Network.destroy(gameObject);
		}
	}
}

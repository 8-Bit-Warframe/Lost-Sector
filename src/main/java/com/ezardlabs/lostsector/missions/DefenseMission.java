package com.ezardlabs.lostsector.missions;

import com.ezardlabs.lostsector.missions.objectives.Cryopod;

public class DefenseMission extends Mission {
	Cryopod cryopod = new Cryopod();

	@Override
	public void load() {

	}

	public void onCryopodDestroyed() {
	}
}

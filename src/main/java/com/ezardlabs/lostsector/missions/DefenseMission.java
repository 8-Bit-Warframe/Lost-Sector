package com.ezardlabs.lostsector.missions;

import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.prefabs.PrefabManager;
import com.ezardlabs.lostsector.map.MapManager;
import com.ezardlabs.lostsector.missions.objectives.Cryopod;

public class DefenseMission extends Mission {
	private GameObject[] cryopods;

	@Override
	public void load() {
		MapManager.loadMap("defense0");
		GameObject player = GameObject.instantiate(PrefabManager.loadPrefab("player"), MapManager.playerSpawn);

		instantiateCamera(player);

		cryopods = GameObject.findAllWithTag("cryopod");
		for (int i = 0; i < cryopods.length; i++) {
			cryopods[i].getComponent(Cryopod.class).setId(i);
		}
	}

	public GameObject[] getCryopods() {
		return cryopods;
	}

	public void onCryopodDestroyed(int id) {
		cryopods[id] = null;
	}
}

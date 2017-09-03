package com.ezardlabs.lostsector.missions;

import com.ezardlabs.dethsquare.Camera;
import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.Vector2;
import com.ezardlabs.dethsquare.prefabs.PrefabManager;
import com.ezardlabs.lostsector.camera.SmartCamera;
import com.ezardlabs.lostsector.map.MapManager;
import com.ezardlabs.lostsector.missions.objectives.Cryopod;

public class DefenseMission extends Mission {
	private Cryopod[] cryopods;

	@Override
	public void load() {
		MapManager.loadMap("defense0");
		GameObject player = GameObject.instantiate(PrefabManager.loadPrefab("player"), MapManager.playerSpawn);
		GameObject.instantiate(new GameObject("Camera", new Camera(true),
				new SmartCamera(player.transform, 1000, new Vector2(100, 100))), new Vector2());
		GameObject[] cryopodObjects = GameObject.findAllWithTag("cryopod");
		cryopods = new Cryopod[cryopodObjects.length];
		for (int i = 0; i < cryopodObjects.length; i++) {
			cryopods[i] = cryopodObjects[i].getComponent(Cryopod.class);
			cryopods[i].setId(i);
		}
	}

	public void onCryopodDestroyed(int id) {
		cryopods[id] = null;
		System.out.println("Cryopod " + id + " destroyed!");
	}
}

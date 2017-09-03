package com.ezardlabs.lostsector.missions;

import com.ezardlabs.dethsquare.Camera;
import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.Vector2;
import com.ezardlabs.dethsquare.prefabs.PrefabManager;
import com.ezardlabs.lostsector.camera.SmartCamera;
import com.ezardlabs.lostsector.map.MapManager;
import com.ezardlabs.lostsector.missions.objectives.Cryopod;

public class DefenseMission extends Mission {
	Cryopod cryopod = new Cryopod();

	@Override
	public void load() {
		MapManager.loadMap("defense0");
		GameObject player = GameObject.instantiate(PrefabManager.loadPrefab("player"), MapManager.playerSpawn);
		GameObject.instantiate(new GameObject("Camera", new Camera(true),
				new SmartCamera(player.transform, 1000, new Vector2(100, 100))), new Vector2());
	}

	public void onCryopodDestroyed() {
	}
}

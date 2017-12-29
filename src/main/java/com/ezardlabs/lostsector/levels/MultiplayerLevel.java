package com.ezardlabs.lostsector.levels;

import com.ezardlabs.dethsquare.Camera;
import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.Vector2;
import com.ezardlabs.dethsquare.networking.Network;
import com.ezardlabs.lostsector.camera.SmartCamera;
import com.ezardlabs.lostsector.map.MapManager;
import com.ezardlabs.lostsector.missions.DefenseMission;
import com.ezardlabs.lostsector.missions.Mission;

public class MultiplayerLevel extends MissionLevel<Mission> {
	public static Mission mission = new DefenseMission();

	public MultiplayerLevel() {
		super(new Mission() {
			@Override
			public void load() {
				MapManager.loadMap("test");
				GameObject player = Network.instantiate("player", MapManager.playerSpawn);
				GameObject.instantiate(new GameObject("Camera", new Camera(true),
						new SmartCamera(player.transform, 1000, new Vector2(100, 100))), MapManager.playerSpawn);
			}
		});
	}
}

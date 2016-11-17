package com.ezardlabs.lostsector.levels;

import com.ezardlabs.dethsquare.Camera;
import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.Level;
import com.ezardlabs.dethsquare.Vector2;
import com.ezardlabs.dethsquare.multiplayer.Network;
import com.ezardlabs.lostsector.map.MapManager;
import com.ezardlabs.lostsector.objects.CameraMovement;

public class MultiplayerLevel extends Level {

	@Override
	public void onLoad() {
		MapManager.playerSpawn = new Vector2(20, 20);
		MapManager.loadMap("test");

		GameObject player = Network.instantiate("player",
				new Vector2(MapManager.playerSpawn.x + Network.getPlayerId() * 200,
						MapManager.playerSpawn.y - 100));

		CameraMovement cm = new CameraMovement();

		GameObject.instantiate(new GameObject("Camera", new Camera(true), cm),
				new Vector2(MapManager.playerSpawn));

		cm.smoothFollow(player.transform);
	}
}

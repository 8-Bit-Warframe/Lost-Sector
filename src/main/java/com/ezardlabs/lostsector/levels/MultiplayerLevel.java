package com.ezardlabs.lostsector.levels;

import com.ezardlabs.dethsquare.Animator;
import com.ezardlabs.dethsquare.Camera;
import com.ezardlabs.dethsquare.Collider;
import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.Level;
import com.ezardlabs.dethsquare.Renderer;
import com.ezardlabs.dethsquare.Rigidbody;
import com.ezardlabs.dethsquare.Vector2;
import com.ezardlabs.dethsquare.multiplayer.Network;
import com.ezardlabs.dethsquare.multiplayer.NetworkAnimator;
import com.ezardlabs.dethsquare.multiplayer.NetworkTransform;
import com.ezardlabs.dethsquare.prefabs.PrefabManager;
import com.ezardlabs.lostsector.map.MapManager;
import com.ezardlabs.lostsector.objects.CameraMovement;
import com.ezardlabs.lostsector.objects.Player;
import com.ezardlabs.lostsector.objects.hud.HUD;
import com.ezardlabs.lostsector.objects.warframes.Frost;

public class MultiplayerLevel extends Level {

	@Override
	public void onLoad() {
		PrefabManager.addPrefab("player",
				() -> new GameObject("Player " + Network.getPlayerId(), new Player(),
						new Renderer(), new Animator(), new Frost(), new Collider(200, 200),
						new Rigidbody(), new NetworkTransform(), new NetworkAnimator()),
				() -> new GameObject("Player " + Network.getPlayerId(), new Renderer(),
						new Animator(), new Frost(), new NetworkTransform(),
						new NetworkAnimator()));

		MapManager.playerSpawn = new Vector2(20, 20);
		MapManager.loadMap("test");

		HUD.init();

		GameObject player = Network.instantiate("player",
				new Vector2(MapManager.playerSpawn.x + Network.getPlayerId() * 200,
						MapManager.playerSpawn.y - 100));

		CameraMovement cm = new CameraMovement();

		GameObject.instantiate(new GameObject("Camera", new Camera(true), cm),
				new Vector2(MapManager.playerSpawn));

		cm.smoothFollow(player.transform);
	}
}

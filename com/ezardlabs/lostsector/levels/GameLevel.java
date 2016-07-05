package com.ezardlabs.lostsector.levels;

import com.ezardlabs.dethsquare.Animator;
import com.ezardlabs.dethsquare.AudioSource;
import com.ezardlabs.dethsquare.AudioSource.AudioClip;
import com.ezardlabs.dethsquare.Camera;
import com.ezardlabs.dethsquare.Collider;
import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.Level;
import com.ezardlabs.dethsquare.Renderer;
import com.ezardlabs.dethsquare.Rigidbody;
import com.ezardlabs.dethsquare.Vector2;
import com.ezardlabs.lostsector.Game;
import com.ezardlabs.lostsector.MapManager;
import com.ezardlabs.lostsector.objects.CameraMovement;
import com.ezardlabs.lostsector.objects.Player;
import com.ezardlabs.lostsector.objects.enemies.corpus.crewmen.DeraCrewman;
import com.ezardlabs.lostsector.objects.hud.HUD;
import com.ezardlabs.lostsector.objects.warframes.Frost;

public class GameLevel extends Level {
	private static CameraMovement cm = new CameraMovement();

	@Override
	public void onLoad() {
		MapManager.loadMap("map");

		HUD.init();

		createPlayer();

		AudioSource as = new AudioSource();
		as.play(new AudioClip("audio/theme.ogg"));

		GameObject.instantiate(new GameObject("Camera", new Camera(true), cm, as), new Vector2());

		for (Vector2 pos : new Vector2[]{new Vector2(2528, 1856),
				new Vector2(1024, 992),
				new Vector2(1632, 192),
				new Vector2(2208, 416),
				new Vector2(928, 1856),
				new Vector2(1120, 1120),
				new Vector2(1696, 864),
				new Vector2(2624, 1344),
				new Vector2(320, 448),
				new Vector2(896, 640),
				new Vector2(1280, 448),
				new Vector2(1728, 1344),
				new Vector2(928, 1440),
				new Vector2(800, 640),
				new Vector2(416, 1280),
				new Vector2(1888, 1344),
				new Vector2(2720, 928),
				new Vector2(2560, 640),
				new Vector2(416, 1856),
				new Vector2(1696, 1856),
				new Vector2(192, 960),
				new Vector2(224, 1568),
				new Vector2(2112, 1056)}) {
			GameObject.instantiate(
					new GameObject("Dera Crewman", new Renderer(), new Animator(),
							new Collider(200, 200), new Rigidbody(), new DeraCrewman()),
					new Vector2(pos.x * 3.125f, pos.y * 3.125f));
		}
	}

	public static void createPlayer() {
		Game.players = new GameObject[]{new GameObject("Player", new Player(), new Renderer(),
				new Animator(), new Frost(), new Collider(200, 200), new Rigidbody())};
		GameObject.instantiate(Game.players[0], new Vector2(20, 20));

		cm.smoothFollow(Game.players[0].transform);
	}
}

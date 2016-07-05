package com.ezardlabs.lostsector.levels;

import com.ezardlabs.dethsquare.Animator;
import com.ezardlabs.dethsquare.AudioSource;
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
import com.ezardlabs.lostsector.objects.hud.HUD;
import com.ezardlabs.lostsector.objects.warframes.Frost;

public class TennoConLevel extends Level {
	private static CameraMovement cm = new CameraMovement();

	@Override
	public void onLoad() {
		MapManager.loadMap("TennoCon_map");

		HUD.init();

		createPlayer();

		AudioSource as = new AudioSource();
		as.play(new AudioSource.AudioClip("audio/theme.ogg"));

		GameObject.instantiate(new GameObject("Camera", new Camera(true), cm, as), new Vector2());
	}

	public static void createPlayer() {
		Game.players = new GameObject[]{new GameObject("Player", new Player(), new Renderer(),
				new Animator(), new Frost(), new Collider(200, 200), new Rigidbody())};
		GameObject.instantiate(Game.players[0], MapManager.playerSpawn);

		cm.smoothFollow(Game.players[0].transform);
	}
}

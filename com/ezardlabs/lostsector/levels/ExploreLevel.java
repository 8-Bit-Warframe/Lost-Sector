package com.ezardlabs.lostsector.levels;

import com.ezardlabs.dethsquare.*;
import com.ezardlabs.dethsquare.AudioSource.AudioClip;
import com.ezardlabs.lostsector.Game;
import com.ezardlabs.lostsector.MapManager;
import com.ezardlabs.lostsector.objects.CameraMovement;
import com.ezardlabs.lostsector.objects.Player;
import com.ezardlabs.lostsector.objects.hud.HUD;
import com.ezardlabs.lostsector.objects.warframes.Frost;

public class ExploreLevel extends Level {
	private static CameraMovement cm = new CameraMovement();

	@Override
	public void onLoad() {
		MapManager.playerSpawn = new Vector2(20.0f, 20.0f);
		MapManager.loadMap("Explore");

		HUD.init();

		createPlayer();

		AudioSource as = new AudioSource();

		GameObject.instantiate(new GameObject("Camera", new Camera(true), cm, as), new Vector2
				(MapManager.playerSpawn.x, MapManager.playerSpawn.y));
		as.play(new AudioClip("audio/theme.ogg"));

		TextureAtlas fontTA = new TextureAtlas("fonts/atlas.png", "fonts/atlas.txt");
		GameObject.instantiate(
				new GameObject(
						"MainMenuWIP",
						new GuiText("DEV BUILD : WORK IN PROGRESS!", fontTA, 30)
				),
				new Vector2(10, 10)
		);
	}

	public static void createPlayer() {
		Game.players = new GameObject[]{new GameObject("Player", new Player(), new Renderer(),
				new Animator(), new Frost(), new Collider(200, 200), new Rigidbody())};

		GameObject.instantiate(Game.players[0], new Vector2(MapManager.playerSpawn.x, MapManager.playerSpawn.y));

		cm.smoothFollow(Game.players[0].transform);
	}
}

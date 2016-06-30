package com.ezardlabs.lostsector.objects.levels;

import com.ezardlabs.dethsquare.Animator;
import com.ezardlabs.dethsquare.Camera;
import com.ezardlabs.dethsquare.Collider;
import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.GuiText;
import com.ezardlabs.dethsquare.Level;
import com.ezardlabs.dethsquare.Renderer;
import com.ezardlabs.dethsquare.Rigidbody;
import com.ezardlabs.dethsquare.TextureAtlas;
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
		MapManager.playerSpawn = new Vector2(20.0f, 20.0f);
		MapManager.loadMap("map");

		HUD.init();

		createPlayer();

		GameObject.instantiate(new GameObject("Camera", new Camera(true), cm), new Vector2(MapManager.playerSpawn.x, MapManager.playerSpawn.y));

//		GameObject.instantiate(new GameObject(null, new GuiText("Test message", new TextureAtlas("fonts/atlas.png", "fonts/atlas.txt"), 50)), new Vector2());
	}

	public static void createPlayer() {
		Game.players = new GameObject[]{new GameObject("Player", new Player(), new Renderer(),
				new Animator(), new Frost(), new Collider(200, 200), new Rigidbody())};

		GameObject.instantiate(Game.players[0], new Vector2(MapManager.playerSpawn.x, MapManager.playerSpawn.y));

		cm.smoothFollow(Game.players[0].transform);
	}
}

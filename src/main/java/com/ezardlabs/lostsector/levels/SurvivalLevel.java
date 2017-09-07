package com.ezardlabs.lostsector.levels;

import com.ezardlabs.dethsquare.Animator;
import com.ezardlabs.dethsquare.AudioSource;
import com.ezardlabs.dethsquare.AudioSource.AudioClip;
import com.ezardlabs.dethsquare.Camera;
import com.ezardlabs.dethsquare.Collider;
import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.GuiText;
import com.ezardlabs.dethsquare.Level;
import com.ezardlabs.dethsquare.Renderer;
import com.ezardlabs.dethsquare.Rigidbody;
import com.ezardlabs.dethsquare.Screen;
import com.ezardlabs.dethsquare.Script;
import com.ezardlabs.dethsquare.TextureAtlas;
import com.ezardlabs.dethsquare.Vector2;
import com.ezardlabs.dethsquare.multiplayer.Network;
import com.ezardlabs.lostsector.Game;
import com.ezardlabs.lostsector.SurvivalManager;
import com.ezardlabs.lostsector.map.MapManager;
import com.ezardlabs.lostsector.objects.CameraMovement;
import com.ezardlabs.lostsector.objects.Player;
import com.ezardlabs.lostsector.objects.warframes.Frost;

public class SurvivalLevel extends Level {
	private static CameraMovement cm = new CameraMovement();
	public SurvivalManager survivalManager = null;

	@Override
	public void onLoad() {
		MapManager.loadMap("Tiny_Sur");

		Game.players = new GameObject[]{Network.instantiate("player", new Vector2(MapManager
				.playerSpawn))};

		GameObject.instantiate(new GameObject("Camera", new Camera(true), cm,
						new AudioSource(new AudioClip("audio/theme.ogg"), true)),
				new Vector2(MapManager.playerSpawn));

		cm.smoothFollow(Game.players[0].transform);

		survivalManager = new SurvivalManager(MapManager.enemySpawns);
		GameObject.instantiate(new GameObject("SurvivalManager", survivalManager), new Vector2());

		TextureAtlas fontTA = TextureAtlas.load("fonts/atlas.png", "fonts/atlas.txt");
		GameObject.instantiate(
				new GameObject(
						"MainMenuWIP",
						new GuiText("DEV BUILD : WORK IN PROGRESS!", fontTA, 30)
				),
				new Vector2(10, Screen.height - 30 - 10)
		);

		GameObject.instantiate(new GameObject("Survival Score", new GuiText("", fontTA, 40), new
				Script() {
					private GuiText guiText;
					private int lastScore = -1;

					@Override
					public void start() {
						guiText = gameObject.getComponent(GuiText.class);
					}

					@Override
					public void update() {
						if (survivalManager.score != lastScore) {
							lastScore = survivalManager.score;
							guiText.setText("Enemies killed: " + lastScore);
						}
					}
				}),	new Vector2(10, 10));
	}

	public static void createPlayer() {
		Game.players = new GameObject[]{new GameObject("Player", new Player(), new Renderer(),
				new Animator(), new Frost(), new Collider(200, 200), new Rigidbody())};

		GameObject.instantiate(Game.players[0], new Vector2(MapManager.playerSpawn.x, MapManager.playerSpawn.y));

		cm.smoothFollow(Game.players[0].transform);
	}
}

package com.ezardlabs.lostsector.levels;

import com.ezardlabs.dethsquare.Camera;
import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.GuiText;
import com.ezardlabs.dethsquare.Level;
import com.ezardlabs.dethsquare.Screen;
import com.ezardlabs.dethsquare.Script;
import com.ezardlabs.dethsquare.TextureAtlas;
import com.ezardlabs.dethsquare.Vector2;
import com.ezardlabs.dethsquare.networking.Network;
import com.ezardlabs.lostsector.SurvivalManager;
import com.ezardlabs.lostsector.camera.SmartCamera;
import com.ezardlabs.lostsector.map.MapManager;

public class SurvivalLevel extends Level {
	public SurvivalManager survivalManager = null;

	@Override
	public void onLoad() {
		MapManager.loadMap("Tiny_Sur");

		GameObject player = Network.instantiate("player", new Vector2(MapManager.playerSpawn));
		GameObject.instantiate(new GameObject("Camera", new Camera(true), new SmartCamera(player.transform, 1000,
				new Vector2(100, 100))), MapManager.playerSpawn);

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
}

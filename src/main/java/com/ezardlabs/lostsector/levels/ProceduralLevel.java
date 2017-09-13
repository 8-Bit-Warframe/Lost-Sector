package com.ezardlabs.lostsector.levels;

import com.ezardlabs.dethsquare.Camera;
import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.GuiText;
import com.ezardlabs.dethsquare.Level;
import com.ezardlabs.dethsquare.Screen;
import com.ezardlabs.dethsquare.TextureAtlas;
import com.ezardlabs.dethsquare.Vector2;
import com.ezardlabs.dethsquare.multiplayer.Network;
import com.ezardlabs.lostsector.camera.SmartCamera;
import com.ezardlabs.lostsector.map.MapManager;
import com.ezardlabs.lostsector.map.procedural.MapConfig;

public class ProceduralLevel extends Level {

	@Override
	public void onLoad() {
		MapManager.playerSpawn = new Vector2(20.0f, 20.0f);
		MapConfig mapCfg = new MapConfig(MapConfig.ProceduralType.CORPUS, 16);
		MapManager.loadProceduralMap(mapCfg);

		GameObject player = Network.instantiate("player", new Vector2(MapManager.playerSpawn));
		GameObject.instantiate(new GameObject("Camera", new Camera(true), new SmartCamera(player.transform, 1000,
				new Vector2(100, 100))), MapManager.playerSpawn);

		TextureAtlas fontTA = TextureAtlas.load("fonts/atlas.png", "fonts/atlas.txt");
		GameObject.instantiate(new GameObject("MainMenuWIP",
						new GuiText("DEV BUILD : WORK IN PROGRESS!", fontTA, 30)),
				new Vector2(10, Screen.height - 30 - 10));
	}
}

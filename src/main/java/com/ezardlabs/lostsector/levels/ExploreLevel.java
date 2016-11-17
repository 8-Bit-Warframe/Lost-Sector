package com.ezardlabs.lostsector.levels;

import com.ezardlabs.dethsquare.AudioSource;
import com.ezardlabs.dethsquare.AudioSource.AudioClip;
import com.ezardlabs.dethsquare.Camera;
import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.GuiText;
import com.ezardlabs.dethsquare.Level;
import com.ezardlabs.dethsquare.Screen;
import com.ezardlabs.dethsquare.TextureAtlas;
import com.ezardlabs.dethsquare.Vector2;
import com.ezardlabs.dethsquare.multiplayer.Network;
import com.ezardlabs.lostsector.Game;
import com.ezardlabs.lostsector.map.MapManager;
import com.ezardlabs.lostsector.objects.CameraMovement;

public class ExploreLevel extends Level {
	private static CameraMovement cm = new CameraMovement();

	@Override
	public void onLoad() {
		MapManager.playerSpawn = new Vector2(20.0f, 20.0f);
		MapManager.loadMap("Explore");

		Game.players = new GameObject[]{Network.instantiate("player", new Vector2(MapManager
				.playerSpawn))};

		AudioSource as = new AudioSource();

		GameObject.instantiate(new GameObject("Camera", new Camera(true), cm, as), new Vector2
				(MapManager.playerSpawn));

		cm.smoothFollow(Game.players[0].transform);

		as.play(new AudioClip("audio/theme.ogg"));

		TextureAtlas fontTA = new TextureAtlas("fonts/atlas.png", "fonts/atlas.txt");
		GameObject.instantiate(
				new GameObject(
						"MainMenuWIP",
						new GuiText("DEV BUILD : WORK IN PROGRESS!", fontTA, 30)
				),
				new Vector2(10, Screen.height - 30 - 10)
		);
	}
}

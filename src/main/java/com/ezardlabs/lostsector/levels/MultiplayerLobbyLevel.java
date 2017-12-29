package com.ezardlabs.lostsector.levels;

import com.ezardlabs.dethsquare.Camera;
import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.GuiText;
import com.ezardlabs.dethsquare.Level;
import com.ezardlabs.dethsquare.LevelManager;
import com.ezardlabs.dethsquare.Screen;
import com.ezardlabs.dethsquare.TextureAtlas;
import com.ezardlabs.dethsquare.Vector2;
import com.ezardlabs.dethsquare.networking.Matchmaker;
import com.ezardlabs.dethsquare.networking.Matchmaker.MatchmakingListener;
import com.ezardlabs.dethsquare.networking.MatchmakingGame;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class MultiplayerLobbyLevel extends Level {

	@Override
	public void onLoad() {
		GuiText guiText = new GuiText("Searching for game...",
				TextureAtlas.load("fonts/atlas.png", "fonts/atlas.txt"), 50);
		GameObject.instantiate(new GameObject("Searching", guiText),
				new Vector2(Screen.width / 2 - guiText.getWidth() / 2, Screen.height / 2 - 25));
		GameObject.instantiate(new GameObject("Camera", new Camera(true)), new Vector2());

		InetAddress ip = null;
		try {
			ip = InetAddress.getByName("localhost");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		Matchmaker matchmaker = new Matchmaker(ip, 3000);
		matchmaker.findGame(new MatchmakingListener() {
			@Override
			public boolean onCreateGame() {
				LevelManager.loadLevel("multiplayer");
				return true;
			}

			@Override
			public void onGameFound(MatchmakingGame game) {
				LevelManager.loadLevel("multiplayer");
			}

			@Override
			public void onError(String error) {
				System.err.println(error);
			}
		});
	}
}

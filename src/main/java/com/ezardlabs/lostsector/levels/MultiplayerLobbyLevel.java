package com.ezardlabs.lostsector.levels;

import com.ezardlabs.dethsquare.Camera;
import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.GuiText;
import com.ezardlabs.dethsquare.Level;
import com.ezardlabs.dethsquare.LevelManager;
import com.ezardlabs.dethsquare.Screen;
import com.ezardlabs.dethsquare.TextureAtlas;
import com.ezardlabs.dethsquare.Vector2;
import com.ezardlabs.dethsquare.multiplayer.Network;

public class MultiplayerLobbyLevel extends Level {

	@Override
	public void onLoad() {
		GuiText guiText = new GuiText("Searching for game...",
				new TextureAtlas("fonts/atlas.png", "fonts/atlas.txt"), 50);
		GameObject.instantiate(new GameObject("Searching", guiText),
				new Vector2(Screen.width / 2 - guiText.getWidth() / 2, Screen.height / 2 - 25));
		GameObject.instantiate(new GameObject("Camera", new Camera(true)), new Vector2());

		Network.findGame(state -> {
//			try {
				switch (state) {
					case MATCHMAKING_SEARCHING:
						guiText.setText("Searching for a game...");
						break;
					case MATCHMAKING_FOUND:
						guiText.setText("Game found");
//						Thread.sleep(1000);
						break;
					case GAME_CONNECTING:
						guiText.setText("Connecting players...");
//						Thread.sleep(1000);
						break;
					case GAME_CONNECTED:
						guiText.setText("Players connected");
//						Thread.sleep(1000);


//						new Timer().schedule(new TimerTask() {
//							@Override
//							public void run() {
//								LevelManager.loadLevel("multiplayer");
//							}
//						}, 1000);
						LevelManager.loadLevel("multiplayer");
						break;
					default:
						break;
				}
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
		});
	}
}

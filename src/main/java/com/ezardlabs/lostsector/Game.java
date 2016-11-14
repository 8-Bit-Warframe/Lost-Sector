package com.ezardlabs.lostsector;

import com.ezardlabs.dethsquare.Animator;
import com.ezardlabs.dethsquare.Collider;
import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.LevelManager;
import com.ezardlabs.dethsquare.Renderer;
import com.ezardlabs.dethsquare.Rigidbody;
import com.ezardlabs.dethsquare.TextureAtlas;
import com.ezardlabs.dethsquare.multiplayer.Network;
import com.ezardlabs.dethsquare.multiplayer.NetworkAnimator;
import com.ezardlabs.dethsquare.multiplayer.NetworkTransform;
import com.ezardlabs.dethsquare.prefabs.PrefabManager;
import com.ezardlabs.dethsquare.util.BaseGame;
import com.ezardlabs.lostsector.levels.ExploreLevel;
import com.ezardlabs.lostsector.levels.MainMenuLevel;
import com.ezardlabs.lostsector.levels.MultiplayerLevel;
import com.ezardlabs.lostsector.levels.MultiplayerLobbyLevel;
import com.ezardlabs.lostsector.levels.ProceduralLevel;
import com.ezardlabs.lostsector.levels.SurvivalLevel;
import com.ezardlabs.lostsector.objects.Player;
import com.ezardlabs.lostsector.objects.environment.Door;
import com.ezardlabs.lostsector.objects.warframes.Frost;

public class Game extends BaseGame {
	public static GameObject[] players;

	public enum DamageType {
		NORMAL,
		SLASH,
		COLD,
		KUBROW
	}

	@Override
	public void create() {
		LevelManager.registerLevel("explore", new ExploreLevel());
		LevelManager.registerLevel("survival", new SurvivalLevel());
		LevelManager.registerLevel("procedural", new ProceduralLevel());
		LevelManager.registerLevel("multiplayer_lobby", new MultiplayerLobbyLevel());
		LevelManager.registerLevel("multiplayer", new MultiplayerLevel());
		LevelManager.registerLevel("main_menu", new MainMenuLevel());

		registerPlayerPrefabs();

		LevelManager.loadLevel("main_menu");
	}

	private void registerPlayerPrefabs() {
		PrefabManager.addPrefab("player",
				() -> new GameObject("Player " + Network.getPlayerId(), new Player(), new HUD(),
						new Renderer(), new Animator(), new Frost(), new Collider(200, 200),
						new Rigidbody(), new NetworkTransform(), new NetworkAnimator()),
				() -> new GameObject("Player " + Network.getPlayerId(), new Renderer(),
						new Animator(), new Frost(), new NetworkTransform(),
						new NetworkAnimator()));
	}

	private void registerDoorPrefabs() {
		TextureAtlas ta = new TextureAtlas("images/environment/atlas.png",
				"images/environment/atlas.txt");
		PrefabManager.addPrefab("door",
				() -> new GameObject("Door", new Door(ta), new Renderer(), new Animator(),
						new Collider(50, 500, true), new NetworkAnimator()));
	}
}
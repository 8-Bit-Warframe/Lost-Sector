package com.ezardlabs.lostsector;

import com.ezardlabs.dethsquare.AudioManager;
import com.ezardlabs.dethsquare.PlayerPrefs;
import com.ezardlabs.dethsquare.animation.Animator;
import com.ezardlabs.dethsquare.AudioListener;
import com.ezardlabs.dethsquare.Collider;
import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.LevelManager;
import com.ezardlabs.dethsquare.graphics.Renderer;
import com.ezardlabs.dethsquare.Rigidbody;
import com.ezardlabs.dethsquare.TextureAtlas;
import com.ezardlabs.dethsquare.multiplayer.NetworkAnimator;
import com.ezardlabs.dethsquare.multiplayer.NetworkRenderer;
import com.ezardlabs.dethsquare.multiplayer.NetworkTransform;
import com.ezardlabs.dethsquare.prefabs.PrefabManager;
import com.ezardlabs.dethsquare.util.BaseGame;
import com.ezardlabs.lostsector.levels.DefenseLevel;
import com.ezardlabs.lostsector.levels.ExploreLevel;
import com.ezardlabs.lostsector.levels.MainMenuLevel;
import com.ezardlabs.lostsector.levels.MultiplayerLevel;
import com.ezardlabs.lostsector.levels.MultiplayerLobbyLevel;
import com.ezardlabs.lostsector.levels.ProceduralLevel;
import com.ezardlabs.lostsector.levels.SurvivalLevel;
import com.ezardlabs.lostsector.missions.SpawnPoint;
import com.ezardlabs.lostsector.objects.Player;
import com.ezardlabs.lostsector.objects.enemies.corpus.crewmen.DeraCrewman;
import com.ezardlabs.lostsector.objects.enemies.corpus.crewmen.ProvaCrewman;
import com.ezardlabs.lostsector.objects.enemies.corpus.crewmen.SupraCrewman;
import com.ezardlabs.lostsector.objects.enemies.corpus.moas.ShockwaveMoa;
import com.ezardlabs.lostsector.objects.environment.Door;
import com.ezardlabs.lostsector.objects.environment.LaserDoor;
import com.ezardlabs.lostsector.objects.environment.Locker;
import com.ezardlabs.lostsector.objects.hud.HUD;
import com.ezardlabs.lostsector.objects.pickups.EnergyPickup;
import com.ezardlabs.lostsector.objects.pickups.HealthPickup;
import com.ezardlabs.lostsector.objects.projectiles.LankaBeam;
import com.ezardlabs.lostsector.objects.warframes.Frost;
import com.ezardlabs.lostsector.objects.warframes.abilities.frost.Avalanche;
import com.ezardlabs.lostsector.objects.warframes.abilities.frost.Freeze;
import com.ezardlabs.lostsector.objects.warframes.abilities.frost.IceWave;
import com.ezardlabs.lostsector.objects.warframes.abilities.frost.Snowglobe;

public class Game extends BaseGame {
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
		LevelManager.registerLevel("defense", new DefenseLevel());
		LevelManager.registerLevel("multiplayer_lobby", new MultiplayerLobbyLevel());
		LevelManager.registerLevel("multiplayer", new MultiplayerLevel());
		LevelManager.registerLevel("main_menu", new MainMenuLevel());

		loadSettings();

		registerPlayerPrefabs();
		registerWarframeAbilityPrefabs();
		registerProjectilePrefabs();
		registerDoorPrefabs();
		registerLockerPrefabs();
		registerEnemyPrefabs();
		registerPickupPrefabs();
		registerSpawnPointPrefabs();

		LevelManager.loadLevel("defense");
	}

	private void loadSettings() {
		AudioManager.setMasterVolume(PlayerPrefs.getInt("audio_master_volume", 5) / 15f);
		AudioManager.setMusicVolume(PlayerPrefs.getInt("audio_music_volume", 5) / 15f);
		AudioManager.setSfxVolume(PlayerPrefs.getInt("audio_sfx_volume", 5) / 15f);
	}

	private void registerPlayerPrefabs() {
		PrefabManager.addPrefab("player",
				() -> new GameObject("Player", "player", new Player(), new HUD(), new Renderer(), new Animator(),
						new Frost(), new Collider(112.5f, 156.25f, 43.75f, 43.75f), new Rigidbody(),
						new AudioListener(), new NetworkTransform(), new NetworkRenderer(), new NetworkAnimator()),
				() -> new GameObject("Other Player", "player", new Renderer(), new Animator(), new Frost(),
						new Collider(112.5f, 156.25f, 43.75f, 43.75f), new NetworkTransform(), new NetworkRenderer(), new NetworkAnimator()));
	}

	private void registerWarframeAbilityPrefabs() {
		PrefabManager.addPrefab("freeze",
				() -> new GameObject("Freeze", new Renderer(), new Animator(),
						new Collider(100, 100, true), new Freeze(),
						new NetworkTransform(), new NetworkAnimator()));
		PrefabManager.addPrefab("ice_wave",
				() -> new GameObject("Ice Wave", new Renderer(), new Animator(),
						new IceWave(), new NetworkAnimator()));
		PrefabManager.addPrefab("snowglobe",
				() -> new GameObject("Snowglobe", new Renderer(),
						new Animator(), new Collider(800, 600), new Snowglobe(),
						new NetworkAnimator()));
		PrefabManager.addPrefab("avalanche",
				() -> new GameObject("Avalanche", new Avalanche()));
	}

	private void registerProjectilePrefabs() {
		PrefabManager.addPrefab("lanka_beam",
				() -> new GameObject("Lanka Beam", new Renderer("images/blue.png", 0, 0),
						new LankaBeam(500), new NetworkTransform(), new NetworkRenderer()),
				() -> new GameObject("Lanka Beam", new Renderer("images/blue.png", 0, 0),
						new NetworkTransform(), new NetworkRenderer()));
	}

	private void registerDoorPrefabs() {
		PrefabManager.addPrefab("door", () -> new GameObject("Door", true, new Door(
				TextureAtlas.load("images/environment/atlas.png", "images/environment/atlas.txt")),
				new Renderer(), new Animator(), new Collider(100, 500, true)));
		PrefabManager.addPrefab("laser_door", () -> new GameObject("Laser Door", true,
				new LaserDoor(TextureAtlas.load("images/environment/atlas.png",
						"images/environment/atlas.txt")), new Renderer(), new Animator(),
				new Collider(100, 500, true)));
	}

	private void registerLockerPrefabs() {
		PrefabManager.addPrefab("locker_locked",
				() -> new GameObject("Locker", true, new Renderer(), new Locker(true,
						TextureAtlas.load("images/environment/atlas.png",
								"images/environment/atlas.txt"))));
		PrefabManager.addPrefab("locker_unlocked",
				() -> new GameObject("Locker", true, new Renderer(), new Locker(false,
						TextureAtlas.load("images/environment/atlas.png",
								"images/environment/atlas.txt")), new Collider(100, 200, true),
						new Animator(), new NetworkAnimator()),
				() -> new GameObject("Locker", true, new Renderer(), new Locker(false,
						TextureAtlas.load("images/environment/atlas.png",
								"images/environment/atlas.txt")), new Animator(),
						new NetworkAnimator()));
	}

	private void registerEnemyPrefabs() {
		PrefabManager.addPrefab("dera_crewman",
				() -> new GameObject("Dera Crewman", new Renderer(), new Animator(),
						new Collider(200, 200), new Rigidbody(), new DeraCrewman(),
						new NetworkTransform(), new NetworkRenderer(), new NetworkAnimator()),
				() -> new GameObject("Dera Crewman", new Renderer(), new Animator(),
						new Collider(200, 200), new NetworkTransform(), new NetworkRenderer(),
						new NetworkAnimator()));
		PrefabManager.addPrefab("prova_crewman",
				() -> new GameObject("Prova Crewman", new Renderer(), new Animator(),
						new Collider(200, 200), new Rigidbody(), new ProvaCrewman(),
						new NetworkTransform(), new NetworkRenderer(), new NetworkAnimator()),
				() -> new GameObject("Prova Crewman", new Renderer(), new Animator(),
						new Collider(200, 200), new NetworkTransform(), new NetworkRenderer(),
						new NetworkAnimator()));
		PrefabManager.addPrefab("supra_crewman",
				() -> new GameObject("Supra Crewman", new Renderer(), new Animator(),
						new Collider(200, 200), new Rigidbody(), new SupraCrewman(),
						new NetworkTransform(), new NetworkRenderer(), new NetworkAnimator()),
				() -> new GameObject("Supra Crewman", new Renderer(), new Animator(),
						new Collider(200, 200), new NetworkTransform(), new NetworkRenderer(),
						new NetworkAnimator()));
		PrefabManager.addPrefab("shockwave_moa",
				() -> new GameObject("Shockwave Moa", new Renderer(), new Animator(), new Collider(200, 200),
						new Rigidbody(), new ShockwaveMoa(), new NetworkTransform(), new NetworkRenderer(),
						new NetworkAnimator()),
				() -> new GameObject("Shockwave Moa", new Renderer(), new Animator(), new Collider(200, 200),
						new Rigidbody(), new NetworkTransform(), new NetworkRenderer(),
						new NetworkAnimator()));
	}

	private void registerPickupPrefabs() {
		PrefabManager.addPrefab("pickup_health", () -> new GameObject("Health Pickup", new Renderer(), new Animator
				(), new Collider(100, 100), new Rigidbody(), new HealthPickup()));
		PrefabManager.addPrefab("pickup_energy", () -> new GameObject("Energy Pickup", new Renderer(), new Animator
				(), new Collider(100, 100), new Rigidbody(), new EnergyPickup()));
	}

	private void registerSpawnPointPrefabs() {
		PrefabManager.addPrefab("spawn_point", () -> new GameObject("Spawn Point", new SpawnPoint()));
	}
}
package com.ezardlabs.lostsector.missions;

import com.ezardlabs.dethsquare.audio.AudioSource;
import com.ezardlabs.dethsquare.audio.AudioSource.AudioClip;
import com.ezardlabs.dethsquare.Camera;
import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.GuiText;
import com.ezardlabs.dethsquare.TextureAtlas;
import com.ezardlabs.dethsquare.Vector2;
import com.ezardlabs.dethsquare.prefabs.PrefabManager;
import com.ezardlabs.lostsector.map.MapManager;
import com.ezardlabs.lostsector.map.procedural.MapConfig;
import com.ezardlabs.lostsector.missions.Mission.EnemyStatusListener;
import com.ezardlabs.lostsector.objects.CameraMovement;
import com.ezardlabs.lostsector.objects.CameraMovement.FollowType;
import com.ezardlabs.lostsector.objects.enemies.Enemy;

public class ExterminateMission extends Mission implements EnemyStatusListener {
	private final int numEnemiesToKill;
	private GuiText missionText;

	public ExterminateMission(int numEnemiesToKill) {
		this.numEnemiesToKill = numEnemiesToKill;
		TextureAtlas ta = TextureAtlas.load("fonts/atlas.png",
				"fonts/atlas.txt");
		missionText = new GuiText("Enemies remaining: " + numEnemiesToKill, ta,
				40);
	}

	@Override
	public void load() {
		// TODO force a certain number of enemies to spawn - no more, no less
		MapManager.playerSpawn = new Vector2(20.0f, 20.0f);
		MapConfig mapCfg = new MapConfig(MapConfig.ProceduralType.CORPUS, 16);
		MapManager.loadProceduralMap(mapCfg);

		GameObject player = GameObject
				.instantiate(PrefabManager.loadPrefab("player"),
						MapManager.playerSpawn);

		GameObject.instantiate(new GameObject("Camera", new Camera(true),
						new CameraMovement(player.transform, FollowType.SMOOTH),
						new AudioSource(new AudioClip("audio/theme.ogg"), true)),
				new Vector2(MapManager.playerSpawn));

		GameObject.instantiate(
				new GameObject("Mission Progress Text", missionText),
				new Vector2(20, 20));
		addEnemyStatusListener(this);
	}

	@Override
	public void onEnemySpawned(Enemy enemy) {
	}

	@Override
	public void onEnemyKilled(Enemy enemy) {
		missionText.setText(
				"Enemies remaining: " + (numEnemiesToKill - enemiesKilled));
	}
}

package com.ezardlabs.lostsector.missions;

import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.Level;
import com.ezardlabs.dethsquare.LevelManager;
import com.ezardlabs.dethsquare.Script;
import com.ezardlabs.dethsquare.prefabs.PrefabManager;
import com.ezardlabs.lostsector.levels.MissionLevel;

import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.concurrent.ThreadLocalRandom;

public class SpawnPoint extends Script {
	private static final LinkedHashMap<String, Float> ENEMY_CHANCES = new LinkedHashMap<>();
	private static final long MIN_INTERVAL = 2000;
	private static final long MAX_INTERVAL = 20000;
	private static final int MAX_ENEMIES = 20;

	private long nextSpawn = 0;
	private Mission currentMission;

	static {
		ENEMY_CHANCES.put("dera_crewman", 0.3f);
		ENEMY_CHANCES.put("prova_crewman", 0.5f);
		ENEMY_CHANCES.put("supra_crewman", 0.2f);
	}

	@Override
	public void start() {
		Level level = LevelManager.getCurrentLevel();
		if (level instanceof MissionLevel) {
			currentMission = ((MissionLevel) level).getMission();
		}
	}

	@Override
	public void update() {
		if (System.currentTimeMillis() > nextSpawn) {
			if (currentMission == null || currentMission.numEnemies < MAX_ENEMIES) {
				String enemyPrefab = getSpawn();
				if (enemyPrefab != null) {
					GameObject.instantiate(PrefabManager.loadPrefab(getSpawn()), transform.position);
				}
			}
			nextSpawn = System.currentTimeMillis() + ThreadLocalRandom.current().nextLong(MIN_INTERVAL, MAX_INTERVAL + 1);
		}
	}

	private static String getSpawn() {
		double rand = Math.random();
		float total = 0;

		for (Entry<String, Float> entry : ENEMY_CHANCES.entrySet()) {
			if (rand < entry.getValue() + total) {
				return entry.getKey();
			} else {
				total += entry.getValue();
			}
		}
		return null;
	}
}

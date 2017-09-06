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
	private static final LinkedHashMap<String, Float> enemyChances = new LinkedHashMap<>();
	private static final long minInterval = 2000;
	private static final long maxInterval = 20000;
	private static final int maxEnemies = 20;

	private long nextSpawn = 0;
	private Mission currentMission;

	static {
		enemyChances.put("dera_crewman", 0.3f);
		enemyChances.put("prova_crewman", 0.5f);
		enemyChances.put("supra_crewman", 0.2f);
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
			if (currentMission == null || currentMission.numEnemies < maxEnemies) {
				String enemyPrefab = getSpawn();
				if (enemyPrefab != null) {
					GameObject.instantiate(PrefabManager.loadPrefab(getSpawn()), transform.position);
				}
			}
			nextSpawn = System.currentTimeMillis() + ThreadLocalRandom.current().nextLong(minInterval, maxInterval + 1);
		}
	}

	private static String getSpawn() {
		double rand = Math.random();
		float total = 0;

		for (Entry<String, Float> entry : enemyChances.entrySet()) {
			if (rand < entry.getValue() + total) {
				return entry.getKey();
			} else {
				total += entry.getValue();
			}
		}
		return null;
	}
}

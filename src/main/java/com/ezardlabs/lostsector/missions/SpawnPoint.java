package com.ezardlabs.lostsector.missions;

import com.ezardlabs.dethsquare.Level;
import com.ezardlabs.dethsquare.LevelManager;
import com.ezardlabs.dethsquare.Script;
import com.ezardlabs.dethsquare.Vector2;
import com.ezardlabs.dethsquare.networking.Network;
import com.ezardlabs.lostsector.levels.MissionLevel;

import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.concurrent.ThreadLocalRandom;

public class SpawnPoint extends Script {
	private static SpawnMode mode = SpawnMode.SINGLE;
	private static int maxEnemies = 0;
	private static int numEnemies = 0;
	private static long minInterval = 0;
	private static long maxInterval = 0;
	private static LinkedHashMap<String, Float> spawnProbabilities;

	private long nextSpawn = 0;
	private Mission mission;

	private enum SpawnMode {
		SINGLE,
		CONTINUOUS,
		WAVE
	}

	@Override
	public void start() {
		Level level = LevelManager.getCurrentLevel();
		if (level instanceof MissionLevel) {
			mission = ((MissionLevel) level).getMission();
		}
	}

	@Override
	public void update() {
		if (System.currentTimeMillis() > nextSpawn) {
			switch (mode) {
				case SINGLE:
					if (numEnemies < maxEnemies) {
						spawnEnemy(transform.position);
						numEnemies++;
					}
					break;
				case CONTINUOUS:
					if (mission == null || mission.numEnemies < maxEnemies) {
						spawnEnemy(transform.position);
					}
					break;
				case WAVE:
					if (numEnemies < maxEnemies) {
						spawnEnemy(transform.position);
						numEnemies++;
					}
					break;
				default:
					break;
			}
			nextSpawn = System.currentTimeMillis() + ThreadLocalRandom.current().nextLong(minInterval, maxInterval + 1);
		}
	}

	private static void spawnEnemy(Vector2 position) {
		String enemyPrefab = getSpawn();
		if (enemyPrefab != null) {
			Network.instantiate(getSpawn(), position);
		}
	}

	private static String getSpawn() {
		double rand = Math.random();
		float total = 0;

		for (Entry<String, Float> entry : spawnProbabilities.entrySet()) {
			if (rand < entry.getValue() + total) {
				return entry.getKey();
			} else {
				total += entry.getValue();
			}
		}
		return null;
	}

	public static void setSingleSpawnMode(int numEnemies, long minInterval, long maxInterval,
			LinkedHashMap<String, Float> spawnProbabilities) {
		SpawnPoint.mode = SpawnMode.SINGLE;
		SpawnPoint.maxEnemies = numEnemies;
		SpawnPoint.numEnemies = 0;
		SpawnPoint.minInterval = minInterval;
		SpawnPoint.maxInterval = maxInterval;
		SpawnPoint.spawnProbabilities = spawnProbabilities;
	}

	public static void setContinuousSpawnMode(int maxEnemies, long minInterval, long maxInterval,
			LinkedHashMap<String, Float> spawnProbabilities) {
		SpawnPoint.mode = SpawnMode.CONTINUOUS;
		SpawnPoint.maxEnemies = maxEnemies;
		SpawnPoint.numEnemies = 0;
		SpawnPoint.minInterval = minInterval;
		SpawnPoint.maxInterval = maxInterval;
		SpawnPoint.spawnProbabilities = spawnProbabilities;
	}

	public static void setWaveSpawnMode(long minInterval, long maxInterval) {
		SpawnPoint.mode = SpawnMode.WAVE;
		SpawnPoint.minInterval = minInterval;
		SpawnPoint.maxInterval = maxInterval;
	}

	public static void spawnWave(int numEnemies, LinkedHashMap<String, Float> spawnProbabilities) {
		SpawnPoint.maxEnemies = numEnemies;
		SpawnPoint.numEnemies = 0;
		SpawnPoint.spawnProbabilities = spawnProbabilities;
	}
}

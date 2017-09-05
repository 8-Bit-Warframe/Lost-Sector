package com.ezardlabs.lostsector.missions;

import com.ezardlabs.lostsector.objects.enemies.Enemy;

import java.util.ArrayList;

public abstract class Mission {
	protected int numEnemies = 0;
	protected int enemiesKilled = 0;
	private long missionStart = 0;
	private boolean completed = false;
	private ArrayList<EnemyStatusListener> enemyStatusListeners = new ArrayList<>();
	private MissionStatusListener missionStatusListener;

	public void startMission() {
		missionStart = System.currentTimeMillis();
	}

	public long getMissionTime() {
		return System.currentTimeMillis() - missionStart;
	}

	public abstract void load();

	protected final void completedMission() {
		completed = true;
		if (missionStatusListener != null) {
			missionStatusListener.onMissionCompleted();
		}
	}

	public final void notifyEnemySpawn(Enemy enemy) {
		numEnemies++;
		for (EnemyStatusListener listener : enemyStatusListeners) {
			listener.onEnemySpawned(enemy);
		}
	}

	public final void notifyEnemyDeath(Enemy enemy) {
		numEnemies--;
		enemiesKilled++;
		for (EnemyStatusListener listener : enemyStatusListeners) {
			listener.onEnemyKilled(enemy);
		}
	}

	void addEnemyStatusListener(EnemyStatusListener enemyStatusListener) {
		enemyStatusListeners.add(enemyStatusListener);
	}

	void removeEnemyStatusListener(EnemyStatusListener enemyStatusListener) {
		enemyStatusListeners.remove(enemyStatusListener);
	}

	void setMissionStatusListener(MissionStatusListener listener) {
		this.missionStatusListener = listener;
	}

	protected interface EnemyStatusListener {
		void onEnemySpawned(Enemy enemy);

		void onEnemyKilled(Enemy enemy);
	}

	protected interface MissionStatusListener {
		void onMissionCompleted();
	}
}

package com.ezardlabs.lostsector.missions;

import com.ezardlabs.lostsector.objects.enemies.Enemy;

public abstract class Mission {
	protected int enemiesKilled = 0;
	private long missionStart = 0;
	private boolean completed = false;
	private EnemyStatusListener enemyStatusListener;
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
		if (enemyStatusListener != null) {
			enemyStatusListener.onEnemySpawned(enemy);
		}
	}

	public final void notifyEnemyDeath(Enemy enemy) {
		enemiesKilled++;
		if (enemyStatusListener != null) {
			enemyStatusListener.onEnemyKilled(enemy);
		}
	}

	void setEnemyStatusListener(EnemyStatusListener enemyStatusListener) {
		this.enemyStatusListener = enemyStatusListener;
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

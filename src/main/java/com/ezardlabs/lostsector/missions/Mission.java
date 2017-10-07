package com.ezardlabs.lostsector.missions;

import com.ezardlabs.dethsquare.AudioListener;
import com.ezardlabs.dethsquare.AudioManager.AudioGroup;
import com.ezardlabs.dethsquare.AudioSource;
import com.ezardlabs.dethsquare.AudioSource.AudioClip;
import com.ezardlabs.dethsquare.Camera;
import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.Vector2;
import com.ezardlabs.lostsector.camera.SmartCamera;
import com.ezardlabs.lostsector.objects.enemies.Enemy;
import com.ezardlabs.lostsector.objects.menus.EscMenu;

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

	protected final void instantiateCamera(GameObject player) {
		GameObject.instantiate(new GameObject("Camera", new Camera(true),
						new SmartCamera(player.transform, 1000, new Vector2(100, 100)), new AudioListener(),
						new AudioSource(new AudioClip("audio/this_is_what_you_are.ogg"), true, 1, AudioGroup.MUSIC)),
				new Vector2());
	}

	final void instantiateMenus() {
		GameObject.instantiate(new GameObject("Menu", new EscMenu()), new Vector2());
	}

	protected final void completeMission() {
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

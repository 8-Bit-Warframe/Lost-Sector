package com.ezardlabs.lostsector.missions;

import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.GuiRenderer;
import com.ezardlabs.dethsquare.GuiText;
import com.ezardlabs.dethsquare.LevelManager;
import com.ezardlabs.dethsquare.Screen;
import com.ezardlabs.dethsquare.Script;
import com.ezardlabs.dethsquare.StateMachine;
import com.ezardlabs.dethsquare.StateMachine.Transition;
import com.ezardlabs.dethsquare.TextureAtlas;
import com.ezardlabs.dethsquare.Vector2;
import com.ezardlabs.dethsquare.prefabs.PrefabManager;
import com.ezardlabs.lostsector.map.MapManager;
import com.ezardlabs.lostsector.missions.objectives.Cryopod;
import com.ezardlabs.lostsector.objects.enemies.CorpseFader;
import com.ezardlabs.lostsector.objects.enemies.Enemy;

import java.util.LinkedHashMap;

public class DefenseMission extends Mission {
	private Director director = new Director();
	private GameObject[] cryopods;

	@Override
	public void load() {
		MapManager.loadMap("defense0");
		GameObject player = GameObject.instantiate(PrefabManager.loadPrefab("player"), MapManager.playerSpawn);

		instantiateCamera(player);
		instantiateMenus();

		cryopods = GameObject.findAllWithTag("cryopod");
		for (int i = 0; i < cryopods.length; i++) {
			cryopods[i].getComponent(Cryopod.class).setId(i);
		}

		addEnemyStatusListener(new EnemyStatusListener() {
			@Override
			public void onEnemySpawned(Enemy enemy) {
				// unused
			}

			@Override
			public void onEnemyKilled(Enemy enemy) {
				director.numKills++;
			}
		});
		GameObject.instantiate(new GameObject("Defense Director", director), new Vector2());
	}

	public GameObject[] getCryopods() {
		return cryopods;
	}

	public void onCryopodDestroyed(int id) {
		cryopods[id] = null;
		int count = 0;
		for (GameObject cryopod : cryopods) {
			if (cryopod == null) {
				count++;
			}
		}
		if (count == cryopods.length) {
			completedMission();
			showEndGameText(director.waveNum - 1);
			GameObject.destroy(director.gameObject);
			director = null;
		}
	}

	private void showEndGameText(int numWavesCleared) {
		TextureAtlas font = TextureAtlas.load("fonts");

		GuiText gameOver = new GuiText("Game Over", font, 200, 101);
		GuiText wavesCleared = new GuiText(numWavesCleared + " waves cleared", font, 150, 101);
		GuiText exiting = new GuiText("Returning to main menu in 10...", font, 100, 101);

		int gap = 50;
		float height = gameOver.getFontSize() + gap + wavesCleared.getFontSize() + gap + exiting.getFontSize();

		GuiRenderer overlay = new GuiRenderer("images/white.png", Screen.width, Screen.height);
		overlay.setDepth(100);
		overlay.setTint(0, 0, 0, 0.8f);
		GameObject.instantiate(new GameObject(null, overlay), new Vector2());

		GameObject.instantiate(new GameObject(null, gameOver),
				new Vector2(960 - gameOver.getWidth() / 2, 540 - height / 2));
		GameObject.instantiate(new GameObject(null, wavesCleared),
				new Vector2(960 - wavesCleared.getWidth() / 2, 540 - height / 2 + gameOver.getFontSize() / 2 + gap));
		GameObject.instantiate(new GameObject(null, exiting, new Script() {
			private GuiText text;
			private long exitTime;

			@Override
			public void start() {
				text = gameObject.getComponent(GuiText.class);
				exitTime = System.currentTimeMillis() + 10000;
			}

			@Override
			public void update() {
				int timeLeft = (int) ((exitTime - System.currentTimeMillis()) / 1000f);
				if (timeLeft <= 0) {
					LevelManager.loadLevel("main_menu");
				} else {
					text.setText("Returning to main menu in " + timeLeft + "...");
				}
			}
		}), new Vector2(960 - exiting.getWidth() / 2,
				540 - height / 2 + gameOver.getFontSize() / 2 + gap + wavesCleared.getFontSize() / 2 + gap));
	}

	private static class Director extends Script {
		private final StateMachine<State> stateMachine = new StateMachine<>();
		private final long interludeTime = 5000;
		private final long textTime = 2000;
		private LinkedHashMap<String, Float> spawnProbabilities = new LinkedHashMap<>();
		private int waveNum = 0;
		private GuiText waveNumText;
		private long time;
		private int numEnemies;
		private int numKills;

		private enum State {
			INTERLUDE,
			TEXT,
			WAVE
		}

		private Director() {
			stateMachine.addState(State.INTERLUDE,
					new Transition<>(State.TEXT, () -> System.currentTimeMillis() - time >= interludeTime, () -> {
						waveNum++;
						waveNumText.setText("Wave " + waveNum);
						waveNumText.gameObject.setActive(true);
						time = System.currentTimeMillis();
						for (GameObject enemy : GameObject.findAllWithTag("enemy_dead")) {
							if (enemy.hasComponent(CorpseFader.class)) {
								GameObject.destroy(enemy);
							} else {
								enemy.addComponent(new CorpseFader());
							}
						}
					}));
			stateMachine.addState(State.TEXT,
					new Transition<>(State.WAVE, () -> System.currentTimeMillis() - time >= textTime, () -> {
						waveNumText.gameObject.setActive(false);
						numEnemies = getNumberOfEnemies(waveNum);
						numKills = 0;
						SpawnPoint.spawnWave(numEnemies, spawnProbabilities);
					}));
			stateMachine.addState(State.WAVE, new Transition<>(State.INTERLUDE, () -> numKills == numEnemies,
					() -> time = System.currentTimeMillis()));
			stateMachine.init(State.INTERLUDE);

			spawnProbabilities.put("dera_crewman", 0.3f);
			spawnProbabilities.put("prova_crewman", 0.5f);
			spawnProbabilities.put("supra_crewman", 0.2f);
		}

		@Override
		public void start() {
			waveNumText = new GuiText("Wave " + waveNum, TextureAtlas.load("fonts"), 200);
			GameObject waveNumberText = GameObject.instantiate(new GameObject("Wave Number", waveNumText),
					new Vector2(960 - waveNumText.getWidth() / 2, 540 - 100));
			waveNumberText.setActive(false);
			waveNumberText.transform.setParent(transform);
			time = System.currentTimeMillis();
			SpawnPoint.setWaveSpawnMode(2000, 10000);
		}

		@Override
		public void update() {
			stateMachine.update();
		}

		private int getNumberOfEnemies(int waveNum) {
			return (int) (50 * Math.log10(waveNum + 1));
		}
	}
}

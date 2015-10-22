package com.ezardlabs.lostsector;

import com.ezardlabs.dethsquare.Animator;
import com.ezardlabs.dethsquare.Camera;
import com.ezardlabs.dethsquare.Collider;
import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.Renderer;
import com.ezardlabs.dethsquare.Rigidbody;
import com.ezardlabs.dethsquare.Vector2;
import com.ezardlabs.dethsquare.util.BaseGame;
import com.ezardlabs.lostsector.objects.CameraMovement;
import com.ezardlabs.lostsector.objects.Player;
import com.ezardlabs.lostsector.objects.enemies.corpus.crewmen.DeraCrewman;
import com.ezardlabs.lostsector.objects.hud.StatusIndicator;
import com.ezardlabs.lostsector.objects.warframes.Frost;

public class Game extends BaseGame {
	public static GameObject[] players;
	private static CameraMovement cm = new CameraMovement();
	public static StatusIndicator statusIndicator = new StatusIndicator();

	public enum DamageType {
		NORMAL,
		SLASH,
		COLD,
		KUBROW
	}

	@Override
	public void create() {
		MapManager.loadMap("map");

		createPlayer();

		GameObject.instantiate(new GameObject("Camera", new Camera(true), cm), new Vector2());

		for (Vector2 pos : new Vector2[]{new Vector2(2900, 600), new Vector2(2000, 2300), new Vector2(4300, 2000), new Vector2(8000, 700), new Vector2(7900, 1300), new Vector2(8800, 5800), new Vector2(8600, 4800), new Vector2(7100, 2100), new Vector2(3600, 3500), new Vector2(2000, 3800), new Vector2(2800, 5300), new Vector2(700, 4300), new Vector2(3500, 4500), new Vector2(5900, 5800)}) {
			GameObject.instantiate(new GameObject("Dera Crewman", new Renderer(), new Animator(), new Collider(200, 200), new Rigidbody(), new DeraCrewman()), pos);
		}

//		GameObject.instantiate(new GameObject("Kubrow", new Renderer(), new Animator(), new Collider(200, 200), new Rigidbody(), new Kubrow()), new Vector2(1500, 20));

//		GameObject.instantiate(new GameObject("Buttons"), new Vector2());
	}

	public static void createPlayer() {
		players = new GameObject[]{new GameObject("Player", new Player(), new Renderer(), new Animator(), new Frost(), new Collider(200, 200), new Rigidbody())};
		GameObject.instantiate(players[0], new Vector2(20, 20));

		cm.smoothFollow(players[0].transform);
	}
}
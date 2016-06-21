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
import com.ezardlabs.lostsector.objects.enemies.halloween.grineer.WereDrahk;
import com.ezardlabs.lostsector.objects.enemies.halloween.corpus.crewmen.PumpkinDeraCrewman;
import com.ezardlabs.lostsector.objects.hud.HUD;
import com.ezardlabs.lostsector.objects.warframes.Frost;

public class Game extends BaseGame {
	public static GameObject[] players;
	private static CameraMovement cm = new CameraMovement();

	public enum DamageType {
		NORMAL,
		SLASH,
		COLD,
		KUBROW
	}

	@Override
	public void create() {
		MapManager.loadMap("map");

		HUD.init();

		createPlayer();

		GameObject.instantiate(new GameObject("Camera", new Camera(true), cm), new Vector2());

		for (Vector2 pos : new Vector2[]{new Vector2(2528, 1856),
				new Vector2(1024, 992),
				new Vector2(1632, 192),
				new Vector2(2208, 416),
				new Vector2(928, 1856),
				new Vector2(1120, 1120),
				new Vector2(1696, 864),
				new Vector2(2624, 1344),
				new Vector2(320, 448),
				new Vector2(896, 640),
				new Vector2(1280, 448),
				new Vector2(1728, 1344),
				new Vector2(928, 1440),
				new Vector2(800, 640),
				new Vector2(416, 1280),
				new Vector2(1888, 1344),
				new Vector2(2720, 928),
				new Vector2(2560, 640),
				new Vector2(416, 1856),
				new Vector2(1696, 1856),
				new Vector2(192, 960),
				new Vector2(224, 1568),
				new Vector2(2112, 1056)}) {
			GameObject.instantiate(
					new GameObject("Pumpkin Dera Crewman", new Renderer(), new Animator(),
							new Collider(200, 200), new Rigidbody(), new PumpkinDeraCrewman()),
					new Vector2(pos.x * 3.125f, pos.y * 3.125f));
		}

		for (Vector2 pos : new Vector2[]{new Vector2(2752, 416),
				new Vector2(896, 192),
				new Vector2(896, 1696),
				new Vector2(2752, 1536),
				new Vector2(2528, 1184),
				new Vector2(576, 1216),}) {
			GameObject.instantiate(new GameObject("WereDrahk", new Renderer(), new Animator(), new Collider(200, 200), new Rigidbody(), new WereDrahk()), new Vector2(pos.x * 3.125f, pos.y * 3.125f));
		}
//		GameObject.instantiate(new GameObject("Kubrow", new Renderer(), new Animator(), new Collider(200, 200), new Rigidbody(), new Kubrow()), new Vector2(1500, 20));

//		GameObject.instantiate(new GameObject("Buttons"), new Vector2());
	}

	public static void createPlayer() {
		players = new GameObject[]{new GameObject("Player", new Player(), new Renderer(),
				new Animator(), new Frost(), new Collider(200, 200), new Rigidbody())};
		GameObject.instantiate(players[0], new Vector2(20, 20));

		cm.smoothFollow(players[0].transform);
	}
}
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
import com.ezardlabs.lostsector.objects.Kubrow;
import com.ezardlabs.lostsector.objects.Player;
import com.ezardlabs.lostsector.objects.enemies.corpus.crewmen.DeraCrewman;
import com.ezardlabs.lostsector.objects.warframes.Frost;

public class Game extends BaseGame {
	public static GameObject[] players;

	public enum DamageType {
		NORMAL,
		SLASH,
		COLD,
		KUBROW
	}

	@Override
	public void create() {
		MapManager.loadMap("kubrow-map");

		//noinspection RedundantArrayCreation
		GameObject.instantiate(new GameObject("Crewman", new Renderer(), new Animator(), new Collider(200, 200), new Rigidbody(), new DeraCrewman()), new Vector2(1500, 20));

//		ta = new TextureAtlas("images/warframes/frost/atlas.png", "images/warframes/frost/atlas.txt");
		players = new GameObject[]{new GameObject("Player", new Player(), new Renderer(), new Animator(), new Frost(), new Collider(200, 200), new Rigidbody())};
		GameObject.instantiate(players[0], new Vector2(20, 20));

		CameraMovement cm = new CameraMovement();
		cm.smoothFollow(players[0].transform);
		GameObject.instantiate(new GameObject("Camera", new Camera(true), cm), new Vector2());

//		GameObject.instantiate(new GameObject("Kubrow", new Renderer(), new Animator(), new Collider(200, 200), new Rigidbody(), new Kubrow()), new Vector2(220, 20));

//		GameObject.instantiate(new GameObject("Buttons"), new Vector2());
	}
}
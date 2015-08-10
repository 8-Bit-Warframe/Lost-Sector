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
		cm.follow(players[0].transform);
		GameObject.instantiate(new GameObject("Camera", new Camera(true), cm), new Vector2());

//		ta = new TextureAtlas("images/kubrows/white/atlas.png", "images/kubrows/white/atlas.txt");
//		GameObject.instantiate(new GameObject("Kubrow", new Renderer(ta, ta.getSprite("idle0"), 200, 200),
//				new Animator(new Animation("idle", new Sprite[]{ta.getSprite("idle0")}, AnimationType.ONE_SHOT, Long.MAX_VALUE), new Animation("run",
//						new Sprite[]{ta.getSprite("run0"), ta.getSprite("run1"), ta.getSprite("run2"), ta.getSprite("run3"), ta.getSprite("run4"), ta.getSprite("run5"), ta.getSprite("run6")},
//						AnimationType.LOOP, 100), new Animation("attack",
//						new Sprite[]{ta.getSprite("attack0"), ta.getSprite("attack1"), ta.getSprite("attack2"), ta.getSprite("attack3"), ta.getSprite("attack4"), ta.getSprite("attack5"), ta
//								.getSprite("attack6"), ta.getSprite("attack7"), ta.getSprite("attack8"), ta.getSprite("attack9"), ta.getSprite("attack10"), ta.getSprite("attack11")},
//						AnimationType.ONE_SHOT, 100, new AnimationListener() {
//					@Override
//					public void onAnimatedStarted(Animator animator) {
//
//					}
//
//					@Override
//					public void onFrame(int frameNum) {
//
//					}
//
//					@Override
//					public void onAnimationFinished(Animator animator) {
//						//noinspection ConstantConditions
//						animator.gameObject.getComponent(Kubrow.class).attacking = false;
//					}
//				})), new Collider(200, 200), new Rigidbody(), new Kubrow()), new Vector2(220, 20));

//		GameObject.instantiate(new GameObject("Buttons"), new Vector2());
	}
}
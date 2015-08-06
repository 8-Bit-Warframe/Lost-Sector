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

//		TextureAtlas ta = new TextureAtlas("images/enemies/corpus/dera/atlas.png", "images/enemies/corpus/dera/atlas.txt");
//		//noinspection RedundantArrayCreation
//		GameObject.instantiate(new GameObject("Crewman", new Renderer(ta, ta.getSprite("idle0"), 200, 200), new Animator(
//				new Animation[]{new Animation("idle", new Sprite[]{ta.getSprite("idle0")}, AnimationType.ONE_SHOT, Long.MAX_VALUE),
//						new Animation("die_kubrow_front", new Sprite[]{ta.getSprite("die_kubrow_front0"),
//								ta.getSprite("die_kubrow_front1"),
//								ta.getSprite("die_kubrow_front2"),
//								ta.getSprite("die_kubrow_front3"),
//								ta.getSprite("die_kubrow_front4"),
//								ta.getSprite("die_kubrow_front5"),
//								ta.getSprite("die_kubrow_front6"),
//								ta.getSprite("die_kubrow_front7"),
//								ta.getSprite("die_kubrow_front8"),
//								ta.getSprite("die_kubrow_front9"),
//								ta.getSprite("die_kubrow_front10")}, AnimationType.ONE_SHOT, 100, new AnimationListener() {
//							@Override
//							public void onAnimatedStarted(Animator animator) {
//								//noinspection ConstantConditions
//								animator.gameObject.getComponent(Renderer.class).setSize(300, 200);
//							}
//
//							@Override
//							public void onAnimationFinished(Animator animator) {
//
//							}
//
//							@Override
//							public void onFrame(int frameNum) {
//
//							}
//						}),
//						new Animation("die_kubrow_back", new Sprite[]{ta.getSprite("die_kubrow_back0"),
//								ta.getSprite("die_kubrow_back1"),
//								ta.getSprite("die_kubrow_back2"),
//								ta.getSprite("die_kubrow_back3"),
//								ta.getSprite("die_kubrow_back4"),
//								ta.getSprite("die_kubrow_back5"),
//								ta.getSprite("die_kubrow_back6"),
//								ta.getSprite("die_kubrow_back7"),
//								ta.getSprite("die_kubrow_back8"),
//								ta.getSprite("die_kubrow_back9"),
//								ta.getSprite("die_kubrow_back10")}, AnimationType.ONE_SHOT, 100, new AnimationListener() {
//							@Override
//							public void onAnimatedStarted(Animator animator) {
//								//noinspection ConstantConditions
//								animator.gameObject.getComponent(Renderer.class).setSize(300, 200);
//							}
//
//							@Override
//							public void onAnimationFinished(Animator animator) {
//
//							}
//
//							@Override
//							public void onFrame(int frameNum) {
//
//							}
//						}),
//						new Animation("frozen", new Sprite[]{ta.getSprite("frozen0")}, AnimationType.ONE_SHOT, Long.MAX_VALUE, new AnimationListener() {
//							@Override
//							public void onAnimatedStarted(Animator animator) {
//
//							}
//
//							@Override
//							public void onAnimationFinished(Animator animator) {
//								Enemy e = ((Enemy) animator.gameObject.getComponentOfType(Enemy.class));
//								//noinspection ConstantConditions
//								if (e.health <= 0) {
//									animator.play("die_cold");
//								} else {
//									animator.play("melt");
//								}
//							}
//
//							@Override
//							public void onFrame(int frameNum) {
//
//							}
//						}),
//						new Animation("melt", new Sprite[]{ta.getSprite("frozen_melt0"), ta.getSprite("frozen_melt1"), ta.getSprite("frozen_melt2")}, AnimationType.ONE_SHOT, 100,
//								new AnimationListener() {
//									@Override
//									public void onAnimatedStarted(Animator animator) {
//
//									}
//
//									@Override
//									public void onAnimationFinished(Animator animator) {
//										//noinspection ConstantConditions
//										((Enemy) animator.gameObject.getComponentOfType(Enemy.class)).frozen = false;
//									}
//
//									@Override
//									public void onFrame(int frameNum) {
//
//									}
//								}),
//						new Animation("die_cold", new Sprite[]{ta.getSprite("frozen_shatter0"),
//								ta.getSprite("frozen_shatter1"),
//								ta.getSprite("frozen_shatter2"),
//								ta.getSprite("frozen_shatter3"),
//								ta.getSprite("frozen_shatter4"),
//								ta.getSprite("frozen_shatter5"),
//								ta.getSprite("frozen_shatter6"),
//								ta.getSprite("frozen_shatter7"),
//								ta.getSprite("frozen_shatter8")}, AnimationType.ONE_SHOT, 100, new AnimationListener() {
//							@Override
//							public void onAnimatedStarted(Animator animator) {
//
//							}
//
//							@Override
//							public void onAnimationFinished(Animator animator) {
//							}
//
//							@Override
//							public void onFrame(int frameNum) {
//
//							}
//						})}), new Collider(200, 200), new Rigidbody(), new DeraCrewman()), new Vector2(1500, 20));

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
package com.ezardlabs.lostsector;

import com.ezardlabs.dethsquare.Animation;
import com.ezardlabs.dethsquare.Animation.AnimationListener;
import com.ezardlabs.dethsquare.Animation.AnimationType;
import com.ezardlabs.dethsquare.Animator;
import com.ezardlabs.dethsquare.Camera;
import com.ezardlabs.dethsquare.Collider;
import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.Renderer;
import com.ezardlabs.dethsquare.TextureAtlas;
import com.ezardlabs.dethsquare.TextureAtlas.Sprite;
import com.ezardlabs.dethsquare.Vector2;
import com.ezardlabs.dethsquare.util.BaseGame;
import com.ezardlabs.lostsector.objects.CameraMovement;
import com.ezardlabs.lostsector.objects.Player;

public class Game extends BaseGame {

	@Override
	public void create() {
		MapManager.loadMap("map");

		TextureAtlas ta = new TextureAtlas("images/warframes/frost/atlas.png",
				"images/warframes/frost/atlas.txt");

		GameObject player = new GameObject("Player", new Player(),
				new Renderer(ta, ta.getSprite("fidle"), 200, 200), new Animator(
				new Animation("idle", new Sprite[]{ta.getSprite("fidle")}, AnimationType.ONE_SHOT,
						0), new Animation("run",
				new Sprite[]{ta.getSprite("frun0"), ta.getSprite("frun1"), ta.getSprite("frun2")},
				AnimationType.LOOP, 80),
				new Animation("jump", new Sprite[]{ta.getSprite("fjump0")}, AnimationType.ONE_SHOT,
						0), new Animation("doublejump",
				new Sprite[]{ta.getSprite("fjump1"), ta.getSprite("fjump2"), ta.getSprite("fjump3"),
							 ta.getSprite("fjump4"), ta.getSprite("fjump5"), ta.getSprite("fjump6"),
							 ta.getSprite("fjump7")}, AnimationType.ONE_SHOT, 50,
				new AnimationListener() {
					@Override
					public void onAnimationFinished(Animator animator) {
						animator.play("fall");
					}
				}), new Animation("fall",
				new Sprite[]{ta.getSprite("ffall0"), ta.getSprite("ffall1"),
							 ta.getSprite("ffall2")}, AnimationType.LOOP, 90)),
				new Collider(200, 200));
		GameObject.instantiate(player, new Vector2(20, 20));

		CameraMovement cm = new CameraMovement();
		cm.smoothFollow(player.transform);
		GameObject.instantiate(new GameObject("Camera", new Camera(true), cm), new Vector2());
	}
}
package com.ezardlabs.lostsector.objects.enemies.corpus.crewmen;

import com.ezardlabs.dethsquare.Animation;
import com.ezardlabs.dethsquare.AnimationType;
import com.ezardlabs.dethsquare.Animator;
import com.ezardlabs.dethsquare.Collider;
import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.Renderer;
import com.ezardlabs.dethsquare.TextureAtlas;
import com.ezardlabs.lostsector.objects.enemies.corpus.Crewman;
import com.ezardlabs.lostsector.objects.projectiles.Laser;

public class SupraCrewman extends Crewman {

	public SupraCrewman() {
		super("supra");
	}

	@Override
	protected Animation getShootAnimation() {
		return new Animation("shoot", new TextureAtlas.Sprite[]{ta.getSprite("shoot0"),
				ta.getSprite("shoot1")}, AnimationType.LOOP, 100, new Animation.AnimationListener() {
			@Override
			public void onAnimatedStarted(Animator animator) {

			}

			@Override
			public void onFrame(Animator animator, int frameNum) {
				if (frameNum == 1) {
					GameObject
							.instantiate(new GameObject("Laser", new Renderer("images/laser.png", 100, 100).setFlipped(gameObject.renderer.hFlipped, false), new Collider(100, 100, true), new Laser()),
									transform.position.offset(gameObject.renderer.hFlipped ? -12.5f : 87.5f, 60));
				}
			}

			@Override
			public void onAnimationFinished(Animator animator) {

			}
		});
	}

	@Override
	protected Animation getLandAnimation() {
		return new Animation("land", new TextureAtlas.Sprite[]{ta.getSprite("land0")}, AnimationType.ONE_SHOT, 100, new Animation.AnimationListener() {
			@Override
			public void onAnimatedStarted(Animator animator) {
			}

			@Override
			public void onFrame(Animator animator, int frameNum) {
			}

			@Override
			public void onAnimationFinished(Animator animator) {
				landing = false;
			}
		});
	}
}

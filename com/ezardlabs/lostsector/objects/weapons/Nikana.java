package com.ezardlabs.lostsector.objects.weapons;

import android.util.Log;

import com.ezardlabs.dethsquare.Animation;
import com.ezardlabs.dethsquare.Animator;
import com.ezardlabs.dethsquare.Renderer;
import com.ezardlabs.dethsquare.TextureAtlas;
import com.ezardlabs.lostsector.objects.Player;

import java.util.Timer;
import java.util.TimerTask;

public class Nikana extends MeleeWeapon implements Animation.AnimationListener {
	Timer t;

	public Nikana() {
		super("Nikana");
	}

	@Override
	public Animation[] getAnimations(TextureAtlas ta) {
		return new Animation[]{new Animation("slice1", new TextureAtlas.Sprite[]{ta.getSprite("nikana_slice0"),
				ta.getSprite("nikana_slice1"),
				ta.getSprite("nikana_slice2")}, Animation.AnimationType.ONE_SHOT, 100, this),
				new Animation("slice2", new TextureAtlas.Sprite[]{ta.getSprite("nikana_slice3"),
						ta.getSprite("nikana_slice4"),
						ta.getSprite("nikana_slice5")}, Animation.AnimationType.ONE_SHOT, 100, this),
				new Animation("slice3", new TextureAtlas.Sprite[]{ta.getSprite("nikana_slice6"),
						ta.getSprite("nikana_slice7"),
						ta.getSprite("nikana_slice8")}, Animation.AnimationType.ONE_SHOT, 100, this),
				new Animation("dash1", new TextureAtlas.Sprite[]{ta.getSprite("nikana_dash0"),
						ta.getSprite("nikana_dash1"),
						ta.getSprite("nikana_dash2")}, Animation.AnimationType.ONE_SHOT, 100, this),
				new Animation("dash2", new TextureAtlas.Sprite[]{ta.getSprite("nikana_dash3"),
						ta.getSprite("nikana_dash4"),
						ta.getSprite("nikana_dash5")}, Animation.AnimationType.ONE_SHOT, 100, this),
				new Animation("dash3", new TextureAtlas.Sprite[]{ta.getSprite("nikana_dash6"),
						ta.getSprite("nikana_dash7"),
						ta.getSprite("nikana_dash8")}, Animation.AnimationType.ONE_SHOT, 100, this),
				new Animation("stow", new TextureAtlas.Sprite[]{ta.getSprite("nikana_stow0")}, Animation.AnimationType.ONE_SHOT, 200, new Animation.AnimationListener() {
					@Override
					public void onAnimatedStarted(Animator animator) {
					}

					@Override
					public void onFrame(int frameNum) {
					}

					@Override
					public void onAnimationFinished(Animator animator) {
						//noinspection ConstantConditions
						animator.gameObject.getComponent(Player.class).melee = false;
						//noinspection ConstantConditions
						animator.gameObject.getComponent(Renderer.class).setSize(200, 200);
						//noinspection ConstantConditions
						animator.gameObject.getComponent(Renderer.class).setOffsets(0, 0);
					}
				})};
	}

	@Override
	public String getNextAnimation(int direction) {
		if (currentAnimation == null) return currentAnimation = "slice1";
		switch (currentAnimation) {
			case "slice1":
				return currentAnimation = "slice2";
			case "slice2":
				return currentAnimation = "slice3";
			case "slice3":
				if (direction == 0) return currentAnimation = "slice1";
				else return currentAnimation = "dash1";
			case "dash1":
				if (direction == 0) return currentAnimation = "slice1";
				else return currentAnimation = "dash2";
			case "dash2":
				if (direction == 0) return currentAnimation = "slice1";
				else return currentAnimation = "dash3";
			case "dash3":
				return currentAnimation = "slice1";
			default:
				return "slice1";
		}
	}

	@Override
	public void onAnimatedStarted(Animator animator) {
		if (t != null) {
			t.cancel();
		}
		//noinspection ConstantConditions
		animator.gameObject.getComponent(Renderer.class).setSize(400, 300);
		//noinspection ConstantConditions
		animator.gameObject.getComponent(Renderer.class).setOffsets(-100, -100);
	}

	@Override
	public void onFrame(int frameNum) {

	}

	@Override
	public void onAnimationFinished(final Animator animator) {
		(t = new Timer()).schedule(new TimerTask() {
			@Override
			public void run() {
				animator.play("stow");
				currentAnimation = null;
			}
		}, 2000);
	}
}

package com.ezardlabs.lostsector.objects.enemies.halloween.generic;

import com.ezardlabs.dethsquare.Animation;
import com.ezardlabs.dethsquare.AnimationType;
import com.ezardlabs.dethsquare.Animator;
import com.ezardlabs.dethsquare.Collider;
import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.Renderer;
import com.ezardlabs.dethsquare.Rigidbody;
import com.ezardlabs.dethsquare.TextureAtlas.Sprite;
import com.ezardlabs.lostsector.objects.enemies.Enemy;

public class Pumpkin extends Enemy {
	private long createdAt = 0;

	public Pumpkin() {
		super("pumpkin", 1);
	}

	@Override
	public void start() {
		super.start();
		gameObject.renderer.setSprite(ta.getSprite("destroy0"));
		gameObject.renderer.setSize(200, 200);
		createdAt = System.currentTimeMillis();
	}

	@Override
	public void update() {
		if (dead) return;
		if (System.currentTimeMillis() - createdAt >= 2000) {
		GameObject.instantiate(
				new GameObject("Pumpkin Runner", new Renderer(), new Animator(), new Collider(200, 200),
						new Rigidbody(), new PumpkinRunner()), transform.position);
			GameObject.destroy(gameObject);
		}
	}

	private Animation getEmptyAnimation(String name) {
		return new Animation(name, new Sprite[]{ta.getSprite("destroy0")},
				AnimationType.ONE_SHOT, 0);
	}

	private Animation getDieAnimation(String name) {
		return new Animation(name, new Sprite[]{ta.getSprite("destroy0"),
				ta.getSprite("destroy1"),
				ta.getSprite("destroy2"),
				ta.getSprite("destroy3"),
				ta.getSprite("destroy4"),
				ta.getSprite("destroy5"),
				ta.getSprite("destroy6"),
				ta.getSprite("destroy7"),
				ta.getSprite("destroy8")}, AnimationType.ONE_SHOT, 100);
	}

	@Override
	protected Animation getFrozenAnimation() {
		return getDieAnimation("frozen");
	}

	@Override
	protected Animation getFallAnimation() {
		return getEmptyAnimation("fall");
	}

	@Override
	protected Animation getIdleAnimation() {
		return getEmptyAnimation("idle");
	}

	@Override
	protected Animation getFrozenShatterAnimation() {
		return getEmptyAnimation("frozen_shatter");
	}

	@Override
	protected Animation getDieKubrowBackAnimation() {
		return getDieAnimation("die_kubrow_back");
	}

	@Override
	protected Animation getDieKubrowFrontAnimation() {
		return getDieAnimation("die_kubrow_front");
	}

	@Override
	protected Animation getDieShootBackAnimation() {
		return getDieAnimation("die_shoot_back");
	}

	@Override
	protected Animation getDieShootFrontAnimation() {
		return getDieAnimation("die_shoot_front");
	}

	@Override
	protected Animation getDieSlashBackAnimation() {
		return getDieAnimation("die_slash_back");
	}

	@Override
	protected Animation getDieSlashFrontAnimation() {
		return getDieAnimation("die_slash_front");
	}

	@Override
	protected Animation getFrozenMeltAnimation() {
		return getEmptyAnimation("frozen_melt");
	}

	@Override
	protected Animation getJumpAnimation() {
		return getEmptyAnimation("jump");
	}

	@Override
	protected Animation getLandAnimation() {
		return getEmptyAnimation("land");
	}

	@Override
	protected Animation getRunAnimation() {
		return getEmptyAnimation("run");
	}

	@Override
	protected Animation getShootAnimation() {
		return getEmptyAnimation("shoot");
	}
}

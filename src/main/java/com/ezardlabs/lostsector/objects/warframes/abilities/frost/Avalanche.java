package com.ezardlabs.lostsector.objects.warframes.abilities.frost;

import com.ezardlabs.dethsquare.Animation;
import com.ezardlabs.dethsquare.Animation.AnimationListener;
import com.ezardlabs.dethsquare.AnimationType;
import com.ezardlabs.dethsquare.Animator;
import com.ezardlabs.dethsquare.Collider;
import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.RectF;
import com.ezardlabs.dethsquare.Renderer;
import com.ezardlabs.dethsquare.Script;
import com.ezardlabs.dethsquare.TextureAtlas;
import com.ezardlabs.dethsquare.TextureAtlas.Sprite;
import com.ezardlabs.lostsector.Game;
import com.ezardlabs.lostsector.objects.enemies.Enemy;

public class Avalanche extends Script {

	@Override
	public void start() {
		TextureAtlas ta = new TextureAtlas("images/warframes/abilities/frost/avalanche/atlas.png", "images/warframes/abilities/frost/avalanche/atlas.txt");
		GameObject.instantiate(new GameObject("Avalanche Background", new Renderer(ta, ta.getSprite("avalanche_background0"), 800, 300), new Animator(new Animation("avalanche_bg",
				new Sprite[]{ta.getSprite("avalanche_background0"),
						ta.getSprite("avalanche_background1"),
						ta.getSprite("avalanche_background2"),
						ta.getSprite("avalanche_background3"),
						ta.getSprite("avalanche_background4"),
						ta.getSprite("avalanche_background5"),
						ta.getSprite("avalanche_background6"),
						ta.getSprite("avalanche_background7"),
						ta.getSprite("avalanche_background8"),
						ta.getSprite("avalanche_background9"),
						ta.getSprite("avalanche_background10"),
						ta.getSprite("avalanche_background11"),
						ta.getSprite("avalanche_background12"),
						ta.getSprite("avalanche_background13"),
						ta.getSprite("avalanche_background14")}, AnimationType.ONE_SHOT, 100, new AnimationListener() {
			@Override
			public void onAnimatedStarted(Animator animator) {
			}

			@Override
			public void onFrame(Animator animator, int frameNum) {
			}

			@Override
			public void onAnimationFinished(Animator animator) {
				GameObject.destroy(animator.gameObject);
			}
		})), new Background()), transform.position);
		GameObject.instantiate(new GameObject("Avalanche Foreground", new Renderer(ta, ta.getSprite("avalanche_foreground0"), 800, 300), new Animator(new Animation("avalanche_fg",
				new Sprite[]{ta.getSprite("avalanche_foreground0"),
						ta.getSprite("avalanche_foreground1"),
						ta.getSprite("avalanche_foreground2"),
						ta.getSprite("avalanche_foreground3"),
						ta.getSprite("avalanche_foreground4"),
						ta.getSprite("avalanche_foreground5"),
						ta.getSprite("avalanche_foreground6"),
						ta.getSprite("avalanche_foreground7"),
						ta.getSprite("avalanche_foreground8"),
						ta.getSprite("avalanche_foreground9"),
						ta.getSprite("avalanche_foreground10"),
						ta.getSprite("avalanche_foreground11"),
						ta.getSprite("avalanche_foreground12"),
						ta.getSprite("avalanche_foreground13"),
						ta.getSprite("avalanche_foreground14")}, AnimationType.ONE_SHOT, 100, new AnimationListener() {
			@Override
			public void onAnimatedStarted(Animator animator) {
			}

			@Override
			public void onFrame(Animator animator, int frameNum) {
				if (frameNum == 9) damageEnemies(5);
			}

			@Override
			public void onAnimationFinished(Animator animator) {
				GameObject.destroy(animator.gameObject);
			}
		})), new Foreground()), transform.position);
		GameObject.destroy(gameObject);
		damageEnemies(0);
	}

	private void damageEnemies(int damage) {
		RectF r = new RectF(transform.position.x, transform.position.y, transform.position.x + 800, transform.position.y + 300);
		Enemy e;
		Collider c;
		for (GameObject go : GameObject.findAllWithTag("enemy")) {
			if ((e = go.getComponentOfType(Enemy.class)) != null && (c = go.getComponent(Collider.class)) != null && (r.contains(c.bounds) || RectF
					.intersects(r, c.bounds))) {
				e.applyDamage(damage, Game.DamageType.COLD, transform.position);
			}
		}
	}

	class Background extends Script {

		@Override
		public void start() {
			gameObject.renderer.setzIndex(2);
			gameObject.animator.play("avalanche_bg");
		}
	}

	class Foreground extends Script {

		@Override
		public void start() {
			gameObject.renderer.setzIndex(5);
			gameObject.animator.play("avalanche_fg");
		}
	}
}

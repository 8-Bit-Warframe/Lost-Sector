package com.ezardlabs.lostsector.objects.warframes.abilities.frost;

import com.ezardlabs.dethsquare.animation.Animator;
import com.ezardlabs.dethsquare.Collider;
import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.RectF;
import com.ezardlabs.dethsquare.Renderer;
import com.ezardlabs.dethsquare.Script;
import com.ezardlabs.dethsquare.TextureAtlas;
import com.ezardlabs.dethsquare.animation.Animations;
import com.ezardlabs.dethsquare.animation.Animations.Validator;
import com.ezardlabs.lostsector.Game;
import com.ezardlabs.lostsector.objects.enemies.Enemy;

import java.util.Timer;
import java.util.TimerTask;

public class Avalanche extends Script {

	@Override
	public void start() {
		TextureAtlas ta = TextureAtlas.load("data/warframes/frost/abilities/avalanche");
		GameObject.instantiate(new GameObject("Avalanche Background",
				new Renderer(ta, ta.getSprite("avalanche_background0"), 800, 300), new Animator(
				Animations.load("data/warframes/frost/abilities/avalanche", ta, new Validator("background"))),
				new Background()), transform.position);
		GameObject.instantiate(new GameObject("Avalanche Foreground",
				new Renderer(ta, ta.getSprite("avalanche_foreground0"), 800, 300), new Animator(
				Animations.load("data/warframes/frost/abilities/avalanche", ta, new Validator("foreground"))),
				new Foreground()), transform.position);
		GameObject.destroy(gameObject);
		damageEnemies(0);
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				damageEnemies(5);
			}
		}, 800);
	}

	private void damageEnemies(int damage) {
		RectF r = new RectF(transform.position.x, transform.position.y, transform.position.x + 800,
				transform.position.y + 300);
		Enemy e;
		Collider c;
		for (GameObject go : GameObject.findAllWithTag("enemy")) {
			if ((e = go.getComponentOfType(Enemy.class)) != null && (c = go.getComponent(Collider.class)) != null &&
					(r.contains(c.getBounds()) || RectF.intersects(r, c.getBounds()))) {
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

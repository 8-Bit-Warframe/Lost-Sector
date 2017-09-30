package com.ezardlabs.lostsector.objects.warframes.abilities.frost;

import com.ezardlabs.dethsquare.Collider;
import com.ezardlabs.dethsquare.Component;
import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.Script;
import com.ezardlabs.dethsquare.TextureAtlas;
import com.ezardlabs.dethsquare.Time;
import com.ezardlabs.dethsquare.Vector2;
import com.ezardlabs.dethsquare.animation.Animations;
import com.ezardlabs.dethsquare.animation.Animations.Validator;
import com.ezardlabs.dethsquare.multiplayer.Network;
import com.ezardlabs.lostsector.Game.DamageType;
import com.ezardlabs.lostsector.objects.enemies.Enemy;

import java.util.ArrayList;

public class IceWave extends Component {
	private ArrayList<GameObject> damagedEnemies = new ArrayList<>();

	@Override
	public void start() {
		TextureAtlas ta = TextureAtlas.load("data/warframes/frost/abilities/ice-wave");
		gameObject.renderer.setTextureAtlas(ta, 800, 400);
		gameObject.renderer.setDepth(5);
		gameObject.animator.setAnimations(
				Animations.load("data/warframes/frost/abilities/ice-wave", ta, new Validator("move")));
		gameObject.animator.play("move");
		Network.destroy(gameObject, 1600);
		if (Network.isHost()) {
			GameObject iceWaveCollider = GameObject.instantiate(
					new GameObject("Ice Wave Collider",
							new Collider(10, 200, true),
							new IceWaveCollision()),
					transform.scale.x < 0 ? new Vector2(
							transform.position.x + 800,
							transform.position.y + 200) : new Vector2(
							transform.position.x, transform.position.y + 200));
			iceWaveCollider.transform.scale.set(transform.scale);
			GameObject.destroy(iceWaveCollider, 1000);
		}
	}

	private class IceWaveCollision extends Script {

		@Override
		public void update() {
			transform.translate(10 * transform.scale.x * Time.fpsScaling60, 0);
		}

		@Override
		public void onTriggerEnter(Collider other) {
			if (other.gameObject.getTag() != null && other.gameObject.getTag().equals("enemy") &&
					!damagedEnemies.contains(other.gameObject)) {
				//noinspection ConstantConditions
				other.gameObject.getComponentOfType(Enemy.class)
								.applyDamage(1, DamageType.COLD, transform.position);
				damagedEnemies.add(other.gameObject);
			}
		}
	}
}

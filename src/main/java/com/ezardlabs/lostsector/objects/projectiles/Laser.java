package com.ezardlabs.lostsector.objects.projectiles;

import com.ezardlabs.dethsquare.Collider;
import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.Layers;
import com.ezardlabs.dethsquare.Script;
import com.ezardlabs.dethsquare.Time;
import com.ezardlabs.lostsector.Game.DamageType;
import com.ezardlabs.lostsector.objects.Entity;

public class Laser extends Script {
	private final int damage;
	private final int solidLayer = Layers.getLayer("Solid");
	private final String[] targetTags;

	public Laser(int damage, String... targetTags) {
		this.damage = damage;
		this.targetTags = targetTags;
	}

	@Override
	public void start() {
		gameObject.setTag("projectile");
		gameObject.renderer.setDepth(5);
	}

	@Override
	public void update() {
		transform.translate(gameObject.transform.scale.x * 15 * Time.fpsScaling60, 0);
		if (transform.position.x <= 0) GameObject.destroy(gameObject);
	}

	@Override
	public void onTriggerEnter(Collider other) {
		if (other.gameObject.getLayer() == solidLayer) {
			gameObject.removeComponent(Collider.class);
			GameObject.destroy(gameObject);
		} else if (other.gameObject.getTag() != null) {
			if ("projectile-barrier".equals(other.gameObject.getTag())) {
				gameObject.removeComponent(Collider.class);
				GameObject.destroy(gameObject);
			}
			for (String tag : targetTags) {
				if (other.gameObject.getTag().equals(tag)) {
					//noinspection ConstantConditions
					other.gameObject.getComponentOfType(Entity.class)
									.applyDamage(damage, DamageType.NORMAL, transform.position);
					gameObject.removeComponent(Collider.class);
					GameObject.destroy(gameObject);
					break;
				}
			}
		}
	}
}

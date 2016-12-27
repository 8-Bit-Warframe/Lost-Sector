package com.ezardlabs.lostsector.objects.projectiles;

import com.ezardlabs.dethsquare.Collider;
import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.Script;
import com.ezardlabs.lostsector.Game.DamageType;
import com.ezardlabs.lostsector.objects.Entity;

public class Laser extends Script {

	private final int damage;

	public Laser(int damage) {
		this.damage = damage;
	}

	@Override
	public void start() {
		gameObject.setTag("projectile");
	}

	@Override
	public void update() {
		transform.translate(gameObject.transform.scale.x * 15, 0);
		if (transform.position.x <= 0) GameObject.destroy(gameObject);
	}
	
	@Override
	public void onTriggerEnter(Collider other) {
		if (other.gameObject.getTag() != null) {
			if (other.gameObject.getTag().equals("player")) {
				//noinspection ConstantConditions
				other.gameObject.getComponentOfType(Entity.class)
								.applyDamage(damage, DamageType.NORMAL,
										transform.position);
				gameObject.removeComponent(Collider.class);
				GameObject.destroy(gameObject);
			} else if (other.gameObject.getTag().equals("solid")) {
				gameObject.removeComponent(Collider.class);
				GameObject.destroy(gameObject);
			}
		}
	}
}

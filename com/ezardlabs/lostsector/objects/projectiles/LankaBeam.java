package com.ezardlabs.lostsector.objects.projectiles;

import com.ezardlabs.dethsquare.Collider;
import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.RectF;
import com.ezardlabs.dethsquare.Script;
import com.ezardlabs.lostsector.Game.DamageType;
import com.ezardlabs.lostsector.objects.enemies.Enemy;

public class LankaBeam extends Script {
	private long chargeTime;
	private int direction;
	private long startTime;
	private int width;

	public LankaBeam(long chargeTime, int direction) {
		this.chargeTime = chargeTime;
		this.direction = direction;
	}

	@Override
	public void start() {
		startTime = System.currentTimeMillis();
		float closestX = direction == 1 ? Float.MAX_VALUE : Float.MIN_VALUE;
		for (Collider c : Collider.staticColliders) {
			if (c.gameObject.name.equals("Door") && c.gameObject.getTag() == null) continue;
			if (!c.gameObject.name.equals("Locker") && c.bounds.top < transform.position.y && c.bounds.bottom > transform.position.y) {
				if (direction == 1 && c.bounds.left > transform.position.x && c.bounds.left < closestX) {
					closestX = c.bounds.left;
				} else if (direction == -1 && c.bounds.right < transform.position.x && c.bounds.right > closestX) {
					closestX = c.bounds.right;
				}
			}
		}
		width = (int) Math.abs(transform.position.x - closestX);
		if (direction == -1) transform.position.x -= width;
		gameObject.renderer.setSize(width, 0);
	}

	@Override
	public void update() {
		float halfHeight = ((System.currentTimeMillis() - startTime) / (float) chargeTime) * 9f;
		if (System.currentTimeMillis() - startTime < chargeTime) {
			gameObject.renderer.setSize(width, (int) (halfHeight * 2));
			gameObject.renderer.setOffsets(0, (int) -halfHeight);
		} else {
			gameObject.renderer.setImage("images/white.png", width, halfHeight * 2);
			GameObject.destroy(gameObject, 100);
			gameObject.removeComponent(LankaBeam.class);
			RectF beam = new RectF(transform.position.x, transform.position.y - halfHeight, transform.position.x + width, transform.position.y + halfHeight);
			Collider c;
			for (GameObject go : GameObject.findAllWithTag("enemy")) {
				if ((c = go.getComponent(Collider.class)) != null && c.bounds.intersect(beam)) {
					System.out.println(go.name);
					//noinspection ConstantConditions
					go.getComponentOfType(Enemy.class).applyDamage(10, DamageType.SLASH, direction == 1 ? transform.position : transform.position.offset(width, 0));
				}
			}
		}
	}
}

package com.ezardlabs.lostsector.objects.projectiles;

import com.ezardlabs.dethsquare.Collider;
import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.Physics;
import com.ezardlabs.dethsquare.Physics.RaycastHit;
import com.ezardlabs.dethsquare.RectF;
import com.ezardlabs.dethsquare.Script;
import com.ezardlabs.dethsquare.Vector2;
import com.ezardlabs.dethsquare.multiplayer.Network;
import com.ezardlabs.lostsector.Game.DamageType;
import com.ezardlabs.lostsector.objects.enemies.Enemy;

public class LankaBeam extends Script {
	private long chargeTime;
	private float direction;
	private long startTime;
	private int width;

	public LankaBeam(long chargeTime) {
		this.chargeTime = chargeTime;
	}

	@Override
	public void start() {
		startTime = System.currentTimeMillis();
		RaycastHit hit = Physics.raycast(transform.position, new Vector2(1, 0), Float.MAX_VALUE);
		width = (int) Math.abs(transform.position.x - hit.point.x);
		if (direction < 0) transform.position.x -= width;
		gameObject.renderer.setSize(width, 0);
		gameObject.renderer.setzIndex(3);
	}

	@Override
	public void update() {
		float halfHeight = ((System.currentTimeMillis() - startTime) / (float) chargeTime) * 9f;
		if (System.currentTimeMillis() - startTime < chargeTime) {
			gameObject.renderer.setSize(width, (int) (halfHeight * 2));
			gameObject.renderer.setOffsets(0, (int) -halfHeight);
		} else {
			gameObject.renderer.setImage("images/white.png", width, halfHeight * 2);
			Network.destroy(gameObject, 100);
			gameObject.removeComponent(LankaBeam.class);
			RectF beam = new RectF(transform.position.x, transform.position.y - halfHeight, transform.position.x + width, transform.position.y + halfHeight);
			Collider c;
			for (GameObject go : GameObject.findAllWithTag("enemy")) {
				if ((c = go.getComponent(Collider.class)) != null && c.bounds.intersect(beam)) {
					//noinspection ConstantConditions
					go.getComponentOfType(Enemy.class).applyDamage(10, DamageType.SLASH, direction > 0 ? transform.position : transform.position.offset(width, 0));
				}
			}
		}
	}

	public void setDirection(float direction) {
		this.direction = direction;
	}
}

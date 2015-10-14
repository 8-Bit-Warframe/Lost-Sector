package com.ezardlabs.lostsector.objects.projectiles;

import com.ezardlabs.dethsquare.Collider;
import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.Script;
import com.ezardlabs.lostsector.objects.warframes.Warframe;

public class Laser extends Script {

	@Override
	public void update() {
		transform.translate(gameObject.renderer.hFlipped ? -15 : 15, 0);
	}
	@Override
	public void onTriggerEnter(Collider other) {
		if (other.gameObject.getTag() != null) {
			if (other.gameObject.getTag().equals("player")) {
				//noinspection ConstantConditions
				((Warframe) other.gameObject.getComponentOfType(Warframe.class)).removeHealth(10);
				gameObject.removeComponent(Collider.class);
				GameObject.destroy(gameObject);
			} else if (other.gameObject.getTag().equals("solid")) {
				gameObject.removeComponent(Collider.class);
				GameObject.destroy(gameObject);
			}
		}
	}
}
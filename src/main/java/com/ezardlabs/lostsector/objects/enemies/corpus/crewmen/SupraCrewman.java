package com.ezardlabs.lostsector.objects.enemies.corpus.crewmen;

import com.ezardlabs.dethsquare.Collider;
import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.Renderer;
import com.ezardlabs.dethsquare.Transform;
import com.ezardlabs.lostsector.ai.RangedBehaviour;
import com.ezardlabs.lostsector.ai.RangedBehaviour.Builder.ShootAction;
import com.ezardlabs.lostsector.objects.enemies.corpus.Crewman;
import com.ezardlabs.lostsector.objects.projectiles.Laser;

public class SupraCrewman extends Crewman {

	public SupraCrewman() {
		super(new RangedBehaviour.Builder().setRange(100).setShootAction(new ShootAction() {
			private boolean fired = false;

			@Override
			public void onShoot(Transform self, Transform target) {
				self.gameObject.animator.play("shoot");
				if (self.gameObject.animator.getCurrentAnimationFrame() == 1) {
					if (!fired) {
						GameObject laser = GameObject.instantiate(
								new GameObject("Laser", new Renderer("images/laser.png", 100, 100),
										new Collider(100, 100, true), new Laser(1)),
								self.position.offset(self.gameObject.transform.scale.x < 0 ? -12.5f : 87.5f, 60));
						laser.transform.scale.set(self.gameObject.transform.scale);
						fired = true;
					}
				} else {
					fired = false;
				}
			}
		}).setMoveSpeed(10).setVisionRange(2000).setWillPatrol(false).create());
	}

	@Override
	protected String getAtlasPath() {
		return "corpus/crewmen/supra";
	}

	@Override
	protected String getAnimationPath() {
		return "animations/enemies/corpus/crewmen/supra";
	}
}

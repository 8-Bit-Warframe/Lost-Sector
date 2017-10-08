package com.ezardlabs.lostsector.objects.enemies.corpus.crewmen;

import com.ezardlabs.dethsquare.AudioManager.AudioGroup;
import com.ezardlabs.dethsquare.AudioSource;
import com.ezardlabs.dethsquare.AudioSource.AudioClip;
import com.ezardlabs.dethsquare.Collider;
import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.Transform;
import com.ezardlabs.dethsquare.graphics.Renderer;
import com.ezardlabs.lostsector.ai.RangedBehaviour;
import com.ezardlabs.lostsector.ai.RangedBehaviour.Builder.ShootAction;
import com.ezardlabs.lostsector.objects.enemies.corpus.Crewman;
import com.ezardlabs.lostsector.objects.projectiles.Laser;

public class SupraCrewman extends Crewman {

	public SupraCrewman() {
		super(new RangedBehaviour.Builder().setRange(1000).setShootAction(new ShootAction() {
			private boolean fired = false;

			@Override
			public void onShoot(Transform self, Transform target) {
				self.gameObject.animator.play("shoot");
				if (self.gameObject.animator.getCurrentAnimationFrame() == 1) {
					if (!fired) {
						GameObject laser = GameObject.instantiate(
								new GameObject("Laser", new Renderer("images/laser.png", 100, 100),
										new Collider(100, 100, true), new Laser(0.5f, "player", "cryopod")),
								self.position.offset(self.gameObject.transform.scale.x < 0 ? -12.5f : 87.5f, 60));
						laser.transform.scale.set(self.gameObject.transform.scale);
						fired = true;
						//noinspection ConstantConditions
						self.gameObject.getComponent(AudioSource.class).play();
					}
				} else {
					fired = false;
				}
			}
		}).setMoveSpeed(10).setVisionRange(2000).setWillPatrol(false).create());
	}

	@Override
	public void start() {
		super.start();
		AudioSource audio = new AudioSource(AudioGroup.SFX);
		audio.setAudioClip(new AudioClip("audio/sfx/weapons/ranged/supra/shoot.ogg"));
		gameObject.addComponent(audio);
	}

	@Override
	protected String getAtlasPath() {
		return "data/enemies/corpus/crewmen/supra";
	}

	@Override
	protected String getAnimationPath() {
		return "data/enemies/corpus/crewmen/supra";
	}
}

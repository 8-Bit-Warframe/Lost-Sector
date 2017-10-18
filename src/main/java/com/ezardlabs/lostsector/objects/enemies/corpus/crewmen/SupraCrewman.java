package com.ezardlabs.lostsector.objects.enemies.corpus.crewmen;

import com.ezardlabs.dethsquare.AudioManager.AudioGroup;
import com.ezardlabs.dethsquare.AudioSource;
import com.ezardlabs.dethsquare.AudioSource.AudioClip;
import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.Transform;
import com.ezardlabs.dethsquare.networking.Network;
import com.ezardlabs.lostsector.ai.RangedBehaviour;
import com.ezardlabs.lostsector.ai.RangedBehaviour.Builder.ShootAction;
import com.ezardlabs.lostsector.objects.enemies.corpus.Crewman;

public class SupraCrewman extends Crewman {

	public SupraCrewman() {
		super(new RangedBehaviour.Builder().setRange(1000).setShootAction(new ShootAction() {
			private AudioClip charge = new AudioClip("audio/sfx/weapons/ranged/supra/charge.ogg");
			private AudioClip shoot = new AudioClip("audio/sfx/weapons/ranged/supra/shoot.ogg");
			private AudioSource audio;
			private boolean fired = false;
			private boolean charging = false;

			@Override
			public void onShoot(Transform self, Transform target) {
				if (audio == null) {
					audio = self.gameObject.getComponent(AudioSource.class);
				}
				self.gameObject.animator.play("shoot");
				if (self.gameObject.animator.getCurrentAnimationFrame() == 0) {
					if (!charging) {
						audio.stop();
						audio.setAudioClip(charge);
						audio.play();
						charging = true;
					}
				} else {
					charging = false;
				}
				if (self.gameObject.animator.getCurrentAnimationFrame() % 2 == 1) {
					if (!fired) {
						GameObject laser = Network.instantiate("laser-0.5",
								self.position.offset(self.gameObject.transform.scale.x < 0 ? -12.5f : 87.5f, 60));
						laser.transform.scale.set(self.gameObject.transform.scale);
						fired = true;
						audio.stop();
						audio.setAudioClip(shoot);
						audio.play();
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
		gameObject.addComponent(new AudioSource(AudioGroup.SFX));
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

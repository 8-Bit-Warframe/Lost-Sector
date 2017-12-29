package com.ezardlabs.lostsector.objects.enemies.corpus.crewmen;

import com.ezardlabs.dethsquare.audio.AudioManager.AudioGroup;
import com.ezardlabs.dethsquare.audio.AudioSource;
import com.ezardlabs.dethsquare.audio.AudioSource.AudioClip;
import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.Transform;
import com.ezardlabs.dethsquare.animation.AnimationType;
import com.ezardlabs.dethsquare.networking.Network;
import com.ezardlabs.lostsector.ai.RangedBehaviour;
import com.ezardlabs.lostsector.ai.RangedBehaviour.Builder.ShootAction;
import com.ezardlabs.lostsector.objects.enemies.corpus.Crewman;

public class DeraCrewman extends Crewman {

	public DeraCrewman() {
		super(new RangedBehaviour.Builder().setRange(1000).setShootAction(new ShootAction() {
			private boolean fired = false;

			@Override
			public void onShoot(Transform self, Transform target) {
				self.gameObject.animator.play("shoot");
				if (self.gameObject.animator.getCurrentAnimationFrame() % 2 == 1) {
					if (!fired) {
						GameObject laser = Network.instantiate("laser-1",
								self.position.offset(self.gameObject.transform.scale.x < 0 ? -12.5f : 87.5f,
										self.gameObject.animator.getCurrentAnimationFrame() == 1 ? 75 : 50));
						laser.transform.scale.set(self.gameObject.transform.scale);
						//noinspection ConstantConditions
						self.gameObject.getComponent(AudioSource.class).play();
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
		return "data/enemies/corpus/crewmen/dera";
	}

	@Override
	protected String getAnimationPath() {
		return "data/enemies/corpus/crewmen/dera";
	}

	@Override
	public void start() {
		super.start();
		AudioSource audio = new AudioSource(AudioGroup.SFX);
		audio.setAudioClip(new AudioClip("audio/sfx/weapons/ranged/dera/shoot.ogg"));
		gameObject.addComponent(audio);
		//noinspection ConstantConditions
		gameObject.animator.getAnimation("shoot").setAnimationType(new AnimationType() {
			private long lastEnd = 0;

			@Override
			public int update(int currentFrame, int numFrames) {
				switch (currentFrame) {
					case 0:
						if (System.currentTimeMillis() - lastEnd > 2000) return 1;
						else return 0;
					case 1:
					case 2:
						return currentFrame + 1;
					case 3:
						lastEnd = System.currentTimeMillis();
						return 0;
					default:
						return 0;
				}
			}
		});
	}
}

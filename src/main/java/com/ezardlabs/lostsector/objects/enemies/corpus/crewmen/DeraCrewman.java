package com.ezardlabs.lostsector.objects.enemies.corpus.crewmen;

import com.ezardlabs.dethsquare.AnimationType;
import com.ezardlabs.dethsquare.AudioManager.AudioGroup;
import com.ezardlabs.dethsquare.AudioSource;
import com.ezardlabs.dethsquare.AudioSource.AudioClip;
import com.ezardlabs.dethsquare.Collider;
import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.Renderer;
import com.ezardlabs.dethsquare.Transform;
import com.ezardlabs.lostsector.ai.RangedBehaviour;
import com.ezardlabs.lostsector.ai.RangedBehaviour.Builder.ShootAction;
import com.ezardlabs.lostsector.objects.enemies.corpus.Crewman;
import com.ezardlabs.lostsector.objects.projectiles.Laser;

public class DeraCrewman extends Crewman {

	public DeraCrewman() {
		super(new RangedBehaviour.Builder().setRange(100).setShootAction(new ShootAction() {
			@Override
			public void onShoot(Transform self, Transform target) {
				self.gameObject.animator.play("shoot");
				if (self.gameObject.animator.getCurrentAnimationFrame() % 2 == 1) {
					GameObject laser = GameObject.instantiate(
							new GameObject("Laser", new Renderer("images/laser.png", 100, 100),
									new Collider(100, 100, true), new Laser(1)),
							self.position.offset(self.gameObject.transform.scale.x < 0 ? -12.5f : 87.5f,
									self.gameObject.animator.getCurrentAnimationFrame() == 1 ? 75 : 50));
					laser.transform.scale.set(self.gameObject.transform.scale);
					//noinspection ConstantConditions
					self.gameObject.getComponent(AudioSource.class).play();
				}
			}
		}).setMoveSpeed(10).setVisionRange(2000).setWillPatrol(false).create());
	}

	@Override
	protected String getAtlasPath() {
		return "corpus/crewmen/dera";
	}

	@Override
	protected String getAnimationPath() {
		return "animations/enemies/corpus/crewmen/dera";
	}

	@Override
	public void start() {
		super.start();
		AudioSource audio = new AudioSource(AudioGroup.SFX);
		audio.setAudioClip(new AudioClip("audio/dera_shoot.ogg"));
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

package com.ezardlabs.lostsector.objects.enemies.corpus.crewmen;

import com.ezardlabs.dethsquare.Animation;
import com.ezardlabs.dethsquare.Animation.AnimationListener;
import com.ezardlabs.dethsquare.AnimationType;
import com.ezardlabs.dethsquare.Animator;
import com.ezardlabs.dethsquare.AudioSource;
import com.ezardlabs.dethsquare.AudioSource.AudioClip;
import com.ezardlabs.dethsquare.Collider;
import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.Renderer;
import com.ezardlabs.dethsquare.TextureAtlas.Sprite;
import com.ezardlabs.lostsector.objects.enemies.corpus.Crewman;
import com.ezardlabs.lostsector.objects.projectiles.Laser;

public class DeraCrewman extends Crewman {
	private AudioSource audio = new AudioSource();
	private AudioClip shoot;

	@Override
	protected String getAtlasPath() {
		return "corpus/crewmen/dera";
	}

	@Override
	public void start() {
		super.start();
		shoot = new AudioClip("audio/dera_shoot.ogg");
		gameObject.addComponent(audio);
		audio.setAudioClip(shoot);
	}

	@Override
	protected Animation getShootAnimation() {
		return new Animation("shoot", new Sprite[]{ta.getSprite("shoot0"),
				ta.getSprite("shoot1"),
				ta.getSprite("shoot2"),
				ta.getSprite("shoot3")}, new AnimationType() {
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
		}, 100, new AnimationListener() {
			@Override
			public void onAnimatedStarted(Animator animator) {

			}

			@Override
			public void onFrame(Animator animator, int frameNum) {
				if (frameNum % 2 == 1) {
					GameObject laser = GameObject.instantiate(
							new GameObject("Laser",
									new Renderer("images/laser.png", 100, 100),
									new Collider(100, 100, true), new Laser(2)),
							transform.position
									.offset(gameObject.transform.scale.x <
													0 ? -12.5f : 87.5f,
											frameNum == 1 ? 75 : 50));
					laser.transform.scale.set(gameObject.transform.scale);
					audio.play();

				}
			}

			@Override
			public void onAnimationFinished(Animator animator) {

			}
		});
	}
}

package com.ezardlabs.lostsector.objects.warframes;

import com.ezardlabs.dethsquare.Animation;
import com.ezardlabs.dethsquare.AnimationType;
import com.ezardlabs.dethsquare.Animator;
import com.ezardlabs.dethsquare.Collider;
import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.Renderer;
import com.ezardlabs.dethsquare.TextureAtlas.Sprite;
import com.ezardlabs.dethsquare.Vector2;
import com.ezardlabs.dethsquare.multiplayer.Network;
import com.ezardlabs.lostsector.objects.Player;
import com.ezardlabs.lostsector.objects.warframes.abilities.frost.Avalanche;
import com.ezardlabs.lostsector.objects.warframes.abilities.frost.Freeze;
import com.ezardlabs.lostsector.objects.warframes.abilities.frost.IceWave;
import com.ezardlabs.lostsector.objects.warframes.abilities.frost.Snowglobe;
import com.ezardlabs.lostsector.objects.weapons.melee.Nikana;
import com.ezardlabs.lostsector.objects.weapons.primary.Lanka;

public class Frost extends Warframe {

	public Frost() {
		super("frost", 15, 3, 150);
	}

	@Override
	public void start() {
		super.start();
		gameObject.renderer.setzIndex(4);
		gameObject.animator.addAnimations(new Animation("cast_avalanche", new Sprite[]{ta.getSprite("avalanche0"),
				ta.getSprite("avalanche1"),
				ta.getSprite("avalanche2"),
				ta.getSprite("avalanche3"),
				ta.getSprite("avalanche4"),
				ta.getSprite("avalanche5"),
				ta.getSprite("avalanche6"),
				ta.getSprite("avalanche7"),
				ta.getSprite("avalanche8"),
				ta.getSprite("avalanche8"),
				ta.getSprite("avalanche10"),
				ta.getSprite("avalanche11"),
				ta.getSprite("avalanche12"),
				ta.getSprite("avalanche13")}, AnimationType.ONE_SHOT, 100, new Animation.AnimationListener() {
			@Override
			public void onAnimatedStarted(Animator animator) {

			}

			@Override
			public void onFrame(Animator animator, int frameNum) {

			}

			@Override
			public void onAnimationFinished(Animator animator) {
				Player p;
				if ((p = animator.gameObject.getComponent(Player.class)) != null) {
					p.state = Player.State.IDLE;
				}
			}
		}));
		setPrimaryWeapon(new Lanka(gameObject));
		setMeleeWeapon(new Nikana(gameObject));
	}

	@Override
	public String getName() {
		return "Frost";
	}

	@Override
	public void ability1() {
		GameObject freeze = Network.instantiate("freeze", new Vector2(
				gameObject.renderer.hFlipped ? transform.position.x : transform.position.x + 100,
				transform.position.y + 50));
		//noinspection ConstantConditions
		freeze.getComponent(Freeze.class).setDirection(gameObject.renderer.hFlipped ? -1 : 1);
	}

	@Override
	public void ability2() {
		//noinspection ConstantConditions
		if (gameObject.getComponent(Player.class).jumpCount == 0) {
			GameObject iceWave = Network.instantiate("ice_wave", new Vector2(
					gameObject.renderer.hFlipped ?
							transform.position.x - 800 : transform.position.x + 200,
					transform.position.y - 200));
			//noinspection ConstantConditions
			iceWave.getComponent(IceWave.class).setDirection(gameObject.renderer.hFlipped ? -1 : 1);
		}
	}

	@Override
	public void ability3() {
		GameObject
				.instantiate(new GameObject("Snowglobe", new Renderer(), new Animator(), new Collider(800, 600), new Snowglobe()), transform.position.offset(-300, -200));
	}

	@Override
	public void ability4() {
		gameObject.animator.play("cast_avalanche");
		GameObject.instantiate(new GameObject("Avalanche", new Avalanche()), transform.position.offset(-300, -100));
	}
}

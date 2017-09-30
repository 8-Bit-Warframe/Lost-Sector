package com.ezardlabs.lostsector.objects.warframes;

import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.Vector2;
import com.ezardlabs.dethsquare.multiplayer.Network;
import com.ezardlabs.lostsector.objects.Player;
import com.ezardlabs.lostsector.objects.weapons.melee.Nikana;
import com.ezardlabs.lostsector.objects.weapons.primary.Lanka;

public class Frost extends Warframe {

	public Frost() {
		super("frost", 15, 3, 150);
	}

	@Override
	public void start() {
		super.start();
		gameObject.renderer.setDepth(0);
		setPrimaryWeapon(new Lanka());
		setMeleeWeapon(new Nikana(gameObject));
	}

	@Override
	public String getName() {
		return "Frost";
	}

	@Override
	protected String getDataPath() {
		return "data/warframes/frost";
	}

	@Override
	public void ability1() {
		GameObject freeze = Network.instantiate("freeze", new Vector2(
				gameObject.transform.scale.x < 0 ? transform.position.x :
						transform.position.x + 100, transform.position.y + 50));
		freeze.transform.scale.set(gameObject.transform.scale);
	}

	@Override
	public void ability2() {
		//noinspection ConstantConditions
		if (gameObject.getComponent(Player.class).jumpCount == 0) {
			GameObject iceWave = Network.instantiate("ice_wave", new Vector2(
					gameObject.transform.scale.x < 0 ?
							transform.position.x - 800 :
							transform.position.x + 200,
					transform.position.y - 200));
			iceWave.transform.scale.set(transform.scale);
		}
	}

	@Override
	public void ability3() {
		Network.instantiate("snowglobe", transform.position.offset(-300, -200));
	}

	@Override
	public void ability4() {
		/*gameObject.animator.play("cast_avalanche");
		GameObject.instantiate(new GameObject("Avalanche", new Avalanche()), transform.position.offset(-300, -100));*/
	}
}

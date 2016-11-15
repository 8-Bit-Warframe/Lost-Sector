package com.ezardlabs.lostsector.objects.environment;

import com.ezardlabs.dethsquare.Animation;
import com.ezardlabs.dethsquare.AnimationType;
import com.ezardlabs.dethsquare.Animator;
import com.ezardlabs.dethsquare.Collider;
import com.ezardlabs.dethsquare.Component;
import com.ezardlabs.dethsquare.TextureAtlas;
import com.ezardlabs.dethsquare.TextureAtlas.Sprite;
import com.ezardlabs.dethsquare.multiplayer.Network;
import com.ezardlabs.lostsector.objects.warframes.Warframe;

public class Locker extends Component {
	private final boolean locked;
	private final TextureAtlas ta;
	private boolean unlocking = false;

	public Locker(boolean locked, TextureAtlas ta) {
		this.locked = locked;
		this.ta = ta;
	}

	@Override
	public void start() {
		super.start();
		gameObject.renderer.setTextureAtlas(ta, 100, 200);
		if (locked) {
			gameObject.renderer.setSprite(ta.getSprite("lockred"));
		} else {
			gameObject.renderer.setSprite(ta.getSprite("lock0"));
			gameObject.animator.addAnimations(new Animation("unlock",
					new Sprite[]{ta.getSprite("lock1"),
							ta.getSprite("lock2"),
							ta.getSprite("lock3"),
							ta.getSprite("lock4"),
							ta.getSprite("lock5")}, AnimationType.ONE_SHOT, 125));
		}
	}

	@Override
	public void onTriggerEnter(Collider other) {
		if (!locked && !unlocking && Network.isHost() && other.gameObject.getTag() != null &&
				other.gameObject.getTag().equals("player")) {
			unlocking = true;
			//noinspection ConstantConditions
			gameObject.getComponent(Animator.class).play("unlock");
			Warframe w = other.gameObject.getComponentOfType(Warframe.class);
			//noinspection ConstantConditions
			w.addHealth(20);
			w.addEnergy(25);
		}
	}
}
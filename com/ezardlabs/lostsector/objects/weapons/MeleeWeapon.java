package com.ezardlabs.lostsector.objects.weapons;

import com.ezardlabs.dethsquare.Animation;
import com.ezardlabs.dethsquare.TextureAtlas;

public abstract class MeleeWeapon extends Weapon {
	protected String currentAnimation = null;

	public MeleeWeapon(String name) {
		super(name);
	}

	public abstract Animation[] getAnimations(TextureAtlas ta);

	public abstract String getNextAnimation(int direction);

	public final void reset() {
		currentAnimation = null;
	}
}

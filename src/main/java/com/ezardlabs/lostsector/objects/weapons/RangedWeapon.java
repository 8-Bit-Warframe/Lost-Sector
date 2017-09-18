package com.ezardlabs.lostsector.objects.weapons;

import com.ezardlabs.dethsquare.animation.Animation;
import com.ezardlabs.dethsquare.TextureAtlas;

public abstract class RangedWeapon extends Weapon {

	public RangedWeapon(String name) {
		super(name);
	}

	public abstract Animation getAnimation(TextureAtlas ta);
}

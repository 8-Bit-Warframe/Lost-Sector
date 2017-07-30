package com.ezardlabs.lostsector.objects.environment;

import com.ezardlabs.dethsquare.TextureAtlas;
import com.ezardlabs.dethsquare.TextureAtlas.Sprite;

public class LaserDoor extends Door {

	public LaserDoor(TextureAtlas ta) {
		super(ta);
	}

	@Override
	protected String getAnimationPath() {
		return "animations/environment/corpus/doors/laser-door";
	}

	@Override
	protected Sprite getInitialSprite(TextureAtlas ta) {
		return ta.getSprite("ldoor0");
	}
}

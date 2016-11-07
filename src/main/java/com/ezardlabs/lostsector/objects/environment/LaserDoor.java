package com.ezardlabs.lostsector.objects.environment;

import com.ezardlabs.dethsquare.Animation;
import com.ezardlabs.dethsquare.AnimationType;
import com.ezardlabs.dethsquare.TextureAtlas;
import com.ezardlabs.dethsquare.TextureAtlas.Sprite;

public class LaserDoor extends Door {

	public LaserDoor(TextureAtlas ta) {
		super(ta);
	}

	@Override
	protected Animation getOpenAnimation(TextureAtlas ta) {
		return new Animation("open", new Sprite[]{ta.getSprite("ldoor")}, AnimationType.ONE_SHOT,
				80);
	}

	@Override
	protected Animation getCloseAnimation(TextureAtlas ta) {
		return new Animation("close", new Sprite[]{ta.getSprite("ldoor0"),
				ta.getSprite("ldoor1"),
				ta.getSprite("ldoor2"),
				ta.getSprite("ldoor3"),
				ta.getSprite("ldoor4"),
				ta.getSprite("ldoor5"),
				ta.getSprite("ldoor6"),
				ta.getSprite("ldoor7"),
				ta.getSprite("ldoor8"),
				ta.getSprite("ldoor9"),
				ta.getSprite("ldoor10")}, AnimationType.OSCILLATE, 80);
	}
}

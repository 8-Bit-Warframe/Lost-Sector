package com.ezardlabs.lostsector.objects.menus;

import com.ezardlabs.dethsquare.Animation;
import com.ezardlabs.dethsquare.Script;
import com.ezardlabs.dethsquare.TextureAtlas;
import com.ezardlabs.dethsquare.animation.Animations;
import com.ezardlabs.dethsquare.animation.Animations.Validator;

public class MainMenuLiset extends Script {
	private Animation[] animations = null;
	private TextureAtlas ta = null;
	private boolean close = false;

	public MainMenuLiset(boolean close) {
		ta = new TextureAtlas("images/menus/liset_frost_atlas.png", "images/menus/liset_frost_atlas.txt");
		animations = Animations.load("animations/ui/main-menu-liset", ta, new Validator("liset_frost_open",
				"liset_frost_close"));

		this.close = close;
	}

	@Override
	public void start() {
		gameObject.renderer.setTextureAtlas(ta, 96 * 8, 168 * 8);
		gameObject.animator.setAnimations(animations);
		gameObject.animator.play(close ? "liset_frost_close" : "liset_frost_open");
	}
}

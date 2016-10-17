package com.ezardlabs.lostsector.objects.menus;

import com.ezardlabs.dethsquare.Animation;
import com.ezardlabs.dethsquare.AnimationType;
import com.ezardlabs.dethsquare.Script;
import com.ezardlabs.dethsquare.TextureAtlas;

public class MainMenuLiset extends Script {
	private Animation[] animations = null;
	private TextureAtlas ta = null;
	private boolean close = false;

	public MainMenuLiset(boolean close) {
		ta = new TextureAtlas("images/menus/liset_frost_atlas.png",
				"images/menus/liset_frost_atlas.txt");
		animations = new Animation[]{new Animation("liset_frost_open",
				new TextureAtlas.Sprite[]{ta.getSprite("closed_0"),
						ta.getSprite("turn_l_1"),
						ta.getSprite("turn_l_2"),
						ta.getSprite("turn_l_3"),
						ta.getSprite("turn_l_4"),
						ta.getSprite("turn_l_5"),
						ta.getSprite("turn_l_6"),
						ta.getSprite("turn_l_7"),
						ta.getSprite("turn_l_8"),
						ta.getSprite("turn_l_9"),
						ta.getSprite("open"),}, AnimationType.ONE_SHOT, 100),
				new Animation("liset_frost_close", new TextureAtlas.Sprite[]{ta.getSprite("open"),
						ta.getSprite("turn_l_9"),
						ta.getSprite("turn_l_8"),
						ta.getSprite("turn_l_7"),
						ta.getSprite("turn_l_6"),
						ta.getSprite("turn_l_5"),
						ta.getSprite("turn_l_4"),
						ta.getSprite("turn_l_3"),
						ta.getSprite("turn_l_2"),
						ta.getSprite("turn_l_1"),
						ta.getSprite("closed_0"),}, AnimationType.ONE_SHOT, 100),};

		this.close = close;
	}

	@Override
	public void start() {
		gameObject.renderer.setTextureAtlas(ta, 96 * 8, 168 * 8);
		gameObject.animator.setAnimations(animations);
		gameObject.animator.play(close ? "liset_frost_close" : "liset_frost_open");
	}
}

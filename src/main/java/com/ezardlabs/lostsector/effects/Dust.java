package com.ezardlabs.lostsector.effects;

import com.ezardlabs.dethsquare.Component;
import com.ezardlabs.dethsquare.TextureAtlas;
import com.ezardlabs.dethsquare.animation.Animations;

public class Dust extends Component {

	@Override
	public void start() {
		TextureAtlas ta = TextureAtlas.load("data/effects/dust");
		gameObject.renderer.setTextureAtlas(ta);
		gameObject.renderer.setDepth(3);
		gameObject.animator.setAnimations(Animations.load("data/effects/dust", ta));
		gameObject.animator.play("dust");
	}
}

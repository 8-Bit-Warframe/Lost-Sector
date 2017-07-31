package com.ezardlabs.lostsector.objects.warframes.abilities.frost;

import com.ezardlabs.dethsquare.Component;
import com.ezardlabs.dethsquare.TextureAtlas;
import com.ezardlabs.dethsquare.animation.Animations;
import com.ezardlabs.dethsquare.animation.Animations.Validator;
import com.ezardlabs.dethsquare.multiplayer.Network;

public class Snowglobe extends Component {

	@Override
	public void start() {
		TextureAtlas ta = new TextureAtlas("images/warframes/abilities/frost/snowglobe/atlas.png",
				"images/warframes/abilities/frost/snowglobe/atlas.txt");
		gameObject.renderer.setTextureAtlas(ta, 800, 600);
		gameObject.animator.setAnimations(
				Animations.load("animations/warframes/frost/abilities/snowglobe", ta, new Validator("snowglobe")));
		gameObject.animator.play("snowglobe");
		gameObject.renderer.setzIndex(4);
		gameObject.setTag("solid");
		Network.destroy(gameObject, 5000);
	}
}

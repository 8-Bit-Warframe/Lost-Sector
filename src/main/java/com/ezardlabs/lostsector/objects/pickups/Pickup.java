package com.ezardlabs.lostsector.objects.pickups;

import com.ezardlabs.dethsquare.Collider.Collision;
import com.ezardlabs.dethsquare.Component;
import com.ezardlabs.dethsquare.Rigidbody;
import com.ezardlabs.dethsquare.TextureAtlas;
import com.ezardlabs.dethsquare.animation.Animations;
import com.ezardlabs.dethsquare.animation.Animations.Validator;

abstract class Pickup extends Component {

	@Override
	public void start() {
		TextureAtlas ta = new TextureAtlas(getAtlasPath() + "/atlas.png", getAtlasPath() + "/atlas.txt");
		gameObject.renderer.setTextureAtlas(ta, 100, 100);
		gameObject.animator.setAnimations(Animations.load(getAnimationPath(), ta, new Validator("animate")));
		gameObject.animator.play("animate");
	}

	protected abstract String getAtlasPath();

	protected abstract String getAnimationPath();

	@Override
	public void onCollision(Collision collision) {
		if ("solid".equals(collision.gameObject.getTag())) {
			gameObject.removeComponent(Rigidbody.class);
			gameObject.collider.setIsTrigger(true);
		}
	}
}

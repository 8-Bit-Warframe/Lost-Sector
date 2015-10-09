package com.ezardlabs.lostsector.objects.warframes.abilities.frost;

import com.ezardlabs.dethsquare.Animation;
import com.ezardlabs.dethsquare.AnimationType;
import com.ezardlabs.dethsquare.Collider;
import com.ezardlabs.dethsquare.Component;
import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.TextureAtlas;

public class Snowglobe extends Component {

	@Override
	public void start() {
		TextureAtlas ta = new TextureAtlas("images/warframes/abilities/frost/snowglobe/atlas.png", "images/warframes/abilities/frost/snowglobe/atlas.txt");
		gameObject.renderer.setTextureAtlas(ta, 800, 600);
		gameObject.animator.setAnimations(new Animation("snowglobe", new TextureAtlas.Sprite[]{ta.getSprite("sg0"),
				ta.getSprite("sg1"),
				ta.getSprite("sg2"),
				ta.getSprite("sg3"),
				ta.getSprite("sg4"),
				ta.getSprite("sg5"),
				ta.getSprite("sg6"),
				ta.getSprite("sg7"),
				ta.getSprite("sg8"),
				ta.getSprite("sg9"),
				ta.getSprite("sg10"),
				ta.getSprite("sg11"),
				ta.getSprite("sg12"),
				ta.getSprite("sg13"),
				ta.getSprite("sg14"),
				ta.getSprite("sg15"),
				ta.getSprite("sg16"),
				ta.getSprite("sg17")}, AnimationType.LOOP, 100));
		gameObject.animator.play("snowglobe");
		gameObject.renderer.setzIndex(5);
		GameObject.destroy(gameObject, 5000);
	}

	@Override
	public void onTriggerEnter(Collider other) {
		if (other.gameObject.getTag().equals("projectile")) {
			GameObject.destroy(other.gameObject);
		}
	}
}

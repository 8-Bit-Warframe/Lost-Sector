package com.ezardlabs.lostsector.objects.environment;

import com.ezardlabs.dethsquare.Collider;
import com.ezardlabs.dethsquare.Component;
import com.ezardlabs.dethsquare.TextureAtlas;
import com.ezardlabs.dethsquare.animation.Animations;
import com.ezardlabs.dethsquare.animation.Animations.Validator;
import com.ezardlabs.dethsquare.networking.Network;
import com.ezardlabs.lostsector.objects.DropTable;

import static java.util.Arrays.asList;

public class Locker extends Component {
	private static final DropTable dropTable = new DropTable(asList("pickup_health", "pickup_energy"), asList(0.5f,
			0.5f));
	private final boolean locked;
	private final TextureAtlas ta;
	private boolean unlocking = false;

	public Locker(boolean locked, TextureAtlas ta) {
		this.locked = locked;
		this.ta = ta;
	}

	@Override
	public void start() {
		super.start();
		gameObject.renderer.setTextureAtlas(ta, 100, 200);
		if (locked) {
			gameObject.renderer.setSprite(ta.getSprite("lockred"));
		} else {
			gameObject.animator.addAnimations(
					Animations.load("animations/environment/corpus/locker", ta, new Validator("lock", "unlock")));
			gameObject.animator.play("lock");
		}
		gameObject.renderer.setDepth(-70);
	}

	@Override
	public void onTriggerEnter(Collider other) {
		if (!locked && !unlocking && Network.isHost() && other.gameObject.getTag() != null &&
				other.gameObject.getTag().equals("player")) {
			unlocking = true;
			gameObject.animator.play("unlock");
			String drop = dropTable.getDrop();
			if (drop != null) {
				Network.instantiate(drop, transform.position);
			}
		}
	}
}
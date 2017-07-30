package com.ezardlabs.lostsector.objects.environment;

import com.ezardlabs.dethsquare.Collider;
import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.Script;
import com.ezardlabs.dethsquare.TextureAtlas;
import com.ezardlabs.dethsquare.TextureAtlas.Sprite;
import com.ezardlabs.dethsquare.Vector2;
import com.ezardlabs.dethsquare.animation.Animations;
import com.ezardlabs.dethsquare.animation.Animations.Validator;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentHashMap.KeySetView;

public class Door extends Script {
	private final TextureAtlas ta;

	public Door(TextureAtlas ta) {
		this.ta = ta;
	}

	@Override
	public void start() {
		gameObject.renderer.setTextureAtlas(ta, 200, 500);
		gameObject.renderer.setSprite(getInitialSprite(ta));
		gameObject.animator.addAnimations(Animations.load(getAnimationPath(), ta, new
				Validator("open", "close")));
		GameObject.instantiate(new GameObject("DoorCollider", true, new Collider(200, 100)),
				new Vector2(transform.position));
		GameObject.instantiate(new GameObject("DoorCollider", true, new Collider(200, 100)),
				transform.position.offset(0, 400));
		GameObject.instantiate(new GameObject("DoorDetection", new Collider(1200, 500, true),
				new DoorDetection(this)), transform.position.offset(-500, 0));
	}

	private void open() {
		gameObject.animator.play("open");
		gameObject.setTag(null);
		gameObject.collider.bounds.setWidth(0);
		gameObject.collider.bounds.setHeight(0);
	}

	private void close() {
		gameObject.animator.play("close");
		gameObject.setTag("solid");
		gameObject.collider.bounds.setWidth(100);
		gameObject.collider.bounds.setHeight(500);
	}

	protected Sprite getInitialSprite(TextureAtlas ta) {
		return ta.getSprite("door0");
	}

	protected String getAnimationPath() {
		return "animations/environment/corpus/doors/door";
	}

	private class DoorDetection extends Script {
		private final Door door;
		private KeySetView<Collider, Boolean> colliders = ConcurrentHashMap.newKeySet();

		private DoorDetection(Door door) {
			this.door = door;
		}

		@Override
		public void update() {
			colliders.stream()
					 .filter(collider -> !gameObject.collider.bounds.contains(collider.bounds) &&
							 !gameObject.collider.bounds.intersects(collider.bounds))
					 .forEach(collider -> colliders.remove(collider));
			if (colliders.size() > 0) {
				door.open();
			} else {
				door.close();
			}
		}

		@Override
		public void onTriggerEnter(Collider other) {
			if (other.gameObject.getTag() != null && (other.gameObject.getTag().equals("player") ||
					other.gameObject.getTag().equals("enemy"))) {
				colliders.add(other);
			}
		}
	}
}

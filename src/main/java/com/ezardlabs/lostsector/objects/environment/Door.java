package com.ezardlabs.lostsector.objects.environment;

import com.ezardlabs.dethsquare.Animation;
import com.ezardlabs.dethsquare.AnimationType;
import com.ezardlabs.dethsquare.Collider;
import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.Script;
import com.ezardlabs.dethsquare.TextureAtlas;
import com.ezardlabs.dethsquare.TextureAtlas.Sprite;
import com.ezardlabs.dethsquare.Vector2;
import com.ezardlabs.dethsquare.multiplayer.Network;

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
		gameObject.animator.addAnimations(getOpenAnimation(ta), getCloseAnimation(ta));
		GameObject.instantiate(new GameObject("DoorCollider", true, new Collider(200, 100)),
				new Vector2(transform.position));
		GameObject.instantiate(new GameObject("DoorCollider", true, new Collider(200, 100)),
				transform.position.offset(0, 400));
		if (Network.isHost()) {
			GameObject.instantiate(new GameObject("DoorDetection", new Collider(1200, 500, true),
					new DoorDetection(this)), transform.position.offset(-500, 0));
		}
	}

	private void open() {
		gameObject.animator.play("open");
		gameObject.setTag(null);
	}

	private void close() {
		gameObject.animator.play("close");
		gameObject.setTag("solid");
	}

	protected Animation getOpenAnimation(TextureAtlas ta) {
		return new Animation("open", new Sprite[]{ta.getSprite("door0"),
				ta.getSprite("door1"),
				ta.getSprite("door2"),
				ta.getSprite("door3"),
				ta.getSprite("door4"),
				ta.getSprite("door5"),
				ta.getSprite("door6")}, AnimationType.ONE_SHOT, 80);
	}

	protected Animation getCloseAnimation(TextureAtlas ta) {
		return new Animation("close", new Sprite[]{ta.getSprite("door6"),
				ta.getSprite("door5"),
				ta.getSprite("door4"),
				ta.getSprite("door3"),
				ta.getSprite("door2"),
				ta.getSprite("door1"),
				ta.getSprite("door0")}, AnimationType.ONE_SHOT, 80);
	}

	protected Sprite getInitialSprite(TextureAtlas ta) {
		return ta.getSprite("door0");
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

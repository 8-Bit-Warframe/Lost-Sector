package com.ezardlabs.lostsector.objects.environment;

import com.ezardlabs.dethsquare.Collider;
import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.Script;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentHashMap.KeySetView;

public class Door extends Script {

	@Override
	public void start() {
		GameObject.instantiate(new GameObject("DoorDetection", new Collider(1200, 500, true),
				new DoorDetection(this)), transform.position.offset(-500, 0));
	}

	private void open() {
		gameObject.animator.play("open");
		gameObject.setTag(null);
	}

	private void close() {
		gameObject.animator.play("close");
		gameObject.setTag("solid");
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
			if (other.gameObject.getTag().equals("player") ||
					other.gameObject.getTag().equals("enemy")) {
				colliders.add(other);
			}
		}
	}
}

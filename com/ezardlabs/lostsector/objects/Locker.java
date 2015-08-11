package com.ezardlabs.lostsector.objects;

import com.ezardlabs.dethsquare.Animator;
import com.ezardlabs.dethsquare.Collider;
import com.ezardlabs.dethsquare.Component;

public class Locker extends Component {
	private final boolean locked;
	private boolean unlocking = false;

	public Locker(boolean locked) {
		this.locked = locked;
	}

	@Override
	public void onTriggerEnter(Collider other) {
		if (!locked && !unlocking && other.gameObject.name.equals("Player")) {
			unlocking = true;
			gameObject.getComponent(Animator.class).play("unlock");
		}
	}
}
package com.ezardlabs.lostsector.objects.environment;

import com.ezardlabs.dethsquare.Animator;
import com.ezardlabs.dethsquare.Collider;
import com.ezardlabs.dethsquare.Component;
import com.ezardlabs.lostsector.objects.warframes.Warframe;

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
			//noinspection ConstantConditions
			gameObject.getComponent(Animator.class).play("unlock");
			Warframe w = (Warframe) other.gameObject.getComponentOfType(Warframe.class);
			//noinspection ConstantConditions
			w.addHealth(20);
			w.addEnergy(25);
		}
	}
}
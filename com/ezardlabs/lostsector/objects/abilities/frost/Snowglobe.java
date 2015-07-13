package com.ezardlabs.lostsector.objects.abilities.frost;

import com.ezardlabs.dethsquare.Collider;
import com.ezardlabs.dethsquare.Component;
import com.ezardlabs.dethsquare.GameObject;

public class Snowglobe extends Component {

	public Snowglobe() {

	}

	@Override
	public void onTriggerEnter(Collider other) {
		if (other.gameObject.name.equals("Bullet")) {
			GameObject.destroy(other.gameObject);
		}
	}
}

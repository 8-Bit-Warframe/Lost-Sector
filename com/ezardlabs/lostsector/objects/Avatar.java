package com.ezardlabs.lostsector.objects;

import com.ezardlabs.dethsquare.Script;

public abstract class Avatar extends Script {
	public int health;

	public Avatar(int health) {
		this.health = health;
	}
}

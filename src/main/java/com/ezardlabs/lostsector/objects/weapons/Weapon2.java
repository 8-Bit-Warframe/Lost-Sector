package com.ezardlabs.lostsector.objects.weapons;

import com.ezardlabs.dethsquare.Script;

public abstract class Weapon2 extends Script {
	private final String name;

	protected Weapon2(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}

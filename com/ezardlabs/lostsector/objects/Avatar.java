package com.ezardlabs.lostsector.objects;

import com.ezardlabs.dethsquare.Script;

public abstract class Avatar extends Script {
	protected final int maxHealth;
	protected int health;

	public Avatar(int maxHealth) {
		this.maxHealth = maxHealth;
		health = maxHealth;
	}
}

package com.ezardlabs.lostsector.objects;

import com.ezardlabs.dethsquare.Script;

public abstract class Entity extends Script {
	protected final int maxHealth;
	protected int health;

	public Entity(int maxHealth) {
		this.maxHealth = maxHealth;
		health = maxHealth;
	}

	public int getHealth() {
		return health;
	}
}

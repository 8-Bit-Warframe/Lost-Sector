package com.ezardlabs.lostsector.objects;

public class Ability {
	private final String name;
	private final Runnable effect;

	public Ability(String name, Runnable effect) {
		this.name = name;
		this.effect = effect;
	}

	public void activate() {
		effect.run();
	}

	public String getName() {
		return name;
	}
}

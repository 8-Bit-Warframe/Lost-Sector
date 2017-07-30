package com.ezardlabs.lostsector.objects.enemies.corpus;

import com.ezardlabs.lostsector.ai.Behaviour;
import com.ezardlabs.lostsector.objects.enemies.Enemy;

public abstract class Crewman extends Enemy {

	public Crewman(Behaviour behaviour) {
		super(3, behaviour);
	}
}
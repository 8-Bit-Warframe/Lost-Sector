package com.ezardlabs.lostsector.objects.enemies.corpus;

import com.ezardlabs.lostsector.ai.Behaviour;
import com.ezardlabs.lostsector.objects.enemies.Enemy;

public abstract class Moa extends Enemy {

	public Moa(Behaviour behaviour) {
		super(2, behaviour);
	}
}

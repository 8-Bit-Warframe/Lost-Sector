package com.ezardlabs.lostsector.objects.enemies;

import com.ezardlabs.lostsector.objects.Avatar;

public class Enemy extends Avatar {

	public Enemy(int health) {
		super(health);
		gameObject.setTag("enemy");
	}

	public void kubrowAttack() {
		gameObject.animator.play("die_kubrow_back");
		gameObject.setTag(null);
	}
}

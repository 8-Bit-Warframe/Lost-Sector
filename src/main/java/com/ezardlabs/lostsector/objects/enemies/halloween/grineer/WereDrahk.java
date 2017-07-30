package com.ezardlabs.lostsector.objects.enemies.halloween.grineer;

import com.ezardlabs.lostsector.ai.MeleeBehaviour;
import com.ezardlabs.lostsector.objects.enemies.Enemy;

public class WereDrahk extends Enemy {

	public WereDrahk() {
		super(5, new MeleeBehaviour.Builder().setMeleeRange(100)
											 .setMeleeDamage(10)
											 .setDamageFrame(4)
											 .setMoveSpeed(10)
											 .setVisionRange(2000)
											 .setWillPatrol(false)
											 .create());
	}

	@Override
	protected String getAtlasPath() {
		return "halloween/grineer/weredrahk";
	}

	@Override
	protected String getAnimationPath() {
		return "enemies/halloween/grineer/weredrahk";
	}
}

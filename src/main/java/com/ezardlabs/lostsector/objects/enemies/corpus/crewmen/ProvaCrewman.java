package com.ezardlabs.lostsector.objects.enemies.corpus.crewmen;

import com.ezardlabs.lostsector.ai.MeleeBehaviour;
import com.ezardlabs.lostsector.objects.enemies.corpus.Crewman;

public class ProvaCrewman extends Crewman {

	public ProvaCrewman() {
		super(new MeleeBehaviour.Builder().setMeleeRange(100)
										  .setMeleeDamage(1)
										  .setDamageFrame(2)
										  .setMoveSpeed(10)
										  .setVisionRange(2000)
										  .setWillPatrol(false)
										  .create());
	}

	@Override
	protected String getAtlasPath() {
		return "corpus/crewmen/prova";
	}

	@Override
	protected String getAnimationPath() {
		return "animations/enemies/corpus/crewmen/prova";
	}
}

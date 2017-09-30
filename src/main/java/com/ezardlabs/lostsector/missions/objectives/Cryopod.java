package com.ezardlabs.lostsector.missions.objectives;

import com.ezardlabs.dethsquare.Layers;
import com.ezardlabs.dethsquare.Level;
import com.ezardlabs.dethsquare.LevelManager;
import com.ezardlabs.dethsquare.Vector2;
import com.ezardlabs.lostsector.Game.DamageType;
import com.ezardlabs.lostsector.levels.DefenseLevel;
import com.ezardlabs.lostsector.objects.ShieldedEntity;

public class Cryopod extends ShieldedEntity {
	private int id;

	public Cryopod() {
		super(100, 200, 1000);
	}

	@Override
	public void start() {
		gameObject.renderer.setImage("data/objectives/cryopod/cryopod.png", 300, 100);
		gameObject.renderer.setOffsets(50, 100);
		gameObject.renderer.setDepth(-10);
		gameObject.setLayer(Layers.getLayer("Objective"));
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	protected void die(DamageType damageType, Vector2 attackOrigin) {
		Level level = LevelManager.getCurrentLevel();
		if (level instanceof DefenseLevel) {
			((DefenseLevel) level).getMission().onCryopodDestroyed(id);
		}
	}
}

package com.ezardlabs.lostsector.missions.objectives;

import com.ezardlabs.dethsquare.Level;
import com.ezardlabs.dethsquare.LevelManager;
import com.ezardlabs.dethsquare.Vector2;
import com.ezardlabs.lostsector.Game.DamageType;
import com.ezardlabs.lostsector.levels.MissionLevel;
import com.ezardlabs.lostsector.missions.DefenseMission;
import com.ezardlabs.lostsector.objects.ShieldedEntity;

public class Cryopod extends ShieldedEntity {

	public Cryopod() {
		super(10, 40, 1500);
	}

	@Override
	protected void die(DamageType damageType, Vector2 attackOrigin) {
		Level level = LevelManager.getCurrentLevel();
		if (level instanceof MissionLevel) {
			MissionLevel missionLevel = (MissionLevel) level;
			if (missionLevel.getMission() instanceof DefenseMission) {
				((DefenseMission) missionLevel.getMission())
						.onCryopodDestroyed();
			}
		}
	}
}

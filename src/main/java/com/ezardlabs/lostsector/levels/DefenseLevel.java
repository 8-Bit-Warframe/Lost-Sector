package com.ezardlabs.lostsector.levels;

import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.lostsector.missions.DefenseMission;

public class DefenseLevel extends MissionLevel<DefenseMission> {

	public DefenseLevel() {
		super(new DefenseMission());
	}

	@Override
	public GameObject[] getObjectiveTargets() {
		return getMission().getCryopods();
	}
}

package com.ezardlabs.lostsector.levels;

import com.ezardlabs.dethsquare.Level;
import com.ezardlabs.dethsquare.Screen;
import com.ezardlabs.lostsector.missions.Mission;

public abstract class MissionLevel<T extends Mission> extends Level {
	private final T mission;

	protected MissionLevel(T mission) {
		this.mission = mission;
	}

	public T getMission() {
		return mission;
	}

	@Override
	public void onLoad() {
		mission.load();
		Screen.setCursorVisible(false);
	}
}

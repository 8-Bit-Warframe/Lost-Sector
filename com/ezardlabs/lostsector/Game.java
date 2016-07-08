package com.ezardlabs.lostsector;

import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.LevelManager;
import com.ezardlabs.dethsquare.util.BaseGame;
import com.ezardlabs.lostsector.levels.ExploreLevel;
import com.ezardlabs.lostsector.levels.MainMenuLevel;
import com.ezardlabs.lostsector.levels.SurvivalLevel;

public class Game extends BaseGame {
	public static GameObject[] players;

	public enum DamageType {
		NORMAL,
		SLASH,
		COLD,
		KUBROW
	}

	@Override
	public void create() {
		LevelManager.registerLevel("explore", new ExploreLevel());
		LevelManager.registerLevel("survival", new SurvivalLevel());
		LevelManager.registerLevel("mainmenu", new MainMenuLevel());

		LevelManager.loadLevel("mainmenu");
	}
}
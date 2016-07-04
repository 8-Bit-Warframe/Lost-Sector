package com.ezardlabs.lostsector;

import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.LevelManager;
import com.ezardlabs.dethsquare.util.BaseGame;
import com.ezardlabs.lostsector.levels.GameLevel;
import com.ezardlabs.lostsector.levels.TennoConLevel;

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
		LevelManager.registerLevel("game", new GameLevel());
		LevelManager.registerLevel("tennocon", new TennoConLevel());

		LevelManager.loadLevel("tennocon");
	}
}
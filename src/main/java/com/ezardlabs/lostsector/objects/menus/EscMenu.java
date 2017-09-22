package com.ezardlabs.lostsector.objects.menus;

import com.ezardlabs.dethsquare.Input;
import com.ezardlabs.dethsquare.Input.KeyCode;
import com.ezardlabs.dethsquare.LevelManager;

public class EscMenu extends Menu {

	public EscMenu() {
		super(new String[]{
				"Resume",
				"Settings",
				"Quit"
		}, new MenuAction[]{
				(menu, index, text) -> menu.close(),
				(menu, index, text) -> {},
				(menu, index, text) -> LevelManager.loadLevel("main_menu")
		}, false);
	}

	@Override
	public void update() {
		if (Input.getKeyDown(KeyCode.ESCAPE)) {
			toggle();
		}
		super.update();
	}
}

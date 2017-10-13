package com.ezardlabs.lostsector.objects.menus;

import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.Input;
import com.ezardlabs.dethsquare.Input.KeyCode;
import com.ezardlabs.dethsquare.LevelManager;
import com.ezardlabs.dethsquare.Screen;
import com.ezardlabs.dethsquare.Vector2;

public class MainMenu extends Menu {
	private GameObject settingsMenu;

	@Override
	public void start() {
		super.start();
		settingsMenu = GameObject.instantiate(
				new GameObject("Settings Menu", new SettingsMenu(menu -> {
					menu.gameObject.setActive(false);
					open();
				})),
				new Vector2(960 - 600, 540 - 500));
		settingsMenu.setActive(false);
		open();
		Screen.setCursorVisible(true);
	}

	@Override
	public void update() {
		if (Input.getKeyDown(KeyCode.ESCAPE) && settingsMenu.isActiveSelf()) {
			settingsMenu.setActive(false);
			open();
		}
		super.update();
	}

	@Override
	protected String[] getOptions() {
		return new String[]{
				"Play",
				"Settings",
				"Quit"
		};
	}

	@Override
	protected MenuAction[] getActions() {
		return new MenuAction[]{
				(menu, index, text) -> LevelManager.loadLevel("defense"),
				(menu, index, text) -> {
					close();
					settingsMenu.setActive(true);
				},
				(menu, index, text) -> System.exit(0)
		};
	}
}

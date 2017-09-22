package com.ezardlabs.lostsector.objects.menus;

import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.Input;
import com.ezardlabs.dethsquare.Input.KeyCode;
import com.ezardlabs.dethsquare.LevelManager;
import com.ezardlabs.dethsquare.Vector2;

public class EscMenu extends Menu {
	private GameObject settingsMenu;

	@Override
	public void start() {
		super.start();
		settingsMenu = GameObject.instantiate(
				new GameObject("Settings Menu", new SettingsMenu(menu -> menu.gameObject.setActive(false))),
				new Vector2());
		settingsMenu.setActive(false);
	}

	@Override
	public void update() {
		if (Input.getKeyDown(KeyCode.ESCAPE)) {
			if (settingsMenu.isActiveSelf()) {
				settingsMenu.setActive(false);
			} else {
				toggle();
			}
		}
		super.update();
	}

	@Override
	protected String[] getOptions() {
		return new String[]{
				"Resume",
				"Settings",
				"Quit"
		};
	}

	@Override
	protected MenuAction[] getActions() {
		return new MenuAction[]{
				(menu, index, text) -> menu.close(),
				new MenuAction() {
					@Override
					public void onMenuItemSelected(Menu menu, int index, String text) {
						settingsMenu.setActive(true);
					}
				},
				(menu, index, text) -> LevelManager.loadLevel("main_menu")
		};
	}
}

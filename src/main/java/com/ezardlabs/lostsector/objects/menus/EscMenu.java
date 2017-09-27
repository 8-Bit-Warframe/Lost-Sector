package com.ezardlabs.lostsector.objects.menus;

import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.Input;
import com.ezardlabs.dethsquare.Input.KeyCode;
import com.ezardlabs.dethsquare.LevelManager;
import com.ezardlabs.dethsquare.Screen;
import com.ezardlabs.dethsquare.Vector2;

public class EscMenu extends Menu {
	private GameObject settingsMenu;

	@Override
	public void start() {
		super.start();
		settingsMenu = GameObject.instantiate(
				new GameObject("Settings Menu", new SettingsMenu(menu -> {
					menu.gameObject.setActive(false);
					open();
				})),
				new Vector2(Screen.width / 2 - 600, Screen.height / 2 - 500));
		settingsMenu.setActive(false);
	}

	@Override
	public void update() {
		if (Input.getKeyDown(KeyCode.ESCAPE)) {
			if (settingsMenu.isActiveSelf()) {
				settingsMenu.setActive(false);
				open();
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
						close();
						settingsMenu.setActive(true);
					}
				},
				(menu, index, text) -> LevelManager.loadLevel("main_menu")
		};
	}
}

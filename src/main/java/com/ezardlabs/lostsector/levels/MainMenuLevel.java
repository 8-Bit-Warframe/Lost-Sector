package com.ezardlabs.lostsector.levels;

import com.ezardlabs.dethsquare.Animator;
import com.ezardlabs.dethsquare.AudioSource;
import com.ezardlabs.dethsquare.AudioSource.AudioClip;
import com.ezardlabs.dethsquare.Camera;
import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.GuiRenderer;
import com.ezardlabs.dethsquare.Level;
import com.ezardlabs.dethsquare.LevelManager;
import com.ezardlabs.dethsquare.Renderer;
import com.ezardlabs.dethsquare.Screen;
import com.ezardlabs.dethsquare.Vector2;
import com.ezardlabs.lostsector.objects.menus.MainMenuLiset;
import com.ezardlabs.lostsector.objects.menus.Menu;
import com.ezardlabs.lostsector.objects.menus.Menu.MenuAction;

public class MainMenuLevel extends Level {

	@Override
	public void onLoad() {
		GameObject.instantiate(new GameObject("Camera", new Camera(true)), new Vector2());

		GameObject.instantiate(new GameObject("Liset", new Renderer(), new Animator(),
				new MainMenuLiset(false)), new Vector2(Screen.width / 2 - 96 * 8 / 2, 75));

		GameObject.instantiate(new GameObject("MainMenuLogo",
						new GuiRenderer("images/menus/main/logo.png", 900, 225)),
				new Vector2(Screen.width / 2 - 450, 10));

		Menu m;
		GameObject.instantiate(new GameObject("MainMenu", m = new Menu(new String[]{"Explore",
						"Survival",
						"Procedural",
						"Multiplayer"}, new MenuAction[]{new MenuAction() {
					@Override
					public void onMenuItemSelected(Menu menu, int index, String text) {
						LevelManager.loadLevel("explore");
					}
				},
						new MenuAction() {
							@Override
							public void onMenuItemSelected(Menu menu, int index, String text) {
								LevelManager.loadLevel("survival");
							}
						},
						new MenuAction() {
							@Override
							public void onMenuItemSelected(Menu menu, int index, String text) {
								LevelManager.loadLevel("procedural");
							}
						}}, new Vector2(0, 250)),
						new AudioSource(new AudioClip("audio/this_is_what_you_are" + ".ogg"), true, 50)),
				new Vector2());
		m.open();
	}
}

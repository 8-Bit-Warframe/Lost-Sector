package com.ezardlabs.lostsector.levels;

import com.ezardlabs.dethsquare.Animator;
import com.ezardlabs.dethsquare.AudioManager.AudioGroup;
import com.ezardlabs.dethsquare.AudioSource;
import com.ezardlabs.dethsquare.AudioSource.AudioClip;
import com.ezardlabs.dethsquare.Camera;
import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.GuiRenderer;
import com.ezardlabs.dethsquare.Level;
import com.ezardlabs.dethsquare.LevelManager;
import com.ezardlabs.dethsquare.Renderer;
import com.ezardlabs.dethsquare.Vector2;
import com.ezardlabs.lostsector.objects.menus.MainMenuLiset;
import com.ezardlabs.lostsector.objects.menus.Menu;
import com.ezardlabs.lostsector.objects.menus.Menu.MenuAction;

public class MainMenuLevel extends Level {

	@Override
	public void onLoad() {
		GameObject.instantiate(new GameObject("Camera", new Camera(true)), new Vector2());

		GameObject.instantiate(
				new GameObject("Liset", new Renderer(), new Animator(), new MainMenuLiset(false)),
				new Vector2(960 - 96 * 8 / 2, 75));

		GameObject.instantiate(new GameObject("MainMenuLogo",
						new GuiRenderer("images/menus/main/logo.png", 900, 225)),
				new Vector2(960 - 450, 10));

		GameObject.instantiate(new GameObject("MainMenu", new Menu(new String[]{"Explore",
				"Survival",
				"Procedural",
				"Multiplayer"},
				new MenuAction[]{(menu, index, text) -> LevelManager.loadLevel("explore"),
						(menu, index, text) -> LevelManager.loadLevel("survival"),
						(menu, index, text) -> LevelManager.loadLevel("procedural"),
						(menu, index, text) -> LevelManager.loadLevel("multiplayer_lobby")},
				new Vector2(0, 250), true),
				new AudioSource(new AudioClip("audio/this_is_what_you_are" + ".ogg"), true,
						AudioGroup.MUSIC)), new Vector2());
	}
}

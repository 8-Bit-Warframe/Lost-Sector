package com.ezardlabs.lostsector.levels;

import com.ezardlabs.dethsquare.AudioListener;
import com.ezardlabs.dethsquare.AudioManager.AudioGroup;
import com.ezardlabs.dethsquare.AudioSource;
import com.ezardlabs.dethsquare.AudioSource.AudioClip;
import com.ezardlabs.dethsquare.Camera;
import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.GuiRenderer;
import com.ezardlabs.dethsquare.Level;
import com.ezardlabs.dethsquare.Vector2;
import com.ezardlabs.lostsector.objects.menus.MainMenu;

public class MainMenuLevel extends Level {

	@Override
	public void onLoad() {
		GameObject.instantiate(new GameObject("Camera", new Camera(true), new AudioListener(),
				new AudioSource(new AudioClip("audio/menu.ogg"), true, AudioGroup.MUSIC)), new Vector2());

		GameObject.instantiate(new GameObject("Logo", new GuiRenderer("images/menus/main/logo.png", 900, 225)),
				new Vector2(510, 15));

		GameObject.instantiate(new GameObject("Main Menu", new MainMenu()), new Vector2());
	}
}

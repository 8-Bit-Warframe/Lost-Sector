package com.ezardlabs.lostsector.levels;

import com.ezardlabs.dethsquare.AudioSource;
import com.ezardlabs.dethsquare.AudioSource.AudioClip;
import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.Level;
import com.ezardlabs.dethsquare.Vector2;
import com.ezardlabs.lostsector.objects.menus.MainMenu;

public class MainMenuLevel extends Level {

	@Override
	public void onLoad() {
		GameObject.instantiate(new GameObject("MainMenu", new MainMenu(),
						new AudioSource(new AudioClip("audio/this_is_what_you_are.ogg"), true, 50)),
				new Vector2());
	}
}

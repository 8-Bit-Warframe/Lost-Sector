package com.ezardlabs.lostsector.levels;

import com.ezardlabs.dethsquare.*;
import com.ezardlabs.lostsector.MapManager;
import com.ezardlabs.lostsector.objects.menus.MainMenu;

/**
 * Created by Benjamin on 2016-07-04.
 */
public class MainMenuLevel extends Level {

    @Override
    public void onLoad() {
        MainMenu.init();

        AudioSource as = new AudioSource();
        as.play(new AudioSource.AudioClip("audio/theme.ogg"));

        GameObject.instantiate(new GameObject("Camera", new Camera(true), as), new Vector2());
    }
}

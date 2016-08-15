package com.ezardlabs.lostsector.levels;

import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.Level;
import com.ezardlabs.dethsquare.Vector2;
import com.ezardlabs.lostsector.objects.menus.MainMenu;

/**
 * Created by Benjamin on 2016-07-04.
 */
public class MainMenuLevel extends Level {

    @Override
    public void onLoad() {
        GameObject.instantiate(new GameObject("MainMenu", new MainMenu()), new Vector2());
    }
}

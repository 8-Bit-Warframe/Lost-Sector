package com.ezardlabs.lostsector.objects.menus;

import com.ezardlabs.dethsquare.Animation;
import com.ezardlabs.dethsquare.AnimationType;
import com.ezardlabs.dethsquare.TextureAtlas;
import com.ezardlabs.dethsquare.TextureAtlas.Sprite;

/**
 * Created by Benjamin on 2016-05-15.
 */
public class MainMenu {
    public static void load() {
        TextureAtlas ta = new TextureAtlas("images/menus/menu_frame.png", "images/menus/menu_frame.txt");
        Animation menuOpen = new Animation("menu_frame_open", new Sprite[] {
                ta.getSprite("frame0"),
                ta.getSprite("frame1"),
                ta.getSprite("frame2"),
                ta.getSprite("frame3"),
                ta.getSprite("frame4"),
                ta.getSprite("frame5"),
                ta.getSprite("frame6")
        }, AnimationType.ONE_SHOT, 100);
    }
}

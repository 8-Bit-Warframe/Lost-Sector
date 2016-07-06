package com.ezardlabs.lostsector.objects.menus;

import com.ezardlabs.dethsquare.*;
import com.ezardlabs.dethsquare.TextureAtlas.Sprite;

/**
 * Created by Benjamin on 2016-05-15.
 */
public class MainMenu extends Script {
    public static void init() {
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

        GameObject.instantiate(new GameObject(null, new GuiText("Explore", new TextureAtlas("fonts/atlas.png", "fonts/atlas.txt"), 50)), new Vector2(Screen.width / 2.0f - 56.0f, Screen.height / 2.0f));
        GameObject.instantiate(new GameObject(null, new GuiText("Survival", new TextureAtlas("fonts/atlas.png", "fonts/atlas.txt"), 50)), new Vector2(Screen.width / 2.0f - 64.0f, Screen.height / 2.0f + 75.0f));

        GameObject.instantiate(
                new GameObject(
                    null,
                    new GuiText("8Bit Warframe is not affiliated with Digital Extremes.", new TextureAtlas("fonts/atlas.png", "fonts/atlas.txt"), 25)
                ),
                new Vector2(10, Screen.height - 25)
        );
    }
}

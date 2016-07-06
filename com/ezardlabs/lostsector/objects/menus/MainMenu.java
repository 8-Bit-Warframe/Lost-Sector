package com.ezardlabs.lostsector.objects.menus;

import com.ezardlabs.dethsquare.*;
import com.ezardlabs.dethsquare.TextureAtlas.Sprite;

/**
 * Created by Benjamin on 2016-05-15.
 */
public class MainMenu extends Script {
    public static void init() {
        TextureAtlas ta = new TextureAtlas("images/menus/atlas.png", "images/menus/atlas.txt");
        Animation menuOpen = new Animation("menu_frame_open", new Sprite[] {
                ta.getSprite("frame0"),
                ta.getSprite("frame1"),
                ta.getSprite("frame2"),
                ta.getSprite("frame3"),
                ta.getSprite("frame4"),
                ta.getSprite("frame5"),
                ta.getSprite("frame6")
        }, AnimationType.ONE_SHOT, 100);

        GameObject.instantiate(
                new GameObject("LisetFrost", new GuiRenderer(ta, ta.getSprite("liset_frost"), 96 * 8, 168 * 8)),
                new Vector2(Screen.width / 2 - 96 * 8 / 2, 25)
        );

        GameObject.instantiate(
                new GameObject("LogoVertical", new GuiRenderer(ta, ta.getSprite("logo_vertical"), 900, 225)),
                new Vector2(Screen.width / 2 - 450, 10)
        );

        GameObject.instantiate(
                new GameObject("MenuFrame", new GuiRenderer(ta, ta.getSprite("frame6"), 350, 400)),
                new Vector2(Screen.width / 2 - 175, Screen.height / 2)
        );

        GameObject.instantiate(new GameObject(null, new GuiText("Explore", new TextureAtlas("fonts/atlas.png", "fonts/atlas.txt"), 50)), new Vector2(Screen.width / 2.0f - 84.0f, Screen.height / 2.0f + 90.0f));
        GameObject.instantiate(new GameObject(null, new GuiText("Survival", new TextureAtlas("fonts/atlas.png", "fonts/atlas.txt"), 50)), new Vector2(Screen.width / 2.0f - 96.0f, Screen.height / 2.0f + 180.0f));
        GameObject.instantiate(new GameObject(null, new GuiText("Exit", new TextureAtlas("fonts/atlas.png", "fonts/atlas.txt"), 50)), new Vector2(Screen.width / 2.0f - 48.0f, Screen.height / 2.0f + 270.0f));

        GameObject.instantiate(
                new GameObject(
                        null,
                        new GuiText("Lost Sector is a community project developed by Warframe fans for Warframe fans!", new TextureAtlas("fonts/atlas.png", "fonts/atlas.txt"), 25)
                ),
                new Vector2(Screen.width / 2 - 650, Screen.height - 100)
        );
        GameObject.instantiate(
                new GameObject(
                        null,
                        new GuiText("Lost Sector is not affiliated with Digital Extremes.", new TextureAtlas("fonts/atlas.png", "fonts/atlas.txt"), 25)
                ),
                new Vector2(10, Screen.height - 25)
        );
    }
}

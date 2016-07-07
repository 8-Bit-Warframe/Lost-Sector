package com.ezardlabs.lostsector.objects.menus;

import com.ezardlabs.dethsquare.*;

/**
 * Created by Benjamin on 2016-07-06.
 */
public class EscMenu {
    public static boolean visible = false;
    private static boolean optionSelected = false;

    private static GameObject goEscMenuFrame;
    private static GameObject goTxtMainMenu;
    private static GameObject goTxtExit;
    private static GuiText gTxtMainMenu;
    private static GuiText gTxtExit;

    public static void show() {
        if(visible) {
            return;
        }
        visible = true;
        optionSelected = false;

        TextureAtlas ta = new TextureAtlas("images/menus/atlas.png", "images/menus/atlas.txt");
        GameObject.instantiate(
                goEscMenuFrame = new GameObject("EscMenuFrame", new GuiRenderer(ta, ta.getSprite("frame3"), 350, 250)),
                new Vector2(Screen.width / 2 - 350 / 2, Screen.height / 2 - 250 / 2)
        );

        TextureAtlas fontTA = new TextureAtlas("fonts/atlas.png", "fonts/atlas.txt");
        if(gTxtMainMenu == null) {
            gTxtMainMenu = new GuiText("Main Menu", fontTA, 50);
            gTxtExit = new GuiText("Exit", fontTA, 50);
            GameObject.instantiate(goTxtMainMenu = new GameObject("TxtMainMenu", gTxtMainMenu), new Vector2(Screen.width / 2.0f - 128.0f, Screen.height / 2.0f - 45.0f));
            GameObject.instantiate(goTxtExit = new GameObject("TxtExit", gTxtExit), new Vector2(Screen.width / 2.0f - 48.0f, Screen.height / 2.0f + 45.0f));
        } else {
            gTxtMainMenu.setText("Main Menu");
            gTxtExit.setText("Exit");
        }
    }

    public static void hide() {
        if(!visible) {
            return;
        }

//        gTxtMainMenu.setText("");
//        gTxtExit.setText("");
        GameObject.destroy(goTxtExit);
        GameObject.destroy(goTxtMainMenu);
        GameObject.destroy(goEscMenuFrame);
        visible = false;
    }

    public static void update() {
        if(!visible) {
            return;
        }

        if(!optionSelected && Input.getKeyUp(Input.KeyCode.MOUSE_LEFT)) {
            if(gTxtMainMenu.hitTest(Input.mousePosition)) {
                optionSelected = true;
                LevelManager.loadLevel("mainmenu");
            } else if(gTxtExit.hitTest(Input.mousePosition)) {
                optionSelected = true;
                System.exit(0);
            }
        }
    }
}

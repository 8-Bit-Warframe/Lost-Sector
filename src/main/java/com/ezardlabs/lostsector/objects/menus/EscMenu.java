package com.ezardlabs.lostsector.objects.menus;

import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.GuiRenderer;
import com.ezardlabs.dethsquare.GuiText;
import com.ezardlabs.dethsquare.Input;
import com.ezardlabs.dethsquare.LevelManager;
import com.ezardlabs.dethsquare.Screen;
import com.ezardlabs.dethsquare.TextureAtlas;
import com.ezardlabs.dethsquare.Vector2;

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
        if(goEscMenuFrame == null) {
            goEscMenuFrame = GameObject.instantiate(
                    new GameObject("EscMenuFrame", new GuiRenderer(ta, ta.getSprite("frame3"), 350, 250)),
                    new Vector2(Screen.width / 2 - 350 / 2, Screen.height / 2 - 250 / 2)
            );
        } else {
            goEscMenuFrame.transform.position.set(
                    Screen.width / 2 - 350 / 2, Screen.height / 2 - 250 / 2);
        }

        TextureAtlas fontTA = new TextureAtlas("fonts/atlas.png", "fonts/atlas.txt");
        if(gTxtMainMenu == null) {
            gTxtMainMenu = new GuiText("Main Menu", fontTA, 50);
        } else {
            gTxtMainMenu.setText("Main Menu");
        }
        goTxtMainMenu = GameObject
                .instantiate(new GameObject("TxtMainMenu", gTxtMainMenu), new Vector2(
                        Screen.width / 2.0f - 128.0f, Screen.height / 2.0f - 45.0f));
        if(gTxtExit == null) {
            gTxtExit = new GuiText("Exit", fontTA, 50);
        } else {
            gTxtExit.setText("Exit");
        }
        goTxtExit = GameObject.instantiate(new GameObject("TxtExit", gTxtExit), new Vector2(
                Screen.width / 2.0f - 48.0f, Screen.height / 2.0f + 45.0f));
    }

    public static void hide() {
        if(!visible) {
            return;
        }
        if(gTxtExit != null) {
            gTxtExit.setText(null);
        }
        if(gTxtMainMenu != null) {
            gTxtMainMenu.setText(null);
        }
        goEscMenuFrame.transform.position.set(100000,100000);
//        GameObject.destroy(goEscMenuFrame);
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
                reset();
            } else if(gTxtExit.hitTest(Input.mousePosition)) {
                optionSelected = true;
                System.exit(0);
            }
        }
    }

    private static void reset() {
        visible = false;
        goEscMenuFrame = null;
        gTxtMainMenu = null;
        goTxtMainMenu = null;
        gTxtExit = null;
        goTxtExit = null;
    }
}

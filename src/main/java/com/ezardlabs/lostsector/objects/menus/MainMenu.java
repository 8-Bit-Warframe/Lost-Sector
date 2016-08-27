package com.ezardlabs.lostsector.objects.menus;

import com.ezardlabs.dethsquare.Animation;
import com.ezardlabs.dethsquare.AnimationType;
import com.ezardlabs.dethsquare.Animator;
import com.ezardlabs.dethsquare.Camera;
import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.GuiRenderer;
import com.ezardlabs.dethsquare.GuiText;
import com.ezardlabs.dethsquare.Input;
import com.ezardlabs.dethsquare.LevelManager;
import com.ezardlabs.dethsquare.Renderer;
import com.ezardlabs.dethsquare.Screen;
import com.ezardlabs.dethsquare.Script;
import com.ezardlabs.dethsquare.TextureAtlas;
import com.ezardlabs.dethsquare.Vector2;

/**
 * Created by Benjamin on 2016-05-15.
 */
public class MainMenu extends Script {

    GuiText gTxtExplore;
    GuiText gTxtSurvival;
    GuiText gTxtProcedural;
    GuiText gTxtExit;

    boolean optionSelected = false;

    @Override
    public void start() {
        GameObject
                .instantiate(new GameObject("MainMenuLisetOpen", new Renderer(), new Animator(), new MainMenuLisetFrost(false)), new Vector2(
                        Screen.width / 2 - 96 * 8 / 2, 75));
//        GameObject.instantiate(new GameObject("MainMenuLisetClose", new Renderer(), new Animator(), new MainMenuLisetFrost(true)), new Vector2(Screen.width / 2 - 96 * 8 / 2, 75));

        TextureAtlas ta = new TextureAtlas("images/menus/atlas.png", "images/menus/atlas.txt");

        GameObject.instantiate(
                new GameObject("MainMenuLogo", new GuiRenderer(ta, ta.getSprite("logo_vertical"), 900, 225)),
                new Vector2(Screen.width / 2 - 450, 10)
        );

        GameObject.instantiate(
                new GameObject("MainMenuFrame", new GuiRenderer(ta, ta.getSprite("frame6"), 350, 490)),
                new Vector2(Screen.width / 2 - 175, Screen.height / 2)
        );

        TextureAtlas fontTA = new TextureAtlas("fonts/atlas.png", "fonts/atlas.txt");

        gTxtExplore = new GuiText("Explore", fontTA, 50);
        gTxtSurvival = new GuiText("Survival", fontTA, 50);
        gTxtProcedural = new GuiText("Procedural", fontTA, 50);
        gTxtExit = new GuiText("Exit", fontTA, 50);

        GameObject.instantiate(new GameObject("TxtExplore", gTxtExplore), new Vector2(
                Screen.width / 2.0f - 84.0f, Screen.height / 2.0f + 90.0f));
        GameObject.instantiate(new GameObject("TxtSurvival", gTxtSurvival), new Vector2(
                Screen.width / 2.0f - 96.0f, Screen.height / 2.0f + 180.0f));
        GameObject.instantiate(new GameObject("TxtSurvival", gTxtProcedural), new Vector2(
                Screen.width / 2.0f - 128.0f, Screen.height / 2.0f + 270.0f));
        GameObject.instantiate(new GameObject("TxtExit", gTxtExit), new Vector2(
                Screen.width / 2.0f - 48.0f, Screen.height / 2.0f + 360.0f));

        GameObject.instantiate(
                new GameObject(
                        "MainMenuCommunityMsg",
                        new GuiText("Lost Sector is a community project developed by Warframe fans for Warframe fans!", fontTA, 25)
                ),
                new Vector2(Screen.width / 2 - 650, Screen.height - 100)
        );
        GameObject.instantiate(
                new GameObject(
                        "MainMenuDisclaimer",
                        new GuiText("8Bit Warframe is not affiliated with Digital Extremes.", fontTA, 25)
                ),
                new Vector2(10, Screen.height - 25)
        );

        GameObject.instantiate(
                new GameObject(
                        "MainMenuWIP",
                        new GuiText("DEV BUILD : WORK IN PROGRESS!", fontTA, 30)
                ),
                new Vector2(10, 10)
        );

//        AudioSource as = new AudioSource();
//        as.play(new AudioSource.AudioClip("audio/theme.ogg"));
//        GameObject.instantiate(new GameObject("AudioSource", as), new Vector2());

        GameObject.instantiate(new GameObject("CameraMainMenu", new Camera(true)), new Vector2());
    }

    @Override
    public void update() {
        if(!optionSelected && Input.getKeyUp(Input.KeyCode.MOUSE_LEFT)) {
            if(gTxtExplore.hitTest(Input.mousePosition)) {
                optionSelected = true;
                this.destroy();
                LevelManager.loadLevel("explore");
            }
            else if(gTxtSurvival.hitTest(Input.mousePosition)) {
                optionSelected = true;
                this.destroy();
                LevelManager.loadLevel("survival");
            }
            else if(gTxtProcedural.hitTest(Input.mousePosition)) {
                optionSelected = true;
                this.destroy();
                LevelManager.loadLevel("procedural");
            }
            else if(gTxtExit.hitTest(Input.mousePosition)) {
                optionSelected = true;
                System.exit(0);
            }
        }
    }

    private class MainMenuLisetFrost extends Script {
        Animation[] animations = null;
        TextureAtlas ta = null;
        boolean close = false;

        public MainMenuLisetFrost(boolean close) {
            ta = new TextureAtlas("images/menus/liset_frost_atlas.png", "images/menus/liset_frost_atlas.txt");
            animations = new Animation[] {
                    new Animation("liset_frost_open", new TextureAtlas.Sprite[] {
                            ta.getSprite("closed_0"),
                            ta.getSprite("turn_l_1"),
                            ta.getSprite("turn_l_2"),
                            ta.getSprite("turn_l_3"),
                            ta.getSprite("turn_l_4"),
                            ta.getSprite("turn_l_5"),
                            ta.getSprite("turn_l_6"),
                            ta.getSprite("turn_l_7"),
                            ta.getSprite("turn_l_8"),
                            ta.getSprite("turn_l_9"),
                            ta.getSprite("open"),
                    }, AnimationType.ONE_SHOT, 100),
                    new Animation("liset_frost_close", new TextureAtlas.Sprite[] {
                            ta.getSprite("open"),
                            ta.getSprite("turn_l_9"),
                            ta.getSprite("turn_l_8"),
                            ta.getSprite("turn_l_7"),
                            ta.getSprite("turn_l_6"),
                            ta.getSprite("turn_l_5"),
                            ta.getSprite("turn_l_4"),
                            ta.getSprite("turn_l_3"),
                            ta.getSprite("turn_l_2"),
                            ta.getSprite("turn_l_1"),
                            ta.getSprite("closed_0"),
                    }, AnimationType.ONE_SHOT, 100),
            };

            this.close = close;
        }

        @Override
        public void start() {
            gameObject.renderer.setTextureAtlas(ta, 96 * 8, 168 * 8);
            gameObject.animator.setAnimations(animations);
            gameObject.animator.play(close ? "liset_frost_close" : "liset_frost_open");
        }
    }
}

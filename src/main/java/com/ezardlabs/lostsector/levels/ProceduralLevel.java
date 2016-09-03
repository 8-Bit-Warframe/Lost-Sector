package com.ezardlabs.lostsector.levels;

import com.ezardlabs.dethsquare.Animator;
import com.ezardlabs.dethsquare.AudioSource;
import com.ezardlabs.dethsquare.AudioSource.AudioClip;
import com.ezardlabs.dethsquare.Camera;
import com.ezardlabs.dethsquare.Collider;
import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.GuiText;
import com.ezardlabs.dethsquare.Level;
import com.ezardlabs.dethsquare.Renderer;
import com.ezardlabs.dethsquare.Rigidbody;
import com.ezardlabs.dethsquare.Screen;
import com.ezardlabs.dethsquare.TextureAtlas;
import com.ezardlabs.dethsquare.Vector2;
import com.ezardlabs.lostsector.Game;
import com.ezardlabs.lostsector.map.MapManager;
import com.ezardlabs.lostsector.map.procedural.MapConfig;
import com.ezardlabs.lostsector.objects.CameraMovement;
import com.ezardlabs.lostsector.objects.Player;
import com.ezardlabs.lostsector.objects.hud.HUD;
import com.ezardlabs.lostsector.objects.warframes.Frost;

public class ProceduralLevel extends Level {
    private static CameraMovement cm = new CameraMovement();

    @Override
    public void onLoad() {
        MapManager.playerSpawn = new Vector2(20.0f, 20.0f);
        MapConfig mapCfg = new MapConfig(MapConfig.ProceduralType.CORPUS, 50);
        MapManager.loadProceduralMap(mapCfg);

        HUD.init();

        createPlayer();

        AudioSource as = new AudioSource();

        GameObject.instantiate(new GameObject("Camera", new Camera(true), cm, as), new Vector2
                (MapManager.playerSpawn.x, MapManager.playerSpawn.y));
//        as.play(new AudioClip("audio/theme.ogg"));

        TextureAtlas fontTA = new TextureAtlas("fonts/atlas.png", "fonts/atlas.txt");
        GameObject.instantiate(
                new GameObject(
                        "MainMenuWIP",
                        new GuiText("DEV BUILD : WORK IN PROGRESS!", fontTA, 30)
                ),
                new Vector2(10, Screen.height - 30 - 10)
        );
    }

    private static void createPlayer() {
        Game.players = new GameObject[]{new GameObject("Player", new Player(), new Renderer(),
                new Animator(), new Frost(), new Collider(200, 200), new Rigidbody())};

        Vector2 playerPos = new Vector2(MapManager.playerSpawn.x, MapManager.playerSpawn.y);
        System.out.println("Player Spawn: " + playerPos);
        GameObject.instantiate(Game.players[0], playerPos);

        cm.smoothFollow(Game.players[0].transform);
    }
}

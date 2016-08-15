package com.ezardlabs.lostsector;

import com.ezardlabs.dethsquare.Animator;
import com.ezardlabs.dethsquare.Collider;
import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.Renderer;
import com.ezardlabs.dethsquare.Rigidbody;
import com.ezardlabs.dethsquare.Script;
import com.ezardlabs.dethsquare.Vector2;
import com.ezardlabs.lostsector.objects.enemies.Enemy;
import com.ezardlabs.lostsector.objects.enemies.corpus.crewmen.DeraCrewman;
import com.ezardlabs.lostsector.objects.enemies.corpus.crewmen.ProvaCrewman;
import com.ezardlabs.lostsector.objects.enemies.corpus.crewmen.SupraCrewman;

import java.util.ArrayList;

/**
 * Created by Benjamin on 2016-07-07.
 */
public class SurvivalManager extends Script {

    public int score = 0;
    public int lives = 3;
    public ArrayList<Vector2> spawns;
    public ArrayList<Enemy> enemies;
    public int maxEnemies;
    public long startTime;
    public long elapsedTime;

    public SurvivalManager(ArrayList<Vector2> enemySpawns) {
        spawns = enemySpawns;
        enemies = new ArrayList<>();
        maxEnemies = 50;
    }

    @Override
    public void start() {
        startTime = System.currentTimeMillis();
        elapsedTime = 0;
    }

    @Override
    public void update() {
        elapsedTime = System.currentTimeMillis() - startTime;

        if (elapsedTime > 5000) {
            if (enemies.size() < maxEnemies) {
                startTime = System.currentTimeMillis();
                int randSpawnIdx = (int) (Math.random() * spawns.size());
                double randEnemy = Math.random();
                Enemy enemy = null;
                String enemyName = null;
                if (randEnemy < 0.5) {
                    enemy = new ProvaCrewman();
                    enemyName = "Prova Crewman";
                } else if (randEnemy < 0.85) {
                    enemy = new DeraCrewman();
                    enemyName = "Dera Crewman";
                } else if (randEnemy < 1.0) {
                    enemy = new SupraCrewman();
                    enemyName = "Supra Crewman";
                }
                if (enemy != null) {
                    enemies.add(enemy);
                    GameObject.instantiate(
                            new GameObject(enemyName, new Renderer(), new Animator(), new Collider(200, 200), new Rigidbody(), enemy),
                            spawns.get(randSpawnIdx)
                    );
                }
                for (int i = enemies.size() - 1; i >= 0; i--) {
                    if (enemies.get(i).getHealth() <= 0) {
                        GameObject.destroy(enemies.get(i).gameObject, 1000);
                        enemies.remove(i);
                    }
                }
            }
        }
    }
}

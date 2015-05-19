package com.ezardlabs.lostsector;

import com.ezardlabs.dethsquare.Collider;
import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.Renderer;
import com.ezardlabs.dethsquare.Screen;
import com.ezardlabs.dethsquare.Vector2;
import com.ezardlabs.dethsquare.util.BaseGame;
import com.ezardlabs.lostsector.objects.Player;

public class Game extends BaseGame {

	@Override
	public void create() {
		MapManager.loadMap("map");
		GameObject.instantiate(
				new GameObject("Player", new Player(), new Renderer("images/fidle.png", 200, 200),
						new Collider(200, 200)), new Vector2(20, 20));
	}

	@Override
	public void update() {
		GameObject.updateAll();
	}

	@Override
	public void render() {
		Renderer.renderAll();
	}

	@Override
	public void onResize(int width, int height) {
		Screen.scale = (float) width / 1920f;
		Screen.width = width;
		Screen.height = height;
	}
}
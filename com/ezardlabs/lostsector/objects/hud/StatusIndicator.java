package com.ezardlabs.lostsector.objects.hud;

import android.util.Log;

import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.GuiRenderer;
import com.ezardlabs.dethsquare.Renderer;
import com.ezardlabs.dethsquare.Screen;
import com.ezardlabs.dethsquare.TextureAtlas;
import com.ezardlabs.dethsquare.Vector2;

public class StatusIndicator {
	private TextureAtlas ta;
	private GuiRenderer energy;
	private GuiRenderer health;
	private GuiRenderer shields;

	public void init() {
		ta = new TextureAtlas("images/hud/atlas.png", "images/hud/atlas.txt");
		GameObject.instantiate(new GameObject("Health Indicator", energy = new GuiRenderer(ta, ta.getSprite("energy_100"), 200, 200)), new Vector2(Screen.width / 2f - 100, 25));
		GameObject.instantiate(new GameObject("Health Indicator", health = new GuiRenderer(ta, ta.getSprite("health_100"), 200, 200)), new Vector2(Screen.width / 2f - 100, 25));
		GameObject.instantiate(new GameObject("Health Indicator", shields = new GuiRenderer(ta, ta.getSprite("shield_2"), 200, 200)), new Vector2(Screen.width / 2f - 100, 25));
	}

	public void setHealth(int health) {
		Log.i("", "[setHealth] setting health to " + health);
		this.health.setSprite(ta.getSprite("health_" + health));
	}

	public void setEnergy(int energy) {
		Log.i("", "[setEnergy] setting energy to " + energy);
		if (energy == 0) {
			this.energy.mode = Renderer.Mode.NONE;
		} else {
			this.energy.mode = Renderer.Mode.SPRITE;
			this.energy.setSprite(ta.getSprite("energy_" + energy));
		}
	}
}

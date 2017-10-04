package com.ezardlabs.lostsector.missions.objectives;

import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.GuiRenderer;
import com.ezardlabs.dethsquare.Layers;
import com.ezardlabs.dethsquare.Level;
import com.ezardlabs.dethsquare.LevelManager;
import com.ezardlabs.dethsquare.Screen;
import com.ezardlabs.dethsquare.Vector2;
import com.ezardlabs.lostsector.Game.DamageType;
import com.ezardlabs.lostsector.levels.DefenseLevel;
import com.ezardlabs.lostsector.objects.ShieldedEntity;
import com.ezardlabs.lostsector.objects.hud.CryopodStatus;

public class Cryopod extends ShieldedEntity {
	private int id;
	private CryopodStatus status = new CryopodStatus();
	private boolean dead = false;

	public Cryopod() {
		super(100, 200, 1000);
	}

	@Override
	public void start() {
		gameObject.renderer.setImage("data/objectives/cryopod/cryopod.png", 300, 100);
		gameObject.renderer.setOffsets(50, 100);
		gameObject.renderer.setDepth(-10);
		gameObject.setLayer(Layers.getLayer("Objective"));

		GameObject.instantiate(
				new GameObject("Cryopod Status", new GuiRenderer("images/hud/cryopod_status.png", 208, 104), status),
				new Vector2(Screen.width / 2 - 104, 12));
	}

	@Override
	public void update() {
		super.update();
		status.setData(getHealth(), getMaxHealth(), getShield(), getMaxShield());
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	protected void die(DamageType damageType, Vector2 attackOrigin) {
		if (!dead) {
			Level level = LevelManager.getCurrentLevel();
			if (level instanceof DefenseLevel) {
				((DefenseLevel) level).getMission().onCryopodDestroyed(id);
			}
			dead = true;
		}
	}
}

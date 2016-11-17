package com.ezardlabs.lostsector.objects.hud;

import com.ezardlabs.dethsquare.Script;
import com.ezardlabs.dethsquare.TextureAtlas;
import com.ezardlabs.dethsquare.Touch;
import com.ezardlabs.dethsquare.Vector2;
import com.ezardlabs.lostsector.objects.hud.WeaponControl.WeaponType;
import com.ezardlabs.lostsector.objects.warframes.Warframe;

public class HUD extends Script {
	private StatusIndicator statusIndicator = new StatusIndicator();
	private WeaponControl weaponControl = new WeaponControl();
	private Warframe warframe;

	public void start() {
		TextureAtlas ta = new TextureAtlas("images/hud/atlas.png", "images/hud/atlas.txt");
		statusIndicator.init(ta);
//		weaponControl.init(ta);
		warframe = gameObject.getComponentOfType(Warframe.class);
		statusIndicator.setWarframeName(warframe.getName());
	}

	public void update() {
		statusIndicator.update(warframe.getHealth(), warframe.getShield(), warframe.getEnergy());
//		weaponControl.update();
	}

	public boolean isAttackButtonPressed(Vector2 position) {
		return weaponControl.isAttackButtonPressed();
	}

	public WeaponType getCurrentWeaponType() {
		return weaponControl.getCurrentWeaponType();
	}

	public void switchWeapons() {
		weaponControl.switchWeapons();
	}

	public boolean isSwitchButtonPressed() {
		return weaponControl.isSwitchButtonPressed();
	}

	public boolean attackButtonHitTest(Touch t) {
		return weaponControl.attackButtonHitTest(t);
	}

	public boolean switchButtonHitTest(Touch t) {
		return weaponControl.switchButtonHitTest(t);
	}
}

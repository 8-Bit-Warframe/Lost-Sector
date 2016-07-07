package com.ezardlabs.lostsector.objects.hud;

import com.ezardlabs.dethsquare.TextureAtlas;
import com.ezardlabs.dethsquare.Touch;
import com.ezardlabs.dethsquare.Vector2;
import com.ezardlabs.lostsector.objects.hud.WeaponControl.WeaponType;

public class HUD {
	public static StatusIndicator statusIndicator = new StatusIndicator();
	public static WeaponControl weaponControl = new WeaponControl();

	public static void init() {
		TextureAtlas ta = new TextureAtlas("images/hud/atlas.png", "images/hud/atlas.txt");
		statusIndicator.init(ta);
//		weaponControl.init(ta);
	}

	public static void update(int health, int energy) {
		statusIndicator.update(health, energy);
//		weaponControl.update();
	}

	public static boolean isAttackButtonPressed(Vector2 position) {
		return weaponControl.isAttackButtonPressed();
	}

	public static WeaponType getCurrentWeaponType() {
		return weaponControl.getCurrentWeaponType();
	}

	public static void switchWeapons() {
		weaponControl.switchWeapons();
	}

	public static boolean isSwitchButtonPressed() {
		return weaponControl.isSwitchButtonPressed();
	}

	public static boolean attackButtonHitTest(Touch t) {
		return weaponControl.attackButtonHitTest(t);
	}

	public static boolean switchButtonHitTest(Touch t) {
		return weaponControl.switchButtonHitTest(t);
	}
}

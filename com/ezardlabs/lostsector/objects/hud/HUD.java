package com.ezardlabs.lostsector.objects.hud;

import com.ezardlabs.dethsquare.TextureAtlas;
import com.ezardlabs.dethsquare.Vector2;

public class HUD {
	public static StatusIndicator statusIndicator = new StatusIndicator();
	public static WeaponControl weaponControl = new WeaponControl();

	public static void init() {
		TextureAtlas ta = new TextureAtlas("images/hud/atlas.png", "images/hud/atlas.txt");
		statusIndicator.init(ta);
		weaponControl.init(ta);
	}

	public static void update() {
		weaponControl.update();
	}

	public static boolean isAttackButtonPressed(Vector2 position) {
		return weaponControl.isAttackButtonPressed();
	}
}

package com.ezardlabs.lostsector.objects.hud;

public class HUD {
	public static StatusIndicator statusIndicator = new StatusIndicator();

	public static void init() {
		statusIndicator.init();
	}
}

package com.ezardlabs.lostsector.objects.enemies;

import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.Script;

public class CorpseFader extends Script {
	private static final float FADE_TIME = 1000;
	private long startTime;
	private float[] tint = new float[4];

	@Override
	public void start() {
		startTime = System.currentTimeMillis();
	}

	@Override
	public void update() {
		System.arraycopy(gameObject.renderer.getTint(), 0, tint, 0, 4);
		tint[3] = (FADE_TIME - (System.currentTimeMillis() - startTime)) / FADE_TIME;
		gameObject.renderer.setTint(tint[0], tint[1], tint[2], tint[3]);
		if (tint[3] == 0) {
			GameObject.destroy(gameObject);
		}
	}
}

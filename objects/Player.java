package com.ezardlabs.lostsector.objects;

import com.ezardlabs.dethsquare.Input;
import com.ezardlabs.dethsquare.PlayerBase;
import com.ezardlabs.dethsquare.Renderer;

public class Player extends PlayerBase {

	public void start() {
	}

	public void update() {
		gravity += 1.25f;
		if (gravity > 78.125f) gravity = 78.125f;
		transform.translate(Input.x * 12.5f, gravity);
		if (Input.x < 0) {
			gameObject.getComponent(Renderer.class).hFlipped = true;
		} else if (Input.x > 0) {
			gameObject.getComponent(Renderer.class).hFlipped = false;
		}
	}
}

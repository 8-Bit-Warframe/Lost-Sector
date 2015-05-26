package com.ezardlabs.lostsector.objects;

import com.ezardlabs.dethsquare.Animator;
import com.ezardlabs.dethsquare.Script;

public class Camera extends Script {

	@Override
	public void start() {
		getComponent(Animator.class).play("cycle");
	}

	@Override
	public void update() {

	}
}

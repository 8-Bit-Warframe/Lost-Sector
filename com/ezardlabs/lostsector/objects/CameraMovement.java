package com.ezardlabs.lostsector.objects;

import com.ezardlabs.dethsquare.Transform;
import com.ezardlabs.dethsquare.Camera;
import com.ezardlabs.dethsquare.Screen;
import com.ezardlabs.dethsquare.Script;

public class CameraMovement extends Script {
	Camera camera;
	int followType = 0;
	Transform target;
	private boolean isQuaking = false;
	private long quakeEndPoint = 0;
	private float quakeStrength = 0;

	public void start() {
		camera = getComponent(Camera.class);
	}

	public void update() {
		if (target != null) {
			float cameraX;
			if (target.position.x < Screen.width / 2) {
				cameraX = 0;
			} else {
				cameraX = target.position.x - Screen.width / 2;
			}
			float cameraY;
			if (target.position.y < Screen.height / 2) {
				cameraY = 0;
			} else {
				cameraY = target.position.y - Screen.height / 2;
			}
			switch (followType) {
				case 1:
					transform.position.x = cameraX;
					transform.position.y = cameraY;
					break;
				case 2:
					float dx = cameraX - transform.position.x;
					float dy = cameraY - transform.position.y;
					double h = Math.sqrt(dx * dx + dy * dy);
					float dn = (float) (h / Math.sqrt(2.0D));
					if (dn != 0) {
						transform.position.x += dx / dn * (dn * 0.04F);
						transform.position.y += dy / dn * (dn * 0.2f);
					}
					break;
			}
		}
		if (isQuaking) updateQuake();
	}

	public void follow(Transform target) {
		followType = 1;
		this.target = target;
	}

	public void smoothFollow(Transform target) {
		followType = 2;
		this.target = target;
	}

	public void startQuake(long length, float strengthFactor) {
		this.quakeStrength = strengthFactor * 3.125f;
		this.quakeEndPoint = (System.currentTimeMillis() + length);
		this.isQuaking = true;
	}

	private void updateQuake() {
		transform.position.x += (int) (35.0F * this.quakeStrength -
				Math.random() * 70.0D * this.quakeStrength);
		transform.position.y += (int) (35.0F * this.quakeStrength -
				Math.random() * 70.0D * this.quakeStrength);
		if (System.currentTimeMillis() >= this.quakeEndPoint) {
			this.isQuaking = false;
		}
	}
}

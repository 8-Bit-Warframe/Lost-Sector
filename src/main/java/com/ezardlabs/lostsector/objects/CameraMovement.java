package com.ezardlabs.lostsector.objects;

import com.ezardlabs.dethsquare.Screen;
import com.ezardlabs.dethsquare.Script;
import com.ezardlabs.dethsquare.Transform;
import com.ezardlabs.dethsquare.Vector2;

public class CameraMovement extends Script {
	private FollowType followType;
	private Transform target;
	private Vector2 offset = new Vector2(-450, 0);
	private boolean isQuaking = false;
	private long quakeEndPoint = 0;
	private float quakeStrength = 0;

	enum FollowType {
		DIRECT,
		SMOOTH
	}

	public CameraMovement() {
		this(null, FollowType.DIRECT);
	}

	public CameraMovement(Transform target, FollowType followType) {
		this.target = target;
		this.followType = followType;
	}

	public void start() {
		if (target != null) {
			follow(target);
			update();
			smoothFollow(target);
		}
	}

	public void update() {
		if (target != null) {
			float x = target.position.x - offset.x;
			float y = target.position.y + offset.y;

			float cameraX;
			if (x < (Screen.width / 2) / Screen.scale) {
				cameraX = 0;
			} else {
				cameraX = x - (Screen.width / 2) / Screen.scale;
			}
			float cameraY;
			if (y < (Screen.height / 2) / Screen.scale) {
				cameraY = 0;
			} else {
				cameraY = y - (Screen.height / 2) / Screen.scale;
			}
			switch (followType) {
				case DIRECT:
					transform.position.x = cameraX;
					transform.position.y = cameraY;
					break;
				case SMOOTH:
					float dx = cameraX - transform.position.x;
					float dy = cameraY - transform.position.y;
					double h = Math.sqrt(dx * dx + dy * dy);
					float dn = (float) (h / Math.sqrt(2.0D));
					if (dn != 0) {
						transform.position.x += dx / dn * (dn * 0.04F);
						transform.position.y += dy / dn * (dn * 0.2f);
					}
					break;
				default:
					break;
			}
		}
		if (isQuaking) updateQuake();
	}

	public void follow(Transform target) {
		followType = FollowType.DIRECT;
		this.target = target;
	}

	public void follow(Transform target, Vector2 offset) {
		followType = FollowType.DIRECT;
		this.target = target;
		this.offset = offset;
	}

	public void smoothFollow(Transform target) {
		followType = FollowType.SMOOTH;
		this.target = target;
	}

	public void smoothFollow(Transform target, Vector2 offset) {
		followType = FollowType.SMOOTH;
		this.target = target;
		this.offset = offset;
	}

	public void startQuake(long length, float strengthFactor) {
		this.quakeStrength = strengthFactor * 3.125f;
		this.quakeEndPoint = (System.currentTimeMillis() + length);
		this.isQuaking = true;
	}

	private void updateQuake() {
		transform.position.x += (int) (35.0F * this.quakeStrength - Math.random() * 70.0D * this.quakeStrength);
		transform.position.y += (int) (35.0F * this.quakeStrength - Math.random() * 70.0D * this.quakeStrength);
		if (System.currentTimeMillis() >= this.quakeEndPoint) {
			this.isQuaking = false;
		}
	}
}

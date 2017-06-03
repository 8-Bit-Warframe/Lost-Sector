package com.ezardlabs.lostsector.camera;

import com.ezardlabs.dethsquare.Screen;
import com.ezardlabs.dethsquare.Script;
import com.ezardlabs.dethsquare.Transform;
import com.ezardlabs.dethsquare.Vector2;
import com.ezardlabs.dethsquare.debug.Debug;

import java.util.ArrayList;

public class SmartCamera extends Script {
	private static final boolean debug = false;

	private static ArrayList<CameraPOI> pois = new ArrayList<>();
	private Transform followTarget;
	private float maxLookahead;
	private Vector2 offset = new Vector2(0, 0);
	private Vector2 base = new Vector2();
	private Vector2 inputTarget = new Vector2();
	private Vector2 inputCurrent = new Vector2();
	private Vector2 target = new Vector2();

	private Vector2 lastFollowTargetPos = new Vector2();

	public SmartCamera(Transform followTarget, float maxLookahead) {
		this.followTarget = followTarget;
		this.maxLookahead = maxLookahead;
	}

	public SmartCamera(Transform followTarget, float maxLookahead, Vector2 offset) {
		this(followTarget, maxLookahead);
		this.offset = offset;
	}

	@Override
	public void start() {
		transform.position.set(followTarget.position);
		lastFollowTargetPos.set(followTarget.position);
	}

	@Override
	public void update() {
		inputTarget.set(0, 0);
		if (followTarget.position.y - lastFollowTargetPos.y < 0) {
			inputTarget.y -= 1;
		}
		if (followTarget.position.x - lastFollowTargetPos.x < 0) {
			inputTarget.x -= 1;
		}
		if (followTarget.position.y - lastFollowTargetPos.y > 0) {
			inputTarget.y += 1;
		}
		if (followTarget.position.x - lastFollowTargetPos.x > 0) {
			inputTarget.x += 1;
		}
		inputTarget.normalise();

		inputTarget.multiplyBy(maxLookahead);

		base.set(followTarget.position.x + offset.x, followTarget.position.y + offset.y);

		lerp(inputCurrent, inputTarget);

		target.set(base.x + inputCurrent.x, base.y + inputCurrent.y);

		Debug.drawCircle(target, 30, 1, 0, 0);

		target.x -= (Screen.width / 2) / Screen.scale;
		target.y -= (Screen.height / 2) / Screen.scale;


		lerp(transform.position, target);

		if (transform.position.x < 0) {
			transform.position.x = 0;
		}
		if (transform.position.y < 0) {
			transform.position.y = 0;
		}

		if (debug) {
			Debug.drawCircle(base, 100, 1, 0, 0);
			Debug.drawCircle(base.offset(inputTarget.dividedBy(maxLookahead).x * 100,
					inputTarget.dividedBy(maxLookahead).y * 100), 20, 1, 0, 0);
		}

		lastFollowTargetPos.set(followTarget.position);
	}

	private void lerp(Vector2 current, Vector2 target) {
		float dx = target.x - current.x;
		float dy = target.y - current.y;
		double h = Math.sqrt(dx * dx + dy * dy);
		float dn = (float) (h / Math.sqrt(2.0D));
		if (dn != 0) {
			current.x += dx / dn * (dn * 0.03F);// 0.04
			current.y += dy / dn * (dn * 0.02f);
		}
	}

	public void setFollowTarget(Transform followTarget) {
		this.followTarget = followTarget;
	}

	static void registerPOI(CameraPOI poi) {
		pois.add(poi);
	}

	static void unregisterPOI(CameraPOI poi) {
		pois.remove(poi);
	}
}

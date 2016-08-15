package com.ezardlabs.lostsector.ai;

import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.Renderer;
import com.ezardlabs.dethsquare.Script;
import com.ezardlabs.dethsquare.Transform;
import com.ezardlabs.dethsquare.Vector2;
import com.ezardlabs.lostsector.NavMesh;
import com.ezardlabs.lostsector.NavMesh.NavPoint;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class Behaviour extends Script {
	private final float moveSpeed;
	private NavPoint lastTargetNavPoint;
	private ArrayList<NavPoint> path;
	protected Transform target;
	private GameObject demo;

	public Behaviour(float moveSpeed) {
		this.moveSpeed = moveSpeed;
		demo = new GameObject("Demo", new Renderer("images/pink.png", 10, 10));
	}

	@Override
	public void start() {
		GameObject.instantiate(demo, new Vector2());
	}

	public void update() {
		NavPoint temp;
		if (target != null && (temp = NavMesh.getNavPoint(target.position)) != lastTargetNavPoint) {
			if (lastTargetNavPoint != null) {
				if (path != null) {
					path.addAll(Arrays.asList(NavMesh.getPath(lastTargetNavPoint.position, temp.position)));
				} else {
					path = new ArrayList<>(Arrays.asList(NavMesh.getPath(lastTargetNavPoint.position, temp.position)));
				}
			}
			lastTargetNavPoint = temp;
		}
	}

	public Transform getTarget() {
		return target;
	}

	public final void setTarget(Transform target) {
		this.target = target;
		setPath(NavMesh.getPath(transform, target));
	}

	public final void setTarget(Vector2 target) {
		setPath(NavMesh.getPath(transform.position, target));
	}

	private void setPath(NavPoint[] path) {
		this.path = new ArrayList<>(Arrays.asList(path));
	}

	protected final void move() {
		if (path.size() > 0) {
			Vector2 target = path.get(0).position;
			if (target.x == transform.position.x) {
				path.remove(0);
				if (path.size() == 0) {
					return;
				}
				target = path.get(0).position;
			}
			demo.transform.position = target;

			if (target.x < transform.position.x) {
				gameObject.renderer.hFlipped = true;
			} else if (target.x > transform.position.x) {
				gameObject.renderer.hFlipped = false;
			}

			if (target.y < transform.position.y && gameObject.rigidbody.velocity.y >= 0) {
				gameObject.rigidbody.velocity.y = -30f;
			}

//			System.out.println("Before: " + transform.position + " -> " + target);

			if (target.x > transform.position.x) {
				transform.translate(moveSpeed, 0);
				if (target.x <= transform.position.x) {
					path.remove(0);
				}
			} else {
				transform.translate(-moveSpeed, 0);
				if (target.x >= transform.position.x) {
					path.remove(0);
				}
			}

//			System.out.println("After: " + transform.position + " -> " + target);
		}
	}
}

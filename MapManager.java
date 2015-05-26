package com.ezardlabs.lostsector;

import com.ezardlabs.dethsquare.Animation;
import com.ezardlabs.dethsquare.Animation.AnimationType;
import com.ezardlabs.dethsquare.Animator;
import com.ezardlabs.dethsquare.Collider;
import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.Renderer;
import com.ezardlabs.dethsquare.TextureAtlas;
import com.ezardlabs.dethsquare.TextureAtlas.Sprite;
import com.ezardlabs.dethsquare.Vector2;
import com.ezardlabs.dethsquare.util.Utils;
import com.ezardlabs.lostsector.objects.Camera;
import com.ezardlabs.lostsector.objects.Door;
import com.ezardlabs.lostsector.objects.Locker;

import java.io.BufferedReader;
import java.io.IOException;

public class MapManager {

	public static void loadMap(String name) {
		TextureAtlas eta = new TextureAtlas("images/environment/atlas.png",
				"images/environment/atlas.txt");
		try {
			String temp;
			BufferedReader reader = Utils.getReader("maps/" + name + ".lsmap");
			while ((temp = reader.readLine()) != null) {
				if (temp.equals("BEGIN BLOCKS")) readInBlocks(reader);
				if (temp.equals("BEGIN LOCKERS")) readInLockers(reader, eta);
				if (temp.equals("BEGIN DOORS")) readInDoors(reader, eta);
				if (temp.equals("BEGIN CAMERAS")) readInCameras(reader, eta);
			}
		} catch (IOException ignored) {
		}
	}

	private static void readInBlocks(BufferedReader reader) throws IOException {
		TextureAtlas ta = new TextureAtlas("images/tiles/atlas.png", "images/tiles/atlas.txt");
		String temp;
		while ((temp = reader.readLine()) != null && !temp.equals("END BLOCKS")) {
			String[] split = temp.split(",");
			float x = Float.valueOf(split[0]) * 3.125f;
			float y = Float.valueOf(split[1]) * 3.125f;
			float w = Float.valueOf(split[2]) * 3.125f;
			float h = Float.valueOf(split[3]) * 3.125f;
			if (Boolean.valueOf(split[5])) {
				GameObject.instantiate(
						new GameObject(null, true, new Renderer(ta, ta.getSprite(split[4]), w, h),
								new Collider(w, h)), new Vector2(x, y));
			} else {
				GameObject.instantiate(
						new GameObject(null, true, new Renderer(ta, ta.getSprite(split[4]), w, h)),
						new Vector2(x, y));
			}
		}
	}

	private static void readInDoors(BufferedReader reader, TextureAtlas ta) throws IOException {
		String temp;
		while ((temp = reader.readLine()) != null && !temp.equals("END DOORS")) {
			String[] split = temp.split(",");
			float x = Float.valueOf(split[0]) * 3.125f;
			float y = Float.valueOf(split[1]) * 3.125f;
			float w = Float.valueOf(split[2]) * 3.125f;
			float h = Float.valueOf(split[3]) * 3.125f;
			if (split[4].equals("door")) {
				GameObject.instantiate(new GameObject("Door", new Door(), new Collider(w, h, true),
								new Renderer(ta, ta.getSprite("door0"), w, h)
										.setFlipped(Boolean.valueOf(split[5]), false), new Animator(
								new Animation("open",
										new Sprite[]{ta.getSprite("door0"), ta.getSprite("door1"),
													 ta.getSprite("door2"), ta.getSprite("door3"),
													 ta.getSprite("door4"), ta.getSprite("door5"),
													 ta.getSprite("door6")}, AnimationType.ONE_SHOT,
										80), new Animation("close",
								new Sprite[]{ta.getSprite("door6"), ta.getSprite("door5"),
											 ta.getSprite("door4"), ta.getSprite("door3"),
											 ta.getSprite("door2"), ta.getSprite("door1"),
											 ta.getSprite("door0")}, AnimationType.ONE_SHOT, 80))),
						new Vector2(x, y));
			} else if (split[4].equals("ldoor")) {
				GameObject.instantiate(
						new GameObject("Door", new Door(), new Collider(100, 500, true),
								new Renderer(ta, ta.getSprite("ldoor0"), w, h)
										.setFlipped(Boolean.valueOf(split[5]), false), new Animator(
								new Animation("open", new Sprite[]{ta.getSprite("ldoor")},
										AnimationType.ONE_SHOT, 80), new Animation("close",
								new Sprite[]{ta.getSprite("ldoor0"), ta.getSprite("ldoor1"),
											 ta.getSprite("ldoor2"), ta.getSprite("ldoor3"),
											 ta.getSprite("ldoor4"), ta.getSprite("ldoor5"),
											 ta.getSprite("ldoor6"), ta.getSprite("ldoor7"),
											 ta.getSprite("ldoor8"), ta.getSprite("ldoor9"),
											 ta.getSprite("ldoor10")}, AnimationType.OSCILLATE,
								80))), new Vector2(x, y));
			}
			GameObject.instantiate(
					new GameObject("DoorCollider", true, new Collider(64 * 3.125f, 32 * 3.125f)),
					new Vector2(x, y));
			GameObject.instantiate(
					new GameObject("DoorCollider", true, new Collider(64 * 3.125f, 32 * 3.125f)),
					new Vector2(x, y + 128 * 3.125f));
//			GameObject.instantiate(
//					new GameObject("DoorCollider", true, new Collider(64 * 3.125f, 32 * 3.125f)),
//					new Vector2(x, y));
//			GameObject.instantiate(
//					new GameObject("DoorCollider", true, new Collider(64 * 3.125f, 32 * 3.125f)),
//					new Vector2(x, y + 128 * 3.125f));
		}
	}

	private static void readInLockers(BufferedReader reader, TextureAtlas ta) throws IOException {
		String temp;
		while ((temp = reader.readLine()) != null && !temp.equals("END LOCKERS")) {
			String[] split = temp.split(",");
			float x = Float.valueOf(split[0]) * 3.125f;
			float y = Float.valueOf(split[1]) * 3.125f;
			if (Boolean.valueOf(split[2])) {
				GameObject.instantiate(new GameObject("Locker", true, new Locker(true),
						new Renderer(ta, ta.getSprite("lockred"), 100, 200)), new Vector2(x, y));
			} else {
				GameObject.instantiate(new GameObject("Locker", true, new Locker(false),
						new Renderer(ta, ta.getSprite("lock0"), 100, 200), new Animator(
						new Animation("unlock",
								new Sprite[]{ta.getSprite("lock1"), ta.getSprite("lock2"),
											 ta.getSprite("lock3"), ta.getSprite("lock4"),
											 ta.getSprite("lock5")}, AnimationType.ONE_SHOT, 125)),
						new Collider(100, 200, true)), new Vector2(x, y));
			}
		}
	}

	private static void readInCameras(BufferedReader reader, TextureAtlas ta) throws IOException {
		String temp;
		while ((temp = reader.readLine()) != null && !temp.equals("END CAMERAS")) {
			String[] split = temp.split(",");
			float x = Float.valueOf(split[0]) * 3.125f;
			float y = Float.valueOf(split[1]) * 3.125f;
			GameObject.instantiate(
					new GameObject(null, true, new Renderer(ta, ta.getSprite("camera0"), 100, 100),
							new Animator(new Animation("cycle",
									new Sprite[]{ta.getSprite("camera0"), ta.getSprite("camera1"),
												 ta.getSprite("camera2"), ta.getSprite("camera3"),
												 ta.getSprite("camera4"), ta.getSprite("camera5"),
												 ta.getSprite("camera6"), ta.getSprite("camera7"),
												 ta.getSprite("camera8")}, AnimationType.OSCILLATE,
									500)), new Camera()), new Vector2(x, y));
		}
	}
}

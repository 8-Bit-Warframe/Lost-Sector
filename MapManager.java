package com.ezardlabs.lostsector;

import com.ezardlabs.dethsquare.Collider;
import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.Renderer;
import com.ezardlabs.dethsquare.TextureAtlas;
import com.ezardlabs.dethsquare.Vector2;
import com.ezardlabs.dethsquare.util.Utils;

import java.io.BufferedReader;
import java.io.IOException;

public class MapManager {

	public static void loadMap(String name) {
		try {
			String temp;
			BufferedReader reader = Utils.getReader("maps/" + name + ".lsmap");
			while ((temp = reader.readLine()) != null) {
				if (temp.equals("BEGIN BLOCKS")) readInBlocks(reader);
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

//	private void readInDoors(BufferedReader reader) throws IOException {
//		String temp;
//		while ((temp = reader.readLine()) != null && !temp.equals("END DOORS")) {
//			String[] split = temp.split(",");
//			float x = Float.valueOf(split[0]) * 3.125f;
//			float y = Float.valueOf(split[1]) * 3.125f;
////			float w = Float.valueOf(split[2]) * 3.125f;
////			float h = Float.valueOf(split[3]) * 3.125f;
//			GameObject.instantiate(
//					new GameObject("Door", new Door(split[4], Boolean.valueOf(split[2])),
//							new Collider(100, 500, true,
//									new Vector2(Boolean.valueOf(split[5]) ? 100 : 0, 0)),
//							new Animator()), new Vector2(x, y));
//			GameObject.instantiate(
//					new GameObject("DoorCollider", true, new Collider(64 * 3.125f, 32 * 3.125f)),
//					new Vector2(x, y));
//			GameObject.instantiate(
//					new GameObject("DoorCollider", true, new Collider(64 * 3.125f, 32 * 3.125f)),
//					new Vector2(x, y + 128 * 3.125f));
//		}
//	}
//
//	private void readInLockers(BufferedReader reader) throws IOException {
//		String temp;
//		while ((temp = reader.readLine()) != null && !temp.equals("END LOCKERS")) {
//			String[] split = temp.split(",");
//			float x = Float.valueOf(split[0]) * 3.125f;
//			float y = Float.valueOf(split[1]) * 3.125f;
//			GameObject.instantiate(new GameObject("Locker", new Locker(Boolean.valueOf(split[2])),
//					new Collider(100, 200, true), new Animator()), new Vector2(x, y));
//		}
//	}
}

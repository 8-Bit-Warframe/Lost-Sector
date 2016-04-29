package com.ezardlabs.lostsector;

import com.ezardlabs.dethsquare.*;
import com.ezardlabs.dethsquare.TextureAtlas.Sprite;
import com.ezardlabs.dethsquare.tmx.*;
import com.ezardlabs.lostsector.objects.enemies.corpus.Crewman;
import com.ezardlabs.lostsector.objects.environment.Camera;
import com.ezardlabs.lostsector.objects.environment.Door;
import com.ezardlabs.lostsector.objects.environment.Locker;

import java.io.BufferedReader;
import java.io.IOException;

import java.util.ArrayList;

public class MapManager {
	private static int[][] solidityMap;
	public static String overrideMapName = null;

	public static void loadMap(String name) {
		if(overrideMapName != null) {
			name = overrideMapName;
		}
		loadTMX(name);
		NavMesh.init(solidityMap);
//		TextureAtlas eta = new TextureAtlas("images/environment/atlas.png", "images/environment/atlas.txt");
//		try {
//			String temp;
//			BufferedReader reader = Utils.getReader("maps/" + name + ".lsmap");
//			while ((temp = reader.readLine()) != null) {
//				if (temp.equals("BEGIN METADATA")) readInMetaData(reader);
//				if (temp.equals("BEGIN BLOCKS")) readInBlocks(reader);
//				if (temp.equals("BEGIN LOCKERS")) readInLockers(reader, eta);
//				if (temp.equals("BEGIN DOORS")) readInDoors(reader, eta);
//				if (temp.equals("BEGIN CAMERAS")) readInCameras(reader, eta);
//			}
//		} catch (IOException ignored) {
//			ignored.printStackTrace();
//		}
//
//		NavMesh.init(solidityMap);
	}

	private static void readInMetaData(BufferedReader reader) throws IOException {
		int width = 0;
		int height = 0;
		String temp;
		while ((temp = reader.readLine()) != null && !temp.equals("END METADATA")) {
			String[] split = temp.split("=");
			if (split[0].equals("map_width")) {
				width = Integer.parseInt(split[1]);
			} else if (split[0].equals("map_height")) {
				height = Integer.parseInt(split[1]);
			}
		}
		solidityMap = new int[width][height];
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
						new GameObject("Collider Tile", true, new Renderer(ta, ta.getSprite(split[4]), w, h).setFlipped(Boolean.valueOf(split[6]), Boolean.valueOf(split[7])), new Collider(w, h)),
						new Vector2(x, y));
			} else {
				GameObject
						.instantiate(new GameObject("Tile", true, new Renderer(ta, ta.getSprite(split[4]), w, h).setFlipped(Boolean.valueOf(split[6]), Boolean.valueOf(split[7]))), new Vector2(x, y));
			}
			solidityMap[((int) (x / 100))][((int) (y / 100))] = Boolean.valueOf(split[5]) ? 1 : 0;
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
				GameObject.instantiate(new GameObject("Door", new Door(), new Collider(w, h, true), new Renderer(ta, ta.getSprite("door0"), w, h).setFlipped(Boolean.valueOf(split[5]), false),
						new Animator(new Animation("open", new Sprite[]{ta.getSprite("door0"),
								ta.getSprite("door1"),
								ta.getSprite("door2"),
								ta.getSprite("door3"),
								ta.getSprite("door4"),
								ta.getSprite("door5"),
								ta.getSprite("door6")}, AnimationType.ONE_SHOT, 80), new Animation("close", new Sprite[]{ta.getSprite("door6"),
								ta.getSprite("door5"),
								ta.getSprite("door4"),
								ta.getSprite("door3"),
								ta.getSprite("door2"),
								ta.getSprite("door1"),
								ta.getSprite("door0")}, AnimationType.ONE_SHOT, 80))), new Vector2(x, y));
			} else if (split[4].equals("ldoor")) {
				GameObject.instantiate(new GameObject("Door", new Door(), new Collider(100, 500, true), new Renderer(ta, ta.getSprite("ldoor0"), w, h).setFlipped(Boolean.valueOf(split[5]), false),
						new Animator(new Animation("open", new Sprite[]{ta.getSprite("ldoor")}, AnimationType.ONE_SHOT, 80), new Animation("close", new Sprite[]{ta.getSprite("ldoor0"),
								ta.getSprite("ldoor1"),
								ta.getSprite("ldoor2"),
								ta.getSprite("ldoor3"),
								ta.getSprite("ldoor4"),
								ta.getSprite("ldoor5"),
								ta.getSprite("ldoor6"),
								ta.getSprite("ldoor7"),
								ta.getSprite("ldoor8"),
								ta.getSprite("ldoor9"),
								ta.getSprite("ldoor10")}, AnimationType.OSCILLATE, 80))), new Vector2(x, y));
			}
			GameObject.instantiate(new GameObject("DoorCollider", true, new Collider(64 * 3.125f, 32 * 3.125f)), new Vector2(x, y));
			GameObject.instantiate(new GameObject("DoorCollider", true, new Collider(64 * 3.125f, 32 * 3.125f)), new Vector2(x, y + 128 * 3.125f));
			solidityMap[((int) (x / 100))][((int) (y / 100))] = 1;
			solidityMap[((int) (x / 100)) + 1][((int) (y / 100))] = 1;
			solidityMap[((int) (x / 100))][((int) (y / 100)) + 4] = 1;
			solidityMap[((int) (x / 100)) + 1][((int) (y / 100)) + 4] = 1;
		}
	}

	private static void readInLockers(BufferedReader reader, TextureAtlas ta) throws IOException {
		String temp;
		while ((temp = reader.readLine()) != null && !temp.equals("END LOCKERS")) {
			String[] split = temp.split(",");
			float x = Float.valueOf(split[0]) * 3.125f;
			float y = Float.valueOf(split[1]) * 3.125f;
			if (Boolean.valueOf(split[2])) {
				GameObject.instantiate(new GameObject("Locker", true, new Locker(true), new Renderer(ta, ta.getSprite("lockred"), 100, 200)), new Vector2(x, y));
			} else {
				GameObject.instantiate(new GameObject("Locker", true, new Locker(false), new Renderer(ta, ta.getSprite("lock0"), 100, 200), new Animator(
						new Animation("unlock", new Sprite[]{ta.getSprite("lock1"), ta.getSprite("lock2"), ta.getSprite("lock3"), ta.getSprite("lock4"), ta.getSprite("lock5")}, AnimationType.ONE_SHOT,
								125)), new Collider(100, 200, true)), new Vector2(x, y));
			}
		}
	}

	private static void readInCameras(BufferedReader reader, TextureAtlas ta) throws IOException {
		String temp;
		while ((temp = reader.readLine()) != null && !temp.equals("END CAMERAS")) {
			String[] split = temp.split(",");
			float x = Float.valueOf(split[0]) * 3.125f;
			float y = Float.valueOf(split[1]) * 3.125f;
			GameObject.instantiate(new GameObject("Camera", true, new Renderer(ta, ta.getSprite("camera0"), 100, 100), new Animator(new Animation("cycle", new Sprite[]{ta.getSprite("camera0"),
					ta.getSprite("camera1"),
					ta.getSprite("camera2"),
					ta.getSprite("camera3"),
					ta.getSprite("camera4"),
					ta.getSprite("camera5"),
					ta.getSprite("camera6"),
					ta.getSprite("camera7"),
					ta.getSprite("camera8")}, AnimationType.OSCILLATE, 500)), new Camera()), new Vector2(x, y));
		}
	}

	// START Tiled TMX Pasring stuff

	private static void loadTMX(String filePath) {
		TMXLoader tmxLoader = new TMXLoader(filePath);
		Map map = tmxLoader.getMap();
		solidityMap = new int[map.getWidth()][map.getHeight()];
		ArrayList<TileSet> tileSets = map.getTileSets();
		ArrayList<TextureAtlas> textureAtlases = new ArrayList<>();
		for(TileSet tileSet : tileSets) {
			textureAtlases.add(new TextureAtlas("maps/" + tileSet.getImageSource(), tileSet.getTileWidth(), tileSet.getTileHeight()));
		}
		float w = map.getTileWidth() * 6.25f;
		float h = map.getTileHeight() * 6.25f;
		boolean isBackgroundLayer = true;
		for(Layer layer : map.getTileLayers()) {
			Tile[] tiles = layer.getTiles();
			if(layer.getName().equals("main")) {
				// Main layer with collision
				instantiateTiles(tiles, true, w, h, 5, map, tileSets, textureAtlases);
				isBackgroundLayer = false;
			} else if(isBackgroundLayer) {
				// Background layer with no collision
				instantiateTiles(tiles, false, w, h, -10, map, tileSets, textureAtlases);
			} else if(!isBackgroundLayer) {
				// Foreground layer with no collision
				instantiateTiles(tiles, false, w, h, 10, map, tileSets, textureAtlases);
			}
		}

		TextureAtlas ta = new TextureAtlas("images/environment/atlas.png", "images/environment/atlas.txt");
		ObjectGroup enemyObjectGroup = null;
		for(ObjectGroup objectGroup : map.getObjectGroups()) {
			if(objectGroup.getName().equals("enemies")) {
				enemyObjectGroup = objectGroup;
			} else {
				instantiateObjects(objectGroup.getObjects(), ta);
			}
		}
		if(enemyObjectGroup != null) {
			instantiateEnemies(enemyObjectGroup.getObjects());
		}
	}

	public static void instantiateTiles(Tile[] tiles, boolean collision, float tileWidth, float tileHeight, int zindex, Map map, ArrayList<TileSet> tileSets, ArrayList<TextureAtlas> textureAtlases) {
		for (int i = 0; i < tiles.length; i++) {
			Tile t = tiles[i];
			long gid = t.getGid();
			if(gid <= 0) {
				continue;
			}
			int col = i % map.getWidth();
			int row = i / map.getHeight();
			float x = col * tileWidth;
			float y = row * tileHeight;

			int tileSetIdx = -1;
			TileSet ts = null;
			for(int j = tileSets.size() - 1; j >= 0; j--) {
				if(gid >= tileSets.get(j).getFirstGid()) {
					tileSetIdx = j;
					ts = tileSets.get(tileSetIdx);
					break;
				}
			}
			if(ts == null) {
				continue;
			}
			TextureAtlas ta = textureAtlases.get(tileSetIdx);
			Sprite sprite = ta.getSprite(String.valueOf(gid - ts.getFirstGid()));
			Renderer renderer = new Renderer(ta, sprite, tileWidth, tileHeight).setzIndex(zindex).setFlipped(t.isFlippedHorizontal(), t.isFlippedVertical());
			if(collision) {
				GameObject.instantiate(
						new GameObject("Tile", true, renderer, new Collider(tileWidth, tileHeight)),
						new Vector2(x, y)
				);
				solidityMap[((int) (x / 100))][((int) (y / 100))] = 1;
			} else {
				GameObject.instantiate(
						new GameObject("Tile", true, renderer),
						new Vector2(x, y)
				);
			}
		}
	}

	public static void instantiateObjects(TMXObject[] objects, TextureAtlas ta) {
		for(TMXObject object : objects) {
			Vector2 pos = new Vector2(object.getX() * 6.25f, object.getY() * 6.25f);
			float w = object.getWidth() * 6.25f;
			float h = object.getHeight() * 6.25f;
			switch(object.getType()) {
				case "locker":
					if(0 + (int)(Math.random() * ((1 - 0) + 1)) == 0)
						GameObject.instantiate(new GameObject("Locker", true, new Locker(true), new Renderer(ta, ta.getSprite("lockred"), 100, 200)), pos);
					else
						GameObject.instantiate(new GameObject("Locker", true, new Locker(false), new Renderer(ta, ta.getSprite("lock0"), 100, 200), new Animator(
							new Animation("unlock", new Sprite[]{ta.getSprite("lock1"), ta.getSprite("lock2"), ta.getSprite("lock3"), ta.getSprite("lock4"), ta.getSprite("lock5")}, AnimationType.ONE_SHOT,
									125)), new Collider(100, 200, true)), pos);
					break;
				case "locker_locked":
					GameObject.instantiate(new GameObject("Locker", true, new Locker(true), new Renderer(ta, ta.getSprite("lockred"), 100, 200)), pos);
					break;
				case "locker_unlocked":
					GameObject.instantiate(new GameObject("Locker", true, new Locker(false), new Renderer(ta, ta.getSprite("lock0"), 100, 200), new Animator(
							new Animation("unlock", new Sprite[]{ta.getSprite("lock1"), ta.getSprite("lock2"), ta.getSprite("lock3"), ta.getSprite("lock4"), ta.getSprite("lock5")}, AnimationType.ONE_SHOT,
									125)), new Collider(100, 200, true)), pos);
					break;
				case "door":
					GameObject.instantiate(new GameObject("Door", new Door(), new Collider(w * 0.5f, h, true), new Renderer(ta, ta.getSprite("door0"), w, h),
							new Animator(new Animation("open", new Sprite[]{ta.getSprite("door0"),
									ta.getSprite("door1"),
									ta.getSprite("door2"),
									ta.getSprite("door3"),
									ta.getSprite("door4"),
									ta.getSprite("door5"),
									ta.getSprite("door6")}, AnimationType.ONE_SHOT, 80), new Animation("close", new Sprite[]{ta.getSprite("door6"),
									ta.getSprite("door5"),
									ta.getSprite("door4"),
									ta.getSprite("door3"),
									ta.getSprite("door2"),
									ta.getSprite("door1"),
									ta.getSprite("door0")}, AnimationType.ONE_SHOT, 80))), pos);
					break;
				case "ldoor":
					GameObject.instantiate(new GameObject("Door", new Door(), new Collider(w * 0.5f, h, true), new Renderer(ta, ta.getSprite("ldoor0"), w, h),
							new Animator(new Animation("open", new Sprite[]{ta.getSprite("ldoor")}, AnimationType.ONE_SHOT, 80), new Animation("close", new Sprite[]{ta.getSprite("ldoor0"),
									ta.getSprite("ldoor1"),
									ta.getSprite("ldoor2"),
									ta.getSprite("ldoor3"),
									ta.getSprite("ldoor4"),
									ta.getSprite("ldoor5"),
									ta.getSprite("ldoor6"),
									ta.getSprite("ldoor7"),
									ta.getSprite("ldoor8"),
									ta.getSprite("ldoor9"),
									ta.getSprite("ldoor10")}, AnimationType.OSCILLATE, 80))), pos);
					break;
			}

			// For doors
			if(object.getType().contains("door")) {
//				GameObject.instantiate(new GameObject("DoorCollider", true, new Collider(64 * 3.125f, 32 * 3.125f)), new Vector2(x, y));
//				GameObject.instantiate(new GameObject("DoorCollider", true, new Collider(64 * 3.125f, 32 * 3.125f)), new Vector2(x, y + 128 * 3.125f));
				GameObject.instantiate(new GameObject("DoorCollider", true, new Collider(w, h * 0.2f)), pos);
				GameObject.instantiate(new GameObject("DoorCollider", true, new Collider(w, h * 0.2f)), new Vector2(pos.x, pos.y + h * 0.8f));
				solidityMap[((int) (pos.x / 100))][((int) (pos.y / 100))] = 1;
				solidityMap[((int) (pos.x / 100)) + 1][((int) (pos.y / 100))] = 1;
				solidityMap[((int) (pos.x / 100))][((int) (pos.y / 100)) + 4] = 1;
				solidityMap[((int) (pos.x / 100)) + 1][((int) (pos.y / 100)) + 4] = 1;
			}
		}
	}

	public static void instantiateEnemies(TMXObject[] objects) {
		for(TMXObject object : objects) {
			Vector2 pos = new Vector2(object.getX() * 6.25f, object.getY() * 6.25f);
			float w = object.getWidth() * 6.25f;
			float h = object.getHeight() * 6.25f;
			switch(object.getType()) {
				case "crewman":
					GameObject.instantiate(
							new GameObject("Crewman", new Renderer(), new Animator(), new Collider(w, h), new Rigidbody(), new Crewman("dera")),
							pos);
					break;
			}
		}
	}
}

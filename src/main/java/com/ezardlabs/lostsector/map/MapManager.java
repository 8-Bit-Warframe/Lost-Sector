package com.ezardlabs.lostsector.map;

import com.ezardlabs.dethsquare.Animation;
import com.ezardlabs.dethsquare.AnimationType;
import com.ezardlabs.dethsquare.Animator;
import com.ezardlabs.dethsquare.Collider;
import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.Renderer;
import com.ezardlabs.dethsquare.Rigidbody;
import com.ezardlabs.dethsquare.TextureAtlas;
import com.ezardlabs.dethsquare.TextureAtlas.Sprite;
import com.ezardlabs.dethsquare.Vector2;
import com.ezardlabs.dethsquare.tmx.*;
import com.ezardlabs.lostsector.NavMesh;
import com.ezardlabs.lostsector.map.procedural.MapConfig;
import com.ezardlabs.lostsector.map.procedural.MapSegment;
import com.ezardlabs.lostsector.map.procedural.MapSegmentConnector;
import com.ezardlabs.lostsector.objects.enemies.corpus.crewmen.DeraCrewman;
import com.ezardlabs.lostsector.objects.enemies.corpus.crewmen.ProvaCrewman;
import com.ezardlabs.lostsector.objects.enemies.corpus.crewmen.SupraCrewman;
import com.ezardlabs.lostsector.objects.environment.Camera;
import com.ezardlabs.lostsector.objects.environment.Door;
import com.ezardlabs.lostsector.objects.environment.Locker;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class MapManager {
	private static int[][] solidityMap;
	public static Vector2 playerSpawn = new Vector2();
	public static ArrayList<Vector2> enemySpawns = null;
	public static String overrideMapName = null;

	public static void loadMap(String name) {
		if(overrideMapName != null) {
			name = overrideMapName;
		}
		enemySpawns = new ArrayList<>();
		TMXLoader tmxLoader = new TMXLoader("maps/" + name + ".tmx");
		loadTMX(tmxLoader.getMap());
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
				GameObject
						.instantiate(new GameObject("Door", new Door(), new Collider(100, 500, true), new Renderer(ta, ta.getSprite("ldoor0"), w, h).setFlipped(Boolean.valueOf(split[5]), false),
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
				GameObject
						.instantiate(new GameObject("Locker", true, new Locker(true), new Renderer(ta, ta.getSprite("lockred"), 100, 200)), new Vector2(x, y));
			} else {
				GameObject
						.instantiate(new GameObject("Locker", true, new Locker(false), new Renderer(ta, ta.getSprite("lock0"), 100, 200), new Animator(
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

	// START Tiled TMX Parsing stuff

	public static void loadProceduralMap(MapConfig mapCfg) {
		// Keep a list of added sections
		ArrayList<MapSegment> renderedSegments = new ArrayList<>();

		solidityMap = new int[16 * mapCfg.numRooms + 2][16];

		MapSegment currSeg = null;
		MapSegment nextSeg = mapCfg.defaultStartSeg;
		Vector2 nextOffset = new Vector2(0.0f, 512.0f);
		// Load rooms in between
		for(int i = 0; i < mapCfg.numRooms; i++) {
			if(i == 0) {
				// First segment
				Object[] objStartSeg = mapCfg.startSegments.values().toArray();
				ArrayList<MapSegment> startSegs = (ArrayList<MapSegment>) getRandObj(objStartSeg);
				nextSeg = getRandObj(startSegs);
			} else if(i == mapCfg.numRooms - 1) {
				// Last segment
				Object[] objStartSeg = mapCfg.endSegments.values().toArray();
				ArrayList<MapSegment> startSegs = (ArrayList<MapSegment>) getRandObj(objStartSeg);
				nextSeg = getRandObj(startSegs);
				continue;	// Do nothing for now.
			} else {
				// Segments in between first and last.
				MapSegmentConnector currConn = getRandomValidConnector(currSeg.connectors);
				if (currConn == null) {
					System.err.println("Null connector found on segment #" + i);
					continue;
				}
				nextSeg = getRandomValidSegment(mapCfg.mainSegments, currConn);

				ArrayList<MapSegmentConnector> nextValidConns = new ArrayList<>();
				for (MapSegmentConnector c : nextSeg.connectors) {
					if (currConn.isValidConnection(c)) {
						nextValidConns.add(c);
					}
				}
				MapSegmentConnector nextConn = getRandObj(nextValidConns);

				currConn.connect(nextConn);

				switch (currConn.side) {
					case CENTRE:
						break;
					case TOP:
						nextOffset.y -= nextSeg.getHeight();
						break;
					case RIGHT:
						nextOffset.x += currSeg.getWidth();
						break;
					case BOTTOM:
						nextOffset.y += currSeg.getHeight();
						break;
					case LEFT:
						nextOffset.x -= nextSeg.getWidth();
						break;
				}
				System.out.println("Next Segment " + i
						+ "\n conn: " + nextConn.toString()
						+ "\n offset: " + nextOffset.toString()
				);
			}
			nextSeg.pos = new Vector2(nextOffset.x, nextOffset.y);
			loadMapSegment(nextSeg);
			renderedSegments.add(nextSeg);
			currSeg = nextSeg;
		}

		// Load end room

		System.out.println(mapCfg.toString());
	}

	private static MapSegmentConnector getRandomValidConnector(ArrayList<MapSegmentConnector> conns) {
		ArrayList<MapSegmentConnector> validConns = new ArrayList<>();
		for(MapSegmentConnector c : conns) {
			if(c.side == MapSegmentConnector.MapSegmentConnectorSide.LEFT || c.isConnected()) {
				continue;
			}
			validConns.add(c);
		}
		return  getRandObj(validConns);
	}

	private static MapSegment getRandomValidSegment(HashMap<String, ArrayList<MapSegment>> mapSegments, MapSegmentConnector conn) {
		ArrayList<MapSegment> arrSegments = mapSegments.get(conn.getMatchingSideType());
		ArrayList<MapSegment> validSegments = new ArrayList<>();
		for(MapSegment seg : arrSegments) {
			boolean hasLeftConn = false;
			for(MapSegmentConnector c : seg.connectors) {
				if(c.side == MapSegmentConnector.MapSegmentConnectorSide.LEFT) {
					hasLeftConn = true;
					break;
				}
			}
			if(conn.side != MapSegmentConnector.MapSegmentConnectorSide.RIGHT && seg.connectors.size() <= 2 && hasLeftConn) {
				continue;
			}
			validSegments.add(new MapSegment(seg.map));
		}
		return getRandObj(validSegments);
	}

	private static <T> T getRandObj(T[] arr) {
		int bound = arr.length;
		if(bound <= 0) {
			return null;
		}
		return arr[ThreadLocalRandom.current().nextInt(0, bound)];
	}

	private static <T> T getRandObj(ArrayList<T> arr) {
		int bound = arr.size();
		if(bound <= 0) {
			return null;
		}
		return arr.get(ThreadLocalRandom.current().nextInt(0, bound));
	}

	private static void loadMapSegment(MapSegment seg) {
		loadTMX(seg.map, seg.pos);
	}

	private static void loadTMX(Map map) {
		solidityMap = new int[map.getWidth()][map.getHeight()];
		loadTMX(map, new Vector2());
	}

	private static void loadTMX(Map map, Vector2 offset) {
		if(map == null) {
			System.err.println("MapManager.loadTMX: Map provided is null");
			return;
		}
		ArrayList<TileSet> tileSets = map.getTileSets();
		ArrayList<TextureAtlas> textureAtlases = new ArrayList<>();
		for(TileSet tileSet : tileSets) {
			String filePath = tileSet.getFilePath();
			int lastSlash = filePath.lastIndexOf("/");
			String folder = "";
			if(lastSlash >= 0) {
				folder = filePath.substring(0, lastSlash);
			}

			textureAtlases.add(new TextureAtlas(folder + "/" + tileSet.getImageSource(), tileSet.getTileWidth(), tileSet.getTileHeight()));
		}
		float w = map.getTileWidth() * 6.25f;
		float h = map.getTileHeight() * 6.25f;
		boolean isBackgroundLayer = true;
		for(Layer layer : map.getTileLayers()) {
			Tile[] tiles = layer.getTiles();
			if(layer.getName().equals("main")) {
				// Main layer with collision
				instantiateTiles(tiles, true, w, h, 5, map, tileSets, textureAtlases, offset);
				isBackgroundLayer = false;
			} else if(isBackgroundLayer) {
				// Background layer with no collision
				instantiateTiles(tiles, false, w, h, -10, map, tileSets, textureAtlases, offset);
			} else if(!isBackgroundLayer) {
				// Foreground layer with no collision
				instantiateTiles(tiles, false, w, h, 10, map, tileSets, textureAtlases, offset);
			}
		}

		TextureAtlas ta = new TextureAtlas("images/environment/atlas.png", "images/environment/atlas.txt");
		ObjectGroup enemyObjectGroup = null;
		for(ObjectGroup objectGroup : map.getObjectGroups()) {
			if(objectGroup.getName().equals("enemies")) {
				enemyObjectGroup = objectGroup;
			} else {
				instantiateObjects(objectGroup.getObjects(), ta, offset);
			}
		}
		if(enemyObjectGroup != null) {
			instantiateEnemies(enemyObjectGroup.getObjects(), offset);
		}
	}

	public static void instantiateTiles(Tile[] tiles, boolean collision, float tileWidth, float tileHeight, int zindex, Map map, ArrayList<TileSet> tileSets, ArrayList<TextureAtlas> textureAtlases, Vector2 offset) {
		for (int i = 0; i < tiles.length; i++) {
			Tile t = tiles[i];
			long gid = t.getGid();
			if(gid <= 0) {
				continue;
			}
			int col = i % map.getWidth();
			int row = i / map.getWidth();
			Vector2 pos = new Vector2();
			float x = (col + offset.x) * tileWidth;
			float y = (row + offset.y) * tileHeight;

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
				try {
					solidityMap[col][row] = 1;
				} catch(ArrayIndexOutOfBoundsException ex) {
					System.out.println("Error setting solidityMap at index [" + col + "][" + row + "]");
				}
			} else {
				GameObject.instantiate(
						new GameObject("Tile", true, renderer),
						new Vector2(x, y)
				);
			}
		}
	}

	public static void instantiateObjects(TMXObject[] objects, TextureAtlas ta, Vector2 offset) {
		for(TMXObject object : objects) {
			Vector2 pos = new Vector2((object.getX() + offset.x * 16.0f) * 6.25f, (object.getY() + offset.y * 16.0f) * 6.25f);
			float w = object.getWidth() * 6.25f;
			float h = object.getHeight() * 6.25f;
			boolean flipH = Boolean.parseBoolean(object.getProperty("flipH", "false"));
			boolean flipV = Boolean.parseBoolean(object.getProperty("flipV", "false"));
			switch(object.getType()) {
				case "player_spawn":
					playerSpawn = new Vector2(pos.x, pos.y);
					break;
				case "locker":
					if(0 + (int)(Math.random() * ((1 - 0) + 1)) == 0)
						GameObject
								.instantiate(new GameObject("Locker", true, new Locker(true), new Renderer(ta, ta.getSprite("lockred"), 100, 200).setFlipped(flipH, flipV)), pos);
					else
						GameObject
								.instantiate(new GameObject("Locker", true, new Locker(false), new Renderer(ta, ta.getSprite("lock0"), 100, 200).setFlipped(flipH, flipV), new Animator(
							new Animation("unlock", new Sprite[]{ta.getSprite("lock1"), ta.getSprite("lock2"), ta.getSprite("lock3"), ta.getSprite("lock4"), ta.getSprite("lock5")}, AnimationType.ONE_SHOT,
									125)), new Collider(100, 200, true)), pos);
					break;
				case "locker_locked":
					GameObject
							.instantiate(new GameObject("Locker", true, new Locker(true), new Renderer(ta, ta.getSprite("lockred"), 100, 200).setFlipped(flipH, flipV)), pos);
					break;
				case "locker_unlocked":
					GameObject
							.instantiate(new GameObject("Locker", true, new Locker(false), new Renderer(ta, ta.getSprite("lock0"), 100, 200).setFlipped(flipH, flipV), new Animator(
							new Animation("unlock", new Sprite[]{ta.getSprite("lock1"), ta.getSprite("lock2"), ta.getSprite("lock3"), ta.getSprite("lock4"), ta.getSprite("lock5")}, AnimationType.ONE_SHOT,
									125)), new Collider(100, 200, true)), pos);
					break;
				case "door":
					if(flipH) {
					}
					GameObject
							.instantiate(new GameObject("Door", new Door(), new Collider(w * 0.5f, h, true), new Renderer(ta, ta.getSprite("door0"), w, h).setFlipped(flipH, flipV),
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
					if(flipH) {
					}
					GameObject
							.instantiate(new GameObject("Door", new Door(), new Collider(w * 0.5f, h, true), new Renderer(ta, ta.getSprite("ldoor0"), w, h).setFlipped(flipH, flipV),
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

	public static void instantiateEnemies(TMXObject[] objects, Vector2 offset) {
		for(TMXObject object : objects) {
			Vector2 pos = new Vector2((object.getX() + offset.x) * 6.25f, (object.getY() + offset.y) * 6.25f);
			float w = object.getWidth() * 6.25f;
			float h = object.getHeight() * 6.25f;
			switch(object.getType()) {
				case "spawn":
					enemySpawns.add(pos);
					break;
				case "prova_crewman":
					GameObject.instantiate(
							new GameObject("Prova Crewman", new Renderer(), new Animator(), new Collider(w, h), new Rigidbody(), new ProvaCrewman()),
							pos);
					break;
				case "dera_crewman":
					GameObject.instantiate(
							new GameObject("Dera Crewman", new Renderer(), new Animator(), new Collider(w, h), new Rigidbody(), new DeraCrewman()),
							pos);
					break;
				case "supra_crewman":
					GameObject.instantiate(
							new GameObject("Supra Crewman", new Renderer(), new Animator(), new Collider(w, h), new Rigidbody(), new SupraCrewman()),
							pos);
					break;
			}
		}
	}
}

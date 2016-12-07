package com.ezardlabs.lostsector.map;

import com.ezardlabs.dethsquare.Animation;
import com.ezardlabs.dethsquare.AnimationType;
import com.ezardlabs.dethsquare.Animator;
import com.ezardlabs.dethsquare.Collider;
import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.Renderer;
import com.ezardlabs.dethsquare.TextureAtlas;
import com.ezardlabs.dethsquare.TextureAtlas.Sprite;
import com.ezardlabs.dethsquare.Vector2;
import com.ezardlabs.dethsquare.multiplayer.Network;
import com.ezardlabs.dethsquare.prefabs.PrefabManager;
import com.ezardlabs.dethsquare.tmx.Layer;
import com.ezardlabs.dethsquare.tmx.Map;
import com.ezardlabs.dethsquare.tmx.ObjectGroup;
import com.ezardlabs.dethsquare.tmx.TMXLoader;
import com.ezardlabs.dethsquare.tmx.TMXObject;
import com.ezardlabs.dethsquare.tmx.Tile;
import com.ezardlabs.dethsquare.tmx.TileSet;
import com.ezardlabs.lostsector.NavMesh;
import com.ezardlabs.lostsector.map.procedural.MapConfig;
import com.ezardlabs.lostsector.map.procedural.MapSegment;
import com.ezardlabs.lostsector.map.procedural.MapSegmentConnector;
import com.ezardlabs.lostsector.objects.environment.Camera;
import com.ezardlabs.lostsector.objects.environment.Door;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class MapManager {
	public static final float MAP_SCALE = 6.25f;
	public static final int TILE_SIZE = 16;
	public static final int MAP_SEGMENT_SIZE = 16;	// Number of tiles that make up a map segment unit. ie. all map segments must be a multiple of this number.
	public static final Vector2 PROC_START_POS = new Vector2(0.0f, 256.0f);

	private static int[][] solidityMap;
	public static Vector2 playerSpawn = new Vector2();
	public static ArrayList<Vector2> enemySpawns = null;
	public static String overrideMapName = null;

	public static long proceduralSeed = -1;
	public static Random rand = null;

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
				GameObject.instantiate(new GameObject("Door", new Door(ta), new Collider(w, h,
						true), new Renderer(ta, ta.getSprite("door0"), w, h).setFlipped(Boolean.valueOf(split[5]), false),
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
						.instantiate(new GameObject("Door", new Door(ta), new Collider(100, 500,
								true), new Renderer(ta, ta.getSprite("ldoor0"), w, h).setFlipped(Boolean.valueOf(split[5]), false),
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
//				GameObject
//						.instantiate(new GameObject("Locker", true, new Locker(true), new Renderer(ta, ta.getSprite("lockred"), 100, 200)), new Vector2(x, y));
			} else {
//				GameObject
//						.instantiate(new GameObject("Locker", true, new Locker(false), new Renderer(ta, ta.getSprite("lock0"), 100, 200), new Animator(
//						new Animation("unlock", new Sprite[]{ta.getSprite("lock1"), ta.getSprite("lock2"), ta.getSprite("lock3"), ta.getSprite("lock4"), ta.getSprite("lock5")}, AnimationType.ONE_SHOT,
//								125)), new Collider(100, 200, true)), new Vector2(x, y));
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

	// START Procedural mapping stuff

	public static void loadProceduralMap(MapConfig mapCfg) {
		// Get random seed from mapCfg.  If not a valid seed, create a new one from system time.
		proceduralSeed = mapCfg.proceduralSeed;
		if(proceduralSeed < 0) {
			proceduralSeed = System.currentTimeMillis();
//			proceduralSeed = 1479363189995L;	// Testing
		}
		rand = new Random(proceduralSeed);
		System.out.println("Procedural Seed: " + proceduralSeed);

		// Keep a list of added sections
		ArrayList<MapSegment> renderSegments = new ArrayList<>();

		MapSegment currSeg = null;
		Vector2 nextOffset = new Vector2(PROC_START_POS.x, PROC_START_POS.y);

		int solidityW = (mapCfg.numRooms * MAP_SEGMENT_SIZE) + ((int)PROC_START_POS.x / TILE_SIZE * MAP_SEGMENT_SIZE);
		int solidityH = (mapCfg.numRooms * MAP_SEGMENT_SIZE) + ((int)PROC_START_POS.y / TILE_SIZE * MAP_SEGMENT_SIZE);
		solidityMap = new int[solidityW][solidityH];

		final int mapSegGridW = (int)(mapCfg.numRooms + (nextOffset.x / MAP_SEGMENT_SIZE));
		final int mapSegGridH = (int)(mapCfg.numRooms + (nextOffset.y / MAP_SEGMENT_SIZE));
		MapSegment[][] mapSegGrid = new MapSegment[mapSegGridW][mapSegGridH];

		// Load rooms in between
		for(int i = 0; i < mapCfg.numRooms; i++) {
			MapSegment nextSeg;
			if(i == 0) {
				// First segment
				Object[] objStartSeg = mapCfg.spawnSegments.values().toArray();
				ArrayList<MapSegment> spawnSegs = (ArrayList<MapSegment>) getRandObj(objStartSeg);
				nextSeg = getRandObj(spawnSegs);
				System.out.print("Next Segment " + i + ": " + nextSeg.map.getFilePath()
						+ "\n offset: " + nextOffset.toString()
				);
			} else {
				MapSegmentConnector currConn = getRandomValidConnector(mapCfg, currSeg, mapSegGrid, true);
				if (currConn == null) {
					System.err.println("Null connector found on segment #" + i);
					continue;
				}
				System.out.println("    Exit From: " + currConn.toString());
				if(i == mapCfg.numRooms - 1) {
					// Extraction segment
					nextSeg = getRandomValidSegment(mapCfg, mapCfg.extractSegments,currSeg, currConn, mapSegGrid, true);
				} else {
					// Main segments.
					nextSeg = getRandomValidSegment(mapCfg, mapCfg.mainSegments, currSeg, currConn, mapSegGrid, true);
				}

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
						nextOffset.x += (currConn.pos.x / currSeg.map.getTileWidth()) - (nextConn.pos.x / nextSeg.map.getTileWidth());
						nextOffset.y -= nextSeg.getHeight();
						break;
					case RIGHT:
						nextOffset.x += currSeg.getWidth();
						nextOffset.y += (currConn.pos.y / currSeg.map.getTileHeight()) - (nextConn.pos.y / nextSeg.map.getTileHeight());
						break;
					case BOTTOM:
						nextOffset.x += (currConn.pos.x / currSeg.map.getTileWidth()) - (nextConn.pos.x / nextSeg.map.getTileWidth());
						nextOffset.y += currSeg.getHeight();
						break;
					case LEFT:
						nextOffset.x -= nextSeg.getWidth();
						nextOffset.y += (currConn.pos.y / currSeg.map.getTileHeight()) - (nextConn.pos.y / nextSeg.map.getTileHeight());
						break;
				}
				System.out.println("Next Segment " + i + ": " + nextSeg.map.getFilePath()
						+ "\n    Enter From: " + nextConn.toString() + " offset: " + nextOffset.toString()
				);
			}
			nextSeg.pos = new Vector2(nextOffset.x, nextOffset.y);
			renderSegments.add(nextSeg);
			addToMapSegGrid(mapSegGrid, nextSeg);
			currSeg = nextSeg;
		}

		System.out.println("Main Path Grid:");
		printMapSegmentGrid(mapSegGrid, mapSegGridW, mapSegGridH);

		// Find "free" connectors and fill them up.
		ArrayList<MapSegment> lockSegmentsToRender = new ArrayList<>();
		for(MapSegment seg : renderSegments) {
			for(MapSegmentConnector conn : seg.connectors) {
				if(conn.isConnected()) {
					continue;
				}
				ArrayList<MapSegment> lockSegments = mapCfg.lockSegments.get(conn.toString());
				if(lockSegments == null) {
					continue;
				}
				// Get valid lock segment and instantiate copy
				MapSegment lockSeg = new MapSegment(getRandObj(lockSegments).map);
				lockSeg.pos = seg.pos;
				if(lockSeg.connectors.size() != 1)
				{
					System.err.println("Lock segment '" + lockSeg.map.getFilePath() + "' has more than one connector!");
				}
				conn.connect(lockSeg.connectors.get(0));
				lockSegmentsToRender.add(lockSeg);
			}
		}
		for(MapSegment seg : lockSegmentsToRender) {
			renderSegments.add(seg);
		}

		for(MapSegment seg : renderSegments) {
			loadMapSegment(seg);
		}

		NavMesh.init(solidityMap);

//		printSolidityMapArray();

		System.out.println(mapCfg.toString());
	}

	private static MapSegmentConnector getRandomValidConnector(MapConfig mapCfg, MapSegment mapSeg, MapSegment[][] mapSegmentGrid, boolean forMainPath) {
		ArrayList<MapSegmentConnector> validConns = new ArrayList<>();
		for(MapSegmentConnector c : mapSeg.connectors) {
			if(c.isConnected()) {
				continue;
			}
			switch(c.side) {
				case CENTRE:
					break;
				case TOP:
//					if(mapSeg.pos.y <= mapCfg.minMapHeight) {
//						continue;
//					}
					break;
				case RIGHT:
					break;
				case BOTTOM:
//					if(mapSeg.pos.y + (mapSeg.getHeight() * mapSeg.map.getTileHeight()) >= mapCfg.maxMapHeight) {
//						continue;
//					}
					break;
				case LEFT:
					if (forMainPath) {
						continue;
					}
					break;
			}
			validConns.add(c);
		}
		return  getRandObj(validConns);
	}

	private static MapSegment getRandomValidSegment(MapConfig mapCfg, HashMap<String, ArrayList<MapSegment>> mapSegments, MapSegment mapSeg, MapSegmentConnector conn, MapSegment[][] mapSegmentGrid, boolean forMainPath) {
		ArrayList<MapSegment> arrSegments = mapSegments.get(conn.getMatchingSideType());
		ArrayList<MapSegment> validSegments = new ArrayList<>();
		for(MapSegment seg : arrSegments) {
			if(forMainPath) {
				boolean hasLeftConn = false;
				for (MapSegmentConnector c : seg.connectors) {
					if (c.side == MapSegmentConnector.MapSegmentConnectorSide.LEFT) {
						hasLeftConn = true;
						break;
					}
				}
				if (conn.side != MapSegmentConnector.MapSegmentConnectorSide.RIGHT && seg.connectors.size() <= 2 && hasLeftConn) {
					continue;
				}
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
		return arr[rand.nextInt(bound)];
	}

	private static <T> T getRandObj(ArrayList<T> arr) {
		int bound = arr.size();
		if(bound <= 0) {
			return null;
		}
		return arr.get(rand.nextInt(bound));
	}

	private static void loadMapSegment(MapSegment seg) {
		loadTMX(seg.map, seg.pos);
	}

	private static MapSegment getMapSegment(MapSegment[][] mapSegGrid, Vector2 pos) {
		MapSegment retMapSeg = null;
		int gridX = (int)(pos.x / TILE_SIZE);
		int gridY = (int)(pos.y / TILE_SIZE);
		try {
			retMapSeg = mapSegGrid[gridX][gridY];
		} catch(ArrayIndexOutOfBoundsException ex) {
			ex.printStackTrace();
		}
		return retMapSeg;
	}

	private static void addToMapSegGrid(MapSegment[][] mapSegGrid, MapSegment mapSeg) {
		int startGridX = (int)(mapSeg.pos.x / TILE_SIZE);
		int startGridY = (int)(mapSeg.pos.y / TILE_SIZE);
		int gridW = mapSeg.map.getWidth() / MAP_SEGMENT_SIZE;
		int gridY = mapSeg.map.getHeight() / MAP_SEGMENT_SIZE;
		for(int x = 0; x < gridW; x++) {
			for(int y = 0; y < gridY; y++) {
				mapSegGrid[startGridX + x][startGridY + y] = mapSeg;
			}
		}
	}

	private static void printMapSegmentGrid(MapSegment[][] mapSegGrid, int mapSegGridW, int mapSegGridH) {
		int spaces = 3;
		for(int x = 0; x < mapSegGridW + 1; x++) {
			String col = x + "";
			System.out.print("|" + col);
			for(int i = col.length(); i < spaces; i++) {
				System.out.print(" ");
			}
		}
		System.out.println();

		for(int y = 0; y < mapSegGridH; y++) {
			String row = (y + 1) + "";
			System.out.print("|" + row);
			for(int j = row.length(); j < spaces; j++) {
				System.out.print(" ");
			}
			for(int x = 0; x < mapSegGridW; x++) {
				String str = "";
				if(mapSegGrid[x][y] != null) {
					str += " *";
				}
				System.out.print("|" + str);
				for(int i = str.length(); i < spaces; i++) {
					System.out.print(" ");
				}
			}
			System.out.println();
		}
	}

	private static void printSolidityMap() {
		System.out.print("{");
		for(int i = 0; i < solidityMap.length; i++) {
			System.out.print("{");
			for(int j = 0; j < solidityMap[i].length; j++) {
				System.out.print(solidityMap[i][j]);
				if(j < solidityMap[i].length - 1) {
					System.out.print(",");
				}
			}
			System.out.print("}");
			if(i < solidityMap.length - 1) {
				System.out.print(",");
			}
		}
		System.out.print("}");
	}

	private static void printSolidityMapArray() {
		System.out.println();
		for(int i = 0; i < solidityMap.length; i++) {
			for(int j = 0; j < solidityMap[i].length; j++) {
				System.out.print(solidityMap[i][j]);
			}
			System.out.println();
		}
	}

	// START Tiled TMX Parsing stuff

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
			if(filePath.length() <= 0) {
				filePath = map.getFilePath();
			}
			int lastSlash = filePath.lastIndexOf("/");
			String folder = "";
			if(lastSlash >= 0) {
				folder = filePath.substring(0, lastSlash);
			}
			textureAtlases.add(new TextureAtlas(folder + "/"+ tileSet.getImageSource(), tileSet.getTileWidth(), tileSet.getTileHeight()));
		}
		float w = map.getTileWidth() * MAP_SCALE;
		float h = map.getTileHeight() * MAP_SCALE;
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
			} else if(!objectGroup.getName().equals("connectors")) {
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
					solidityMap[(int)(col + offset.x)][(int)(row + offset.y)] = 1;
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
			Vector2 pos = new Vector2((object.getX() + offset.x * TILE_SIZE) * MAP_SCALE, (object.getY() + offset.y * TILE_SIZE) * MAP_SCALE);
			float w = object.getWidth() * MAP_SCALE;
			float h = object.getHeight() * MAP_SCALE;
			boolean flipH = Boolean.parseBoolean(object.getProperty("flipH", "false"));
			boolean flipV = Boolean.parseBoolean(object.getProperty("flipV", "false"));
			switch(object.getType()) {
				case "player_spawn":
					playerSpawn = new Vector2(pos.x, pos.y);
					break;
				case "locker":
					if (Network.isHost()) {
						if ((int) (Math.random() * 2) == 0) {
							Network.instantiate("locker_locked", pos);
						} else {
							Network.instantiate("locker_unlocked", pos);
						}
					}
					break;
				case "locker_locked":
					GameObject.instantiate(PrefabManager.loadPrefab("locker_locked"), pos);
					break;
				case "locker_unlocked":
					if (Network.isHost()) Network.instantiate("locker_unlocked", pos);
					break;
				case "door":
					GameObject.instantiate(PrefabManager.loadPrefab("door"), pos);
					break;
				case "ldoor":
					GameObject.instantiate(PrefabManager.loadPrefab("laser_door"), pos);
					break;
			}

			// For doors
			if(object.getType().contains("door")) {
				solidityMap[((int) (pos.x / 100))][((int) (pos.y / 100))] = 1;
				solidityMap[((int) (pos.x / 100)) + 1][((int) (pos.y / 100))] = 1;
				solidityMap[((int) (pos.x / 100))][((int) (pos.y / 100)) + 4] = 1;
				solidityMap[((int) (pos.x / 100)) + 1][((int) (pos.y / 100)) + 4] = 1;
			}
		}
	}

	public static void instantiateEnemies(TMXObject[] objects, Vector2 offset) {
		for(TMXObject object : objects) {
			Vector2 pos = new Vector2((object.getX() + offset.x) * MAP_SCALE, (object.getY() + offset.y) * MAP_SCALE);
			switch(object.getType()) {
				case "spawn":
					enemySpawns.add(pos);
					break;
				case "prova_crewman":
					if (Network.isHost()) Network.instantiate("prova_crewman", pos);
					break;
				case "dera_crewman":
					if (Network.isHost()) Network.instantiate("dera_crewman", pos);
					break;
				case "supra_crewman":
					if (Network.isHost()) Network.instantiate("supra_crewman", pos);
					break;
			}
		}
	}
}

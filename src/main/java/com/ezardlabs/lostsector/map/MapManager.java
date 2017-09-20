package com.ezardlabs.lostsector.map;

import com.ezardlabs.dethsquare.Collider;
import com.ezardlabs.dethsquare.GameObject;
import com.ezardlabs.dethsquare.Layers;
import com.ezardlabs.dethsquare.graphics.Renderer;
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
import com.ezardlabs.lostsector.missions.objectives.Cryopod;

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
		TMXLoader tmxLoader;
		if (overrideMapName == null) {
			tmxLoader = new TMXLoader("maps/" + name + ".tmx", false);
		}else {
			tmxLoader = new TMXLoader(name, true);
		}
		loadTMX(tmxLoader.getMap());
		NavMesh.init(solidityMap);
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
					default:
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
				default:
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
			textureAtlases.add(TextureAtlas.load(folder + "/"+ tileSet.getImageSource(), tileSet.getTileWidth(), tileSet.getTileHeight()));
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

		TextureAtlas ta = TextureAtlas.load("images/environment/atlas.png", "images/environment/atlas.txt");
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
			Renderer renderer = new Renderer(ta, sprite, tileWidth, tileHeight);
			renderer.setDepth(zindex);
			if(collision) {
				GameObject tile = GameObject.instantiate(
						new GameObject("Tile", true, renderer, new Collider(tileWidth, tileHeight)), new Vector2(x, y),
						new Vector2(t.isFlippedHorizontal() ? -1 : 1, t.isFlippedVertical() ? -1 : 1));
				tile.setLayer(Layers.getLayer("Solid"));
				try {
					solidityMap[(int)(col + offset.x)][(int)(row + offset.y)] = 1;
				} catch(ArrayIndexOutOfBoundsException ex) {
					System.out.println("Error setting solidityMap at index [" + col + "][" + row + "]");
				}
			} else {
				GameObject.instantiate(new GameObject("Tile", true, renderer), new Vector2(x, y),
						new Vector2(t.isFlippedHorizontal() ? -1 : 1, t.isFlippedVertical() ? -1 : 1));
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
				case "cryopod":
					GameObject.instantiate(
							new GameObject("Cryopod", "cryopod", new Renderer(), new Collider(300, 100), new Cryopod()),
							pos);
					break;
				default:
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
			Vector2 pos = new Vector2((object.getX() + offset.x * TILE_SIZE) * MAP_SCALE, (object.getY() + offset.y * TILE_SIZE) * MAP_SCALE);
			switch(object.getType()) {
				case "spawn":
					enemySpawns.add(pos);
					GameObject.instantiate(PrefabManager.loadPrefab("spawn_point"), pos);
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
				default:
					break;
			}
		}
	}
}

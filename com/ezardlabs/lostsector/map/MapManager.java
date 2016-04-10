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
import com.ezardlabs.lostsector.objects.environment.Camera;
import com.ezardlabs.lostsector.objects.environment.Door;
import com.ezardlabs.lostsector.objects.environment.Locker;

import java.io.BufferedReader;
import java.io.IOException;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import java.util.ArrayList;

public class MapManager {
	private static int[][] solidityMap;

	public static void loadMap(String name) {
		loadTMX(name);
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

	private static void loadTMX(String name) {
		try {
			File mapFile = new File("assets/maps/" + name + ".tmx");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(mapFile);
			doc.getDocumentElement().normalize();
			Element root = doc.getDocumentElement();
			if(!root.getNodeName().equals("map")) {
				throw new Exception("Invalid map file.");
			}
			NamedNodeMap rootAttrs = root.getAttributes();
//			String version = rootAttrs.getNamedItem("version").getNodeValue();
//			String orientation = rootAttrs.getNamedItem("orientation").getNodeValue();
//			String renderOrder = rootAttrs.getNamedItem("renderorder").getNodeValue();
			int mapWidth = Integer.parseInt(rootAttrs.getNamedItem("width").getNodeValue());
			int mapHeight = Integer.parseInt(rootAttrs.getNamedItem("height").getNodeValue());
			int mapTileWidth = Integer.parseInt(rootAttrs.getNamedItem("tilewidth").getNodeValue());
			int mapTileHeight = Integer.parseInt(rootAttrs.getNamedItem("tileheight").getNodeValue());
			Map map = new Map(mapWidth, mapHeight, mapTileWidth, mapTileHeight);
			NodeList tileSets = root.getElementsByTagName("tileset");
			for(int i = 0; i < tileSets.getLength(); i++) {
				Node node = tileSets.item(i);
				NamedNodeMap nodeAttrs = node.getAttributes();
				int firstGid = Integer.parseInt(nodeAttrs.getNamedItem("firstgid").getNodeValue());
				Node source = nodeAttrs.getNamedItem("source");
				TileSet tileSet = null;
				if(source == null) {
					tileSet = loadTMXTileSet(node, firstGid);
				} else {
					tileSet = loadTMXTileSet("assets/maps/" + source.getNodeValue(), firstGid);
				}
				if(tileSet != null) {
					map.addTileSet(tileSet);
				}
			}
			NodeList tileLayers = root.getElementsByTagName("layer");
			boolean isBackgroundLayer = true;
			for(int i = 0; i < tileLayers.getLength(); i++) {
				Node node = tileLayers.item(i);
				NamedNodeMap nodeAttrs = node.getAttributes();
				String layerName = nodeAttrs.getNamedItem("name").getNodeValue();
				int layerWidth = Integer.parseInt(nodeAttrs.getNamedItem("width").getNodeValue());
				int layerHeight = Integer.parseInt(nodeAttrs.getNamedItem("height").getNodeValue());
				NodeList childNodes = node.getChildNodes();
				MapTile[] mapTiles = null;
				for(int j = 0; j < childNodes.getLength(); j++) {
					if(childNodes.item(j).getNodeName().equals("data")) {
						mapTiles = loadTMXTiles(childNodes.item(j), layerWidth, layerHeight);
						break;
					}
				}
				if(mapTiles == null) {
					throw new Exception("Cannot load tiles for layer: " + layerName);
				}
				MapLayer mapLayer = new MapLayer(layerName, layerWidth, layerHeight, mapTiles);
				if(layerName.equals("main")) {
					map.setMainLayer(mapLayer);
					isBackgroundLayer = false;
				} else if(isBackgroundLayer) {
					map.addBackgroundLayer(mapLayer);
				} else if(!isBackgroundLayer) {
					map.addForegroundLayer(mapLayer);
				}
			}
			NodeList objectGroups = doc.getElementsByTagName("objectgroup");

			TileSet tileSet = map.getTileSets().get(0);
			MapTile[] tiles = map.getMainLayer().getTiles();
			for(int i = 0; i < tiles.length; i++) {
				if(tiles[i].getGid() > 0) {
					int w = map.getTileWidth();
					int h = map.getTileHeight();
					int row = i / map.getHeight();
					int col = i % map.getWidth();
					int x = row * w;
					int y = col * h;

					int u = 16;
					int v = 16;
					Renderer renderer = new Renderer("maps/" + tileSet.getImageSource(), u, v, w, h);
					GameObject.instantiate(
							new GameObject("Collider Tile", true, renderer, new Collider(w, h)),
							new Vector2(x, y)
					);
				}
			}
/*			TextureAtlas eta = new TextureAtlas("images/environment/atlas.png", "images/environment/atlas.txt");
			try {
				String temp;
				BufferedReader reader = Utils.getReader("maps/" + name + ".lsmap");
				while ((temp = reader.readLine()) != null) {
					if (temp.equals("BEGIN METADATA")) readInMetaData(reader);
					if (temp.equals("BEGIN BLOCKS")) readInBlocks(reader);
					if (temp.equals("BEGIN LOCKERS")) readInLockers(reader, eta);
					if (temp.equals("BEGIN DOORS")) readInDoors(reader, eta);
					if (temp.equals("BEGIN CAMERAS")) readInCameras(reader, eta);
				}
			} catch (IOException ignored) {
				ignored.printStackTrace();
			}
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
			}*/

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static TileSet loadTMXTileSet(String name, int firstGid) {
		TileSet tileSet = null;
		try {
			File tileSetFile = new File(name);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(tileSetFile);
			doc.getDocumentElement().normalize();
			Element root = doc.getDocumentElement();
			tileSet = loadTMXTileSet(root, firstGid);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return tileSet;
	}

	public static TileSet loadTMXTileSet(Node root, int firstGid) {
		NamedNodeMap rootAttrs = root.getAttributes();
		String name = rootAttrs.getNamedItem("name").getNodeValue();
		int tileWidth = Integer.parseInt(rootAttrs.getNamedItem("tilewidth").getNodeValue());
		int tileHeight = Integer.parseInt(rootAttrs.getNamedItem("tileheight").getNodeValue());
		int tileCount = Integer.parseInt(rootAttrs.getNamedItem("tilecount").getNodeValue());
		String imageSource = "";
		int imageWidth = 0;
		int imageHeight = 0;
		MapTile[] mapTiles = new MapTile[tileCount];
		NodeList rootChildren = root.getChildNodes();
		for(int i = 0; i < rootChildren.getLength(); i++) {
			if(rootChildren.item(i).getNodeName().equals("image")) {
				NamedNodeMap childAttrs = rootChildren.item(i).getAttributes();
				imageSource = childAttrs.getNamedItem("source").getNodeValue();
				imageWidth = Integer.parseInt(childAttrs.getNamedItem("width").getNodeValue());
				imageHeight = Integer.parseInt(childAttrs.getNamedItem("height").getNodeValue());
			} else if(rootChildren.item(i).getNodeName().equals("tile")) {
				NamedNodeMap childAttrs = rootChildren.item(i).getAttributes();
				int gid = Integer.parseInt(childAttrs.getNamedItem("gid").getNodeValue());
				mapTiles[gid] = new MapTile(gid);
			}
		}
		TileSet tileSet = new TileSet(name, firstGid, tileWidth, tileHeight, tileCount);
		tileSet.setImage(imageSource, imageWidth, imageHeight);
		return tileSet;
	}

	public static MapTile[] loadTMXTiles(Node data, int layerWidth, int layerHeight) {
		NodeList childNodes = data.getChildNodes();
//		MapTile[] mapTiles = new MapTile[layerWidth * layerHeight];
		ArrayList<MapTile> arrMapTiles = new ArrayList<>();
		for(int i = 0; i < childNodes.getLength(); i++) {
			String nodeName = childNodes.item(i).getNodeName();
			if(childNodes.item(i).getNodeName().equals("tile")) {
				Node node = childNodes.item(i);
				NamedNodeMap attrs = node.getAttributes();
				Node gid = childNodes.item(i).getAttributes().getNamedItem("gid");
//				mapTiles[i / 2] = new MapTile(Integer.parseInt(gid.getNodeValue()));
				arrMapTiles.add(new MapTile(Integer.parseInt(gid.getNodeValue())));
			}
		}
		return arrMapTiles.toArray(new MapTile[layerWidth * layerHeight]);
	}
}

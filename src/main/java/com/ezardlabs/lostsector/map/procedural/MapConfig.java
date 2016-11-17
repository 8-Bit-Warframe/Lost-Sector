package com.ezardlabs.lostsector.map.procedural;

import com.ezardlabs.dethsquare.tmx.Map;
import com.ezardlabs.dethsquare.tmx.TMXLoader;
import com.ezardlabs.dethsquare.util.IOUtils;
import com.ezardlabs.lostsector.map.MapManager;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Benjamin on 2016-08-28.
 */

public class MapConfig {

    public enum ProceduralType {
        CORPUS
    }

    public ProceduralType type;
    public int numRooms;
    public int minMapHeight;
    public int maxMapHeight;

    public HashMap<String, ArrayList<MapSegment>> spawnSegments;
    public HashMap<String, ArrayList<MapSegment>> mainSegments;
//    public HashMap<String, ArrayList<MapSegment>> connectSegments;
    public HashMap<String, ArrayList<MapSegment>> endSegments;
    public HashMap<String, ArrayList<MapSegment>> extractSegments;
    public HashMap<String, ArrayList<MapSegment>> lockSegments;

    public String strSpawnDir;
    public String strMainDir;
    public String strConnectDir;
    public String strEndDir;
    public String strExtractDir;
    public String strLockDir;

    public MapConfig(ProceduralType type, int numRooms) {
        this.type = type;
        this.numRooms = numRooms;
        this.minMapHeight = (int)((-3 * MapManager.TILE_SIZE) + MapManager.PROC_START_POS.y);
        this.maxMapHeight = (int)((3 * MapManager.TILE_SIZE) + MapManager.PROC_START_POS.y);

        spawnSegments = new HashMap<>();
        mainSegments = new HashMap<>();
//        connectSegments = new HashMap<>();
        endSegments = new HashMap<>();
        extractSegments = new HashMap<>();
        lockSegments = new HashMap<>();

        String strType = getTypeString();
        this.strSpawnDir = "maps/procedural/" + strType + "/spawn";
        this.strMainDir = "maps/procedural/" + strType + "/main";
        this.strConnectDir = "maps/procedural/" + strType + "/connect";
        this.strEndDir = "maps/procedural/" + strType + "/end";
        this.strExtractDir = "maps/procedural/" + strType + "/extract";
        this.strLockDir = "maps/procedural/" + strType + "/lock";

        loadSegments(this.spawnSegments, this.strSpawnDir);
        loadSegments(this.mainSegments, this.strMainDir);
//        loadSegments(this.connectSegments, this.strConnectDir);
        loadSegments(this.endSegments, this.strEndDir);
        loadSegments(this.extractSegments, this.strExtractDir);
        loadSegments(this.lockSegments, this.strLockDir);

        for(String key : this.mainSegments.keySet()) {
            System.out.println(key);
            for(MapSegment seg : this.mainSegments.get(key)) {
                System.out.println("\t" + seg.map.getFilePath());
            }
        }
    }

    public String getTypeString() {
        switch(this.type) {
            case CORPUS:
                return "corpus_test";
            default:
                return "";
        }
    }

    private void loadSegments(HashMap<String, ArrayList<MapSegment>> segments, String dirPath) {
        System.out.println("Loading TMX MapSegments: " + dirPath);
        TMXLoader tmxLoader;
        String[] strFileNames = IOUtils.listFileNames(dirPath);
        for(int i = 0; i < strFileNames.length; i++) {
            if(strFileNames[i] == null || !strFileNames[i].endsWith(".tmx")) {
                continue;
            }
            tmxLoader = new TMXLoader(dirPath + "/" + strFileNames[i]);
            Map newMap = tmxLoader.getMap();
            if(newMap.getWidth() % MapManager.MAP_SEGMENT_SIZE != 0 || newMap.getHeight() % MapManager.MAP_SEGMENT_SIZE != 0) {
                System.err.println("The dimensions of '" + newMap.getFilePath() + "' are not a multiple of " + MapManager.MAP_SEGMENT_SIZE + ".");
                continue;
            }
            addSegment(segments, new MapSegment(newMap), segments == this.lockSegments);
        }
    }

    private void addSegment(HashMap<String, ArrayList<MapSegment>> segments, MapSegment seg, boolean isLockSegment) {
        for(MapSegmentConnector conn : seg.connectors) {
            String type = (isLockSegment ? conn.toString() : conn.getSideType());
            if(!segments.containsKey(type)) {
                segments.put(type, new ArrayList<>());
            }
            segments.get(type).add(seg);
        }
    }
}
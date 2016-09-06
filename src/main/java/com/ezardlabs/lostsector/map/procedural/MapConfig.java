package com.ezardlabs.lostsector.map.procedural;

import com.ezardlabs.dethsquare.tmx.TMXLoader;
import com.ezardlabs.dethsquare.util.Utils;

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

    public HashMap<String, ArrayList<MapSegment>> spawnSegments;
    public HashMap<String, ArrayList<MapSegment>> mainSegments;
    public HashMap<String, ArrayList<MapSegment>> connectSegments;
    public HashMap<String, ArrayList<MapSegment>> endSegments;
    public HashMap<String, ArrayList<MapSegment>> extractSegments;

    public String strSpawnTileSetPath;
    public String strMainTileSetPath;
    public String strConnectTileSetPath;
    public String strEndTileSetPath;
    public String strExtractTileSetPath;

    public MapConfig(ProceduralType type, int numRooms) {
        this.type = type;
        this.numRooms = numRooms;

        spawnSegments = new HashMap<>();
        mainSegments = new HashMap<>();
        connectSegments = new HashMap<>();
        endSegments = new HashMap<>();
        extractSegments = new HashMap<>();

        String strType = getTypeString();
        this.strSpawnTileSetPath = "maps/procedural/" + strType + "/spawn";
        this.strMainTileSetPath = "maps/procedural/" + strType + "/main";
        this.strConnectTileSetPath = "maps/procedural/" + strType + "/connect";
        this.strEndTileSetPath = "maps/procedural/" + strType + "/end";
        this.strExtractTileSetPath = "maps/procedural/" + strType + "/extract";

        loadSegments(this.spawnSegments, this.strSpawnTileSetPath);
        loadSegments(this.mainSegments, this.strMainTileSetPath);
        loadSegments(this.connectSegments, this.strConnectTileSetPath);
        loadSegments(this.endSegments, this.strEndTileSetPath);
        loadSegments(this.extractSegments, this.strExtractTileSetPath);
    }

    private void loadSegments(HashMap<String, ArrayList<MapSegment>> segments, String dirPath) {
        TMXLoader tmxLoader;
        String[] strFileNames = Utils.getAllFileNames(dirPath);
        for(int i = 0; i < strFileNames.length; i++) {
            tmxLoader = new TMXLoader(dirPath + "/" + strFileNames[i]);
            addSegment(segments, new MapSegment(tmxLoader.getMap()));
        }
    }

    public String getTypeString() {
        switch(this.type) {
            case CORPUS:
                return "corpus";
            default:
                return "";
        }
    }

    private void addSegment(HashMap<String, ArrayList<MapSegment>> segments, MapSegment seg) {
        for(MapSegmentConnector conn : seg.connectors) {
            if(!segments.containsKey(conn.toString())) {
                segments.put(conn.toString(), new ArrayList<>());
            }
            segments.get(conn.toString()).add(seg);
        }
    }
}
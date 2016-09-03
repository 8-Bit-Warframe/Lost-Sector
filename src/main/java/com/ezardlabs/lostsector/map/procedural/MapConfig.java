package com.ezardlabs.lostsector.map.procedural;

import com.ezardlabs.dethsquare.tmx.TMXLoader;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Benjamin on 2016-08-28.
 */

public class MapConfig {

    public enum ProceduralType {
        NONE,
        CORPUS
    }

    public static final int MAX_START = 2;
    public static final int MAX_MAIN = 6;
    public static final int MAX_END = 1;

    public HashMap<String, ArrayList<MapSegment>> startSegments;
    public HashMap<String, ArrayList<MapSegment>> mainSegments;
    public HashMap<String, ArrayList<MapSegment>> endSegments;

    public MapSegment defaultStartSeg;

    public ProceduralType type;
    public int numRooms;

    public MapConfig(ProceduralType type, int numRooms) {
        startSegments = new HashMap<>();
        mainSegments = new HashMap<>();
        endSegments = new HashMap<>();

        this.type = type;
        this.numRooms = numRooms;
        String strType = getTypeString();
        TMXLoader tmxLoader;
        for(int i = 0; i < MAX_START; i++) {
            tmxLoader = new TMXLoader("maps/procedural/" + strType + "/start_16x16_" + (i + 1) + ".tmx");
            addSegment(startSegments, new MapSegment(tmxLoader.getMap()));
            if(i == 0) {
                this.defaultStartSeg = new MapSegment(tmxLoader.getMap());
            }
        }
        for(int i = 0; i < MAX_MAIN; i++) {
            tmxLoader = new TMXLoader("maps/procedural/" + strType + "/16x16_" + (i + 1) + ".tmx");
            addSegment(mainSegments, new MapSegment(tmxLoader.getMap()));
        }
        for(int i = 0; i < MAX_END; i++) {
            tmxLoader = new TMXLoader("maps/procedural/" + strType + "/end_16x16_" + (i + 1) + ".tmx");
            addSegment(endSegments, new MapSegment(tmxLoader.getMap()));
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
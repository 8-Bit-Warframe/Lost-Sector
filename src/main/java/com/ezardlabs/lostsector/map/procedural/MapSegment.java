package com.ezardlabs.lostsector.map.procedural;

import com.ezardlabs.dethsquare.Vector2;
import com.ezardlabs.dethsquare.tmx.Map;
import com.ezardlabs.dethsquare.tmx.ObjectGroup;
import com.ezardlabs.dethsquare.tmx.TMXObject;

import java.util.ArrayList;

/**
 * Created by Benjamin on 2016-08-28.
 */


public class MapSegment {
    public Map map;
    public Vector2 pos;
    public ArrayList<MapSegmentConnector> connectors;

    public MapSegment(Map map) {
        this.map = map;
        this.pos = new Vector2();
        this.connectors = new ArrayList<>();
        try {
            ObjectGroup connectorGroup = null;
            for(ObjectGroup objGrp : map.getObjectGroups()) {
                if(objGrp.getName().equals("connectors")) {
                    connectorGroup = objGrp;
                    break;
                }
            }
            if (connectorGroup == null) {
                throw new Exception("No connectors found in map, " + this.map.getFilePath());
            }
            for(TMXObject tmxObj : connectorGroup.getObjects()) {
                MapSegmentConnector conn = new MapSegmentConnector(this.map, tmxObj.getType(), new Vector2(tmxObj.getX(), tmxObj.getY()));
                this.connectors.add(conn);
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public int getWidth() {
        return this.map.getWidth();
    }

    public int getHeight() {
        return this.map.getHeight();
    }
}

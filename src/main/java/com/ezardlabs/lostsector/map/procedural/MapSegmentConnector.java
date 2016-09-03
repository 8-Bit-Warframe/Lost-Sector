package com.ezardlabs.lostsector.map.procedural;

import com.ezardlabs.dethsquare.Vector2;
import com.ezardlabs.dethsquare.tmx.Map;

/**
 * Created by Benjamin on 2016-08-28.
 */


public class MapSegmentConnector {

    public enum MapSegmentConnectorSide {
        CENTRE,
        TOP,
        RIGHT,
        BOTTOM,
        LEFT
    }

    public Map parentMap;
    public String sideType;
    public MapSegmentConnectorSide side;
    public int type;
    public Vector2 pos;
    public MapSegmentConnector connection;

    public MapSegmentConnector(Map parentMap, String sideType, Vector2 pos) {
        this.parentMap = parentMap;
        this.sideType = sideType;
        this.pos = pos;
        this.connection = null;
        String[] typeSplit = sideType.split("_");
        this.side = MapSegmentConnectorSide.CENTRE;
        this.type = Integer.parseInt(typeSplit[1]);
        switch(typeSplit[0]) {
            case "c":
                side = MapSegmentConnectorSide.CENTRE;
                break;
            case "t":
                side = MapSegmentConnectorSide.TOP;
                break;
            case "r":
                side = MapSegmentConnectorSide.RIGHT;
                break;
            case "b":
                side = MapSegmentConnectorSide.BOTTOM;
                break;
            case "l":
                side = MapSegmentConnectorSide.LEFT;
                break;
        }
    }

    public boolean equals(MapSegmentConnector conn) {
        return (this.side == conn.side && this.type == conn.type);
    }

    public boolean isValidConnection(MapSegmentConnector conn) {
        boolean correctSide = false;
        switch(this.side) {
            case CENTRE:
                correctSide = (conn.side == MapSegmentConnectorSide.CENTRE);
                break;
            case TOP:
                correctSide = (conn.side == MapSegmentConnectorSide.BOTTOM);
                break;
            case RIGHT:
                correctSide = (conn.side == MapSegmentConnectorSide.LEFT);
                break;
            case BOTTOM:
                correctSide = (conn.side == MapSegmentConnectorSide.TOP);
                break;
            case LEFT:
                correctSide = (conn.side == MapSegmentConnectorSide.RIGHT);
                break;
        }
        // Sides must fit and type must be the same.
        return correctSide && this.type == conn.type;
    }

    public void connect(MapSegmentConnector conn) {
        this.connection = conn;
        conn.connection = this;
    }

    public String getMatchingSideType() {
        String retSideType = "";
        switch(this.side) {
            case CENTRE:
                retSideType += "c_";
                break;
            case TOP:
                retSideType += "b_";
                break;
            case RIGHT:
                retSideType += "l_";
                break;
            case BOTTOM:
                retSideType += "t_";
                break;
            case LEFT:
                retSideType += "r_";
                break;
        }
        return retSideType + this.type;
    }

    public boolean isConnected() {
        return this.connection != null;
    }

    @Override
    public String toString() {
        return this.sideType;
    }
}

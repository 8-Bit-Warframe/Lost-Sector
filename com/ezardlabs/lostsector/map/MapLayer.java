package com.ezardlabs.lostsector.map;

import java.util.ArrayList;

/**
 * Created by Benjamin on 2016-04-09.
 */
public class MapLayer {
    private String name;
    private int width;
    private int height;

    private MapTile[] tiles;

    public MapLayer(String name, int width, int height, MapTile[] mapTiles) {
        this.name = name;
        this.width = width;
        this.height = height;
        this.tiles = mapTiles;
    }

    public String getName() {
        return this.name;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public MapTile[] getTiles() {
        return this.tiles;
    }

    public MapTile getTile(int idx) {
        return this.tiles[idx];
    }
}

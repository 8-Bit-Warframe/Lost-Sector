package com.ezardlabs.lostsector.map;

import java.util.ArrayList;

/**
 * Created by Benjamin on 2016-04-09.
 */
public class Map {
    private int width;
    private int height;
    private int tileWidth;
    private int tileHeight;

    private ArrayList<MapLayer> fgLayers;
    private ArrayList<MapLayer> bgLayers;
    private MapLayer mainLayer;

    private ArrayList<TileSet> tileSets;

    public Map(int width, int height, int tileWidth, int tileHeight) {
        this.width = width;
        this.height = height;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.fgLayers = new ArrayList<>();
        this.bgLayers = new ArrayList<>();
        this.mainLayer = null;
        this.tileSets = new ArrayList<>();
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getTileWidth() {
        return this.tileWidth;
    }

    public int getTileHeight() {
        return this.tileHeight;
    }

    public ArrayList<MapLayer> getForegroundLayers() {
        return this.fgLayers;
    }

    public ArrayList<MapLayer> getBackgroundLayers() {
        return this.bgLayers;
    }

    public MapLayer getMainLayer() {
        return this.mainLayer;
    }

    public ArrayList<TileSet> getTileSets() {
        return this.tileSets;
    }

    public void addForegroundLayer(MapLayer mapLayer) {
        this.fgLayers.add(mapLayer);
    }

    public void addBackgroundLayer(MapLayer mapLayer) {
        this.bgLayers.add(mapLayer);
    }

    public void setMainLayer(MapLayer mapLayer) {
        this.mainLayer = mapLayer;
    }

    public void addTileSet(TileSet tileSet) {
        this.tileSets.add(tileSet);
    }
}

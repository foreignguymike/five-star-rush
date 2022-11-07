package com.distraction.fsr.tilemap.data;

import com.distraction.fsr.tilemap.Restaurant;

public class RestaurantData {

    public Restaurant.Type restaurantType;
    public int numRows;
    public int numCols;
    public int[][] map;
    public int[][] bgMap;

    public RestaurantData(Restaurant.Type restaurantType, int numRows, int numCols, int[][] map, int[][] bgMap) {
        this.restaurantType = restaurantType;
        this.numRows = numRows;
        this.numCols = numCols;
        this.map = map;
        this.bgMap = bgMap;
    }
}

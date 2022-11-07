package com.distraction.fsr.tilemap;

public class TilePosition {

    public int row;
    public int col;

    public TilePosition() {}

    public TilePosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public void set(int row, int col) {
        this.row = row;
        this.col = col;
    }

}

package com.distraction.fsr.entity;

import com.distraction.fsr.tilemap.Restaurant;
import com.distraction.fsr.tilemap.TilePosition;
import com.distraction.fsr.utils.Direction;

public abstract class TileEntity extends Entity {

    protected final Restaurant restaurant;
    public final TilePosition tp;

    protected Direction direction = Direction.DOWN;

    protected TileEntity(Restaurant restaurant) {
        this.restaurant = restaurant;
        tp = new TilePosition();
    }

    public void setPositionFromTile(int row, int col) {
        tp.set(row, col);
        p.x = Restaurant.getXFromCol(col);
        p.y = Restaurant.getYFromRow(row);
        d.set(p);
    }

    public void updateTilePosition() {
        tp.set(Restaurant.getRowFromY(p.y), Restaurant.getColFromX(p.x));
    }

}

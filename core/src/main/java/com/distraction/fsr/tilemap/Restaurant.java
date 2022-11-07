package com.distraction.fsr.tilemap;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.distraction.fsr.Global;
import com.distraction.fsr.entity.Food;
import com.distraction.fsr.entity.Player;
import com.distraction.fsr.entity.Table;
import com.distraction.fsr.entity.TileEntity;
import com.distraction.fsr.tilemap.data.RestaurantData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Restaurant {

    public static final int TILE_SIZE = 80;

    private static final Comparator<TileEntity> COMP = (t1, t2) -> Float.compare(t2.p.y, t1.p.y);

    public static float getXFromCol(int col) {
        return col * TILE_SIZE + TILE_SIZE / 2f;
    }

    public static float getYFromRow(int row) {
        return row * TILE_SIZE + TILE_SIZE / 2f;
    }

    public static int getColFromX(float x) {
        return (int) (x / TILE_SIZE);
    }

    public static int getRowFromY(float y) {
        return (int) (y / TILE_SIZE);
    }

    public enum Type {
        FRUIT
    }

    public final Type type;

    public final int numRows;
    public final int numCols;
    private final int[][] map;
    private final int[][] bgMap;

    private final TextureRegion[] floorTiles;

    private final List<Table> tables;
    private final List<TileEntity> tileEntities;
    private final List<Food> foodToAdd;
    private int foodCount = 0;

    public Restaurant(Global global, Type type, int level) {
        this.type = type;

        RestaurantData data = global.levelData.data.get(level);
        numRows = data.numRows;
        numCols = data.numCols;
        map = data.map;
        bgMap = data.bgMap;

        floorTiles = new TextureRegion[2];
        for (int i = 0; i < floorTiles.length; i++) {
            floorTiles[i] = global.getImage(type.name() + "floor", i);
        }

        tables = new ArrayList<>();
        tileEntities = new ArrayList<>();
        foodToAdd = new ArrayList<>();

        for (int row = 0; row < numRows; row++) {
            for (int col = numCols - 1; col >= 0; col--) {
                if (map[row][col] > 0) {
                    Table table = new Table(global, this);
                    table.setPositionFromTile(row, col);
                    tileEntities.add(table);
                    tables.add(table);
                    Food.Type foodType = Food.Type.find(type, map[row][col]);
                    if (foodType != null) {
                        foodCount++;
                        Food food = new Food(global, this, foodType);
                        food.tp.set(row, col);
                        foodToAdd.add(food);
                    }
                }
            }
        }
        Collections.shuffle(foodToAdd);
    }

    public void addPlayer(Player player) {
        tileEntities.add(player);
    }

    public boolean isOOB(int row, int col) {
        return row < 0 || col < 0 || row >= map.length || col >= map[0].length;
    }

    public boolean isBlocked(int row, int col) {
        return isOOB(row, col) || map[row][col] > 0;
    }

    public boolean initFood() {
        if (foodCount > 0) {
            Food food = foodToAdd.get(foodCount - 1);
            for (Table table : tables) {
                if (table.tp.row == food.tp.row && table.tp.col == food.tp.col) {
                    table.setFood(food);
                    foodCount--;
                    return true;
                }
            }
        }
        return false;
    }

    public TilePosition randomEmpty() {
        while (true) {
            int row = MathUtils.random(numRows - 1);
            int col = MathUtils.random(numCols - 1);
            if (map[row][col] <= 0) return new TilePosition(row, col);
        }
    }

    public Table getTables(int row, int col) {
        for (Table table : tables) {
            if (table.tp.row == row && table.tp.col == col) return table;
        }
        return null;
    }

    public float getWidth() {
        return numCols * TILE_SIZE;
    }

    public float getHeight() {
        return numRows * TILE_SIZE;
    }

    public boolean contains(float x, float y) {
        return x >= 0 && x <= getWidth() && y >= 0 && y <= getHeight();
    }

    public void update(float dt) {
        tileEntities.sort(COMP);
        for (TileEntity tileEntity : tileEntities) {
            tileEntity.update(dt);
            if (tileEntity instanceof Table) {
                Table table = (Table) tileEntity;
                Food food = table.food;
                if (food != null) {
                    food.update(dt);
                    if (food.player != null && food.atDestination()) {
                        food.player.ate(food);
                        table.food = null;
                    }
                }
            }
        }
    }

    public void render(SpriteBatch sb) {
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                sb.draw(floorTiles[bgMap[row][col]], col * TILE_SIZE, row * TILE_SIZE);
            }
        }
        for (TileEntity tileEntity : tileEntities) {
            tileEntity.render(sb);
        }
    }

}

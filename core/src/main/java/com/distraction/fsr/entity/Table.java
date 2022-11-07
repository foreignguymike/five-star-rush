package com.distraction.fsr.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.distraction.fsr.Global;
import com.distraction.fsr.tilemap.Restaurant;
import com.distraction.fsr.utils.Constants;

public class Table extends TileEntity {

    private final TextureRegion image;

    public Food food = null;

    public Table(Global global, Restaurant restaurant) {
        super(restaurant);
        image = global.getImage(restaurant.type.name() + "table");
        setSize(image);
    }

    public void setFood(Food food) {
        this.food = food;
        food.setPositionFromTile(tp.row, tp.col);
    }

    @Override
    public void setPositionFromTile(int row, int col) {
        tp.set(row, col);
        p.x = Restaurant.getXFromCol(col);
        p.y = Restaurant.getYFromRow(row) + Constants.HEIGHT * .0185f;
        d.set(p);
    }

    @Override
    public void update(float dt) {
        if (food != null) {
            food.update(dt);
            if (food.player != null && food.atDestination()) {
                food.player.ate(food);
                food = null;
            }
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.draw(image, left(), bottom());
        if (food != null) food.render(sb);
    }
}

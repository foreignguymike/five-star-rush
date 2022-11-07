package com.distraction.fsr.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.distraction.fsr.Global;
import com.distraction.fsr.tilemap.Restaurant;

public class TileSelect extends TileEntity {

    private TextureRegion image;
    private TextureRegion imagex;
    private float time;

    public TileSelect(Global global, Restaurant restaurant) {
        super(restaurant);

        image = global.getImage("tileselect");
        imagex = global.getImage("tileselectx");
        setSize(image);
    }

    @Override
    public void update(float dt) {
        time += dt;
        scale = 1 + 0.1f * MathUtils.sin(5 * time);
    }

    @Override
    public void render(SpriteBatch sb) {
        if (restaurant.isOOB(tp.row, tp.col)) return;
        TextureRegion t = restaurant.isBlocked(tp.row, tp.col) ? imagex : image;
        sb.draw(t, sleft(), sbottom(), swidth(), sheight());
    }
}

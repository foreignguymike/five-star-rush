package com.distraction.fsr.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.distraction.fsr.Global;
import com.distraction.fsr.tilemap.Restaurant;
import com.distraction.fsr.utils.Utils;

public class TeleportColumn extends Entity {

    public TextureRegion pixel;
    private float alpha = 1f;

    public TeleportColumn(Global global) {
        pixel = global.getImage("pixel");
        setSize(Restaurant.TILE_SIZE, Restaurant.TILE_SIZE);
    }

    @Override
    public void update(float dt) {
        alpha -= 4 * dt;
    }

    @Override
    public void render(SpriteBatch sb) {
        Utils.setAlpha(sb, alpha);
        sb.draw(pixel, sleft(), sbottom(), swidth(), sheight());
        Utils.setAlpha(sb, 1);
    }
}

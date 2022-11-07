package com.distraction.fsr.entity;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.distraction.fsr.Global;
import com.distraction.fsr.tilemap.Restaurant;

public class PopupText extends Entity {

    private final String text;
    private final BitmapFont font;
    private float alpha = 1;

    public PopupText(Global global, String text) {
        this.text = text;
        font = global.i64Font();
        v.y = Restaurant.TILE_SIZE / 2f;
    }

    @Override
    public void update(float dt) {
        p.add(v.x * dt, v.y * dt);
        alpha -= 2 * dt;
        if (alpha < 0) {
            alpha = 0;
            remove = true;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        font.setColor(1, 1, 1, alpha);
        font.draw(sb, text, p.x, p.y);
        font.setColor(1, 1, 1, 1);
    }
}

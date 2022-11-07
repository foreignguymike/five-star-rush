package com.distraction.fsr.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.distraction.fsr.utils.GameColor;
import com.distraction.fsr.utils.Utils;

public class CountdownImage extends Entity {

    private TextureRegion image;
    private float alpha = 1f;

    private boolean start;
    private float timer;

    public CountdownImage(TextureRegion image) {
        this.image = image;
        setSize(image);
    }

    public void start() {
        start = true;
    }

    @Override
    public void update(float dt) {
        if (!start) return;
        timer += dt;
        if (timer < 0.5f) {
            scale = MathUtils.map(0, 0.5f, 10f, -10f, timer);
            if (scale < 1) scale = 1;
        } else if (timer < 1f) {
            scale = 1f;
            alpha = MathUtils.map(0.5f, 1f, 1f, -1f, timer);
            alpha = MathUtils.clamp(alpha, 0, 1);
        } else {
            alpha = 0f;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        if (!start) return;
        sb.setColor(GameColor.WHITE);
        Utils.setAlpha(sb, alpha);
        sb.draw(image, sleft(), sbottom(), swidth(), sheight());
        Utils.setAlpha(sb, 1);
    }
}

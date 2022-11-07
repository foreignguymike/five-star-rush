package com.distraction.fsr.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.distraction.fsr.utils.Constants;

public class Background extends Entity {

    public enum RepeatMode {
        BOTH,
        HORIZONTAL,
        VERTICAL
    }

    private final TextureRegion image;
    private final RepeatMode repeatMode = RepeatMode.BOTH;

    public Background(TextureRegion image) {
        this.image = image;

        setSize(image);
    }

    @Override
    public void render(SpriteBatch sb) {
        float hcount = Constants.WIDTH / s.x + 1;
        float vcount = Constants.WIDTH / s.y + 1;

        if (repeatMode == RepeatMode.BOTH) {
            for (int row = 0; row < hcount; row++) {
                for (int col = 0; col < vcount; col++) {
                    sb.draw(image, p.x + col * s.x, p.y + row * s.y);
                }
            }
        } else if (repeatMode == RepeatMode.HORIZONTAL) {
            for (int col = 0; col < vcount; col++) {
                sb.draw(image, p.x + col * s.x, p.y);
            }
        } else if (repeatMode == RepeatMode.VERTICAL) {
            for (int row = 0; row < hcount; row++) {
                sb.draw(image, p.x, p.y + row * s.y);
            }
        }
    }
}

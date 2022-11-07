package com.distraction.fsr.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ImageEntity extends Entity {

    private final TextureRegion image;

    public ImageEntity(TextureRegion image) {
        this.image = image;
        setSize(image);
    }

    @Override
    public void update(float dt) {
        move(dt);
    }

    @Override
    public void render(SpriteBatch sb) {
        float scale = this.scale;
        if (scale < 0) scale = 0;
        sb.draw(image, sleft(), sbottom(), swidth(), sheight());
    }
}

package com.distraction.fsr.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.distraction.fsr.Global;

public class IconButton extends Entity {

    public interface ClickListener {
        void onClick();
    }

    private TextureRegion bg;
    private TextureRegion sbg;
    private TextureRegion icon;
    private boolean down;
    private float padding = 5f;

    private ClickListener listener;

    private float timer = 0f;
    private boolean bouncing = false;

    public IconButton(Global global, TextureRegion icon, ClickListener listener) {
        bg = global.getImage("buttonbg");
        sbg = global.getImage("buttonselectedbg");
        this.icon = icon;
        this.listener = listener;

        setSize(bg);
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public void setPadding(float padding) {
        this.padding = padding;
    }

    public void click() {
        if (listener != null) {
            listener.onClick();
            bounce();
        }
    }

    private void bounce() {
        timer = 0f;
        bouncing = true;
    }

    @Override
    public boolean contains(float x, float y) {
        return super.contains(x, y, padding);
    }

    @Override
    public void update(float dt) {
        if (bouncing) {
            timer += dt;
            if (timer > 0.125f) bouncing = false;
            scale = 1 + 0.1f * MathUtils.sin(8 * MathUtils.PI * timer);
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.draw(down ? sbg : bg, sleft(), sbottom(), swidth(), sheight());
        sb.draw(icon, sleft(icon), sbottom(icon), swidth(icon), sheight(icon));
    }
}

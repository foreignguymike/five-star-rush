package com.distraction.fsr.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.distraction.fsr.Global;
import com.distraction.fsr.utils.Constants;
import com.distraction.fsr.utils.MyViewport;

public abstract class Screen {

    // global context
    protected Global global;

    // base viewport
    protected MyViewport viewport;

    // for ui
    protected MyViewport uiViewport;

    protected Vector2 m = new Vector2();
    protected boolean ignoreInput;

    protected final TextureRegion pixel;

    protected Screen(Global global) {
        this.global = global;
        viewport = new MyViewport(Constants.WIDTH, Constants.HEIGHT);
        uiViewport = new MyViewport(Constants.WIDTH, Constants.HEIGHT);
        pixel = global.getImage("pixel");
    }

    public void resize(int w, int h) {
        viewport.update(w, h);
        uiViewport.update(w, h);
    }

    protected void unproject() {
        m.set(Gdx.input.getX(), Gdx.input.getY());
        viewport.unproject(m);
    }

    protected void unproject(Viewport viewport) {
        m.set(Gdx.input.getX(), Gdx.input.getY());
        viewport.unproject(m);
    }

    protected abstract void handleInput(float dt);

    public abstract void update(float dt);

    public abstract void render(SpriteBatch sb);

}

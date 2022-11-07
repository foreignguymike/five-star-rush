package com.distraction.fsr.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.distraction.fsr.Global;
import com.distraction.fsr.utils.Constants;

public class TransitionScreen extends Screen {

    private final Screen nextScreen;
    private final int numPop;

    protected float duration = 0.5f;
    protected float time = 0f;
    protected boolean next = false;

    public TransitionScreen(Global global, Screen nextScreen) {
        this(global, nextScreen, 1);
    }

    public TransitionScreen(Global global, Screen nextScreen, int numPop) {
        super(global);
        this.nextScreen = nextScreen;
        this.numPop = numPop;

        global.screenManager.depth++;
    }

    @Override
    protected void handleInput(float dt) {

    }

    @Override
    public void update(float dt) {
        time += dt;
        if (!next && time > duration / 2) {
            next = true;
            nextScreen.ignoreInput = true;
            for (int i = 0; i < numPop; i++) global.screenManager.pop();
            global.screenManager.depth -= numPop - 1;
            global.screenManager.replace(nextScreen);
            global.screenManager.push(this);
        }
        if (time > duration) {
            global.screenManager.depth--;
            global.screenManager.pop();
            nextScreen.ignoreInput = false;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        float interp = time / duration;
        float perc = interp < 0.5f ? interp * 2 : 1f - (time - duration / 2) / duration * 2;
        Color c = sb.getColor();
        sb.setColor(Color.BLACK);
        sb.setProjectionMatrix(viewport.getCamera().combined);
        sb.begin();
        {
            sb.draw(pixel, 0, Constants.HEIGHT, Constants.WIDTH, -perc * Constants.HEIGHT / 2);
            sb.draw(pixel, 0, 0, Constants.WIDTH, perc * Constants.HEIGHT / 2);
        }
        sb.end();
        sb.setColor(c);
    }
}


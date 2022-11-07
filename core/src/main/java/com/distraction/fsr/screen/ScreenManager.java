package com.distraction.fsr.screen;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Stack;

public class ScreenManager extends Stack<Screen> {

    public int depth = 1;

    public void replace(Screen s) {
        pop();
        push(s);
    }

    public void resize(int w, int h) {
        for (int i = size() - depth; i < size(); i++) {
            get(i).resize(w, h);
        }
    }

    public void update(float dt) {
        for (int i = size() - depth; i < size(); i++) {
            get(i).update(dt);
        }
    }

    public void render(SpriteBatch sb) {
        for (int i = size() - depth; i < size(); i++) {
            get(i).render(sb);
        }
    }

}

package com.distraction.fsr.utils;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Animation {

    public enum Mode {
        REPEAT,
        ONCE
    }

    private final TextureRegion[] frames;
    private float interval;
    private Mode mode;

    private float timer;
    private int index;
    private int playCount;

    public Animation(TextureRegion[] frames, float interval) {
        this(frames, interval, Mode.REPEAT);
    }

    public Animation(TextureRegion[] frames, float interval, Mode mode) {
        this.frames = frames;
        this.interval = interval;
        this.mode = mode;
    }

    private void nextFrame() {
        index++;
        if (index == frames.length) {
            playCount++;
            if (mode == Mode.ONCE) index--;
            if (mode == Mode.REPEAT) index = 0;
        }
    }

    public void replay() {
        if (mode == Mode.ONCE) {
            playCount = 0;
            index = 0;
        }
    }

    public int getPlayCount() {
        return playCount;
    }

    public void update(float dt) {
        if (interval <= 0 || (mode == Mode.ONCE && playCount > 0)) return;
        timer += dt;
        if (timer > interval) {
            timer = 0;
            nextFrame();
        }
    }

    public TextureRegion get() {
        return frames[index];
    }

}

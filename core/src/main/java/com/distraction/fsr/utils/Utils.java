package com.distraction.fsr.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public class Utils {

    public static int[][] int2D(int rows, int cols, int[] array) {
        if (rows * cols != array.length)
            throw new IllegalArgumentException("invalid size " + rows + " * " + cols + " != " + array.length);
        int[][] ret = new int[rows][cols];
        int index = 0;
        for (int row = rows - 1; row >= 0; row--) {
            for (int col = 0; col < cols; col++) {
                ret[row][col] = array[index++];
            }
        }
        return ret;
    }

    public static float roundTo(float v, float m) {
        v *= m;
        v = MathUtils.round(v);
        v /= m;
        return v;
    }

    public static boolean areKeysJustPressed(int... keys) {
        for (int i = 0; i < keys.length; i++) {
            if (Gdx.input.isKeyJustPressed(keys[i])) return true;
        }
        return false;
    }

    public static boolean areKeysPressed(int... keys) {
        for (int i = 0; i < keys.length; i++) {
            if (Gdx.input.isKeyPressed(keys[i])) return true;
        }
        return false;
    }

    public static boolean lmbJustPressed() {
        return Gdx.input.isButtonJustPressed(Input.Buttons.LEFT);
    }

    public static boolean rmbJustPressed() {
        return Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT);
    }

    public static void setAlpha(SpriteBatch sb, float alpha) {
        Color c = sb.getColor();
        sb.setColor(c.r, c.g, c.b, alpha);
    }

    public static void takeScreenshot() {
        // GWT doesn't like
        String name = "FS2 " + java.util.UUID.randomUUID();
        byte[] pixels = com.badlogic.gdx.utils.ScreenUtils.getFrameBufferPixels(0, 0, Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight(), true);
        for (int i = 4; i < pixels.length; i += 4) {
            pixels[i - 1] = (byte) 255;
        }
        com.badlogic.gdx.graphics.Pixmap pixmap = new com.badlogic.gdx.graphics.Pixmap(Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight(), com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888);
        pixmap.getPixels().clear();
        pixmap.getPixels().put(pixels);
        com.badlogic.gdx.graphics.PixmapIO.writePNG(Gdx.files.external(name + ".png"), pixmap);
        pixmap.dispose();
    }

}

package com.distraction.fsr.utils;

import com.badlogic.gdx.graphics.Color;

public class GameColor {

    public static final Color WHITE = from(255, 255, 255);
    public static final Color BLACK = from(0, 0, 0);

    public static final Color DARK_GRAY = from(78, 83, 113);
    public static final Color DARK_RED = from(120, 31, 44);
    public static final Color OUTLINE = from(38, 36, 58);

    private static Color from(int r, int g, int b) {
        return new Color(r / 255f, g / 255f, b / 255f, 1);
    }

}

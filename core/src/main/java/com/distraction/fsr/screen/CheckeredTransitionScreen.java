package com.distraction.fsr.screen;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.distraction.fsr.Global;
import com.distraction.fsr.utils.Constants;
import com.distraction.fsr.utils.GameColor;

class CheckeredTransitionScreen extends TransitionScreen {

    public CheckeredTransitionScreen(Global global, Screen nextScreen) {
        super(global, nextScreen);
    }

    public CheckeredTransitionScreen(Global global, Screen nextScreen, int numPop) {
        super(global, nextScreen, numPop);
    }

    {
        duration = 1.3f;
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(GameColor.BLACK);
        sb.setProjectionMatrix(viewport.getCamera().combined);
        sb.begin();
        {
            float squareSize = Constants.WIDTH / 16f;
            for (int row = 0; row < 9; row++) {
                for (int col = 0; col < 16; col++) {
                    float size;
                    float ttime = time - ((9 - row + col) / 40f) * (duration / 2);
                    if (time < duration / 2) size = squareSize * (ttime / (duration / 6));
                    else size = squareSize - squareSize * ((ttime - duration / 2) / (duration / 6));
                    size = MathUtils.clamp(size, 0, squareSize);
                    sb.draw(pixel, squareSize * 0.5f + squareSize * col - size / 2, squareSize * 0.5f + squareSize * row - size / 2, size, size);
                }
            }
        }
        sb.end();
    }
}


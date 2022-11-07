package com.distraction.fsr.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.distraction.fsr.Global;
import com.distraction.fsr.entity.Food;
import com.distraction.fsr.entity.IconButton;
import com.distraction.fsr.entity.ImageEntity;
import com.distraction.fsr.tilemap.Restaurant;
import com.distraction.fsr.utils.Constants;
import com.distraction.fsr.utils.GameColor;
import com.distraction.fsr.utils.ScoreData;

import java.util.Map;

public class ScoreScreen extends Screen {

    private final ImageEntity bg;

    private final ImageEntity[] foodIcons;
    private final int[] foodCounts;
    private final int[] scoreCounts;
    private final int bonusCount;
    private final int bonusScore;
    private int totalScore;

    private final BitmapFont font;
    private final TextureRegion pixel;

    private final IconButton refreshButton;

    public ScoreScreen(Global global, ScoreData scoreData) {
        super(global);

        bg = new ImageEntity(global.getImage("scoreframe"));
        bg.p.set(Constants.WIDTH / 2f, Constants.HEIGHT + bg.s.y / 2f);
        bg.d.set(Constants.WIDTH / 2f, Constants.HEIGHT / 2f);
        bg.setVectorFromDist(Restaurant.TILE_SIZE * 20);

        foodIcons = new ImageEntity[scoreData.map.size()];
        foodCounts = new int[foodIcons.length];
        scoreCounts = new int[foodIcons.length];

        int i = 0;
        for (Map.Entry<Food.Type, Integer> entry : scoreData.map.entrySet()) {
            foodIcons[i] = new ImageEntity(global.getImage(entry.getKey().key));
            foodCounts[i] = entry.getValue();
            scoreCounts[i] = entry.getKey().score * entry.getValue();
            i++;
        }
        bonusCount = scoreData.bonusCount;
        bonusScore = bonusCount * 15;

        for (i = 0; i < scoreCounts.length; i++) {
            totalScore += scoreCounts[i];
        }
        totalScore += bonusScore;

        font = global.i28Font();
        pixel = global.getImage("pixel");

        refreshButton = new IconButton(global, global.getImage("refreshicon"), this::play);

    }

    private void play() {
        ignoreInput = true;
        global.screenManager.push(new CheckeredTransitionScreen(global, new PlayScreen(global, 0), 2));
    }

    @Override
    protected void handleInput(float dt) {
        if (ignoreInput) return;

        unproject();
        if (Gdx.input.justTouched()) {
            if (refreshButton.contains(m.x, m.y)) {
                refreshButton.setDown(true);
                global.audioHandler.playSound("clickdown", 0.5f);
            }
        }
        if (!Gdx.input.isTouched()) {
            if (refreshButton.isDown() && refreshButton.contains(m.x, m.y)) {
                refreshButton.click();
                global.audioHandler.playSound("clickup", 0.5f);
            }
            refreshButton.setDown(false);
        }
    }

    @Override
    public void update(float dt) {
        handleInput(dt);
        bg.update(dt);
        for (int i = 0; i < foodIcons.length; i++) {
            foodIcons[i].p.set(bg.p.x - Constants.WIDTH * .0416f, bg.p.y + Constants.HEIGHT * 0.2222f - i * Restaurant.TILE_SIZE);
        }
        refreshButton.p.set(bg.p.x, bg.bottom());
        refreshButton.update(dt);


    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();

        sb.setProjectionMatrix(viewport.getCamera().combined);
        sb.setColor(GameColor.WHITE);
        bg.render(sb);

        font.setColor(GameColor.OUTLINE);
        for (int i = 0; i < foodIcons.length; i++) {
            foodIcons[i].render(sb);
            font.draw(sb, "x" + foodCounts[i], foodIcons[i].p.x + Constants.WIDTH * .0213f, foodIcons[i].p.y + Constants.HEIGHT * .0148f);
            font.draw(sb, scoreCounts[i] + "", bg.p.x + Constants.WIDTH * .0417f, foodIcons[i].p.y + Constants.HEIGHT * .0148f);
        }

        float y = foodIcons.length == 0 ? bg.p.y : foodIcons[foodIcons.length - 1].p.y;
        font.draw(sb, "Bonus", bg.p.x - Constants.WIDTH * .0833f, y - Constants.HEIGHT * .0556f);
        font.draw(sb, "x" + bonusCount, bg.p.x - Constants.WIDTH * .0212f, y - Constants.HEIGHT * .0556f);
        font.draw(sb, bonusScore + "", bg.p.x + Constants.WIDTH * .0417f, y - Constants.HEIGHT * .0556f);
        font.draw(sb, "Total Score", bg.p.x - Constants.WIDTH * .0938f, y - Constants.HEIGHT * .148f);
        font.draw(sb, totalScore + "", bg.p.x + Constants.WIDTH * .0417f, y - Constants.HEIGHT * .148f);
        sb.setColor(GameColor.BLACK);
        sb.draw(pixel, bg.p.x - Constants.WIDTH * .1354f, y - Constants.HEIGHT * .1222f, Constants.WIDTH * .2708f, 4);
        sb.setColor(GameColor.WHITE);
        refreshButton.render(sb);

        sb.end();
    }
}

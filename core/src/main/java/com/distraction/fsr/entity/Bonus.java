package com.distraction.fsr.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.distraction.fsr.Global;
import com.distraction.fsr.tilemap.Restaurant;
import com.distraction.fsr.utils.Constants;
import com.distraction.fsr.utils.GameColor;

import java.util.ArrayList;
import java.util.List;

public class Bonus extends Entity {

    public interface BonusFinishListener {
        void onFinish(Food.Type[] list);
    }

    private final Global global;
    private final BonusFinishListener listener;
    private final List<BonusSet> bonusList;

    private final TextureRegion pixel;
    private final TextureRegion bonusHeader;
    private BonusSet removingSet = null;

    public Bonus(Global global, BonusFinishListener listener, float x, float y) {
        this.global = global;
        this.listener = listener;
        bonusList = new ArrayList<>();
        p.set(x, y);
        while (bonusList.size() < 50) {
            addBonusSet();
        }
        pixel = global.getImage("pixel");
        bonusHeader = global.getImage("bonusheader");
    }

    private void addBonusSet() {
        BonusSet bonusSet = new BonusSet(global, this::finish);
        bonusSet.p.set(p.x + Constants.WIDTH * .1489f * bonusList.size(), p.y);
        bonusSet.d.set(bonusSet.p);
        bonusList.add(bonusSet);
    }

    public void ate(Food.Type foodType) {
        bonusList.get(0).checkFood(foodType);
    }

    public void setFood(Food.Type foodType) {
        for (int i = 0; i < bonusList.size(); i++) {
            BonusSet bonusSet = bonusList.get(i);
            if (bonusSet.full()) continue;
            bonusSet.setFood(foodType);
            break;
        }
    }

    private void finish(Food.Type[] list) {
        listener.onFinish(list);
        removingSet = bonusList.remove(0);
        removingSet.startRemove();
        for (int i = 0; i < bonusList.size(); i++) {
            BonusSet bonusSet = bonusList.get(i);
            bonusSet.d.x = p.x + Constants.WIDTH * .1489f * i;
            bonusSet.setVectorFromDist(Restaurant.TILE_SIZE * 15);
        }
    }

    @Override
    public void update(float dt) {
        for (BonusSet bonusSet : bonusList) bonusSet.update(dt);
        if (removingSet != null) removingSet.update(dt);
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(GameColor.WHITE);
        BonusSet bs = bonusList.get(0);
        sb.draw(bonusHeader, left() - bs.swidth() / 2f, top() + bs.sheight() / 2f);
        for (BonusSet bonusSet : bonusList) {
            sb.setColor(GameColor.WHITE);
            bonusSet.render(sb);
            sb.setColor(GameColor.OUTLINE);
            sb.draw(pixel, bonusSet.right() + Constants.WIDTH * .0106f, bonusSet.bottom(), 4, Constants.HEIGHT * .0778f);
        }
        if (removingSet != null) removingSet.render(sb);
    }
}

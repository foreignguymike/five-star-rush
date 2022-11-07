package com.distraction.fsr.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.distraction.fsr.Global;
import com.distraction.fsr.tilemap.Restaurant;
import com.distraction.fsr.utils.Constants;
import com.distraction.fsr.utils.GameColor;
import com.distraction.fsr.utils.Utils;

public class BonusSet extends Entity {

    public interface BonusListener {
        void onFinish(Food.Type[] list);
    }

    private final Global global;
    private final BonusListener listener;
    private final Food.Type[] list;
    private final TextureRegion[] listIcons;

    private final TextureRegion bg;
    private final TextureRegion bgSelected;

    private int count = 0;
    private float alpha = 1;

    public BonusSet(Global global, BonusListener listener) {
        this.global = global;
        this.listener = listener;

        bg = global.getImage("bonusbg");
        bgSelected = global.getImage("bonusselectedbg");

        list = new Food.Type[3];
        listIcons = new TextureRegion[3];
        setSize(Restaurant.TILE_SIZE * 3, Restaurant.TILE_SIZE);
    }

    public void setFood(Food.Type type) {
        if (list[0] == null) {
            list[0] = type;
            listIcons[0] = global.getImage(type.key);
        } else if (list[1] == null) {
            list[1] = type;
            listIcons[1] = global.getImage(type.key);
        } else if (list[2] == null) {
            list[2] = type;
            listIcons[2] = global.getImage(type.key);
        }
    }

    public boolean full() {
        return list[0] != null && list[1] != null && list[2] != null;
    }

    public void checkFood(Food.Type type) {
        if (list[count] == type) count++;
        else listener.onFinish(null);
        if (count == 3) listener.onFinish(list);
    }

    public void startRemove() {
        remove = true;
        d.x = p.x - Constants.WIDTH * .2128f;
        setVectorFromDist(Restaurant.TILE_SIZE * 15);
    }

    @Override
    public void update(float dt) {
        move(dt);
        if (remove) {
            alpha -= 4 * dt;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(GameColor.WHITE);
        Utils.setAlpha(sb, alpha);
        for (int i = 0; i < 3; i++) {
            sb.draw(i >= count ? bg : bgSelected, left() + Restaurant.TILE_SIZE * i, bottom());
            TextureRegion icon = listIcons[i];
            if (icon != null)
                sb.draw(icon, left() + Restaurant.TILE_SIZE * i + (Restaurant.TILE_SIZE - icon.getRegionWidth()) / 2f, bottom() + (Restaurant.TILE_SIZE - icon.getRegionHeight()) / 2f);
        }
        sb.setColor(GameColor.WHITE);
    }
}

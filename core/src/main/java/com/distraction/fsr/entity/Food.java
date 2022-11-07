package com.distraction.fsr.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.distraction.fsr.Global;
import com.distraction.fsr.tilemap.Restaurant;
import com.distraction.fsr.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class Food extends TileEntity {

    private static final float SPEED = Restaurant.TILE_SIZE * 2.5f;
    private static final float TABLE_OFFSET = Constants.WIDTH * .0180f;

    public enum Type {
        APPLE("apple", Restaurant.Type.FRUIT, 1, 5, 1),
        ORANGE("orange", Restaurant.Type.FRUIT, 2, 6, 1),
        BANANA("banana", Restaurant.Type.FRUIT, 3, 7, 1),
        GRAPES("grapes", Restaurant.Type.FRUIT, 4, 8, 1);

        public final String key;
        public final Restaurant.Type restaurantType;
        public final int index;
        public final int score;
        public final int weight;

        Type(String key, Restaurant.Type restaurantType, int index, int score, int weight) {
            this.key = key;
            this.restaurantType = restaurantType;
            this.index = index;
            this.score = score;
            this.weight = weight;
        }

        public static Type find(Restaurant.Type restaurantType, int index) {
            Type[] all = values();
            for (Type foodType: all) {
                if (foodType.restaurantType == restaurantType && foodType.index == index) return foodType;
            }
            return null;
        }

        public static Type random(Restaurant.Type restaurantType) {
            Type[] all = values();
            List<Type> types = new ArrayList<>();
            for (Type foodType : all) if (foodType.restaurantType == restaurantType) types.add(foodType);
            int totalWeight = 0;
            for (Type type : types) {
                totalWeight += type.weight;
            }
            int index = 0;
            for (float r = MathUtils.random() * totalWeight; index < types.size() - 1; index++) {
                r -= types.get(index).weight;
                if (r <= 0f) break;
            }
            return types.get(index);
        }
    }

    public final Type type;
    public Player player;
    private float totalDist;
    private float currDist;

    private final TextureRegion image;

    public Food(Global global, Restaurant restaurant, Type type) {
        super(restaurant);
        this.type = type;
        image = global.getImage(type.key);
        setSize(image);
        scale = 0f;
    }

    public void setDest(float destx, float desty) {
        d.set(destx, desty);
        float dx = d.x - p.x;
        float dy = d.y - p.y;
        totalDist = (float) Math.sqrt(dx * dx + dy * dy);
        v.set(SPEED * dx / totalDist, SPEED * dy / totalDist);
    }

    @Override
    public void update(float dt) {
        move(dt);
        scale += 3 * dt;
        if (scale > 1) scale = 1;
        if (totalDist > 0) {
            float dx = d.x - p.x;
            float dy = d.y - p.y;
            currDist = (float) Math.sqrt(dx * dx + dy * dy);
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        if (totalDist > 0) {
            sb.draw(image, sleft(), sbottom() + TABLE_OFFSET * currDist / totalDist + Restaurant.TILE_SIZE * MathUtils.sin(MathUtils.PI - (MathUtils.PI * currDist / totalDist)), swidth(), sheight());
        } else {
            sb.draw(image, sleft(), sbottom() + TABLE_OFFSET, swidth(), sheight());
        }
    }
}

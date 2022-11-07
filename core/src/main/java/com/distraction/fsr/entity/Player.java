package com.distraction.fsr.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.distraction.fsr.Global;
import com.distraction.fsr.tilemap.Restaurant;
import com.distraction.fsr.utils.Animation;
import com.distraction.fsr.utils.Direction;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Player extends TileEntity {

    public interface EatListener {
        void onEat(Food food);
    }

    private static final float SPEED = Restaurant.TILE_SIZE * 7.5f;

    private final Global global;
    private final EatListener eatListener;

    private final Map<String, Animation> animationMap;
    private String currentAnimationKey;
    private Animation currentAnimation;

    public Food food = null;

    public Player(Global global, Restaurant restaurant, EatListener eatListener) {
        super(restaurant);
        this.global = global;
        this.eatListener = eatListener;

        setSize(Restaurant.TILE_SIZE, Restaurant.TILE_SIZE);

        TextureRegion[][] playerSprites = global.getImage("player").split(Restaurant.TILE_SIZE, Restaurant.TILE_SIZE);

        animationMap = new HashMap<>();
        animationMap.put("down", new Animation(Arrays.copyOf(playerSprites[0], 2), 0.5f));
        animationMap.put("up", new Animation(Arrays.copyOf(playerSprites[1], 2), 0.5f));
        animationMap.put("right", new Animation(Arrays.copyOf(playerSprites[2], 2), 0.5f));
        animationMap.put("downwalk", new Animation(playerSprites[3], 0.1f));
        animationMap.put("upwalk", new Animation(playerSprites[4], 0.1f));
        animationMap.put("rightwalk", new Animation(playerSprites[5], 0.1f));
        animationMap.put("eating", new Animation(Arrays.copyOf(playerSprites[6], 3), 0.05f, Animation.Mode.ONCE));
        animationMap.put("finisheating", new Animation(Arrays.copyOf(playerSprites[7], 3), 0.05f, Animation.Mode.ONCE));


        setAnimation("down");
    }

    public void setPositionFromTile(int row, int col) {
        tp.set(row, col);
        p.x = Restaurant.getXFromCol(col);
        p.y = Restaurant.getYFromRow(row);
        d.set(p);
    }

    public void eat() {
        if (food != null || !atDestination()) return;
        int nr = tp.row;
        int nc = tp.col;
        if (direction == Direction.RIGHT) nc++;
        else if (direction == Direction.LEFT) nc--;
        else if (direction == Direction.UP) nr++;
        else if (direction == Direction.DOWN) nr--;

        Table table = restaurant.getTables(nr, nc);
        if (table == null) return;
        if (table.food != null) {
            food = table.food;
            table.food.setDest(p.x, p.y);
            table.food.player = this;
            global.audioHandler.playSound("foodjump", 0.3f);
        }
    }

    public void ate(Food food) {
        this.food = null;
        eatListener.onEat(food);
        setAnimation("finisheating");
    }

    public void moveToTile(int numRows, int numCols) {
        if (atDestination() && food == null && !Objects.equals(currentAnimationKey, "finisheating")) {

            if (numRows > 0) direction = Direction.UP;
            else if (numRows < 0) direction = Direction.DOWN;
            else if (numCols > 0) direction = Direction.RIGHT;
            else if (numCols < 0) direction = Direction.LEFT;

            if (!restaurant.isBlocked(tp.row + numRows, tp.col + numCols)) {
                if (numRows != 0) {
                    tp.row += numRows;
                    d.set(Restaurant.getXFromCol(tp.col), Restaurant.getYFromRow(tp.row));
                    if (d.y > p.y) v.y = SPEED;
                    else if (d.y < p.y) v.y = -SPEED;
                }

                if (numCols != 0) {
                    tp.col += numCols;
                    d.set(Restaurant.getXFromCol(tp.col), Restaurant.getYFromRow(tp.row));
                    if (d.x > p.x) v.x = SPEED;
                    else if (d.x < p.x) v.x = -SPEED;
                }
            }
        }
    }

    private void setAnimation(String key) {
        if (!Objects.equals(key, currentAnimationKey)) {
            currentAnimation = animationMap.get(key);
            currentAnimationKey = key;
            currentAnimation.replay();
        }
    }

    private void updateAnimation() {
        if (Objects.equals(currentAnimationKey, "finisheating") && currentAnimation.getPlayCount() == 0) {
            return;
        }
        String key = "";
        if (food != null) key = "eating";
        else {
            if (direction == Direction.UP) key = "up";
            if (direction == Direction.DOWN) key = "down";
            if (direction == Direction.LEFT || direction == Direction.RIGHT) key = "right";
            if (!atDestination()) key += "walk";
        }
        setAnimation(key);
    }

    @Override
    public void update(float dt) {
        move(dt);
        updateAnimation();
        currentAnimation.update(dt);
    }

    @Override
    public void render(SpriteBatch sb) {
        if (currentAnimationKey.contains("right")) {
            if (direction == Direction.RIGHT) {
                sb.draw(currentAnimation.get(), left(), bottom());
            } else {
                sb.draw(currentAnimation.get(), left() + Restaurant.TILE_SIZE, bottom(), -Restaurant.TILE_SIZE, Restaurant.TILE_SIZE);
            }
        } else {
            sb.draw(currentAnimation.get(), left(), bottom());
        }
        // workaround for food rendering order, don't do this at home
        if (food != null) {
            food.render(sb);
        }
    }
}

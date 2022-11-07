package com.distraction.fsr.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.distraction.fsr.Global;
import com.distraction.fsr.entity.Bonus;
import com.distraction.fsr.entity.CountdownImage;
import com.distraction.fsr.entity.Entity;
import com.distraction.fsr.entity.Food;
import com.distraction.fsr.entity.ImageEntity;
import com.distraction.fsr.entity.Player;
import com.distraction.fsr.entity.PopupText;
import com.distraction.fsr.entity.TeleportColumn;
import com.distraction.fsr.entity.TileSelect;
import com.distraction.fsr.tilemap.Restaurant;
import com.distraction.fsr.tilemap.TilePosition;
import com.distraction.fsr.utils.Constants;
import com.distraction.fsr.utils.GameColor;
import com.distraction.fsr.utils.ScoreData;
import com.distraction.fsr.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class PlayScreen extends Screen {

    public enum Stage {
        ADDING_FOOD,
        SELECTING_DROP_ZONE,
        DROPPING,
        COUNTDOWN,
        PLAYING,
        TIME_UP,
        IGNORE
    }

    private final int level;

    private Stage stage = Stage.ADDING_FOOD;

    private final Restaurant restaurant;
    private final Player player;

    private final BitmapFont font;
    private final BitmapFont font32;

    private float timer = 0f;
    private float gameTimer = 20;
    private int teleportCount = 1;

    private final TileSelect tileSelect;

    private final List<CountdownImage> countdownImages;

    private final Bonus bonus;

    private final List<Entity> popups;

    private final ScoreData scoreData;

    private final ImageEntity headerFrame;

    private float fade = 0f;

    private final ImageEntity instructions;

    public PlayScreen(Global global, int level) {
        super(global);
        this.level = level;

        restaurant = new Restaurant(global, Restaurant.Type.FRUIT, level);
        player = new Player(global, restaurant, this::handleFoodEaten);
        player.setPositionFromTile(restaurant.numRows, restaurant.numCols);

        restaurant.addPlayer(player);

        font = global.i28Font();
        font32 = global.i64Font();

        tileSelect = new TileSelect(global, restaurant);

        countdownImages = new ArrayList<>();
        countdownImages.add(new CountdownImage(global.getImage("cdgetready")));
        countdownImages.add(new CountdownImage(global.getImage("cd3")));
        countdownImages.add(new CountdownImage(global.getImage("cd2")));
        countdownImages.add(new CountdownImage(global.getImage("cd1")));
        countdownImages.add(new CountdownImage(global.getImage("cdrush")));
        countdownImages.add(new CountdownImage(global.getImage("cdtime")));
        for (CountdownImage countdownImage : countdownImages) {
            countdownImage.p.set(Constants.WIDTH / 2f, Constants.HEIGHT / 2f);
        }

        bonus = new Bonus(global, this::bonusFinish, Constants.WIDTH * .6809f, Constants.HEIGHT * .8852f);

        popups = new ArrayList<>();

        scoreData = new ScoreData();

        headerFrame = new ImageEntity(global.getImage("headerframe"));
        headerFrame.p.set(Constants.WIDTH / 2f, Constants.HEIGHT * .9074f);

        instructions = new ImageEntity(global.getImage("instructions"));
        instructions.p.set(Constants.WIDTH * 0.3617f, Constants.HEIGHT * .9074f);
    }

    private void clampViewportToRestaurant(float x, float y) {
        viewport.setPosition(
                MathUtils.clamp(x, Constants.WIDTH / 2f, restaurant.getWidth() - Constants.WIDTH / 2f),
                restaurant.getHeight() < Constants.HEIGHT ?
                        Constants.HEIGHT / 2f :
                        MathUtils.clamp(y, Constants.HEIGHT / 2f, Math.min(Constants.HEIGHT / 2f, restaurant.getHeight() - Constants.HEIGHT / 2f)));
    }

    private void handleFoodEaten(Food food) {
        bonus.ate(food.type);
        scoreData.addFood(food.type);
    }

    private void bonusFinish(Food.Type[] list) {
        if (list != null) {
            PopupText popupText = new PopupText(global, "FANTASTIC!");
            popupText.p.set(player.p.x - Constants.WIDTH * .0777f, player.p.y + Constants.HEIGHT * .0941f);
            popups.add(popupText);
            teleportCount++;
            scoreData.addBonus();
            if (teleportCount > 1) teleportCount = 1;
            global.audioHandler.playSound("bonus", 0.5f);
        } else {
            global.audioHandler.playSound("bonusfail", 0.5f);
        }
    }

    private void teleport(int row, int col) {
        TeleportColumn teleportColumn = new TeleportColumn(global);
        teleportColumn.p.set(player.p);
        popups.add(teleportColumn);
        player.setPositionFromTile(row, col);
        teleportCount--;
        teleportColumn = new TeleportColumn(global);
        teleportColumn.p.set(player.p);
        popups.add(teleportColumn);
    }

    @Override
    protected void handleInput(float dt) {
        if (ignoreInput) return;

        if (Gdx.input.isKeyJustPressed(Input.Keys.R))
            global.screenManager.push(new CheckeredTransitionScreen(global, new PlayScreen(global, level)));

        if (stage == Stage.ADDING_FOOD || stage == Stage.SELECTING_DROP_ZONE) {
            unproject();
            if (restaurant.contains(m.x, m.y)) {
                unproject(uiViewport);
                if (m.x < Constants.WIDTH / 3f) viewport.translate(-100 * dt, 0);
                else if (m.x > 2f * Constants.WIDTH / 3f) viewport.translate(100 * dt, 0);
                if (m.y < Constants.HEIGHT / 3f) viewport.translate(0, -100 * dt);
                else if (m.y > 2f * Constants.HEIGHT / 3f) viewport.translate(0, 100 * dt);
                clampViewportToRestaurant(viewport.getCamera().position.x, viewport.getCamera().position.y);
            }
        }

        if (stage == Stage.SELECTING_DROP_ZONE) return;

        if (stage == Stage.COUNTDOWN) return;

        if (stage == Stage.PLAYING) {
            unproject();
            if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
                int row = Restaurant.getRowFromY(m.y);
                int col = Restaurant.getColFromX(m.x);
                tileSelect.setPositionFromTile(row, col);
            } else {
                tileSelect.p.set(-100, -100);
            }
            if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
                if (teleportCount > 0 && !restaurant.isBlocked(tileSelect.tp.row, tileSelect.tp.col)) {
                    teleport(tileSelect.tp.row, tileSelect.tp.col);
                }
            }
            if (Utils.areKeysPressed(Input.Keys.LEFT, Input.Keys.A)) player.moveToTile(0, -1);
            else if (Utils.areKeysPressed(Input.Keys.RIGHT, Input.Keys.D)) player.moveToTile(0, 1);
            if (Utils.areKeysPressed(Input.Keys.UP, Input.Keys.W)) player.moveToTile(1, 0);
            else if (Utils.areKeysPressed(Input.Keys.DOWN, Input.Keys.S)) player.moveToTile(-1, 0);
            if (Utils.areKeysJustPressed(Input.Keys.SPACE) || Utils.lmbJustPressed()) player.eat();
        }

    }

    @Override
    public void update(float dt) {
        handleInput(dt);
        restaurant.update(dt);
        tileSelect.update(dt);

        if (stage == Stage.ADDING_FOOD) {
            timer += dt;
            if (timer > 0.03f) {
                timer = 0;
                boolean stillAdding = restaurant.initFood();
                stillAdding |= restaurant.initFood();
                stillAdding |= restaurant.initFood();
                if (!stillAdding) stage = Stage.SELECTING_DROP_ZONE;
                bonus.setFood(Food.Type.random(restaurant.type));
                bonus.setFood(Food.Type.random(restaurant.type));
                bonus.setFood(Food.Type.random(restaurant.type));
            }
        }
        if (stage == Stage.SELECTING_DROP_ZONE) {
            TilePosition tp = restaurant.randomEmpty();
            tileSelect.setPositionFromTile(tp.row, tp.col);
            player.setPositionFromTile(restaurant.numRows + 1, tileSelect.tp.col);
            player.d.set(Restaurant.getXFromCol(tileSelect.tp.col), Restaurant.getYFromRow(tileSelect.tp.row));
            player.setVectorFromDist(Restaurant.TILE_SIZE * 20);
            stage = Stage.DROPPING;
        }
        if (stage == Stage.DROPPING) {
            if (player.atDestination()) {
                player.updateTilePosition();
                stage = Stage.COUNTDOWN;
                timer = 0;
            }
        }
        if (stage == Stage.COUNTDOWN) {
            if (timer == 0) countdownImages.get(0).start();
            else if (timer < 1 && timer + dt > 1) countdownImages.get(1).start();
            else if (timer < 2 && timer + dt > 2) countdownImages.get(2).start();
            else if (timer < 3 && timer + dt > 3) countdownImages.get(3).start();
            else if (timer < 4 && timer + dt > 4) countdownImages.get(4).start();
            if (timer < 1.15f && timer + dt > 1.15f ||
                    timer < 2.15f && timer + dt > 2.15f ||
                    timer < 3.15f && timer + dt > 3.15f)
                global.audioHandler.playSound("countdownlow", 0.5f);
            else if (timer < 4.15f && timer + dt > 4.15f)
                global.audioHandler.playSound("countdownhigh", 0.5f);
            for (CountdownImage countdownImage : countdownImages) countdownImage.update(dt);
            timer += dt;
            if (timer >= 4.25f) {
                stage = Stage.PLAYING;
                timer = 0;
            }
        }
        if (stage == Stage.PLAYING) {
            for (CountdownImage countdownImage : countdownImages) countdownImage.update(dt);
            gameTimer -= dt;
            clampViewportToRestaurant(player.p.x, player.p.y);
            if (gameTimer <= 0) {
                stage = Stage.TIME_UP;
                global.audioHandler.playSound("countdownhigh", 0.5f);
                countdownImages.get(5).start();
                gameTimer = 0f;
            }
        }
        if (stage == Stage.TIME_UP) {
            for (CountdownImage countdownImage : countdownImages) countdownImage.update(dt);
            timer += dt;
            if (timer >= 1) {
                global.screenManager.push(new ScoreScreen(global, scoreData));
                global.screenManager.depth++;
                stage = Stage.IGNORE;
                ignoreInput = true;
            }
        }
        if (stage == Stage.IGNORE) {
            fade += dt;
            if (fade > 0.5f) fade = 0.5f;
        }
        bonus.update(dt);
        for (int i = popups.size() - 1; i >= 0; i--) {
            Entity entity = popups.get(i);
            entity.update(dt);
            if (entity.remove) popups.remove(i);
        }

    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();

        sb.setProjectionMatrix(uiViewport.getCamera().combined);
        sb.setColor(GameColor.DARK_RED);
        sb.draw(pixel, 0, Constants.HEIGHT - 50, Constants.WIDTH, 50);
        sb.setColor(GameColor.WHITE);
        headerFrame.render(sb);
        instructions.render(sb);

        sb.setProjectionMatrix(viewport.getCamera().combined);
        sb.setColor(GameColor.WHITE);
        restaurant.render(sb);
        if (stage == Stage.SELECTING_DROP_ZONE || stage == Stage.PLAYING) tileSelect.render(sb);
        if (stage == Stage.DROPPING) player.render(sb);
        for (Entity entity : popups) entity.render(sb);

        sb.setProjectionMatrix(uiViewport.getCamera().combined);
        font32.setColor(GameColor.OUTLINE);
        font32.draw(sb, "" + Utils.roundTo(gameTimer, 10), Constants.WIDTH * .0676f, Constants.HEIGHT * .932f);
        sb.setColor(GameColor.WHITE);
        bonus.render(sb);
        for (CountdownImage countdownImage : countdownImages) countdownImage.render(sb);

        if (stage == Stage.IGNORE) {
            sb.setColor(GameColor.BLACK);
            Utils.setAlpha(sb, fade);
            sb.draw(pixel, 0, 0, Constants.WIDTH, Constants.HEIGHT);
        }

        sb.end();
    }
}

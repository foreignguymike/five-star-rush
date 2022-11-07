package com.distraction.fsr;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.distraction.fsr.screen.ScreenManager;
import com.distraction.fsr.tilemap.data.LevelData;
import com.distraction.fsr.utils.AudioHandler;

public class Global {

   private static final String ATLAS_NAME = "fsr.atlas";
   private static final String I28_NAME = "fonts/impact28.fnt";
   private static final String I64_NAME = "fonts/impact64.fnt";

   public final AssetManager assetManager;
   public final ScreenManager screenManager;
   public final LevelData levelData;
   public final AudioHandler audioHandler;

   public Global() {
      assetManager = new AssetManager();
      assetManager.load(ATLAS_NAME, TextureAtlas.class);
      assetManager.load(I28_NAME, BitmapFont.class);
      assetManager.load(I64_NAME, BitmapFont.class);
      assetManager.finishLoading();

      screenManager = new ScreenManager();
      levelData = new LevelData();
      audioHandler = new AudioHandler();
   }

   public TextureRegion getImage(String key) {
      return assetManager.get(ATLAS_NAME, TextureAtlas.class).findRegion(key);
   }

   public TextureRegion getImage(String key, int index) {
      return assetManager.get(ATLAS_NAME, TextureAtlas.class).findRegion(key, index);
   }

   public BitmapFont i28Font() {
      return assetManager.get(I28_NAME, BitmapFont.class);
   }

   public BitmapFont i64Font() {
      return assetManager.get(I64_NAME, BitmapFont.class);
   }

   public void dispose() {
      assetManager.dispose();
   }

}


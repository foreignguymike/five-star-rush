package com.distraction.fsr;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.distraction.fsr.screen.PlayScreen;
import com.distraction.fsr.utils.Utils;

public class FiveStarRush extends ApplicationAdapter {
	private SpriteBatch sb;
	private Global global;

	@Override
	public void create() {
		sb = new SpriteBatch();
		global = new Global();

		global.screenManager.push(new PlayScreen(global, 0));
	}

	@Override
	public void resize(int width, int height) {
		global.screenManager.resize(width, height);
	}

	@Override
	public void render() {
		if (Gdx.input.isKeyJustPressed(Input.Keys.F8)) Utils.takeScreenshot();

		Gdx.gl.glClearColor(0, 0, 0, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		global.screenManager.update(Gdx.graphics.getDeltaTime());
		global.screenManager.render(sb);
	}

	@Override
	public void dispose() {
		sb.dispose();
		global.dispose();
	}
}
package com.thronewars.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.thronewars.GdxGame;

public class MainMenuScreen implements Screen {

	private final Viewport viewport = new ScreenViewport();
	private final GlyphLayout layout = new GlyphLayout();
	
	private final GdxGame game;
	private final Batch batch;
	private final BitmapFont font;
	
	public MainMenuScreen(GdxGame game) {
		this.game = game;
		this.batch = game.getBatch();
		this.font = game.getFont();
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
		input();
		update(delta);
		draw();
	}

	private void input() {
		if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			game.setScreen(new GameScreen(game));
			dispose();
			return;
		}
	}

	private void update(float delta) {

	}

	private void draw() {
		ScreenUtils.clear(Color.BLACK);
		viewport.apply();
		batch.setProjectionMatrix(viewport.getCamera().combined);
		batch.begin();
		
		float centerX = (viewport.getWorldWidth() / 2) - (layout.width / 2);
		float centerY = (viewport.getWorldHeight() / 2) + (layout.height / 2);
		
		layout.setText(font, "Throne Wars");
		font.draw(batch, layout, centerX, centerY);
		
		batch.end();
	}
	
	@Override
	public void resize(int width, int height) {
		viewport.update(width, height, true);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}

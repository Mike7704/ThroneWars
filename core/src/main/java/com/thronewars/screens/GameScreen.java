package com.thronewars.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.thronewars.GdxGame;

/**
 * Game screen for the game.
 */
public class GameScreen implements Screen {

	private static final float WORLD_WIDTH = 16f;
	private static final float WORLD_HEIGHT = 9f;
	
	private final Viewport gameViewport = new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT);
	
	private final GdxGame game;
	private final Batch batch;
	
	private final Texture backgroundTexture = new Texture(Gdx.files.internal("textures/environment/winter/3.png"));
	
	public GameScreen(GdxGame game) {
		this.game = game;
		this.batch = game.getBatch();
		
		backgroundTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.ClampToEdge);
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
			game.setScreen(new MainMenuScreen(game));
			dispose();
			return;
		}
	}

	private void update(float delta) {

	}

	private void draw() {
		ScreenUtils.clear(Color.BLACK);
		gameViewport.apply();
		batch.setProjectionMatrix(gameViewport.getCamera().combined);
		batch.begin();

		drawBackground();
		
		batch.end();
	}
	
	/**
	 * Draws the background texture to fill the entire viewport.
	 */
	private void drawBackground() {
		float worldWidth = gameViewport.getWorldWidth();
	    float worldHeight = gameViewport.getWorldHeight();
	    // Calculate the horizontal texture coordinate based on the world and texture width
		float u2 = worldWidth / WORLD_WIDTH;
		
		batch.draw(
		    backgroundTexture,
		    0, 0,
		    worldWidth, worldHeight,
		    0, 1,
		    u2, 0
		);
	}
	
	@Override
	public void resize(int width, int height) {
		gameViewport.update(width, height, true);
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
		backgroundTexture.dispose();		
	}

}

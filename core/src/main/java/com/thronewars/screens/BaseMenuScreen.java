package com.thronewars.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.thronewars.GdxGame;
import com.thronewars.managers.ScreenManager;
import com.thronewars.managers.UIManager;

/**
 * Abstract base class for menu screens in the game. Provides common functionality for creating and managing menu UI elements.
 */
public abstract class BaseMenuScreen extends ScreenAdapter {

	protected final GdxGame game;
	protected final Stage stage;
	protected final UIManager uiManager;

	protected BaseMenuScreen(GdxGame game) {
		this.game = game;
		this.uiManager = game.getUIManager();

		FitViewport viewport = new FitViewport(ScreenManager.VIEW_WIDTH, ScreenManager.VIEW_HEIGHT);

		stage = new Stage(viewport, game.getBatch());
		
		//stage.setDebugAll(true);
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void render(float delta) {
		ScreenUtils.clear(Color.BLACK);

		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void hide() {
		if (Gdx.input.getInputProcessor() == stage) {
			Gdx.input.setInputProcessor(null);
		}
	}

	@Override
	public void dispose() {
		stage.dispose();
	}
}

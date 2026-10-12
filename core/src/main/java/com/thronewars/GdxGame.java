package com.thronewars;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.thronewars.assets.GameAssets;
import com.thronewars.managers.ScreenManager;
import com.thronewars.managers.UIManager;
import com.thronewars.screens.MainMenuScreen;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class GdxGame extends Game {

	private SpriteBatch batch;
	private GameAssets gameAssets;

	private UIManager uiManager;
	private ScreenManager screenManager;

	@Override
	public void create() {
		batch = new SpriteBatch();

		gameAssets = new GameAssets();
		gameAssets.load();
		
		uiManager = new UIManager(gameAssets);
		screenManager = new ScreenManager(this);

		screenManager.showScreen(new MainMenuScreen(this));
	}

	public SpriteBatch getBatch() {
		return batch;
	}

	public GameAssets getGameAssets() {
		return gameAssets;
	}

	public UIManager getUIManager() {
		return uiManager;
	}

	public ScreenManager getScreenManager() {
		return screenManager;
	}

	@Override
	public void dispose() {
		// Dispose the active screen
		super.dispose();

		if (gameAssets != null) {
			gameAssets.dispose();
		}

		if (batch != null) {
			batch.dispose();
		}
	}
}

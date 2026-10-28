package com.thronewars;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.thronewars.assets.GameAssets;
import com.thronewars.managers.ScreenManager;
import com.thronewars.managers.ScreenType;
import com.thronewars.managers.SettingsManager;
import com.thronewars.managers.UIManager;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class GdxGame extends Game {

	private SpriteBatch batch;
	private GameAssets gameAssets;

	private SettingsManager settingsManager;
	private UIManager uiManager;
	private ScreenManager screenManager;
	
	@Override
	public void create() {
		batch = new SpriteBatch();

		gameAssets = new GameAssets();
		gameAssets.load();
		
		settingsManager = new SettingsManager();
		uiManager = new UIManager(gameAssets);
		screenManager = new ScreenManager(this);

		screenManager.showScreen(ScreenType.MAIN_MENU);
	}

	@Override
	public void render() {
	    if (Gdx.input.isKeyJustPressed(Input.Keys.F11)) {
	        toggleFullscreen();
	    }

	    super.render();
	}

	public void toggleFullscreen() {
	    if (Gdx.graphics.isFullscreen()) {
	    	settingsManager.setFullscreenEnabled(false);
	        Gdx.graphics.setWindowedMode(1280, 720);
	    } else {
	    	settingsManager.setFullscreenEnabled(true);
	        Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
	    }
	}
	
	public SpriteBatch getBatch() {
		return batch;
	}

	public GameAssets getGameAssets() {
		return gameAssets;
	}

	public SettingsManager getSettingsManager() {
	    return settingsManager;
	}
	
	public UIManager getUIManager() {
		return uiManager;
	}

	public ScreenManager getScreenManager() {
		return screenManager;
	}

	@Override
	public void dispose() {
		if (screenManager != null) {
			screenManager.dispose();
		}
		
		super.dispose();
		
		if (gameAssets != null) {
			gameAssets.dispose();
		}

		if (batch != null) {
			batch.dispose();
		}
	}
}

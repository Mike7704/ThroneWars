package com.thronewars.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.thronewars.GdxGame;
import com.thronewars.managers.ScreenManager;
import com.thronewars.managers.UIManager;
import com.thronewars.ui.components.TWButton;
import com.thronewars.ui.components.TWLabel;

/**
 * Screen to display the main menu of the game.
 */
public class MainMenuScreen extends ScreenAdapter {

	private final GdxGame game;
	private final Stage stage;

	public MainMenuScreen(GdxGame game) {
		this.game = game;

		FitViewport uiViewport = new FitViewport(ScreenManager.GAME_WIDTH, ScreenManager.GAME_HEIGHT);

		stage = new Stage(uiViewport, game.getBatch());

		createMainMenu();
	}

	/**
	 * Creates the main menu UI elements and adds them to the stage.
	 */
	private void createMainMenu() {
		UIManager uiManager = game.getUIManager();
		
		Table table = new Table();
		table.setFillParent(true);
		table.center();

		TWLabel title = new TWLabel("THRONE WARS", uiManager.getLabelLargeStyle());

		table.add(title).padBottom(50f);
		table.row();

		float buttonWidth = 360f;
		float buttonHeight = 80f;
		float buttonPadding = 12f;
		
		TWButton playButton = new TWButton("Play", uiManager.getButtonStyle());

		TWButton settingsButton = new TWButton("Settings", uiManager.getButtonStyle());

		TWButton exitButton = new TWButton("Exit", uiManager.getButtonStyle());

		playButton.setOnClick(() -> game.getScreenManager().showScreen(new GameScreen(game)));

		settingsButton.setOnClick(() -> System.out.println("Settings Button"));

		exitButton.setOnClick(() -> Gdx.app.exit());

		table.add(playButton).width(buttonWidth).height(buttonHeight).pad(buttonPadding);
		table.row();

		table.add(settingsButton).width(buttonWidth).height(buttonHeight).pad(buttonPadding);
		table.row();

		table.add(exitButton).width(buttonWidth).height(buttonHeight).pad(buttonPadding);

		stage.addActor(table);
		
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
		// Do not clear another screen's input processor.
		if (Gdx.input.getInputProcessor() == stage) {
			Gdx.input.setInputProcessor(null);
		}
	}

	@Override
	public void dispose() {
		// Disposes the Stage, not the shared game batch.
		stage.dispose();
	}
}

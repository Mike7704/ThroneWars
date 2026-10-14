package com.thronewars.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.thronewars.GdxGame;
import com.thronewars.managers.ScreenType;
import com.thronewars.ui.components.TWButton;
import com.thronewars.ui.components.TWLabel;

/**
 * Screen to display the main menu of the game.
 */
public class MainMenuScreen extends BaseMenuScreen {

	public MainMenuScreen(GdxGame game) {
		super(game);
		createMainMenu();
	}

	/**
	 * Creates the main menu UI elements and adds them to the stage.
	 */
	private void createMainMenu() {		
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

		playButton.setOnClick(() -> game.getScreenManager().showScreen(ScreenType.GAME));

		settingsButton.setOnClick(() -> game.getScreenManager().showScreen(ScreenType.SETTINGS_MENU));

		exitButton.setOnClick(() -> Gdx.app.exit());

		table.add(playButton).width(buttonWidth).height(buttonHeight).pad(buttonPadding);
		table.row();

		table.add(settingsButton).width(buttonWidth).height(buttonHeight).pad(buttonPadding);
		table.row();

		table.add(exitButton).width(buttonWidth).height(buttonHeight).pad(buttonPadding);

		stage.addActor(table);
	}
}

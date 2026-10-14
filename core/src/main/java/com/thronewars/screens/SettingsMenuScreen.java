package com.thronewars.screens;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.thronewars.GdxGame;
import com.thronewars.managers.ScreenType;
import com.thronewars.ui.components.TWButton;
import com.thronewars.ui.components.TWLabel;

/**
 * Screen to display the settings menu of the game.
 */
public class SettingsMenuScreen extends BaseMenuScreen  {

	public SettingsMenuScreen(GdxGame game) {
		 super(game);
	     createSettingsMenu();
	}

	/**
	 * Creates the settings menu UI elements and adds them to the stage.
	 */
	private void createSettingsMenu() {
		Table table = new Table();
		table.setFillParent(true);
		table.center();

		TWLabel title = new TWLabel("SETTINGS", uiManager.getLabelLargeStyle());

		table.add(title).padBottom(50f);
		table.row();

		float buttonWidth = 360f;
		float buttonHeight = 80f;
		float buttonPadding = 12f;

		TWButton backButton = new TWButton("Back", uiManager.getButtonStyle());

		backButton.setOnClick(() -> game.getScreenManager().showScreen(ScreenType.MAIN_MENU));

		table.add(backButton).width(buttonWidth).height(buttonHeight).pad(buttonPadding);

		stage.addActor(table);
	}
}

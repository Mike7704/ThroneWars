package com.thronewars.screens;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.thronewars.GdxGame;
import com.thronewars.managers.ScreenType;
import com.thronewars.managers.SettingsManager;
import com.thronewars.ui.components.TWButton;
import com.thronewars.ui.components.TWLabel;

/**
 * Screen to display the settings menu of the game.
 */
public class SettingsMenuScreen extends BaseMenuScreen  {

	private final SettingsManager settings;
	
	public SettingsMenuScreen(GdxGame game) {
		 super(game);
		 settings = game.getSettingsManager();
		 
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

		float buttonWidth = 720f;
		float buttonHeight = 80f;
		float buttonPadding = 12f;

		TWButton fullscreenButton = new TWButton(getFullscreenButtonText(), uiManager.getButtonStyle());
		
		TWButton shadowsButton = new TWButton(getShadowsButtonText(), uiManager.getButtonStyle());
		
		TWButton backButton = new TWButton("Back", uiManager.getButtonStyle());

		fullscreenButton.setOnClick(() -> {
			game.toggleFullscreen();
			fullscreenButton.setText(getFullscreenButtonText());
		});
		
		shadowsButton.setOnClick(() -> {
			settings.setEntityShadowsEnabled(!settings.areEntityShadowsEnabled());
			shadowsButton.setText(getShadowsButtonText());
		});
		
		backButton.setOnClick(() -> game.getScreenManager().showScreen(ScreenType.MAIN_MENU));

		table.add(fullscreenButton).width(buttonWidth).height(buttonHeight).pad(buttonPadding);
		table.row();
		
		table.add(shadowsButton).width(buttonWidth).height(buttonHeight).pad(buttonPadding);
		table.row();
		
		table.add(backButton).width(buttonWidth).height(buttonHeight).pad(buttonPadding);

		stage.addActor(table);
	}
	
	private String getFullscreenButtonText() {
		return "Fullscreen: " + (settings.isFullscreenEnabled() ? "ON" : "OFF");
	}
	
	private String getShadowsButtonText() {
		return "Entity Shadows: " + (settings.areEntityShadowsEnabled() ? "ON" : "OFF");
	}
}

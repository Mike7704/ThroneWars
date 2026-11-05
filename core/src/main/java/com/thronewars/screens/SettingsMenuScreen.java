package com.thronewars.screens;

import java.util.function.Consumer;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.thronewars.GdxGame;
import com.thronewars.managers.ScreenType;
import com.thronewars.managers.SettingsManager;
import com.thronewars.ui.components.TWButton;
import com.thronewars.ui.components.TWLabel;

/**
 * Screen to display the settings menu of the game.
 */
public class SettingsMenuScreen extends BaseMenuScreen {

	private static final float BUTTON_WIDTH = 720f;
	private static final float BUTTON_HEIGHT = 80f;
	private static final float BUTTON_PADDING = 12f;

	private final SettingsManager settings;

	public SettingsMenuScreen(GdxGame game) {
		super(game);
		settings = game.getSettingsManager();

		createSettingsMenu();
	}

	/**
	 * Creates the settings menu UI and adds it to the stage.
	 */
	private void createSettingsMenu() {
		Table table = new Table();
		table.setFillParent(true);
		table.center();

		TWLabel title = new TWLabel("SETTINGS", uiManager.getLabelLargeStyle());

		table.add(title).padBottom(50f);
		table.row();

		createDisplaySettings(table);
		createAudioSettings(table);

		table.add(createBackButton()).width(BUTTON_WIDTH).height(BUTTON_HEIGHT).pad(BUTTON_PADDING);

		stage.addActor(table);
	}

	/**
	 * Creates the display settings.
	 */
	private void createDisplaySettings(Table table) {
		TWButton fullscreenButton = new TWButton(getFullscreenButtonText(), uiManager.getButtonStyle());

		fullscreenButton.setOnClick(() -> {
			game.toggleFullscreen();
			fullscreenButton.setText(getFullscreenButtonText());
		});

		TWButton shadowsButton = new TWButton(getShadowsButtonText(), uiManager.getButtonStyle());

		shadowsButton.setOnClick(() -> {
			settings.setEntityShadowsEnabled(!settings.areEntityShadowsEnabled());
			shadowsButton.setText(getShadowsButtonText());
		});

		addButton(table, fullscreenButton);
		addButton(table, shadowsButton);
	}

	/**
	 * Creates the audio settings.
	 */
	private void createAudioSettings(Table table) {
		Slider musicSlider = createSlider(settings.getMusicVolume());
		Slider soundEffectsSlider = createSlider(settings.getSoundEffectsVolume());

		addSliderListeners(musicSlider, game.getAudioManager()::setMusicVolume, game.getAudioManager()::saveMusicVolume);

		addSliderListeners(soundEffectsSlider, game.getAudioManager()::setSoundEffectsVolume, game.getAudioManager()::saveSoundEffectsVolume);

		addSliderOption(table, "Music Volume:", musicSlider);
		addSliderOption(table, "Sound Effects:", soundEffectsSlider);
	}

	/**
	 * Creates a volume slider with the specified initial value.
	 */
	private Slider createSlider(float initialValue) {
		Slider slider = new Slider(0f, 1f, 0.01f, false, uiManager.getSliderStyle());
		slider.setValue(initialValue);
		return slider;
	}

	/**
	 * Adds listeners for updating and saving a slider's value.
	 */
	private void addSliderListeners(Slider slider, Consumer<Float> update, Runnable save) {

		slider.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				update.accept(slider.getValue());
			}
		});

		slider.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				save.run();
			}
		});
	}

	/**
	 * Adds a button to the settings table.
	 */
	private void addButton(Table table, TWButton button) {
		table.add(button).width(BUTTON_WIDTH).height(BUTTON_HEIGHT).pad(BUTTON_PADDING);
		table.row();
	}

	/**
	 * Adds a labelled slider to the settings table.
	 */
	private void addSliderOption(Table table, String text, Slider slider) {
		table.add(createSliderOption(text, slider)).width(BUTTON_WIDTH).height(BUTTON_HEIGHT).pad(BUTTON_PADDING);
		table.row();
	}

	/**
	 * Creates a labelled slider row using the button background.
	 */
	private Table createSliderOption(String text, Slider slider) {
		Table row = new Table();

		row.setBackground(uiManager.getButtonStyle().up);

		TWLabel label = new TWLabel(text, uiManager.getLabelMediumStyle());

		row.add(label).right().expandX().padRight(24f);
		row.add(slider).width(300f).height(30f).padRight(48f);

		row.addListener(new InputListener() {
			@Override
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				if (pointer == -1) {
					row.setBackground(uiManager.getButtonStyle().over);
				}
			}

			@Override
			public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
				if (pointer == -1) {
					row.setBackground(uiManager.getButtonStyle().up);
				}
			}
		});

		return row;
	}

	/**
	 * Creates the button used to return to the main menu.
	 */
	private TWButton createBackButton() {
		TWButton backButton = new TWButton("Back", uiManager.getButtonStyle());

		backButton.setOnClick(() -> game.getScreenManager().showScreen(ScreenType.MAIN_MENU));

		return backButton;
	}

	private String getFullscreenButtonText() {
		return "Fullscreen: " + (settings.isFullscreenEnabled() ? "ON" : "OFF");
	}

	private String getShadowsButtonText() {
		return "Entity Shadows: " + (settings.areEntityShadowsEnabled() ? "ON" : "OFF");
	}
}

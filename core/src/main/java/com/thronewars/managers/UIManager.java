package com.thronewars.managers;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.thronewars.assets.GameAssets;

/**
 * Manages the UI styles and assets for the game.
 */
public class UIManager {

	private final TextureAtlas atlas;
	private final BitmapFont font;

	private final TextButtonStyle buttonStyle;
	private final LabelStyle labelStyle;

	public UIManager(GameAssets assets) {
		atlas = assets.getUIAtlas();
		font = assets.getUIFont();

		// Buttons
		buttonStyle = new TextButtonStyle();
		buttonStyle.up = new TextureRegionDrawable(atlas.findRegion("button"));
		buttonStyle.over = new TextureRegionDrawable(atlas.findRegion("button-down"));
		buttonStyle.down = new TextureRegionDrawable(atlas.findRegion("button-down"));
		buttonStyle.font = font;

		// Labels
		labelStyle = new LabelStyle();
		labelStyle.font = font;
	}

	public TextButtonStyle getButtonStyle() {
		return buttonStyle;
	}

	public Label.LabelStyle getLabelStyle() {
		return labelStyle;
	}
}

package com.thronewars.managers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.thronewars.assets.GameAssets;

/**
 * Manages the UI styles and assets for the game.
 */
public class UIManager {

	private final TextureAtlas atlas;

	private final TextButtonStyle buttonStyle;
	
	private final LabelStyle labelLargeStyle;
	private final LabelStyle labelMediumStyle;
	private final LabelStyle labelSmallStyle;
	
	private final SliderStyle sliderStyle;

	public UIManager(GameAssets assets) {
		atlas = assets.getUIAtlas();

		// Buttons
		buttonStyle = new TextButtonStyle();
		buttonStyle.up = new TextureRegionDrawable(atlas.findRegion("button"));
		buttonStyle.over = new TextureRegionDrawable(atlas.findRegion("button-down"));
		buttonStyle.down = new TextureRegionDrawable(atlas.findRegion("button-down"));
		buttonStyle.font = assets.getMediumFont();

		// Labels
		labelLargeStyle = new LabelStyle();
		labelLargeStyle.font = assets.getLargeFont();

		labelMediumStyle = new LabelStyle();
		labelMediumStyle.font = assets.getMediumFont();
		
		labelSmallStyle = new LabelStyle();
		labelSmallStyle.font = assets.getSmallFont();
		
		// Sliders
		sliderStyle = new SliderStyle();
		sliderStyle.background = new TextureRegionDrawable(atlas.findRegion("button"));
		sliderStyle.knob = new TextureRegionDrawable(atlas.findRegion("button-down")); // Temp
	}

	public TextButtonStyle getButtonStyle() {
		return buttonStyle;
	}

	public LabelStyle getLabelLargeStyle() {
		return labelLargeStyle;
	}
	
	public LabelStyle getLabelMediumStyle() {
		return labelMediumStyle;
	}
	
	public LabelStyle getLabelSmallStyle() {
		return labelSmallStyle;
	}
	
	public SliderStyle getSliderStyle() {
		return sliderStyle;
	}
}

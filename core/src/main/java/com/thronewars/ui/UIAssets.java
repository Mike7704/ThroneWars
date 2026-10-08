package com.thronewars.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

/**
 * UIAssets class for managing UI assets.
 */
public class UIAssets {

	private final TextureAtlas atlas;
	private BitmapFont font;
	
	public UIAssets() {
		atlas = new TextureAtlas("textures/ui/throne-wars-ui.atlas");
		
	   	loadFont();
	}
	
	private void loadFont() {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/CinzelDecorative-Bold.otf"));
    	FreeTypeFontParameter fontParams = new FreeTypeFontGenerator.FreeTypeFontParameter();
    	fontParams.size = 24;
    	fontParams.color = Color.WHITE;
    	font = generator.generateFont(fontParams);
    	generator.dispose();
	}

	public BitmapFont getFont() {
		return font;
	}
	
	public TextureRegion getButton() {
		return atlas.findRegion("button");
	}

	public TextureRegion getButtonDown() {
		return atlas.findRegion("button-down");
	}

	public TextureRegion getButtonFancy() {
		return atlas.findRegion("button-fancy");
	}

	public TextureRegion getButtonSmall() {
		return atlas.findRegion("button-small");
	}

	public TextureRegion getButtonSmallDown() {
		return atlas.findRegion("button-small-down");
	}
	
	public TextureRegion getButtonSmallFancy() {
		return atlas.findRegion("button-small-fancy");
	}

	public void dispose() {
		atlas.dispose();
		font.dispose();
	}
}

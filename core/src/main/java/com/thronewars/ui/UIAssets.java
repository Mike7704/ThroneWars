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

	private static UIAssets instance;
	
	private final TextureAtlas atlas;
	private final BitmapFont font;
	
	private final TextureRegion buttonTexture;
    private final TextureRegion buttonHoverTexture;
    private final TextureRegion buttonPressedTexture;
	
	public UIAssets() {
		if (instance != null) {
			throw new IllegalStateException("UIAssets has already been initialized.");
		}
		
		instance = this;
		
		atlas = new TextureAtlas("textures/ui/throne-wars-ui.atlas");
		
	   	font = loadFont();
	   	
	   	buttonTexture = atlas.findRegion("button");
        buttonHoverTexture = atlas.findRegion("button-down");
        buttonPressedTexture = atlas.findRegion("button-down");
	}
	
	private BitmapFont loadFont() {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/CinzelDecorative-Bold.otf"));

		try {
			FreeTypeFontParameter params = new FreeTypeFontParameter();
			params.size = 24;
			params.color = Color.WHITE;

			return generator.generateFont(params);
		}
		finally {
			generator.dispose();
		}
	}

	public BitmapFont getFont() {
		return font;
	}
	
	public TextureRegion getButton() {
        return buttonTexture;
    }

    public TextureRegion getButtonHover() {
        return buttonHoverTexture;
    }

    public TextureRegion getButtonPressed() {
        return buttonPressedTexture;
    }

	public static UIAssets getInstance() {
		if (instance == null) {
			throw new IllegalStateException("UIAssets must be initialized before creating UI components.");
		}

		return instance;
	}
    
	public void dispose() {
		atlas.dispose();
		font.dispose();
	}
}

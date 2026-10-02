package com.thronewars;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.thronewars.screens.MainMenuScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class GdxGame extends Game {

	private Batch batch;
	private BitmapFont font;
	
    @Override
    public void create() {
    	batch = new SpriteBatch();
    	
    	FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/CinzelDecorative-Bold.otf"));
    	FreeTypeFontParameter fontParams = new FreeTypeFontGenerator.FreeTypeFontParameter();
    	fontParams.size = 24;
    	fontParams.color = Color.WHITE;
    	font = generator.generateFont(fontParams);
    	generator.dispose();
    	
    	setScreen(new MainMenuScreen(this));
    	
    }

    @Override
    public void dispose() {
    	super.dispose();
    	batch.dispose();
    	font.dispose();
    }
    
    public Batch getBatch() {
    	return batch;
    }
    
    public BitmapFont getFont() {
    	return font;
    }
}

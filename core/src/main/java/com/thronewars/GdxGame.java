package com.thronewars;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.thronewars.screens.MainMenuScreen;
import com.thronewars.ui.UIAssets;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class GdxGame extends Game {

	private Batch batch;
	
	private UIAssets uiAssets;
	
    @Override
    public void create() {
    	batch = new SpriteBatch();
    	    	
    	uiAssets = new UIAssets();
    	
    	setScreen(new MainMenuScreen(this));
    	
    }

    @Override
    public void dispose() {
    	super.dispose();
    	batch.dispose();
    	uiAssets.dispose();
    }
    
    public Batch getBatch() {
    	return batch;
    }

	public UIAssets getUIAssets() {
		return uiAssets;
	}
}

package com.thronewars.ui.components;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.thronewars.GdxGame;

/**
 * TWButton class for creating a button.
 */
public class TWButton extends Actor {
		
	private final TextureRegion normalRegion;

	private final GdxGame game;
	private final GlyphLayout layout = new GlyphLayout();
	private final String text;

	public TWButton(String text, float width, float height, GdxGame game) {

		this.text = text;
		this.game = game;

		normalRegion = game.getUIAssets().getButton();
		
		setSize(width, height);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		// Draw the button background.
		batch.draw(normalRegion, getX(), getY(), getWidth(), getHeight());

		// Centre the text on the button.
		layout.setText(game.getUIAssets().getFont(), text);

		float textX = getX() + (getWidth() - layout.width) / 2f;
		float textY = getY() + (getHeight() + layout.height) / 2f;

		game.getUIAssets().getFont().draw(batch, layout, textX, textY);
	}
}
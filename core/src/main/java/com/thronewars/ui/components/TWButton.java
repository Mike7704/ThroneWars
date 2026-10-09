package com.thronewars.ui.components;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.thronewars.ui.UIAssets;

/**
 * TWButton class for creating a button.
 */
public class TWButton extends Actor {
		
	private final UIAssets ui = UIAssets.getInstance();
	private final GlyphLayout layout = new GlyphLayout();
	private final String text;

	private Runnable onClick;
	
	private boolean hovered;
	private boolean pressed;

	public TWButton(String text, float width, float height) {

		this.text = text;
		
		setSize(width, height);
		
		addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (onClick != null) {
					onClick.run();
				}
			}
			
			@Override
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				hovered = true;
			}

			@Override
			public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
				hovered = false;
			}

			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				if (!super.touchDown(event, x, y, pointer, button)) {
					return false;
				}
				pressed = true;
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				pressed = false;
			}
		});
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		if (pressed) {
			batch.draw(ui.getButtonPressed(), getX(), getY(), getWidth(), getHeight());
		}
		else if (hovered) {
			batch.draw(ui.getButtonHover(), getX(), getY(), getWidth(), getHeight());
		}
		else {
			batch.draw(ui.getButton(), getX(), getY(), getWidth(), getHeight());
		}

		layout.setText(ui.getFont(), text);

		float textX = getX() + (getWidth() - layout.width) / 2f;
		float textY = getY() + (getHeight() + layout.height) / 2f;

		ui.getFont().draw(batch, layout, textX, textY);
	}
	
	public void setOnClick(Runnable onClick) {
	    this.onClick = onClick;
	}
}
package com.thronewars.ui.components;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

/**
 * Creates a button.
 */
public class TWButton extends TextButton {

	private Runnable onClick;

	public TWButton(String text, TextButtonStyle style) {
		super(text, style);

		getLabel().setAlignment(Align.center);
		
		addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (onClick != null) {
					onClick.run();
				}
			}
		});
	}

	public void setOnClick(Runnable onClick) {
		this.onClick = onClick;
	}
}
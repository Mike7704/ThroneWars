package com.thronewars.ui.components;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.thronewars.managers.UIManager;

/**
 * Creates a label.
 */
public class TWLabel extends Label {

	public TWLabel(String text, UIManager uiManager) {
	    super(text, uiManager.getLabelStyle());
	}
}

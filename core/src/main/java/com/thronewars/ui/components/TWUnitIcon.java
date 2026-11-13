package com.thronewars.ui.components;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.thronewars.assets.UnitAnimation;
import com.thronewars.entities.EntityUnit;
import com.thronewars.entities.EntityDirection;

/**
 * Displays a unit character's portrait.
 */
public class TWUnitIcon extends Image {

    public TWUnitIcon(EntityUnit unit) {
        super(new TextureRegionDrawable(
        		unit.getUnitAssets().getFrame(
                UnitAnimation.IDLE,
                EntityDirection.DOWN,
                0f
            )
        ));
    }
}
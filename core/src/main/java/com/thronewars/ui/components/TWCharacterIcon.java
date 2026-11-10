package com.thronewars.ui.components;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.thronewars.assets.CharacterAnimation;
import com.thronewars.entities.EntityCharacter;
import com.thronewars.entities.EntityDirection;

/**
 * Displays a character's portrait.
 */
public class TWCharacterIcon extends Image {

    public TWCharacterIcon(EntityCharacter character) {
        super(new TextureRegionDrawable(
            character.getCharacterAssets().getFrame(
                CharacterAnimation.IDLE,
                EntityDirection.DOWN,
                0f
            )
        ));
    }
}